package com.nomachetejuggling.trajectories

import annotation.tailrec

case class Piece(isValid: (Coordinates, Coordinates) => Boolean) {

    def bigTable(size: Int = 15): Board = movesStartingAt(Board(size), Coordinates.fromNumeric(size / 2 + 1, size / 2 + 1))

    def movesStartingAt(board: Board, startPosition: Coordinates): Board = calculateMovesForBoard(board.set(startPosition -> 0), 0)

    def st(board: Board, start: Coordinates, index: Int) = movesStartingAt(board, start).filterWhere(v => v == index)

    @tailrec
    final def calculateMovesForBoard(board: Board, startPosition: Int = 0): Board = {
        val positions: Set[Coordinates] = board.filter {
            case (coords: Coordinates, v: Int) => v == startPosition
        }.activeSpaces

        val canMoveTo = for {
            position <- positions
            possible <- (board.allSpaces -- board.activeSpaces) if isValid(position, possible)
        } yield (possible, startPosition + 1)

        if (canMoveTo.isEmpty)
            board
        else
            calculateMovesForBoard(board.set(canMoveTo), startPosition + 1)
    }


}
