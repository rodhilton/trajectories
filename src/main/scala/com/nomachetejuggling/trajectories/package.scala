package com.nomachetejuggling

package object trajectories {

    object Coordinates {
        def fromNumeric(col: Int, row: Int): Coordinates = new Coordinates(('a' to 'z')(col-1), row)
        def fromNumeric(coordsNumeric: Tuple2[Int, Int]): Coordinates = fromNumeric(coordsNumeric._1, coordsNumeric._2)
    }

    type Coordinates = Tuple2[Char, Int]

    implicit def toCoordinatesOps(coords: Coordinates) = new CoordinatesOps(coords)

    implicit def toArea(area: Tuple2[Coordinates, Coordinates]) = new Area(area._1, area._2)

    class CoordinatesOps(coords: Coordinates) {
        lazy val col = coords._1
        lazy val row = coords._2

        def to(other: Coordinates): Area = {
            val area = (coords, other)
            assert(area.isValid, "Area is not valid: "+area)
            area
        }

        lazy val asNumeric: (Int, Int) = (col-'a'+1, row)
    }
}
