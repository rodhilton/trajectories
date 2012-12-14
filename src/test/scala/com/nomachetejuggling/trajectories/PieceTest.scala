package com.nomachetejuggling.trajectories

import org.junit.Test
import org.junit.Assert._

class PieceTest {

    val rookMove = (from:Coordinates, to: Coordinates) => {
        val fromN = from.asNumeric
        val toN = to.asNumeric
        fromN._1 == toN._1 || fromN._2 == toN._2
    }

    val bishopMove = (from:Coordinates, to: Coordinates) => {
        val fromN = from.asNumeric
        val toN = to.asNumeric
        val offsetHorizontal = (fromN._1 - toN._1).abs
        val offsetVertical = (fromN._2 - toN._2).abs

        offsetHorizontal == offsetVertical
    }

    val kingMove = (from:Coordinates, to: Coordinates) => {
        val fromN = from.asNumeric
        val toN = to.asNumeric
        val offsetHorizontal = (fromN._1 - toN._1).abs
        val offsetVertical = (fromN._2 - toN._2).abs

        offsetHorizontal <= 1 && offsetVertical <= 1
    }

    val knightMove = (from:Coordinates, to: Coordinates) => {
        val fromN = from.asNumeric
        val toN = to.asNumeric
        val offsetHorizontal = (fromN._1 - toN._1).abs
        val offsetVertical = (fromN._2 - toN._2).abs

        (offsetHorizontal == 1 && offsetVertical == 2) ||
            (offsetHorizontal == 2 && offsetVertical == 1)
    }

    val queenMove = (from:Coordinates, to: Coordinates) => {
        bishopMove(from,to) || rookMove(from,to)
    }

    @Test
    def shouldGenerateCorrectBoard() {
//        [1][ ][2][ ][2][ ][2][ ][2][ ][2][ ][2][ ][1]  15
//        [ ][1][ ][2][ ][2][ ][2][ ][2][ ][2][ ][1][ ]  14
//        [2][ ][1][ ][2][ ][2][ ][2][ ][2][ ][1][ ][2]  13
//        [ ][2][ ][1][ ][2][ ][2][ ][2][ ][1][ ][2][ ]  12
//        [2][ ][2][ ][1][ ][2][ ][2][ ][1][ ][2][ ][2]  11
//        [ ][2][ ][2][ ][1][ ][2][ ][1][ ][2][ ][2][ ]  10
//        [2][ ][2][ ][2][ ][1][ ][1][ ][2][ ][2][ ][2]  9
//        [ ][2][ ][2][ ][2][ ][0][ ][2][ ][2][ ][2][ ]  8
//        [2][ ][2][ ][2][ ][1][ ][1][ ][2][ ][2][ ][2]  7
//        [ ][2][ ][2][ ][1][ ][2][ ][1][ ][2][ ][2][ ]  6
//        [2][ ][2][ ][1][ ][2][ ][2][ ][1][ ][2][ ][2]  5
//        [ ][2][ ][1][ ][2][ ][2][ ][2][ ][1][ ][2][ ]  4
//        [2][ ][1][ ][2][ ][2][ ][2][ ][2][ ][1][ ][2]  3
//        [ ][1][ ][2][ ][2][ ][2][ ][2][ ][2][ ][1][ ]  2
//        [1][ ][2][ ][2][ ][2][ ][2][ ][2][ ][2][ ][1]  1
//         a  b  c  d  e  f  g  h  i  j  k  l  m  n  o


        val bishop = Piece(bishopMove)

        val table: Board = bishop.bigTable
        println(table)
        assertEquals(table.size, 15)
        assertEquals(table('h',8), 0)
        assertEquals(table('g',9), 1)
        assertEquals(table('j',2), 2)
        assertFalse(table.isDefinedAt('l',9))
    }

    @Test
    def shouldGenerateCorrectBoardAlso() {
        val rook = Piece(rookMove)

        val table: Board = rook.bigTable
        println(table)
        assertEquals(table.size, 15)
    }

    @Test
    def shouldGenerateCorrectBoardAlsoToo() {
        val queen = Piece(queenMove)

        val table: Board = queen.bigTable
        println(table)
        assertEquals(table.size, 15)
    }

    @Test
    def shouldGenerateCorrectBoardForKing() {
        val king = Piece(kingMove)

        val table: Board = king.bigTable
        println(table)
        assertEquals(table.size, 15)
    }

    @Test
    def shouldGenerateCorrectBoardForKnight() {
        val knight = Piece(knightMove)

        val table: Board = knight.bigTable
        println(table)
        assertEquals(table.size, 15)
    }

}
