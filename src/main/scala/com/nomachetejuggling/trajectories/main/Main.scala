package com.nomachetejuggling.trajectories.main

import org.apache.commons.cli.{Option => Opt, _}
import com.nomachetejuggling.trajectories._

object Main {
    def main(args: Array[String]) {
        new Main().execute(args)
    }
}

class Main() {

    def execute(args: Array[String]) {
        val pieceOpt = new Opt("p", "piece", true, "The piece to use (pawn, king, queen, knight, bishop, rook, weird)")
        val startOpt = new Opt("s", "start", true, "The space to start (a1, b6, ...)")
        val destOpt = new Opt("d", "dest", true, "The space to end (a2, c8, ...)")
        val sizeOpt = new Opt("z", "size", true, "Size of the board (must be <= 26)")
        val allOpt = new Opt("a", "all", false, "Output information about all paths (can be slow)")
        val helpOpt = new Opt("h", "help", false, "Print this help")
        val illegalOpt = new Opt("i", "illegal", true, "Input spaces that are illegal separated by commas (a2,b3,e6)")

        val options = new Options()

        options.addOption(pieceOpt)
        options.addOption(startOpt)
        options.addOption(destOpt)
        options.addOption(sizeOpt)
        options.addOption(allOpt)
        options.addOption(helpOpt)
        options.addOption(illegalOpt)

        // create the parser
        val parser = new GnuParser()
        try {
            // parse the command line arguments
            val line = parser.parse(options, args)
            if (line.hasOption("help")) throw new ParseException("Help Requested")

            if (line.hasOption("piece") && line.hasOption("start") && line.hasOption("dest")) {
                val startInput: String = line.getOptionValue("start")
                val destInput: String = line.getOptionValue("dest")
                val pieceInput: String = line.getOptionValue("piece")
                val sizeInput: String = line.getOptionValue("size")
                val illegalInput: String = line.getOptionValue("illegal")

                val startCoord = parseShortCode(startInput)
                val destCoord = parseShortCode(destInput)
                val piece = parsePiece(pieceInput)
                val size = parseSize(sizeInput)
                val illegal = parseIllegal(illegalInput)
                val all = line.hasOption("all")

                if (startCoord.isEmpty || destCoord.isEmpty) throw new ParseException("Coordinates are in invalid format")
                if (piece.isEmpty) throw new ParseException("No piece known by name " + pieceInput)
                if (all && size > 10) throw new ParseException("Cannot request all paths on extremely large boards")

                val pathFinder = new PathFinder(piece.get)

                if (all) {
                    val paths = pathFinder.getPaths(Board(size).illegal(illegal), startCoord.get, destCoord.get)

                    println("Number of shortest trajectories from " + startInput + " to " + destInput + ": " + paths.size)

                    if (paths.size == 0) {
                        println("You have probably cut off all paths via illegal spaces")
                    } else {
                        val path =
                            if (paths.size == 1) {
                                println("Here it is: \n")
                                paths.head
                            } else {
                                println("Here's one of them: \n")
                                util.Random.shuffle(paths).head
                            }
                        println(path.toFullBoardString)
                        println("")
                        println(paths.mkString("\n"))

                        println("\nShortest path is "+(path.size-1)+" moves")
                    }

                } else {
                    val path = pathFinder.getPath(Board(size).illegal(illegal), startCoord.get, destCoord.get)
                    println(path.toFullBoardString)
                    println("")
                    println(path)
                    println("\nShortest path is "+(path.size-1)+" moves")
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
            val illegalList = s.split(",")
            illegalList.collect {
                case shortCode: String => parseShortCode(shortCode)
            }.flatten.toSet
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