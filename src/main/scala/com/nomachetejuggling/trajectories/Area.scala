package com.nomachetejuggling.trajectories

case class Area(topLeft: Coordinates, bottomRight: Coordinates) {
    def isValid: Boolean = isOrientedProperly && isSquare

    def contains(coords: Coordinates): Boolean = {
        (topLeft.col to bottomRight.col).contains(coords.col) &&
            (bottomRight.row to topLeft.row).contains(coords.row)
    }

    private def isOrientedProperly = bottomRight.col >= topLeft.col && topLeft.row >= bottomRight.row

    private def isSquare = bottomRight.col - topLeft.col == topLeft.row - bottomRight.row
}
