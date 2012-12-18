package com.nomachetejuggling.trajectories

import annotation.tailrec
import scala.collection.mutable.{Map => MutableMap}

object Piece {
    val normal = (from: Coordinates, to: Coordinates) => (from, to)
    val flip = (from: Coordinates, to: Coordinates) => ((from.col, -from.row), (to.col, -to.row))
}

case class Piece(
                    isValid: (Coordinates, Coordinates) => Boolean,
                    reverser: (Coordinates, Coordinates) => (Coordinates, Coordinates) = Piece.normal
                    ) {

    lazy val reverse: Piece = {
        val transformedIsValid = (from: Coordinates, to: Coordinates) => {
            val reversed = reverser(from, to)
            isValid(reversed._1, reversed._2)
        }
        Piece(transformedIsValid, reverser)
    }

    def movesStartingAt(board: Board, startPosition: Coordinates): Board = calculateMovesForBoard(board.set(startPosition -> 0), 0)

    private val stCache: MutableMap[(Board, Coordinates, Int), Board] = MutableMap()

    def st(board: Board, start: Coordinates, index: Int): Board = {
        val key: (Board, Coordinates, Int) = (board, start, index)

        if (!stCache.isDefinedAt(key))
            stCache.put(key, movesStartingAt(board, start).filterWhere(v => v == index))

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
