package com.nomachetejuggling.trajectories

import org.junit.Test
import org.junit.Assert._

class PathFinderTest {

    @Test
    def shouldSum() {
        val pathFinder = new PathFinder(Pieces.king)

        val board = Board(8)

        val sum=pathFinder.getSum(board, ('a',2), ('c',7))
        println(sum)
        assertEquals(sum('a', 2), 5)
        assertEquals(sum('b', 3), 5)
        assertEquals(sum('c', 4), 5)
        assertEquals(sum('c', 5), 5)
        assertFalse(sum.isDefinedAt('d', 7))
    }

    @Test
    def shouldPath() {
        val pathFinder = new PathFinder(Pieces.king)

        val board = Board(8)

        val path = pathFinder.getPath(board, ('a',2), ('c',7))

        println(path)
    }

}
