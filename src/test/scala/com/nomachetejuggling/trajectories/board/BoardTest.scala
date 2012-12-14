package com.nomachetejuggling.trajectories.board

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

    @Test(expected = classOf[AssertionError])
    def cannotCropOutOfBounds() {
        val a = Board(4)

        a.crop(('e', 4), ('f', 3))
    }

    @Test(expected = classOf[AssertionError])
    def cannotCropANonSquare() {
        val a = Board(4)

        a.crop(('a', 4), ('b', 2))
    }

    //what if you crop empty?

    @Test(expected = classOf[AssertionError])
    def cannotCropInvalidly() {
        val a = Board(4)

        a.crop(('c', 1), ('a', 3))
    }

    @Test
    def shouldCheckForBoundaryRestrictions() {
        val partial = Board.insideBounds(('b', 3), ('c', 2), _: Tuple2[Char, Int])

        assertFalse(partial(('a', 4)))
        assertFalse(partial(('a', 3)))
        assertFalse(partial(('a', 2)))
        assertFalse(partial(('a', 1)))

        assertFalse(partial(('b', 4)))
        assertTrue(partial(('b', 3)))
        assertTrue(partial(('b', 2)))
        assertFalse(partial(('b', 1)))

        assertFalse(partial(('c', 4)))
        assertTrue(partial(('c', 3)))
        assertTrue(partial(('c', 2)))
        assertFalse(partial(('c', 1)))

        assertFalse(partial(('d', 4)))
        assertFalse(partial(('d', 3)))
        assertFalse(partial(('d', 2)))
        assertFalse(partial(('d', 1)))
    }

    @Test
    def shouldCalculateBoundaries() {
        assertEquals(('c', 2), Board.bottomRightFor(('b', 3), 2))
        assertEquals(('b', 3), Board.bottomRightFor(('b', 3), 1))
        assertEquals(('d', 1), Board.bottomRightFor(('b', 3), 3))
    }

    @Test
    def canCrop() {
        /*
        [ ][6][ ][ ] 4
        [ ][1][2][6] 3
        [6][3][4][ ] 2
        [ ][ ][6][ ] 1
         a  b  c  d
         */
        val a = Board(4).set(
            ('b', 4) -> 6,
            ('d', 3) -> 6,
            ('a', 2) -> 6,
            ('c', 1) -> 6,
            ('b', 3) -> 1,
            ('c', 3) -> 2,
            ('b', 2) -> 3,
            ('c', 2) -> 4
        )

        val cropped = a.crop(('b', 3), ('c', 2))

        assertEquals(cropped.size, 2)
        assertEquals(cropped('a', 2), 1)
        assertEquals(cropped('b', 2), 2)
        assertEquals(cropped('a', 1), 3)
        assertEquals(cropped('b', 1), 4)

    }

    @Test
    def canCropOffset() {
        /*
        [7][8][9] 3
        [4][5][6] 2
        [1][2][3] 1
         a  b  c
         */
        val a = Board(4).set(
            ('a', 1) -> 1,
            ('b', 1) -> 2,
            ('c', 1) -> 3,
            ('a', 2) -> 4,
            ('b', 2) -> 5,
            ('c', 2) -> 6,
            ('a', 3) -> 7,
            ('b', 3) -> 8,
            ('c', 3) -> 9
        )

        val croppedTopLeft = a.crop(('a', 3), ('b', 2))
        val croppedTopRight = a.crop(('b', 3), ('c', 2))
        val croppedBottomLeft = a.crop(('a', 2), ('b', 1))
        val croppedBottomRight = a.crop(('b', 2), ('c', 1))

        assertEquals(croppedTopLeft.size, 2)
        assertEquals(croppedTopRight.size, 2)
        assertEquals(croppedBottomLeft.size, 2)
        assertEquals(croppedBottomRight.size, 2)

        assertEquals(croppedTopLeft('a', 1), 4)
        assertEquals(croppedTopRight('a', 1), 5)
        assertEquals(croppedBottomLeft('a', 1), 1)
        assertEquals(croppedBottomRight('a', 1), 2)
    }

    @Test
    def canGetTopLeftOffset() {
        val b = Board(3)

        assertEquals(b.getTopLeftOffset(('b',1)), (-1, 2))
        assertEquals(b.getTopLeftOffset(('b',2)), (-1, 1))
        assertEquals(b.getTopLeftOffset(('a',3)), (0, 0))
        assertEquals(b.getTopLeftOffset(('c',1)), (-2, 2))
    }

    @Test
    def canOffset() {
        assertEquals(('d', 8), Board.addOffset(('f',5), (-2, 3)))
    }

    @Test
    def canCropTiny() {
        /*
        [4][3][2] 3
        [5][8][1] 2
        [6][7][0] 1
         a  b  c
         */
        val a = Board(4).set(
            ('a', 1) -> 6,
            ('a', 2) -> 5,
            ('a', 3) -> 4,
            ('b', 1) -> 7,
            ('b', 2) -> 8,
            ('b', 3) -> 3,
            ('c', 1) -> 0,
            ('c', 2) -> 1,
            ('c', 3) -> 2
        )

        val cropped = a.crop(('b', 2), ('b', 2))

        assertEquals(cropped.size, 1)
        assertEquals(cropped('a', 1), 8)
    }


    /**
     * This one is complicated.  Lets say I have a large board:
     *
     * [ ][2][ ] 3
     * [ ][0][1] 2  3x3
     * [6][2][3] 1
     * a  b  c
     *
     * And I want to overlay on top of it a smaller board:
     *
     * [0][1] 2  2x2
     * [2][4] 1
     * a  b
     *
     * I have to declare what point is mapping to what point.
     *
     * So the big board b2 will map to the small board a2.  In the process of overlaying, the function will also take a joining function, like +, which will add points together
     *
     * [0][2] 2
     * [4][7] 1
     * a  b
     *
     */
    @Test
    def shouldOverlay() {

        /*
         *
         * [ ][ ][ ] 3
         * [ ][0][0] 2  3x3
         * [ ][2][2] 1
         *  a  b  c
         *
         * And I want to overlay on top of it a smaller board:
         *
         * [3][3] 2  2x2
         * [1][1] 1
         *  a  b
         */
        val a = Board(3).set(
            ('b', 1) -> 2,
            ('c', 1) -> 2,
            ('b', 2) -> 0,
            ('c', 2) -> 0
        )

        val b = Board(2).set(
            ('a', 1) -> 1,
            ('b', 1) -> 1,
            ('a', 2) -> 3,
            ('b', 2) -> 3
        )

        val c = a.overlay(b, ('b', 2), (a, b) => scala.math.max(a,b))

        assertEquals(c.size, 2)
        assertEquals(c('a', 1), 2)
        assertEquals(c('b', 1), 2)
        assertEquals(c('a', 2), 3)
        assertEquals(c('b', 2), 3)

    }

    @Test
    def shouldOverlayArbitrary() {

        val a = Board(7).set(
            ('d', 4) -> 6
        )

        val b = Board(3).set(
            ('b', 2) -> 4
        )

        val c = a.overlay(b, ('d', 4), ('b', 2), (i,j)=>i+j)

        assertEquals(c.size, 3)
        assertEquals(c('b', 2), 10)

    }

    @Test
    def shouldOverlayCustom() {

        /*
         *
         * [ ][ ][ ] 3
         * [ ][0][0] 2  3x3
         * [ ][2][2] 1
         *  a  b  c
         *
         * And I want to overlay on top of it a smaller board:
         *
         * [3][3] 2  2x2
         * [1][1] 1
         *  a  b
         */
        val a = Board(3).set(
            ('b', 1) -> 2,
            ('c', 1) -> 2,
            ('b', 2) -> 0,
            ('c', 2) -> 0
        )

        val b = Board(2).set(
            ('a', 1) -> 1,
            ('b', 1) -> 1,
            ('a', 2) -> 3,
            ('b', 2) -> 3
        )

        val c = a.overlay(b, ('b', 2), (a, b) => scala.math.max(a,b))

        assertEquals(c.size, 2)
        assertEquals(c('a', 1), 2)
        assertEquals(c('b', 1), 2)
        assertEquals(c('a', 2), 3)
        assertEquals(c('b', 2), 3)

    }

    @Test(expected = classOf[AssertionError])
    def cannotOverlaySpotNotOnBigBoard() {

        val a = Board(3)
        val b = Board(2)

        a.overlay(b, ('d', 1), (i,j)=>i+j)
    }
}
