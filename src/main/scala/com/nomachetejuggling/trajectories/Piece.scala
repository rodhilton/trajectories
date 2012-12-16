package com.nomachetejuggling.trajectories

import annotation.tailrec
import scala.collection.mutable.{Map=>MutableMap}
import com.nomachetejuggling.trajectories._

object Piece {
    val normal = (from: Coordinates, to: Coordinates) => (from, to)
    val flip = (from: Coordinates, to: Coordinates) => ((from.col, -from.row), (to.col, -to.row))
}


case class Piece(
                    isValid: (Coordinates, Coordinates) => Boolean,
                    reverseTransformer: (Coordinates, Coordinates) => (Coordinates, Coordinates) = Piece.normal
                    ) {
    private val stCache: MutableMap[(Board, Coordinates, Int), Board] = MutableMap()

    lazy val reverse: Piece = {
        val transformedIsValid = (from: Coordinates, to: Coordinates) => {
            val reversed = reverseTransformer(from, to)
            isValid(reversed._1, reversed._2)
        }
        Piece(transformedIsValid, reverseTransformer)
    }

    def bigTable(size: Int = 15): Board = movesStartingAt(Board(size), Coordinates.fromNumeric(size / 2 + 1, size / 2 + 1))

    def movesStartingAt(board: Board, startPosition: Coordinates): Board = calculateMovesForBoard(board.set(startPosition -> 0), 0)

    def st(board: Board, start: Coordinates, index: Int): Board = {
        val key: (Board, Coordinates, Int) = (board, start, index)

        if(!stCache.isDefinedAt(key))
        {
            val moves: Board = movesStartingAt(board, start).filterWhere(v => v == index)
            stCache.put(key, moves)
        }

        stCache(key)
    }

    @tailrec
    final def calculateMovesForBoard(board: Board, startPosition: Int = 0): Board = {
        val positions: Set[Coordinates] = board.filter {
            case (coords: Coordinates, v: Int) => v == startPosition
        }.activeSpaces

        val canMoveTo = for {
            position <- positions
            possible <- (board.legalSpaces -- board.activeSpaces) if isValid(position, possible)
        } yield (possible, startPosition + 1)

        if (canMoveTo.isEmpty)
            board
        else
            calculateMovesForBoard(board.set(canMoveTo), startPosition + 1)
    }


}
