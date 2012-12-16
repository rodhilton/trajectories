package com.nomachetejuggling.trajectories

object Pieces {

    lazy val king = Piece(Moves.kingMove)
    lazy val queen = Piece(Moves.queenMove)
    lazy val bishop = Piece(Moves.bishopMove)
    lazy val knight = Piece(Moves.knightMove)
    lazy val rook = Piece(Moves.rookMove)
    lazy val pawn = Piece(Moves.pawnMove, Piece.flip)
    lazy val weird = Piece(Moves.weirdMove)

    private object Moves {
        lazy val rookMove = (from:Coordinates, to: Coordinates) => {
            val fromN = from.asNumeric
            val toN = to.asNumeric
            fromN._1 == toN._1 || fromN._2 == toN._2
        }

        lazy val pawnMove = (from:Coordinates, to: Coordinates) => {
            val fromN = from.asNumeric
            val toN = to.asNumeric
            fromN._1 == toN._1 && toN._2 == fromN._2 + 1
        }

        lazy val bishopMove = (from:Coordinates, to: Coordinates) => {
            val fromN = from.asNumeric
            val toN = to.asNumeric
            val offsetHorizontal = (fromN._1 - toN._1).abs
            val offsetVertical = (fromN._2 - toN._2).abs

            offsetHorizontal == offsetVertical
        }

        lazy val kingMove = (from:Coordinates, to: Coordinates) => {
            val fromN = from.asNumeric
            val toN = to.asNumeric
            val offsetHorizontal = (fromN._1 - toN._1).abs
            val offsetVertical = (fromN._2 - toN._2).abs

            offsetHorizontal <= 1 && offsetVertical <= 1
        }

        lazy val knightMove = (from:Coordinates, to: Coordinates) => {
            val fromN = from.asNumeric
            val toN = to.asNumeric
            val offsetHorizontal = (fromN._1 - toN._1).abs
            val offsetVertical = (fromN._2 - toN._2).abs

            (offsetHorizontal == 1 && offsetVertical == 2) ||
                (offsetHorizontal == 2 && offsetVertical == 1)
        }

        lazy val weirdMove = (from:Coordinates, to: Coordinates) => {
            val fromN = from.asNumeric
            val toN = to.asNumeric
            val offsetHorizontal = (fromN._1 - toN._1).abs
            val offsetVertical = (fromN._2 - toN._2).abs

            //diagonal 2, straight 1
            (offsetHorizontal == offsetVertical && offsetHorizontal == 2) ||
                (offsetHorizontal != offsetVertical && ((offsetHorizontal == 0 && offsetVertical == 1) || (offsetHorizontal == 1 && offsetVertical == 0)))
        }

        lazy val queenMove = (from:Coordinates, to: Coordinates) => {
            bishopMove(from,to) || rookMove(from,to)
        }
    }

}
