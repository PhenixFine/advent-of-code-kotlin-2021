import LineType.*
import utils.*
import utils.Direction.*

fun main() {
    val testInput = getDiagramInfo(readInput("Day05_test"))
    check(part1(testInput) == 5) { "Wrong Answer for test 1" }
    check(part2(testInput) == 12) { "Wrong Answer for test 2" }

    val input = getDiagramInfo(readInput("Day05"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: DiagramInfo) = Diagram(input, false).result

private fun part2(input: DiagramInfo) = Diagram(input, true).result

private fun getDiagramInfo(input: List<String>): DiagramInfo {
    val lines = mutableListOf<Line>()
    var max = 0

    input.forEach { string ->
        val numbers = string.split(" -> ", ",").map { it.toInt() }
        check(numbers.size == 4) { "problem with getDiagramInfo." }
        lines.add(Line.getLine(numbers[0], numbers[1], numbers[2], numbers[3]))
        if (numbers.any { it > max }) max = numbers.maxOrNull() ?: max
    }
    return DiagramInfo(lines, max + 1)
}

private class Diagram(val info: DiagramInfo, diagonal: Boolean) {
    private val diagram = MutableList(info.max) { MutableList(info.max) { 0 } }
    val result: Int

    init {
        info.lines.forEach { if (diagonal || it.type != DIAGONAL) addLine(it) }
        result = diagram.flatten().count { it >= 2 }
    }

    private fun addLine(line: Line) {
        var start = line.start

        diagram[start]++
        do {
            start += line.direction.coordinate
            diagram[start]++
        } while (start != line.end)
    }
}

private class DiagramInfo(val lines: List<Line>, val max: Int)

private class Line private constructor(
    val start: Coordinate,
    val end: Coordinate,
    val direction: Direction,
    val type: LineType
) {

    companion object {

        fun getLine(x1: Int, y1: Int, x2: Int, y2: Int): Line {
            val coordinate1 = Coordinate(y1, x1)
            val coordinate2 = Coordinate(y2, x2)

            return when {
                x1 == x2 && y1 < y2 -> Line(coordinate1, coordinate2, DOWN, VERTICAL)
                x1 == x2 && y1 > y2 -> Line(coordinate2, coordinate1, DOWN, VERTICAL)
                y1 == y2 && x1 < x2 -> Line(coordinate1, coordinate2, RIGHT, HORIZONTAL)
                y1 == y2 && x1 > x2 -> Line(coordinate2, coordinate1, RIGHT, HORIZONTAL)
                x1 < x2 && y1 < y2 -> Line(coordinate1, coordinate2, DOWN_RIGHT, DIAGONAL)
                x1 > x2 && y1 > y2 -> Line(coordinate2, coordinate1, DOWN_RIGHT, DIAGONAL)
                x1 > x2 && y1 < y2 -> Line(coordinate1, coordinate2, DOWN_LEFT, DIAGONAL)
                x1 < x2 && y1 > y2 -> Line(coordinate2, coordinate1, DOWN_LEFT, DIAGONAL)
                else -> error("Something went wrong with getLine: x1: $x1, y1: $y1, x2: $x2, y2: $y2")
            }
        }
    }
}

private enum class LineType {
    HORIZONTAL, VERTICAL, DIAGONAL
}