package com.nomachetejuggling.trajectories

import scala.Some

case class Board(size: Int,
                 boardSpace: Map[Coordinates, Int] = Map.empty,
                 illegalSpaces: Set[Coordinates] = Set.empty) {

    private lazy val columns = ('a' to ('a' to 'z')(size - 1))
    private lazy val rows = (1 to size).reverse

    private lazy val allSpaces = columns.flatMap(c => rows.map(r => (c, r))).toSet

    lazy val legalSpaces = allSpaces -- illegalSpaces
    lazy val activeSpaces: Set[Coordinates] = boardSpace.keySet

    def set(coordinates: Coordinates, value: Int): Board = {
        assert((columns.head, size) to(columns.last, 1) contains coordinates, "Tried to set outside of bounds: " + coordinates + ", but size is " + size)
        assert(!illegalSpaces.contains(coordinates), "Cannot set a value on an illegal space: " + coordinates)

        Board(size, boardSpace.updated(coordinates, value), illegalSpaces)
    }

    def illegal(illegalCoords: Coordinates): Board = {
        assert((columns.head, size) to(columns.last, 1) contains illegalCoords, "Tried to set illegal outside of bounds: " + illegalCoords + ", but size is " + size)

        Board(size, boardSpace - illegalCoords, illegalSpaces + illegalCoords)
    }

    def get(coordinates: Coordinates): Option[Int] = boardSpace.get(coordinates)

    def plus(otherBoard: Board): Board = merge(otherBoard, (left, right) => left + right, (a, b) => a ++ b)

    def intersect(otherBoard: Board): Board = merge(otherBoard, (left, right) => scala.math.max(left, right), (a, b) => a & b)

    def filter(f: ((Coordinates, Int)) => Boolean): Board = Board(size, boardSpace.filter(f), illegalSpaces)

    def contains(coords: Coordinates): Boolean = columns.contains(coords.col) && rows.contains(coords.row)

    def isDefinedAt(coords: Coordinates): Boolean = boardSpace.isDefinedAt(coords)

    def merge(otherBoard: Board, joiner: ((Int, Int) => Int), merger: ((Set[Coordinates], Set[Coordinates]) => Set[Coordinates])): Board = {
        assert(size == otherBoard.size, "Tried to merge two boards of unequal size. Me: " + size + ", other: " + otherBoard.size)

        val merged = merger(activeSpaces, otherBoard.activeSpaces)

        val spacesAsTuples = merged.map {
            coords =>
                (get(coords), otherBoard.get(coords)) match {
                    case (Some(left), Some(right)) => coords -> joiner(left, right)
                    case (Some(left), None) => coords -> joiner(left, 0)
                    case (None, Some(right)) => coords -> joiner(0, right)
                    case (None, None) => throw new Exception("This can't happen, each space has to be in one of the two maps")
                }
        }

        Board(size, spacesAsTuples.toMap, merger(illegalSpaces, otherBoard.illegalSpaces))
    }

    override lazy val toString = {
        val spaceWidth = boardSpace.values.max.toString.size

        val rowStrings = for {
            row <- rows
            rowString = columns.map(col =>
                boardSpace.get((col, row)) match {
                    case Some(x) => "[" + ("%" + spaceWidth + "d").format(x) + "]"
                    case None => {
                        if (illegalSpaces.contains((col, row))) "[" + ("#" * spaceWidth) + "]"
                        else "[" + (" " * spaceWidth) + "]"
                    }
                }
            )
        } yield rowString.mkString("") + "  " + row

        rowStrings.mkString("\n") + "\n" + columns.map(c => (" " * spaceWidth) + c + " ").mkString("")
    }

}