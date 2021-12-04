import utils.readInput

private const val ZERO = '0'
private const val ONE = '1'

fun main() {
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    var gamma = ""
    var epsilon = ""

    repeat(input.first().length) { i ->
        val isZeroMost = isZeroMostCommon(i, input)
        gamma += getZeroOrOne(isZeroMost)
        epsilon += getZeroOrOne(!isZeroMost)
    }
    return gamma.toInt(2) * epsilon.toInt(2)
}

private fun part2(input: List<String>): Int {
    val isZeroMost = isZeroMostCommon(0, input)
    val oxygen = getRating(LifeSupport(true, isZeroMost, input))
    val co2 = getRating(LifeSupport(false, isZeroMost, input))

    return oxygen * co2
}

private fun isZeroMostCommon(index: Int, input: List<String>): Boolean {
    return (input.count { it[index] == ZERO }) > input.size / 2
}

private fun getZeroOrOne(isZero: Boolean) = if (isZero) ZERO else ONE

private fun getRating(lifeSupport: LifeSupport): Int {
    var rating = ""

    with(lifeSupport) {
        repeat(input.first().length) { i ->
            rating += if (input.size > 1) {
                if (i > 0) isZeroMost = isZeroMostCommon(i, input)
                val filter = getZeroOrOne(if (isOxygen) isZeroMost else !isZeroMost)
                input = filterList(i, input, filter)
                filter
            } else input.first()[i]
        }
    }
    return rating.toInt(2)
}

private fun filterList(index: Int, input: List<String>, filter: Char): List<String> {
    return input.filter { it[index] == filter }
}

private data class LifeSupport(val isOxygen: Boolean, var isZeroMost: Boolean, var input: List<String>)