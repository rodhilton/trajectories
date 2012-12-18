package com.nomachetejuggling.trajectories.main

import org.apache.commons.cli.{Option => Opt, _}
import com.nomachetejuggling.trajectories._

object Main {
    def main(args: Array[String]) {
        new Main().execute(args)
    }
}

class Main() {
    val PIECE = "piece"
    val START = "start"
    val DEST = "dest"
    val SIZE = "size"
    val ALL = "all"
    val HELP = "help"
    val ILLEGAL = "illegal"

    def execute(args: Array[String]) {

        val options = new Options()

        options.addOption(new Opt("p", PIECE, true, "The piece to use (pawn, king, queen, knight, bishop, rook, weird)"))
        options.addOption(new Opt("s", START, true, "The space to start (a1, b6, ...)"))
        options.addOption(new Opt("d", DEST, true, "The space to end (a2, c8, ...)"))
        options.addOption(new Opt("z", SIZE, true, "Size of the board (must be <= 26)"))
        options.addOption(new Opt("a", ALL, false, "Output information about all paths (can be slow)"))
        options.addOption(new Opt("h", HELP, false, "Print this help"))
        options.addOption(new Opt("i", ILLEGAL, true, "Input spaces that are illegal separated by commas (a2,b3,e6)"))

        val parser = new GnuParser()
        try {
            val line = parser.parse(options, args)
            if (line.hasOption(HELP)) throw new ParseException("Help Requested")

            if (line.hasOption(PIECE) && line.hasOption(START) && line.hasOption(DEST)) {
                val startInput: String = line.getOptionValue(START)
                val destInput: String = line.getOptionValue(DEST)
                val pieceInput: String = line.getOptionValue(PIECE)
                val sizeInput: String = line.getOptionValue(SIZE)
                val illegalInput: String = line.getOptionValue(ILLEGAL)

                val startCoord = parseShortCode(startInput)
                val destCoord = parseShortCode(destInput)
                val piece = parsePiece(pieceInput)
                val size = parseSize(sizeInput)
                val illegal = parseIllegal(illegalInput)
                val all = line.hasOption(ALL)

                if (startCoord.isEmpty || destCoord.isEmpty) throw new ParseException("Coordinates are in invalid format")
                if (piece.isEmpty) throw new ParseException("No piece known by name " + pieceInput)

                val pathFinder = new PathFinder(piece.get)

                val board = Board(size).illegal(illegal)

                if (all) {
                    val paths = pathFinder.getPaths(board, startCoord.get, destCoord.get)

                    paths match {
                        case Nil => println("It's impossible to reach the destination from the start position.")
                        case path :: _ => {
                            println("Number of shortest trajectories from " + startInput + " to " + destInput + ": " + paths.size)
                            println((if (paths.size == 1) "Here it is" else "Here's one of them") + ": \n")
                            println(path.toFullBoardString)
                            println("")
                            println(paths.mkString("\n"))
                            println("\nShortest path is " + (path.size - 1) + " moves")
                        }
                    }

                } else {
                    val pathOption = pathFinder.getPath(board, startCoord.get, destCoord.get)

                    pathOption match {
                        case None => println("It's impossible to reach the destination from the start position.")
                        case Some(path) => {
                            println(path.toFullBoardString)
                            println("")
                            println(path)
                            println("\nShortest path is " + (path.size - 1) + " moves")
                        }
                    }
                }

            } else {
                throw new ParseException("Incomplete arguments")
            }
        }
        catch {
            case exp: ParseException => {
                System.err.println(exp.getMessage)
                val formatter = new HelpFormatter()
                formatter.printHelp("trajectories", options)
            }
        }
    }

    private def parseIllegal(s: String): Set[Coordinates] = {
        try {
            s.split(",").flatMap(code => parseShortCode(code)).toSet
        }
        catch {
            case _: Exception => Set.empty
        }
    }

    private def parseSize(s: String): Int = {
        s match {
            case s: String if (1 to 26).contains(s.toInt) => s.toInt
            case _ => 8
        }
    }

    private def parsePiece(s: String): Option[Piece] = {
        s.toLowerCase match {
            case "pawn" => Some(Pieces.pawn)
            case "king" => Some(Pieces.king)
            case "queen" => Some(Pieces.queen)
            case "rook" => Some(Pieces.rook)
            case "bishop" => Some(Pieces.bishop)
            case "knight" => Some(Pieces.knight)
            case "weird" => Some(Pieces.weird)
            case _ => None
        }
    }

    private def parseShortCode(s: String): Option[Coordinates] = {
        val ShortCodePattern = """([a-z])(\d{1,2})""".r
        s.toLowerCase match {
            case ShortCodePattern(col, row) => Some((col.head, row.toInt))
            case _ => None
        }
    }
}