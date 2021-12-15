package utils

data class Coordinate(val rowY: Int, val columnX: Int) {

    operator fun plus(coordinate: Coordinate) = Coordinate(rowY + coordinate.rowY, columnX + coordinate.columnX)
}

fun String.xyToCoordinate(divider: String = ",") = split(divider).let {
    return@let Coordinate(it.last().toInt(), it.first().toInt())
}

fun <T> List<List<T>>.getOrNull(coordinate: Coordinate) = this.getOrNull(coordinate.rowY)?.getOrNull(coordinate.columnX)

operator fun <T> List<List<T>>.get(coordinate: Coordinate) = this[coordinate.rowY][coordinate.columnX]

operator fun <T> MutableList<MutableList<T>>.set(coordinate: Coordinate, value: T) {
    this[coordinate.rowY][coordinate.columnX] = value
}