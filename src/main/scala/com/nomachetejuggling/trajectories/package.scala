package com.nomachetejuggling

package object trajectories {

    object Coordinates {
        def fromNumeric(col: Int, row: Int): Coordinates = new Coordinates(('a' to 'z')(col-1), row)
        def fromNumeric(coordsNumeric: Tuple2[Int, Int]): Coordinates = fromNumeric(coordsNumeric._1, coordsNumeric._2)
    }

    type Coordinates = Tuple2[Char, Int]

    implicit def toCoordinatesOps(coords: Coordinates) = new CoordinatesOps(coords)
    implicit def toBoardOps(board: Board) = new BoardOps(board)

    implicit def toArea(area: Tuple2[Coordinates, Coordinates]) = new Area(area._1, area._2)

    class CoordinatesOps(coords: Coordinates) {
        lazy val col = coords._1
        lazy val row = coords._2

        def to(other: Coordinates): Area = {
            val area = (coords, other)
            assert(area.isValid, "Area is not valid: "+area)
            area
        }

        lazy val asNumeric: (Int, Int) = (col-'a'+1, row)
    }

    class BoardOps(board: Board) {
        def +(otherBoard: Board): Board = board.plus(otherBoard)

        def &(otherBoard: Board): Board = board.intersect(otherBoard)

        def contains(c: Char, i: Int): Boolean = board.contains((c, i))

        def isDefinedAt(c: Char, i: Int): Boolean = board.isDefinedAt((c, i))

        def isLegal(coords: Coordinates): Boolean = board.contains(coords) && !board.illegalSpaces.contains(coords)

        def isLegal(c: Char, i: Int): Boolean = board.isLegal((c, i))

        def set(pairs: Iterable[(Coordinates, Int)]): Board = {
            pairs.foldLeft(board) {
                (b: Board, pair: (Coordinates, Int)) => b.set(pair._1, pair._2)
            }
        }

        def set(pairs: (Coordinates, Int)*): Board = board.set(pairs.toSeq)

        def illegal(coords: Iterable[Coordinates]): Board = {
            coords.foldLeft(board) {
                (b: Board, c: Coordinates) => b.illegal(c)
            }
        }

        def illegal(coords: Coordinates*): Board = board.illegal(coords.toSeq)

        def apply(t: Coordinates): Int = board.get(t) match {
            case Some(x) => x
            case None => throw new NoSuchElementException
        }

        def apply(c: Char, i: Int): Int = board((c, i))

        def filterWhere(predicate: (Int) => Boolean): Board = board.filter(t => predicate(t._2))
    }
}
