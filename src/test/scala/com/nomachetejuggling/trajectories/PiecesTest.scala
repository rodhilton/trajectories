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
        val table: Board = Pieces.pawn.bigTable(9)

        println(table)

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
        //[5][4][5][5][5][4][5][5][5][4][5][5][5][4][5]  15
        //[4][3][4][4][4][3][4][4][4][3][4][4][4][3][4]  14
        //[5][4][4][3][4][4][4][3][4][4][4][3][4][4][5]  13
        //[5][4][3][2][3][3][3][2][3][3][3][2][3][4][5]  12
        //[5][4][4][3][3][2][3][3][3][2][3][3][4][4][5]  11
        //[4][3][4][3][2][1][2][2][2][1][2][3][4][3][4]  10
        //[5][4][4][3][3][2][2][1][2][2][3][3][4][4][5]  9
        //[5][4][3][2][3][2][1][0][1][2][3][2][3][4][5]  8
        //[5][4][4][3][3][2][2][1][2][2][3][3][4][4][5]  7
        //[4][3][4][3][2][1][2][2][2][1][2][3][4][3][4]  6
        //[5][4][4][3][3][2][3][3][3][2][3][3][4][4][5]  5
        //[5][4][3][2][3][3][3][2][3][3][3][2][3][4][5]  4
        //[5][4][4][3][4][4][4][3][4][4][4][3][4][4][5]  3
        //[4][3][4][4][4][3][4][4][4][3][4][4][4][3][4]  2
        //[5][4][5][5][5][4][5][5][5][4][5][5][5][4][5]  1
        // a  b  c  d  e  f  g  h  i  j  k  l  m  n  o
        val table: Board = Pieces.weird.bigTable(15)
        println(table)

        assertEquals(table('g',9), 2)
        assertEquals(table('f',10), 1)
        assertEquals(table('g',11), 3)
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

    @Test
    def shouldNotReverseNormalPieces() {
        val whiteKing = Pieces.king
        val blackKing = whiteKing.reverse

        assertTrue(whiteKing.isValid(('a', 2), ('a',3)))
        assertTrue(blackKing.isValid(('a', 2), ('a',3)))
    }

    @Test
    def shouldReversePawn() {
        val whitePawn = Pieces.pawn
        val blackPawn = whitePawn.reverse

        assertTrue(whitePawn.isValid(('a', 2), ('a',3)))
        assertFalse(whitePawn.isValid(('a', 3), ('a',2)))

        assertFalse(blackPawn.isValid(('a', 2), ('a',3)))
        assertTrue(blackPawn.isValid(('a', 3), ('a',2)))
    }

}
