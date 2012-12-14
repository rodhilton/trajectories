import java.util.NoSuchElementException

trait Board {
    val size: Int
    def set(coordinates: Tuple2[Char, Int], value: Int): Board
    def get(coordinates: Tuple2[Char, Int]): Option[Int]
    def add(otherBoard: Board): Board

    //Convenience Interface:
    def set(pair: Tuple2[Tuple2[Char, Int], Int]): Board = {
        set(pair._1, pair._2)
    }
//
//    def set(pair: Tuple2[Tuple2[Char, Int], Int]): Board = {
//        set(pair._1, pair._2)
//    }

    def apply(t: Tuple2[Char, Int]): Int = get(t) match {
        case Some(x) => x
        case None => throw new NoSuchElementException
    }

    def apply(c: Char, i: Int): Int = this((c,i))

}

object Board {
    def apply(size: Int): Board = new BoardImpl(size, Map.empty)

    class BoardImpl(override val size: Int, boardSpace: Map[Tuple2[Char,Int], Int]) extends Board {
        val columns = ('a' to ('a' to 'z')(size-1))
        val rows = (1 to size).reverse
        val allSpaces = columns.flatMap(c => rows.map(r => (c, r)))

        override def set(coordinates: Tuple2[Char,Int], value: Int): Board = new BoardImpl(size, boardSpace.updated(coordinates, value))

        override def get(coordinates: Tuple2[Char, Int]): Option[Int] = boardSpace.get(coordinates)

        override def add(otherBoard: Board): Board = {
            assert(otherBoard.size == size)
            println(allSpaces)
            otherBoard
        }

        override def toString() = {
            val rowStrings = for{
                row <- rows
                rowString = columns.map(col =>
                    boardSpace.get((col, row)) match {
                        case Some(x) => "["+x+"]"
                        case None => "[ ]"
                    }
                )
            } yield rowString.mkString("")+"  "+row

            rowStrings.mkString("\n")+ "\n"+columns.map(c=> " "+c+" ").mkString("")
        }

    }
}

class Space
