import utils.Coordinate
import utils.Direction.*
import utils.getOrNull
import utils.readInput

fun main() {
    val testInput = Cave.getCave(readInput("Day09_test"))
    check(part1(testInput) == 15) { "Wrong Answer for test 1" }
    check(part2(testInput) == 1134) { "Wrong Answer for test 2" }

    val input = Cave.getCave(readInput("Day09"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: Cave) = input.lowestSum()

private fun part2(input: Cave) = input.calculateBasin()

private class Cave private constructor(private val cave: List<List<Int>>) {
    private val lowestPoints: List<Pair<Coordinate, Int>>

    init {
        lowestPoints = lowestPoints()
    }

    private fun lowestPoints(): List<Pair<Coordinate, Int>> {
        val lowest = mutableListOf<Pair<Coordinate, Int>>()

        for (row in cave.indices) {
            for ((column, value) in cave[row].withIndex()) {
                if (value < skip) {
                    val coordinate = Coordinate(row, column)
                    if (isLowest(value, coordinate)) lowest.add(Pair(coordinate, value))
                }
            }
        }
        return lowest
    }

    private fun isLowest(value: Int, coordinate: Coordinate): Boolean {
        for (direction in directions) {
            if (value >= getValue(coordinate + direction.coordinate)) return false
        }
        return true
    }

    private fun getValue(coordinate: Coordinate) = cave.getOrNull(coordinate) ?: skip

    fun lowestSum() = lowestPoints.sumOf { it.second + 1 }

    fun calculateBasin(): Int {
        val (num1, num2, num3) = lowestPoints.map { findAllNeighbors(it.first) }.sortedDescending()

        return num1 * num2 * num3
    }

    private fun findAllNeighbors(coordinate: Coordinate): Int {
        val checked = mutableListOf<Coordinate>()
        val toCheck = mutableListOf(coordinate)

        while (toCheck.isNotEmpty()) {
            toCheck.first().let {
                toCheck.addAll(findNeighbors(it, checked + toCheck))
                checked.add(it)
                toCheck.remove(it)
            }
        }
        return checked.size
    }

    private fun findNeighbors(coordinate: Coordinate, filter: List<Coordinate>): List<Coordinate> {
        val neighbors = mutableListOf<Coordinate>()

        for (direction in directions) {
            val moved = coordinate + direction.coordinate
            if (getValue(moved) < skip && !filter.contains(moved)) neighbors.add(moved)
        }
        return neighbors
    }

    companion object {
        private const val skip = 9
        private val directions = listOf(UP, DOWN, LEFT, RIGHT)

        fun getCave(input: List<String>) = Cave(input.map { line -> line.map { it.digitToInt() } })
    }
}