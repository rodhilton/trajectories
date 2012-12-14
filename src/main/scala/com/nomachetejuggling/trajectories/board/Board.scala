package com.nomachetejuggling.trajectories.board

import java.util.NoSuchElementException

trait Board {
    type Coordinates = Tuple2[Char, Int]

    val size: Int

    def set(coordinates: Coordinates, value: Int): Board
    def get(coordinates: Coordinates): Option[Int]
    def plus(otherBoard: Board): Board
    def merge(otherBoard: Board, joiner: ((Int, Int)=>Int) ): Board
    def intersect(otherBoard: Board): Board
    def filter(f: Tuple2[Tuple2[Char, Int], Int] => Boolean): Board
    def activeSpaces: Set[Tuple2[Char,Int]]
    def allSpaces: Set[Tuple2[Char,Int]]
    def crop(topLeft: Tuple2[Char, Int], bottomRight: Tuple2[Char, Int]): Board
    def overlay(otherBoard: Board, mySpot: Tuple2[Char, Int], otherSpot: Tuple2[Char, Int], joiner: ((Int, Int)=>Int)): Board
    def contains(coords: Tuple2[Char, Int]): Boolean
    def isDefinedAt(coords: Tuple2[Char, Int]): Boolean
    def toMap: Map[Tuple2[Char, Int], Int]
    def getTopLeftOffset(coords: Tuple2[Char, Int]): Tuple2[Int, Int]

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
    def overlay(otherBoard: Board, mySpot: Tuple2[Char, Int], joiner: ((Int, Int)=>Int)): Board = {
        overlay(otherBoard, mySpot, ('a', otherBoard.size), joiner)
    }

    def apply(t: Tuple2[Char, Int]): Int = get(t) match {
        case Some(x) => x
        case None => throw new NoSuchElementException
    }

    def apply(c: Char, i: Int): Int = this((c, i))

}

object Board {
    def apply(size: Int): Board = new BoardImpl(size, Map.empty)

    //TODO: figure out why I can't do this
    //def apply(size: Int, pairs: Tuple2[Tuple2[Char, Int], Int]*): Board = Board(size).set(pairs)

    case class BoardImpl(override val size: Int, boardSpace: Map[Tuple2[Char, Int], Int]) extends Board {
        val columns = ('a' to ('a' to 'z')(size - 1))
        val rows = (1 to size).reverse
        override lazy val allSpaces = columns.flatMap(c => rows.map(r => (c, r))).toSet

        override lazy val toMap: Map[Tuple2[Char, Int], Int] = boardSpace

        override def set(coordinates: Tuple2[Char, Int], value: Int): Board = {
            assert(insideBounds((columns.first, size), (columns.last, 1), coordinates), "Tried to set outside of bounds: "+coordinates+", but size is "+size)
            new BoardImpl(size, boardSpace.updated(coordinates, value))
        }

        override def get(coordinates: Tuple2[Char, Int]): Option[Int] = boardSpace.get(coordinates)

        override def plus(otherBoard: Board): Board = {
            merge(otherBoard, (i, j)=>i+j)
        }

        override def intersect(otherBoard: Board): Board = {
            assert(size == otherBoard.size)

            //TODO: get rid of this ugly duplication from merge()
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
            val offset = smallBoard.getTopLeftOffset(otherSpot)
            val myTopLeft = addOffset(mySpot, offset)


            val cropped = this.crop(myTopLeft, bottomRightFor(myTopLeft, smallBoard.size))

            cropped.merge(smallBoard, joiner)
        }

        override def merge(otherBoard: Board, joiner: ((Int, Int)=>Int)): Board = {
            assert(size == otherBoard.size, "Tried to merge two boards of unequal size. Me: "+size+", other: "+otherBoard.size)

            val spacesAsTuples = ( activeSpaces ++ otherBoard.activeSpaces).map {
                coords =>
                    (this.get(coords), otherBoard.get(coords)) match {
                        case (Some(left), Some(right)) => coords->joiner(left,right)
                        case (Some(left), None) => coords->joiner(left,0)
                        case (None, Some(right)) => coords->joiner(right,0)
                        case (None, None) => throw new Exception("This can't happen, each space has to be in one of the two maps")
                    }
            }

            new BoardImpl(size, spacesAsTuples.toMap)
        }

        override def getTopLeftOffset(coords: Tuple2[Char, Int]): Tuple2[Int, Int] = {
            ('a'-coords._1, size-coords._2)
        }

        override def crop(topLeft: Tuple2[Char, Int], bottomRight: Tuple2[Char, Int]): Board = {
            assert(this.contains(topLeft) && this.contains(bottomRight), "Asked to crop from "+topLeft+" to "+bottomRight+", but my size is "+size)
            assertValidZone(topLeft, bottomRight)

            //Remove everything outside of our bounds, leaving only the correct elements (with the wrong indexes)
            val inside = insideBounds(topLeft, bottomRight, _: (Char, Int))

            val filteredBoard = this.filter{ case (coords, value) => inside(coords) }

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

    //Helper methods on Board
    def insideBounds(topLeft: Tuple2[Char, Int], bottomRight: Tuple2[Char, Int], coordsToCheck: Tuple2[Char, Int]): Boolean = {
        assertValidZone(topLeft, bottomRight)
        (topLeft._1 to bottomRight._1).contains(coordsToCheck._1) && (bottomRight._2 to topLeft._2).contains(coordsToCheck._2)
    }

    def bottomRightFor(topLeft: Tuple2[Char, Int], size: Int): Tuple2[Char, Int] = {
        assert(topLeft._2 >= size, "Trying to find the bottom right without enough room: "+topLeft+" - "+size)
        ((topLeft._1 + size - 1).toChar, topLeft._2 - size + 1)
    }

    def addOffset(left: Tuple2[Char,Int], offset: Tuple2[Int, Int]): Tuple2[Char, Int] = {
        ((left._1+offset._1).toChar, left._2+offset._2)
    }

    protected def assertValidZone(topLeft: Tuple2[Char, Int], bottomRight: Tuple2[Char, Int]) {
        //This looks reversed because the numbers start at the bottom but the letters start on the right
        assert(bottomRight._1 >= topLeft._1 && topLeft._2 >= bottomRight._2, "Crop coordinates not valid: "+topLeft+" to "+bottomRight)
        assert(bottomRight._1 - topLeft._1 == topLeft._2 - bottomRight._2, "Asked to crop to a nonsquare: "+(bottomRight._1 - topLeft._1)+"x"+(topLeft._2 - bottomRight._2))
    }

}