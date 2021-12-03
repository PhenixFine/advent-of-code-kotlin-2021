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
    val oxygen = LifeSupport(true, isZeroMost, input).getRating()
    val co2 = LifeSupport(false, isZeroMost, input).getRating()

    return oxygen * co2
}

private fun isZeroMostCommon(index: Int, input: List<String>): Boolean {
    return (input.count { it[index] == ZERO }) > input.size / 2
}

private fun getZeroOrOne(isZero: Boolean) = if (isZero) ZERO else ONE

private class LifeSupport(
    private val isOxygen: Boolean,
    private var isZeroMost: Boolean,
    private var input: List<String>
) {

    fun getRating(): Int {
        var rating = ""

        repeat(input.first().length) { i ->
            rating += if (input.size > 1) {
                if (i > 0) isZeroMost = isZeroMostCommon(i, input)
                val filter = getZeroOrOne(if (isOxygen) isZeroMost else !isZeroMost)
                filterList(i, filter)
                filter
            } else input.first()[i]
        }
        return rating.toInt(2)
    }

    private fun filterList(index: Int, filter: Char) {
        input = input.filter { it[index] == filter }
    }
}