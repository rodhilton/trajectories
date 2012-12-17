package com.nomachetejuggling.trajectories

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

    //TODO: some ugly duplication here with getPaths, but they ARE fundamentally different...
    //TODO: this needs to be an Option, possible to have no path
    //TODO: this should be tail recursive
    //TODO: vars, ugh
    def getPath(implicit board: Board, start: Coordinates, end: Coordinates): Path = {
        val sum = getSum(board, start, end)

        var thePath = start :: Nil

        var current = start
        while (current != end) {
            val moves = possibleMoves(thePath, sum, board)

            val selectedSpace = util.Random.shuffle(moves.toList).head

            thePath = selectedSpace :: thePath
            current = selectedSpace
        }

        Path(thePath)
    }

    def getPaths(implicit board: Board, start: Coordinates, end: Coordinates): List[Path] = {
        val sum = getSum(board, start, end)

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
