package com.nomachetejuggling.trajectories

import java.util.NoSuchElementException
import scala.Some

trait Board {
    val size: Int

    def set(coordinates: Coordinates, value: Int): Board

    def illegal(illegalCoords: Coordinates): Board

    def get(coordinates: Coordinates): Option[Int]

    def activeSpaces: Set[Coordinates]

    def legalSpaces: Set[Coordinates]

    def illegalSpaces: Set[Coordinates]

    def plus(otherBoard: Board): Board

    def intersect(otherBoard: Board): Board

    def merge(otherBoard: Board, joiner: ((Int, Int) => Int), merger: ((Set[Coordinates], Set[Coordinates]) => Set[Coordinates]) = Board.union): Board

    def filter(f: ((Coordinates, Int)) => Boolean): Board

    def contains(coords: Coordinates): Boolean

    def isDefinedAt(coords: Coordinates): Boolean

    //Convenience Interface:
    def +(otherBoard: Board): Board = plus(otherBoard)

    def &(otherBoard: Board): Board = intersect(otherBoard)

    def contains(c: Char, i: Int): Boolean = contains((c, i))

    def isDefinedAt(c: Char, i: Int): Boolean = isDefinedAt((c, i))

    def isLegal(coords: Coordinates): Boolean = contains(coords) && !illegalSpaces.contains(coords)

    def isLegal(c: Char, i: Int): Boolean = isLegal((c, i))

    def set(pairs: Iterable[(Coordinates, Int)]): Board = {
        pairs.foldLeft(this) {
            (b: Board, pair: (Coordinates, Int)) => b.set(pair._1, pair._2)
        }
    }

    def set(pairs: (Coordinates, Int)*): Board = set(pairs.toSeq)

    def illegal(coords: Iterable[Coordinates]): Board = {
        coords.foldLeft(this) {
            (b: Board, c: Coordinates) => b.illegal(c)
        }
    }

    def illegal(coords: Coordinates*): Board = illegal(coords.toSeq)

    def apply(t: Coordinates): Int = get(t) match {
        case Some(x) => x
        case None => throw new NoSuchElementException
    }

    def apply(c: Char, i: Int): Int = this((c, i))

    def filterWhere(predicate: (Int) => Boolean): Board = filter(t => predicate(t._2))
}

object Board {
    def apply(size: Int): Board = new BoardImpl(size, Map.empty, Set.empty)

    val intersection = (a: Set[Coordinates], b: Set[Coordinates]) => a & b
    val union = (a: Set[Coordinates], b: Set[Coordinates]) => a ++ b

    case class BoardImpl(override val size: Int,
                         boardSpace: Map[Coordinates, Int],
                         override val illegalSpaces: Set[Coordinates]) extends Board {

        val columns = ('a' to ('a' to 'z')(size - 1))
        val rows = (1 to size).reverse

        private lazy val allSpaces = columns.flatMap(c => rows.map(r => (c, r))).toSet

        override lazy val legalSpaces = allSpaces -- illegalSpaces
        override lazy val activeSpaces: Set[Coordinates] = boardSpace.keySet

        override def set(coordinates: Coordinates, value: Int): Board = {
            assert((columns.head, size) to(columns.last, 1) contains coordinates, "Tried to set outside of bounds: " + coordinates + ", but size is " + size)
            assert(!illegalSpaces.contains(coordinates), "Cannot set a value on an illegal space: " + coordinates)

            new BoardImpl(size, boardSpace.updated(coordinates, value), illegalSpaces)
        }

        override def illegal(illegalCoords: Coordinates): Board = {
            assert((columns.head, size) to(columns.last, 1) contains illegalCoords, "Tried to set illegal outside of bounds: " + illegalCoords + ", but size is " + size)

            new BoardImpl(size, boardSpace - illegalCoords, illegalSpaces + illegalCoords)
        }

        override def get(coordinates: Coordinates): Option[Int] = boardSpace.get(coordinates)

        override def plus(otherBoard: Board): Board = merge(otherBoard, (left, right) => left + right, union)

        override def intersect(otherBoard: Board): Board = merge(otherBoard, (left, right) => scala.math.max(left, right), intersection)

        override def filter(f: ((Coordinates, Int)) => Boolean): Board = new BoardImpl(size, boardSpace.filter(f), illegalSpaces)

        override def contains(coords: Coordinates): Boolean = columns.contains(coords.col) && rows.contains(coords.row)

        override def isDefinedAt(coords: Coordinates): Boolean = boardSpace.isDefinedAt(coords)

        override def merge(otherBoard: Board, joiner: ((Int, Int) => Int), merger: ((Set[Coordinates], Set[Coordinates]) => Set[Coordinates])): Board = {
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

            new BoardImpl(size, spacesAsTuples.toMap, merger(illegalSpaces, otherBoard.illegalSpaces))
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

}