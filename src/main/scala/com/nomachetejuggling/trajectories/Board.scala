package com.nomachetejuggling.trajectories

import java.util.NoSuchElementException
import scala.Some

trait Board {
    val size: Int

    def set(coordinates: Coordinates, value: Int): Board
    def get(coordinates: Coordinates): Option[Int]
    def plus(otherBoard: Board): Board
    def merge(otherBoard: Board, joiner: ((Int, Int) => Int), merger: ((Set[Coordinates], Set[Coordinates]) => Set[Coordinates]) = Board.union): Board
    def intersect(otherBoard: Board): Board
    def filter(f: ((Coordinates, Int)) => Boolean): Board
    def activeSpaces: Set[Coordinates]
    def allSpaces: Set[Coordinates]
    def crop(area: Area): Board
    def overlay(otherBoard: Board, mySpot: Coordinates, otherSpot: Coordinates, joiner: ((Int, Int) => Int)): Board
    def mask(otherBoard: Board): Board
    def contains(coords: Coordinates): Boolean
    def isDefinedAt(coords: Coordinates): Boolean
    def toMap: Map[Coordinates, Int]
    def getTopLeftOffset(coords: Coordinates): (Int, Int)
    def getTopLeft: Coordinates

    //Convenience Interface:
    def +(otherBoard: Board): Board = plus(otherBoard)
    def &(otherBoard: Board): Board = intersect(otherBoard)
    def contains(c: Char, i: Int): Boolean = contains((c, i))
    def isDefinedAt(c: Char, i: Int): Boolean = isDefinedAt((c, i))
    def set(pairs: (Coordinates, Int)*): Board = {
        pairs.foldLeft(this) {
            (b: Board, pair: (Coordinates, Int)) => b.set(pair._1, pair._2)
        }
    }

    def set(pairs: Iterable[(Coordinates, Int)]): Board = {
        pairs.foldLeft(this) {
            (b: Board, pair: (Coordinates, Int)) => b.set(pair._1, pair._2)
        }
    }

    def overlay(otherBoard: Board, mySpot: Coordinates, joiner: ((Int, Int) => Int)): Board = {
        overlay(otherBoard, mySpot, ('a', otherBoard.size), joiner)
    }

    def apply(t: Coordinates): Int = get(t) match {
        case Some(x) => x
        case None => throw new NoSuchElementException
    }

    def apply(c: Char, i: Int): Int = this((c, i))

    def filterWhere(predicate: (Int)=>Boolean): Board = {
        filter{ case (_: Coordinates, v: Int) => predicate(v)}
    }
}

object Board {
    def apply(size: Int): Board = new BoardImpl(size, Map.empty)

    //TODO: figure out why I can't do this
    //def apply(size: Int, pairs: Tuple2[Tuple2[Char, Int], Int]*): Board = Board(size).set(pairs)

    val intersection = (a: Set[Coordinates], b: Set[Coordinates]) => a & b
    val union = (a: Set[Coordinates], b: Set[Coordinates]) => a ++ b

    case class BoardImpl(override val size: Int, boardSpace: Map[Coordinates, Int]) extends Board {
        val columns = ('a' to ('a' to 'z')(size - 1))
        val rows = (1 to size).reverse
        override lazy val allSpaces = columns.flatMap(c => rows.map(r => (c, r))).toSet

        override lazy val toMap: Map[Coordinates, Int] = boardSpace

        override def getTopLeft: Coordinates = ('a', size)

        override def set(coordinates: Coordinates, value: Int): Board = {
            assert((columns.head, size) to (columns.last, 1) contains coordinates, "Tried to set outside of bounds: " + coordinates + ", but size is " + size)
            new BoardImpl(size, boardSpace.updated(coordinates, value))
        }

        override def get(coordinates: Coordinates): Option[Int] = boardSpace.get(coordinates)

        override def plus(otherBoard: Board): Board = merge(otherBoard, (left, right) => left + right, union)

        override def intersect(otherBoard: Board): Board = merge(otherBoard, (left, right) => scala.math.max(left, right), intersection)

        override def mask(otherBoard: Board): Board = merge(otherBoard, (left, right) => left, intersection)

        override def filter(f: ((Coordinates, Int)) => Boolean): Board = new BoardImpl(size, boardSpace.filter(f))

        override def contains(coords: Coordinates): Boolean = columns.contains(coords.col) && rows.contains(coords.row)

        override def isDefinedAt(coords: Coordinates): Boolean = boardSpace.isDefinedAt(coords)

        override def activeSpaces: Set[Coordinates] = boardSpace.keySet

        override def overlay(smallBoard: Board, mySpot: Coordinates, otherSpot: Coordinates, joiner: ((Int, Int) => Int)): Board = {
            assert(contains(mySpot), "Local board did not contain spot " + mySpot + ", size is " + size)

            val offset = smallBoard.getTopLeftOffset(otherSpot)
            val myTopLeft = mySpot + offset

            val cropped = crop(myTopLeft to (myTopLeft + smallBoard.size))

            cropped.merge(smallBoard, joiner, union)
        }

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

            new BoardImpl(size, spacesAsTuples.toMap)
        }


        override def crop(area: Area): Board = {
            assert(contains(area.topLeft) && contains(area.bottomRight), "Asked to crop from " + area.topLeft + " to " + area.bottomRight + ", but my size is " + size)
            assert((area.topLeft to area.bottomRight).isValid, "Area given is not valid: " + area)

            //Remove everything outside of our bounds, leaving only the correct elements (with the wrong indexes)
            val filteredBoard = filter {
                case (coords, value) => area.contains(coords)
            }

            //Now fix the indexes
            val offset = (-area.topLeft.col + 'a'.toInt, -area.bottomRight.row + 1)
            val offsetMap = filteredBoard.toMap.collect {
                case (coords, value) => coords + offset -> value
            }

            new BoardImpl(area.topLeft.row - area.bottomRight.row + 1, offsetMap)
        }

        //Could this be as simple as getTopLeft - coords?  Perhaps in the Ops?
        override def getTopLeftOffset(coords: Coordinates): (Int, Int) = {
            ('a' - coords.col, size - coords.row)
        }

        override lazy val toString = {
            val rowStrings = for {
                row <- rows
                rowString = columns.map(col =>
                    boardSpace.get((col, row)) match {
                        case Some(x) => "[" + x + "]"
                        case None => "[ ]"
                    }
                )
            } yield rowString.mkString("") + "  " + row

            rowStrings.mkString("\n") + "\n" + columns.map(c => " " + c + " ").mkString("")
        }

    }
}