import utils.readInput

private const val FORWARD = "forward"
private const val DOWN = "down"
private const val UP = "up"

fun main() {
    val testInput = getPairs(readInput("Day02_test"))
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = getPairs(readInput("Day02"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<Pair<String, Int>>): Int {
    var horizontal = 0
    var depth = 0

    input.forEach { (direction, num) ->
        when (direction) {
            FORWARD -> horizontal += num
            DOWN -> depth += num
            UP -> depth -= num
        }
    }
    return horizontal * depth
}

private fun part2(input: List<Pair<String, Int>>): Int {
    var horizontal = 0
    var depth = 0
    var aim = 0

    input.forEach { (direction, num) ->
        when (direction) {
            FORWARD -> {
                horizontal += num
                depth += num * aim
            }
            DOWN -> aim += num
            UP -> aim -= num
        }
    }
    return horizontal * depth
}

private fun getPairs(input: List<String>) =
    input.map { string -> string.split(" ").let { it[0] to it[1].toInt() } }