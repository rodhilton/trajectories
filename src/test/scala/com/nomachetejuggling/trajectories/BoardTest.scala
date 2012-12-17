package com.nomachetejuggling.trajectories

import org.junit.Test
import org.junit.Assert._

class BoardTest {

    @Test
    def shouldSize() {
        assertEquals(Board(5).size, 5)
    }

    @Test
    def shouldToString() {
        val b = Board(5)
        val b2 = b.set(('a', 3), 0)

        val s = b2.toString
        assertTrue(s contains "[0][ ][ ][ ][ ]  3")
        assertTrue(s contains "[ ][ ][ ][ ][ ]  5")
        assertTrue(s contains " a  b  c  d  e")
    }

    @Test
    def shouldSetAndGet() {
        val b = Board(5)
        val b2 = b.set(('a', 3), 1)

        val element = b2.get(('a', 3))

        assertTrue(element.isDefined)
        assertEquals(element.get, 1)
    }

    @Test
    def shouldEasierSet() {
        val b = Board(5)
        val b2 = b.set(('a', 3) -> 1)

        val element = b2.get(('a', 3))

        assertTrue(element.isDefined)
        assertEquals(element.get, 1)
    }

    @Test
    def shouldBeConvenient() {
        val b = Board(5).set(('a', 3) -> 1, ('b', 2) -> 2)

        assertEquals(b('a', 3), 1)
        assertEquals(b('b', 2), 2)
    }

    @Test
    def shouldSupplySpaces() {
        val b = Board(5).set(('a', 3) -> 1, ('b', 2) -> 2)

        assertEquals(b.activeSpaces, Set(('a', 3), ('b', 2)))
    }

    @Test(expected = classOf[AssertionError])
    def shouldDisallowAddingDifferentSizes() {
        val a = Board(5)
        val b = Board(6)

        a.plus(b)
    }

    @Test
    def shouldAdd() {
        val a = Board(5).set(('c', 2) -> 5, ('b', 1) -> 2)
        val d = Board(5).set(('c', 2) -> 2, ('d', 5) -> 3)

        val c = a.plus(d)

        assertEquals(c('b', 1), 2)
        assertEquals(c('c', 2), 7) // These two cells should be added together
        assertEquals(c('d', 5), 3)

        assertEquals(a + d, c)
    }

    @Test(expected = classOf[AssertionError])
    def shouldDisallowIntersectingDifferentSizes() {
        val a = Board(5)
        val b = Board(6)

        a.intersect(b)
    }

    @Test
    def shouldIntersect() {
        val a = Board(5).set(('c', 2) -> 5, ('b', 1) -> 2)
        val d = Board(5).set(('c', 2) -> 2, ('d', 5) -> 3)

        val c = a.intersect(d)

        assertTrue(c.get('b', 1).isEmpty)
        assertEquals(c('c', 2), 5) //The maximum
        assertTrue(c.get('d', 5).isEmpty)

        assertEquals(a & d, c)
    }

    @Test
    def shouldFilter() {
        val a = Board(5).set(('c', 2) -> 5, ('b', 1) -> 1)

        val c = a.filter {
            case ((col, row), value) =>
                value > 1
        }

        assertEquals(c('c', 2), 5)
        assertTrue(c.get('b', 1).isEmpty)
    }

    @Test
    def considersSpacesWithNoValues() {
        val a = Board(3).set(('b', 2) -> 0)

        assertTrue(a.contains(('a', 1)))
        assertTrue(a.contains('b', 2))
        assertFalse(a.isDefinedAt(('a', 1)))
        assertTrue(a.isDefinedAt('b', 2))
    }

    @Test
    def shouldFilterByValue() {
        val a = Board(3).set(('a', 1) -> 1, ('b', 2) -> 2)

        val c = a.filterWhere {
            v: Int => v == 1
        }

        assertEquals(c('a', 1), 1)
        assertFalse(c.isDefinedAt('b', 2))
    }

    @Test
    def shouldSupportIllegalSpaces() {
        val basic = Board(3).set(('a', 1) -> 1, ('b', 2) -> 2).illegal(('a', 2))
        println(basic)
        assertFalse(basic.isLegal(('a', 2)))

        val illegalAfterSetting = Board(3).set(('a', 1) -> 1, ('b', 2) -> 2).illegal(('a', 1))
        assertFalse(illegalAfterSetting.isDefinedAt('a', 1))
        assertFalse(illegalAfterSetting.legalSpaces.contains(('a', 1)))
    }

    @Test
    def shouldSupportIllegalsWhenMerging() {

        val a = Board(2).illegal(('a', 1))
        val b = Board(2).illegal(('b', 2))

        val merged = a.merge(b, (i,j)=>i+j)

        assertFalse(merged.isLegal('a', 1))
        assertFalse(merged.isLegal('b', 2))

    }

    @Test(expected = classOf[AssertionError])
    def illegalOverridesSet() {
        Board(3).illegal(('a', 1)).set(('a', 1) -> 1)
    }

}
