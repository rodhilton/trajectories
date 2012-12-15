package com.nomachetejuggling.trajectories

class PathFinder(piece: Piece) {
    def getSum(board: Board, start: Coordinates, end: Coordinates): Board = {
        val startBoard = piece.calculateMovesForBoard(board.set(start->0))

        val destBoard = piece.calculateMovesForBoard(board.set(end->0))

        val sum=startBoard+destBoard

        val shortestPathLength = sum(end)
        sum.filterWhere(v=>v==shortestPathLength)
    }

    def getPath(implicit board: Board, start: Coordinates, end: Coordinates): Path = {
        val sum = getSum(board, start, end)

        var thePath = start :: Nil

        var current = start
        while(current!= end) {
            val indexed = thePath.zipWithIndex
            val hmm = indexed.foldLeft(sum){
                case (sumBoard: Board, (prevCoords: Coordinates, prevIndex: Int)) => {
                    val st=piece.st(board, prevCoords, prevIndex + 1)
                    st intersect sumBoard
                }
            }

            val selectedSpace = util.Random.shuffle(hmm.activeSpaces.toList).head
            thePath = selectedSpace :: thePath
            current = selectedSpace
        }

        Path(thePath.reverse)
    }

    def getPaths(board: Board, start:Coordinates, end: Coordinates): List[Path] = {
        val sum = getSum(board, start, end)


        Nil
    }
}

case class Path(coordsList: List[Coordinates])(implicit board: Board) {

    override def toString: String = {
        board.set(coordsList.zipWithIndex).toString
    }
}
