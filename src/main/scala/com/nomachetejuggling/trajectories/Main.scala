package com.nomachetejuggling.trajectories

import org.apache.commons.cli.{Option => Opt, _}
import org.apache.commons.cli

object Main {
    def main(args: Array[String]) {
        new Main().execute(args)
    }
}

class Main() {

    def execute(args: Array[String]) {
        val pieceOpt = new Opt("p", "piece", true, "The piece to use (pawn, king, queen, knight, bishop, rook")
        val startOpt = new Opt("s", "start", true, "The space to start (a1, b6, ...)")
        val destOpt = new Opt("d", "dest", true, "The space to end (a2, c8, ...)")
        val sizeOpt = new Opt("z", "size", true, "Size of the board (must be <= 26)")
        val allOpt = new Opt("a", "all", false, "Print information about all paths")

        val options = new Options()

        options.addOption(pieceOpt)
        options.addOption(startOpt)
        options.addOption(destOpt)
        options.addOption(sizeOpt)
        options.addOption(allOpt)

        // create the parser
        val parser = new GnuParser()
        try {
            // parse the command line arguments
            val line = parser.parse(options, args)

            if (line.hasOption("piece") && line.hasOption("start") && line.hasOption("dest")) {
                val startInput: String = line.getOptionValue("start")
                val destInput: String = line.getOptionValue("dest")
                val pieceInput: String = line.getOptionValue("piece")
                val sizeInput: String = line.getOptionValue("size")

                val startCoord = parseShortCode(startInput)
                val destCoord = parseShortCode(destInput)
                val piece = parsePiece(pieceInput)
                val size = parseSize(sizeInput)
                val all = line.hasOption("all")

                if (startCoord.isEmpty || destCoord.isEmpty) throw new ParseException("Coordinates are in invalid format")
                if (piece.isEmpty) throw new ParseException("No piece known by name " + pieceInput)
                if (all && size > 10) throw new ParseException("Cannot request all paths on extremely large boards")

                val pathFinder = new PathFinder(piece.get)

                if (all) {
                    val paths = pathFinder.getPaths(Board(size), startCoord.get, destCoord.get)

                    println("There are "+paths.size+" shortest trajectories from "+startInput+" to "+destInput+".")
                    println("Here's one of them: \n")

                    println(util.Random.shuffle(paths).head.toFullBoardString)
                } else {
                    val path = pathFinder.getPath(Board(size), startCoord.get, destCoord.get)
                    println(path.toFullBoardString)
                }

            } else {
                throw new ParseException("Incomplete arguments")
            }
        }
        catch {
            case exp: ParseException => {
                System.err.println("Failure: " + exp.getMessage)
                val formatter = new HelpFormatter()
                formatter.printHelp("trajectories", options)
            }
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