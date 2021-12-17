import utils.Coordinate
import utils.Direction.*
import utils.getOrNull
import utils.readInput
import java.util.*

private const val RESET = 10

fun main() {
    val testInput = Chitons(readInput("Day15_test"))
    check(part1(testInput) == 40) { "Wrong Answer for test 1" }
    check(part2(testInput) == 315) { "Wrong Answer for test 2" }

    val input = Chitons(readInput("Day15"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: Chitons) = input.sampleCaveLowestRisk

private fun part2(input: Chitons) = input.fullCaveLowestRisk

private class Chitons(private val cave: List<String>) {
    val sampleCaveLowestRisk by lazy { lowestRisk(cave) }
    val fullCaveLowestRisk by lazy { lowestRisk(cave, true) }

    companion object {
        private val directions = listOf(UP, DOWN, LEFT, RIGHT)

        private fun lowestRisk(cave: List<String>, isFullCave: Boolean = false): Int {
            val riskLevels = if (isFullCave) getFullCave(cave) else getSampleCave(cave)
            val start = riskLevels.first().first().also { it.distance = 0 }
            val end = riskLevels.last().last()
            val compareByDistance: Comparator<Risk> = compareBy { it.distance }
            val priorityQueue = PriorityQueue(compareByDistance).also { it.add(start) }

            while (priorityQueue.isNotEmpty() && !end.isProcessed) {
                priorityQueue.remove().let { risk ->
                    if (!risk.isProcessed) getAllNeighbors(risk.coordinate, riskLevels).let { neighbors ->
                        setRiskMin(risk, neighbors)
                        neighbors.filterNot { it.isProcessed }.let { unProcessedNeighbors ->
                            setNeighborsMin(risk, unProcessedNeighbors)
                            priorityQueue.addAll(unProcessedNeighbors)
                        }
                        risk.isProcessed = true
                    }
                }
            }
            return end.distance
        }

        private fun setRiskMin(risk: Risk, neighbors: List<Risk>) {
            val min = neighbors.minOf { it.distance } + risk.weight

            if (risk.distance > min) risk.distance = min
        }

        private fun setNeighborsMin(risk: Risk, neighbors: List<Risk>) {
            for (neighbor in neighbors) {
                val weight = risk.distance + neighbor.weight
                if (neighbor.distance > weight) neighbor.distance = weight
            }
        }

        private fun getAllNeighbors(coordinate: Coordinate, riskLevels: List<List<Risk>>): List<Risk> {
            val list = mutableListOf<Risk>()

            for (direction in directions) {
                riskLevels.getOrNull(coordinate + direction.coordinate)?.let { list.add(it) }
            }
            return list
        }

        private fun getSampleCave(input: List<String>) = input
            .mapIndexed { i, line -> mapLine(line).mapIndexed { j, num -> Risk(Coordinate(i, j), num) } }

        private fun getFullCave(input: List<String>): List<List<Risk>> {
            val list = input.map { expandLine(it) }.toMutableList()
            val size = list.size

            repeat(4) {
                (if (list.size > size) list.drop(list.size - size) else list.toList()).forEach {
                    list.add(it.map { num -> transform(num) })
                }
            }
            return list.mapIndexed { i, line -> line.mapIndexed { j, num -> Risk(Coordinate(i, j), num) } }
        }

        private fun expandLine(line: String): List<Byte> {
            val list = mutableListOf(mapLine(line))

            repeat(4) {
                list.add(list.last().map { transform(it) })
            }
            return list.flatten()
        }

        private fun transform(num: Byte) = (num + 1).let { (if (it == RESET) 1 else it).toByte() }

        private fun mapLine(line: String) = line.map { it.digitToInt().toByte() }
    }
}

private class Risk(
    val coordinate: Coordinate,
    val weight: Byte,
    var distance: Int = Int.MAX_VALUE - RESET,
    var isProcessed: Boolean = false
)