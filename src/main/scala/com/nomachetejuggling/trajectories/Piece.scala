package com.nomachetejuggling.trajectories

case class Piece(isValid: (Coordinates, Coordinates) => Boolean) {

    def bigTable: Board = {
        val start = Board(15).set(('h', 8) -> 0)

        def fillOutFor(current: Int, board: Board): Board = {
            val positions: Set[Coordinates] = board.filter {
                case (coords: Coordinates, v: Int) => v == current
            }.activeSpaces

            val canMoveTo = for {
                position <- positions
                possible <- (board.allSpaces -- board.activeSpaces ) if isValid(position, possible)
            } yield (possible, current + 1)

            if (canMoveTo.isEmpty) {
                board
            } else {
                fillOutFor(current + 1, board.set(canMoveTo))
            }
        }
        fillOutFor(0, start)
    }


}
