package utils

enum class Direction(val coordinate: Coordinate) {
    UP(Coordinate(-1, 0)),
    DOWN(Coordinate(1, 0)),
    LEFT(Coordinate(0, -1)),
    RIGHT(Coordinate(0, 1)),
    UP_LEFT(Coordinate(-1, -1)),
    UP_RIGHT(Coordinate(-1, 1)),
    DOWN_LEFT(Coordinate(1, -1)),
    DOWN_RIGHT(Coordinate(1, 1));
}