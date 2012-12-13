import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
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

//    @Test(expected = classOf[AssertionError])
//    def shouldDisallowIntersectingDifferentSizes() {
//        val a = Board(5)
//        val b = Board(6)
//
//        a.intersect(b)
//    }

    @Test
    def shouldFilter() {
        val a = Board(5).set(('c', 2) -> 5, ('b', 1) -> 1)

        val c = a.filter{ case ((col, row), value) =>
            value > 1
        }

        assertEquals(c('c', 2), 5)
        assertTrue(c.get('b',1).isEmpty)
    }
}
