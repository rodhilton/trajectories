package com.nomachetejuggling

package object trajectories {
    type Coordinates = Tuple2[Char, Int]
    type Area = (Coordinates, Coordinates)

    implicit def toCoordinatesOps(coords: Coordinates) = new CoordinatesOps(coords)

    implicit def toAreaOps(area: Area) = new AreaOps(area)

    class CoordinatesOps(coords: Coordinates) {
        lazy val column = coords._1
        lazy val col = column
        lazy val row = coords._2

        //Pretty sure I can figure out how to make one of these call the other
        def add(offset: Tuple2[Int, Int]): Coordinates = {
            ((coords._1 + offset._1).toChar, coords._2 + offset._2)
        }

        def add(size: Int): Coordinates = {
            assert(coords.col >= size, "Trying to find the bottom right without enough room: "+coords+" - "+size)
            assert(coords.row - size >= 0, "Trying to find the bottom right without enough room: "+coords+" - "+size)
            ((coords.col + size - 1).toChar, coords.row - size + 1)
        }

        def +(offset: Tuple2[Int, Int]): Coordinates = add(offset)
        def +(size: Int): Coordinates = add(size)

        def to(other: Coordinates): Area = {
            val area = (coords, other)
            assert(area.isValid, "Area is not valid: "+area)
            area
        }

        lazy val asNumeric: (Int, Int) = (column-'a'+1, row)
    }

    class AreaOps(area: Area) {
        lazy val topLeft = area._1
        lazy val bottomRight = area._2

        def isValid: Boolean = isOrientedProperly && isSquare

        def contains(coords: Coordinates): Boolean = {
            (area.topLeft.col to area.bottomRight.col).contains(coords.col) &&
                (area.bottomRight.row to area.topLeft.row).contains(coords.row)
        }

        private def isOrientedProperly = area.bottomRight.col >= area.topLeft.col && area.topLeft.row >= area.bottomRight.row

        private def isSquare = area.bottomRight.col - area.topLeft.col == area.topLeft.row - area.bottomRight.row
    }

}
