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
        //Manual Inspection required, this is for display purposes
        println(b2.toString)
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
    def shouldManySet() {
        val b = Board(5)
        val b2 = b.set(('a', 3) -> 1).set(('b', 2) -> 2)

        assertEquals(b2('a', 3), 1)
        assertEquals(b2('b', 2), 2)
    }

    @Test
    def shouldAdd() {
//        val a = Board(5).set()
    }
}
