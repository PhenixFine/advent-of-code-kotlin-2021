import utils.readInput
import kotlin.math.abs

fun main() {
    val testInput = getCrabs(readInput("Day07_test"))
    check(part1(testInput) == 37) { "Wrong Answer for test 1" }
    check(part2(testInput) == 168) { "Wrong Answer for test 2" }

    val input = getCrabs(readInput("Day07"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<Int>) = calculate(input)

private fun part2(input: List<Int>) = calculate(input, true)

private fun getCrabs(input: List<String>): List<Int> {
    val numbers = input.first().split(",").map { it.toInt() }
    val crabs = IntArray((numbers.maxOrNull() ?: 0) + 1)

    numbers.forEach { crabs[it]++ }
    return crabs.toList()
}

private fun calculate(input: List<Int>, isSumOf: Boolean = false): Int {
    var checkSet = setOf(0, input.lastIndex / 2, input.lastIndex)
    var lastSet = setOf<Int>()
    var lowestFuel = Pair(0, Int.MAX_VALUE)

    while (!lastSet.containsAll(checkSet)) {
        var fuel = checkSet.map { it to calculateFuel(input, it, isSumOf) }.sortedBy { it.second }
        if (fuel.first().second < lowestFuel.second) lowestFuel = fuel.first()
        fuel = (if (fuel.size > 2) fuel.subList(0, 2) else fuel).sortedBy { it.first }
        lastSet = checkSet
        checkSet = if (fuel.size == 1) setOf(fuel.first().first) else getSearchIndexes(fuel, input.lastIndex)
    }
    return lowestFuel.second
}

private fun calculateFuel(input: List<Int>, moveTo: Int, isSumOf: Boolean): Int {
    var fuel = 0

    for ((index, count) in input.withIndex()) {
        if (count > 0 && index != moveTo) {
            val num = abs(index - moveTo)
            fuel += (if (isSumOf) sumOfNumber(num) else num) * count
        }
    }
    return fuel
}

private fun getSearchIndexes(input: List<Pair<Int, Int>>, max: Int): Set<Int> {
    val first = (input[0].first + 1).coerceAtMost(max)
    val last = (input[1].first - 1).coerceAtLeast(0)

    return setOf(first, first + (last - first) / 2, last)
}

private fun sumOfNumber(num: Int) = num * (1 + num) / 2