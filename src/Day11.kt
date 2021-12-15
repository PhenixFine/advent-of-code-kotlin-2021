import utils.Coordinate
import utils.Direction
import utils.getOrNull
import utils.readInput

fun main() {
    val testInput = Octopuses.get(readInput("Day11_test"))
    check(part1(testInput) == 1656) { "Wrong Answer for test 1" }
    check(part2(testInput) == 195) { "Wrong Answer for test 2" }

    val input = Octopuses.get(readInput("Day11"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: Octopuses): Int {
    var flashes = 0

    repeat(100) {
        flashes += input.step()
    }
    input.reset()
    return flashes
}

private fun part2(input: Octopuses) = input.findSync()

private class Octopuses private constructor(private val cavern: List<List<OctopusInfo>>) {

    fun reset() = cavern.forEach { list -> list.forEach { it.resetEnergy() } }

    fun findSync(): Int {
        var steps = 0

        while (cavern.any { list -> list.any { it.energy != 0 } }) {
            step()
            steps++
        }
        return steps
    }

    fun step(): Int {
        var flashes = 0

        for ((row, list) in cavern.withIndex()) {
            for ((column, octopus) in list.withIndex()) {
                if (!octopus.flashed) flashes += stepOctopus(Pair(Coordinate(row, column), octopus))
            }
        }
        cavern.forEach { list -> list.forEach { it.resetFlash() } }
        return flashes
    }

    private fun stepOctopus(_octopus: Pair<Coordinate, OctopusInfo>): Int {
        val octopuses = ArrayDeque(listOf(_octopus))
        var flashes = 0

        while (octopuses.isNotEmpty()) {
            val octopus = octopuses.removeLast()
            if (!octopus.second.flashed && octopus.second.stepFlashed()) {
                flashes++
                octopuses.addAll(getNeighbors(octopus.first))
            }
        }
        return flashes
    }

    private fun getNeighbors(coordinate: Coordinate): List<Pair<Coordinate, OctopusInfo>> {
        val list = mutableListOf<Pair<Coordinate, OctopusInfo>>()

        for (direction in Direction.values()) {
            val moved = coordinate + direction.coordinate
            cavern.getOrNull(moved)?.let { if (!it.flashed) list.add(Pair(moved, it)) }
        }
        return list
    }

    companion object {

        fun get(input: List<String>) =
            Octopuses(input.map { line -> line.map { char -> OctopusInfo(char.digitToInt()) } })
    }
}

private class OctopusInfo(private val initialEnergy: Int) {
    var energy = initialEnergy
        private set
    var flashed = false
        private set

    fun stepFlashed(): Boolean {
        return if (!flashed && ++energy == 10) {
            flashed = true
            energy = 0
            true
        } else false
    }

    fun resetFlash() {
        flashed = false
    }

    fun resetEnergy() {
        energy = initialEnergy
    }
}