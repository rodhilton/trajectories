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
        assertTrue(s contains " a  b  c  d  e ")

//        println(b2.toString)
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

        assertEquals(b.activeSpaces, Set(('a',3), ('b',2)))
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

        assertEquals(a+d, c)
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

        val c = a.filter{ case ((col, row), value) =>
            value > 1
        }

        assertEquals(c('c', 2), 5)
        assertTrue(c.get('b',1).isEmpty)
    }

    @Test
    def considersSpacesWithNoValues() {
        val a = Board(3).set(('b',2)->0)

        assertTrue(a.contains(('a',1)))
        assertTrue(a.contains('b',2))
        assertFalse(a.isDefinedAt(('a',1)))
        assertTrue(a.isDefinedAt('b',2))
    }



    /**
     * This one is complicated.  Lets say I have a large board:
     *
     * [ ][2][ ] 3
     * [ ][0][1] 2  3x3
     * [6][2][3] 1
     *  a  b  c
     *
     * And I want to overlay on top of it a smaller board:
     *
     * [0][1] 2  2x2
     * [2][4] 1
     *  a  b
     *
     * I have to declare what point is mapping to what point.
     *
     * So the big board b2 will map to the small board a2.  In the process of overlaying, the function will also take a joining function, like +, which will add points together
     *
     * [0][2] 2
     * [4][7] 1
     *  a  b
     *
     */
    @Test
    def shouldOverlay() {

        val a = Board(3).set(
            ('b',3)->2,
            ('b',2)->0,
            ('c',2)->1,
            ('a',1)->6,
            ('b',1)->2,
            ('c',1)->3
        )

        val b = Board(3).set(
            ('b',3)->2,
            ('b',2)->0,
            ('c',2)->1,
            ('a',1)->6,
            ('b',1)->2,
            ('c',1)->3
        )

        //TODO: make sure you dont overlap a larger board
        //TODO: make sure you can't overlap a board such that it falls off (is that worth doing?  seems like that's alright, it should intersect)
        //TODO: make sure you don't specify overlap spots not on the boards

        val c = a.overlay(b, ('b',2), ('a',2))

        assertEquals(c.size, 2)
        assertEquals(c('a', 1), 4)
        assertEquals(c('a', 2), 0)
        assertEquals(c('b', 1), 7)
        assertEquals(c('b', 2), 2)

    }

    @Test(expected=classOf[AssertionError])
    def cannotOverlaySpotNotOnBigBoard() {

        val a = Board(3)
        val b = Board(2)

        a.overlay(b, ('d',1), ('a',1))
    }

    @Test(expected=classOf[AssertionError])
    def cannotOverlaySpotNotOnSmallBoard() {

        val a = Board(3)
        val b = Board(2)

        a.overlay(b, ('a',1), ('c',1))
    }
}
