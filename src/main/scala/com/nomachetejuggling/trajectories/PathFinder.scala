package com.nomachetejuggling.trajectories

import annotation.tailrec

class PathFinder(piece: Piece) {
    def getSum(board: Board, start: Coordinates, end: Coordinates): Board = {
        val startBoard = piece.calculateMovesForBoard(board.set(start -> 0))
        val destBoard = piece.reverse.calculateMovesForBoard(board.set(end -> 0))

        val sum = startBoard + destBoard

        val shortestPathLength = sum(end)

        sum.filterWhere(_ == shortestPathLength)
    }

    def possibleMoves(thePath: List[Coordinates], sum: Board, board: Board): Set[Coordinates] = {
        thePath.zipWithIndex.map {
            case (prevCoords: Coordinates, prevIndex: Int) => piece.st(board, prevCoords, prevIndex + 1)
        }.foldLeft(sum)(_ & _).activeSpaces
    }

    def getPath(implicit board: Board, start: Coordinates, end: Coordinates): Option[Path] = {
        val sum = getSum(board, start, end)

        @tailrec
        def path(partial: List[Coordinates]): List[Coordinates] = {
            if(partial.head == end) partial
            else {
                val moves = possibleMoves(partial, sum, board)

                if (moves.isEmpty) List.empty
                else path(moves.head :: partial)
            }
        }


        val thePath = path(start :: Nil)
        thePath match {
            case Nil => None
            case p@_ => Some(Path(p))
        }
    }

    def getPaths(implicit board: Board, start: Coordinates, end: Coordinates): List[Path] = {
        val sum = getSum(board, start, end)

        @tailrec
        def paths(partials: List[List[Coordinates]]): List[List[Coordinates]] = {
            val partialsNotDone = partials.filter((p: List[Coordinates]) => p.head != end)

            if (partialsNotDone.size == 0) {
                partials
            } else {
                val newPartials = for {
                    partial <- partials
                    move <- possibleMoves(partial, sum, board)
                } yield (move :: partial)
                paths(newPartials)
            }
        }

        val allPaths = paths(List(start :: Nil))

        allPaths.map(new Path(_))
    }
}

case class Path(coordsList: List[Coordinates])(implicit board: Board) {

    lazy val size = coordsList.size
    def toFullBoardString: String = board.set(coordsList.reverse.zipWithIndex).toString

    override def toString: String = coordsList.reverse.map((c: Coordinates) => (c.col + "" + c.row)).mkString("->")
}
