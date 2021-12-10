package utils

data class Coordinate(val row: Int, val column: Int) {

    operator fun plus(coordinate: Coordinate) = Coordinate(row + coordinate.row, column + coordinate.column)
}