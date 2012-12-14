package com.nomachetejuggling.trajectories.board

import collection.immutable.IndexedSeq
import java.util.NoSuchElementException

trait Board {
    val size: Int

    def set(coordinates: Tuple2[Char, Int], value: Int): Board
    def get(coordinates: Tuple2[Char, Int]): Option[Int]
    def plus(otherBoard: Board): Board
    def intersect(otherBoard: Board): Board
    def filter(f: Tuple2[Tuple2[Char, Int], Int] => Boolean): Board
    def activeSpaces: Set[Tuple2[Char,Int]]
    def allSpaces: Set[Tuple2[Char,Int]]
    def crop(topLeft: Tuple2[Char, Int], bottomRight: Tuple2[Char, Int]): Board
    def overlay(otherBoard: Board, mySpot: Tuple2[Char, Int], otherSpot: Tuple2[Char, Int], joiner: ((Int, Int)=>Int) = {(i,j)=>i+j}): Board
    def contains(coords: Tuple2[Char, Int]): Boolean
    def isDefinedAt(coords: Tuple2[Char, Int]): Boolean
    def toMap: Map[Tuple2[Char, Int], Int]

    //Convenience Interface:
    def +(otherBoard: Board): Board = this.plus(otherBoard)
    def &(otherBoard: Board): Board = this.intersect(otherBoard)
    def contains(c:Char, i:Int): Boolean = this.contains((c,i))
    def isDefinedAt(c:Char, i:Int): Boolean = this.isDefinedAt((c,i))
    def set(pairs: Tuple2[Tuple2[Char, Int], Int]*): Board = {
        pairs.foldLeft(this) {
            case (b: Board, pair: Tuple2[Tuple2[Char, Int], Int]) =>
                b.set(pair._1, pair._2)
        }
    }

    def apply(t: Tuple2[Char, Int]): Int = get(t) match {
        case Some(x) => x
        case None => throw new NoSuchElementException
    }

    def apply(c: Char, i: Int): Int = this((c, i))

}

object Board {
    def apply(size: Int): Board = new BoardImpl(size, Map.empty)

    def insideBounds(topLeft: Tuple2[Char, Int], bottomRight: Tuple2[Char, Int], coordsToCheck: Tuple2[Char, Int]): Boolean = {
        assertValidZone(topLeft, bottomRight)
        (topLeft._1 to bottomRight._1).contains(coordsToCheck._1) && (bottomRight._2 to topLeft._2).contains(coordsToCheck._2)
    }

    protected def assertValidZone(topLeft: Tuple2[Char, Int], bottomRight: Tuple2[Char, Int]) {
        //This looks reversed because the numbers start at the bottom but the letters start on the right
        assert(bottomRight._1 >= topLeft._1 && topLeft._2 >= bottomRight._2, "Crop coordinates not valid: "+topLeft+" to "+bottomRight)
        assert(bottomRight._1 - topLeft._1 == topLeft._2 - bottomRight._2, "Asked to crop to a nonsquare: "+(bottomRight._1 - topLeft._1)+"x"+(topLeft._2 - bottomRight._2))
    }

    //TODO: figure out why I can't do this
    //def apply(size: Int, pairs: Tuple2[Tuple2[Char, Int], Int]*): Board = Board(size).set(pairs)

    case class BoardImpl(override val size: Int, boardSpace: Map[Tuple2[Char, Int], Int]) extends Board {
        val columns = ('a' to ('a' to 'z')(size - 1))
        val rows = (1 to size).reverse
        override lazy val allSpaces = columns.flatMap(c => rows.map(r => (c, r))).toSet

        override lazy val toMap: Map[Tuple2[Char, Int], Int] = boardSpace

        override def set(coordinates: Tuple2[Char, Int], value: Int): Board = new BoardImpl(size, boardSpace.updated(coordinates, value))

        override def get(coordinates: Tuple2[Char, Int]): Option[Int] = boardSpace.get(coordinates)

        override def plus(otherBoard: Board): Board = {
            assert(size == otherBoard.size, "Tried to add two boards of unequal size. Me: "+size+", other: "+otherBoard.size)

            val spacesAsTuples = ( activeSpaces ++ otherBoard.activeSpaces).map {
                coords =>
                    (this.get(coords), otherBoard.get(coords)) match {
                        case (Some(left), Some(right)) => coords->(left+right)
                        case (Some(left), None) => coords->left
                        case (None, Some(right)) => coords->right
                        case (None, None) => throw new Exception("This can't happen, each space has to be in one of the two maps")
                    }
            }

            new BoardImpl(size, spacesAsTuples.toMap)
        }

        override def intersect(otherBoard: Board): Board = {
            assert(size == otherBoard.size)

            //TODO: get rid of this ugly duplication from plus()
            val spacesAsTuples = ( activeSpaces & otherBoard.activeSpaces).map {
                coords => coords -> scala.math.max(this(coords), otherBoard(coords))
            }

            new BoardImpl(size, spacesAsTuples.toMap)
        }

        def contains(coords: Tuple2[Char, Int]): Boolean = columns.contains(coords._1) && rows.contains(coords._2)
        def isDefinedAt(coords: Tuple2[Char, Int]): Boolean = this.boardSpace.isDefinedAt(coords)

        override def activeSpaces: Set[Tuple2[Char,Int]] = boardSpace.keySet

        override def filter(f: Tuple2[Tuple2[Char, Int], Int] => Boolean): Board = new BoardImpl(size, boardSpace.filter(f))

        override def overlay(smallBoard: Board, mySpot: Tuple2[Char, Int], otherSpot: Tuple2[Char, Int], joiner: ((Int, Int)=>Int)): Board = {
            assert(this.contains(mySpot), "Local board did not contain spot "+mySpot+", size is "+size)
            assert(smallBoard.contains(otherSpot), "Other board did not contain spot "+otherSpot+", size is "+smallBoard.size)

            val colOffset = otherSpot._1 - mySpot._1
            val rowOffset = otherSpot._2 - mySpot._2



            this

        }

        override def crop(topLeft: Tuple2[Char, Int], bottomRight: Tuple2[Char, Int]): Board = {
            assert(this.contains(topLeft) && this.contains(bottomRight), "Asked to crop from "+topLeft+" to "+bottomRight+", but my size is "+size)
            assertValidZone(topLeft, bottomRight)


            //Remove everything outside of our bounds, leaving only the correct elements (with the wrong indexes)
            val inside = insideBounds(topLeft, bottomRight, _: (Char, Int))

            val filteredBoard = this.filter{ case (coords, value) =>
                inside(coords)
            }

            val offsetMap = filteredBoard.toMap.collect{ case ((col, row), value) =>
                (((col-topLeft._1)+'a'.toInt).toChar, row-bottomRight._2+1) -> value
            }

            new BoardImpl(topLeft._2 - bottomRight._2 + 1, offsetMap)
        }

        override def toString() = {
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

class Space
