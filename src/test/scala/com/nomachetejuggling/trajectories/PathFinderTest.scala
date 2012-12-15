package com.nomachetejuggling.trajectories

import org.junit.{Ignore, Test}
import org.junit.Assert._

class PathFinderTest {

    @Test
    def shouldSum() {
        val pathFinder = new PathFinder(Pieces.king)

        val board = Board(8)

        val sum=pathFinder.getSum(board, ('a',2), ('c',7))
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

    @Test
    def shouldPaths() {
        val pathFinder = new PathFinder(Pieces.king)

        val board = Board(8)

        val paths = pathFinder.getPaths(board, ('a',2), ('c',7))

        assertEquals(paths.size, 25)
    }


    @Test
    def shouldPathsAgain() {
        val pathFinder = new PathFinder(Pieces.king)

        val board = Board(8)

        val paths = pathFinder.getPaths(board, ('a',5), ('h',5))

        assertEquals(paths.size, 393)
    }

    @Test
    def shouldPathsFromAssignment12() {
        val pathFinder = new PathFinder(Pieces.king)

        val board = Board(8)

        val paths = pathFinder.getPaths(board, ('a',4), ('h',4))

        assertEquals(paths.size, 393)
    }

    @Test
    def shouldPathForLargePaths() {
        val pathFinder = new PathFinder(Pieces.king)

        val path = pathFinder.getPath(Board(15), ('h',1), ('h',15))

        assertEquals(path.coordsList.size,15)
    }

    @Test
    @Ignore("This test takes a really long time")
    def shouldPathForMassivePaths() {
        val pathFinder = new PathFinder(Pieces.king)

        val path = pathFinder.getPath(Board(26), ('h',1), ('h',26))

        assertEquals(path.coordsList.size,26)
    }

}
