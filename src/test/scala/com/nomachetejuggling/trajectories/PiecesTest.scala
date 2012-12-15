package com.nomachetejuggling.trajectories

import org.junit.Test
import org.junit.Assert._

class PiecesTest {

    @Test
    def shouldGenerateCorrectBoardForBishop() {
        //[1][ ][2][ ][1]  5
        //[ ][1][ ][1][ ]  4
        //[2][ ][0][ ][2]  3
        //[ ][1][ ][1][ ]  2
        //[1][ ][2][ ][1]  1
        // a  b  c  d  e

        val table: Board = Pieces.bishop.bigTable(5)

        assertEquals(table.size, 5)
        assertEquals(table('c',3), 0)
        assertEquals(table('d',2), 1)
        assertEquals(table('a',3), 2)
        assertFalse(table.isDefinedAt('e',4))
    }

    @Test
    def shouldGenerateCorrectBoardForRook() {
        //[2][1][2]  3
        //[1][0][1]  2
        //[2][1][2]  1
        // a  b  c
        val table: Board = Pieces.rook.bigTable(3)

        assertEquals(table.size, 3)
        assertEquals(table('b',2), 0)
        assertEquals(table('a',2), 1)
        assertEquals(table('a',3), 2)
    }

    @Test
    def shouldGenerateCorrectBoardForQueen() {
        //[1][2][1][2][1]  5
        //[2][1][1][1][2]  4
        //[1][1][0][1][1]  3
        //[2][1][1][1][2]  2
        //[1][2][1][2][1]  1
        // a  b  c  d  e
        val table: Board = Pieces.queen.bigTable(5)

        assertEquals(table.size, 5)
        assertEquals(table('c',3), 0)
        assertEquals(table('a',1), 1)
        assertEquals(table('c',1), 1)
        assertEquals(table('e',2), 2)
    }

    @Test
    def shouldGenerateCorrectBoardForKing() {
        //[2][2][2][2][2]  5
        //[2][1][1][1][2]  4
        //[2][1][0][1][2]  3
        //[2][1][1][1][2]  2
        //[2][2][2][2][2]  1
        // a  b  c  d  e
        val table: Board = Pieces.king.bigTable(5)

        assertEquals(table.size, 5)
        assertEquals(table('c',3), 0)
        assertEquals(table('d',2), 1)
        assertEquals(table('a',5), 2)
    }

    @Test
    def shouldGenerateCorrectBoardForKnight() {
        //[4][1][2][1][4]  5
        //[1][2][3][2][1]  4
        //[2][3][0][3][2]  3
        //[1][2][3][2][1]  2
        //[4][1][2][1][4]  1
        // a  b  c  d  e
        val table: Board = Pieces.knight.bigTable(5)

        assertEquals(table.size, 5)
        assertEquals(table('c',3), 0)
        assertEquals(table('b',1), 1)
        assertEquals(table('d',4), 2)
        assertEquals(table('c',4), 3)
        assertEquals(table('a',5), 4)
    }

    @Test
    def shouldGenerateCorrectBoardForPawn() {
        //[ ][ ][2][ ][ ]  5
        //[ ][ ][1][ ][ ]  4
        //[ ][ ][0][ ][ ]  3
        //[ ][ ][ ][ ][ ]  2
        //[ ][ ][ ][ ][ ]  1
        // a  b  c  d  e
        val table: Board = Pieces.pawn.bigTable(5)

        assertEquals(table.size, 5)
        assertEquals(table('c',3), 0)
        assertEquals(table('c',4), 1)
        assertEquals(table('c',5), 2)
        for (i<- 1 to 5) {
            assertFalse(table.isDefinedAt('a',i))
            assertFalse(table.isDefinedAt('b',i))
            assertFalse(table.isDefinedAt('d',i))
            assertFalse(table.isDefinedAt('e',i))
        }
        assertFalse(table.isDefinedAt('c',1))
        assertFalse(table.isDefinedAt('c',2))
    }

    @Test
    def shouldGenerateCorrectBoardForWeird() {
        //[2][2][2][3][2][3][2]  7
        //[3][2][2][2][2][2][2]  6
        //[2][2][1][1][1][2][2]  5
        //[2][2][1][0][1][2][2]  4
        //[2][2][1][1][1][2][2]  3
        //[3][2][2][2][2][2][2]  2
        //[2][2][2][3][2][3][2]  1
        // a  b  c  d  e  f  g
        val table: Board = Pieces.weird.bigTable(7)

        assertEquals(table('d',4), 0)
        assertEquals(table('a',2), 3)
        assertEquals(table('g',2), 2)
    }

    @Test
    def shouldGenerateForArbitraryBoards() {
        //[0][1][2][1][0]  5
        //[1][1][2][1][1]  4
        //[2][2][2][2][2]  3
        //[1][1][2][1][1]  2
        //[0][1][2][1][0]  1
        // a  b  c  d  e

        val piece: Piece = Pieces.king

        val board: Board = Board(5).set(('a', 1)->0, ('a',5)->0, ('e',1)->0, ('e', 5)->0)

        val moveBoard = piece.calculateMovesForBoard(board)

        assertEquals(moveBoard.size, 5)
        assertEquals(moveBoard('a',1), 0)
        assertEquals(moveBoard('a',2), 1)
        assertEquals(moveBoard('a',3), 2)
        assertEquals(moveBoard('a',4), 1)
        assertEquals(moveBoard('a',5), 0)
        assertEquals(moveBoard('c',3), 2)
        assertEquals(moveBoard('e',2), 1)
    }

}
