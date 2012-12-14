package com.nomachetejuggling.trajectories

package object board {
    type Coordinates = Tuple2[Char, Int]

    implicit def toCoordinatesOps(coords: Coordinates) = new CoordinatesOps(coords)

    class CoordinatesOps(coords: Coordinates) {
        def add(offset: Tuple2[Int, Int]): Coordinates = {
            ((coords._1+offset._1).toChar, coords._2+offset._2)
        }
        def +(offset: Tuple2[Int, Int]): Coordinates = this.add(offset)
    }
}
