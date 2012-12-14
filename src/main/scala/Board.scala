import collection.immutable.IndexedSeq
import java.util.NoSuchElementException

trait Board {
    val size: Int

    def set(coordinates: Tuple2[Char, Int], value: Int): Board
    def get(coordinates: Tuple2[Char, Int]): Option[Int]
    def plus(otherBoard: Board): Board
    def filter(f: Tuple2[Tuple2[Char, Int], Int] => Boolean): Board

    //Convenience Interface:
    def +(otherBoard: Board): Board = this.plus(otherBoard)

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

    //TODO: figure out why I can't do this
    //def apply(size: Int, pairs: Tuple2[Tuple2[Char, Int], Int]*): Board = Board(size).set(pairs)

    case class BoardImpl(override val size: Int, boardSpace: Map[Tuple2[Char, Int], Int]) extends Board {
        val columns = ('a' to ('a' to 'z')(size - 1))
        val rows = (1 to size).reverse
        val allPossibleSpaces = columns.flatMap(c => rows.map(r => (c, r)))

        override def set(coordinates: Tuple2[Char, Int], value: Int): Board = new BoardImpl(size, boardSpace.updated(coordinates, value))

        override def get(coordinates: Tuple2[Char, Int]): Option[Int] = boardSpace.get(coordinates)

        override def plus(otherBoard: Board): Board = {
            assert(size == otherBoard.size)

            val allSpacesAsOptionalTuples = allPossibleSpaces.map {
                coords =>
                    (this.get(coords), otherBoard.get(coords)) match {
                        case (Some(left), Some(right)) => Some((coords, left + right))
                        case (Some(left), None) => Some((coords, left))
                        case (None, Some(right)) => Some((coords, right))
                        case (None, None) => None
                    }
            }

            new BoardImpl(size, allSpacesAsOptionalTuples.flatten.toMap)
        }

        override def filter(f: Tuple2[Tuple2[Char, Int], Int] => Boolean): Board = new BoardImpl(size, boardSpace.filter(f))

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
