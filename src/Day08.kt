import utils.readInput

fun main() {
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26) { "Wrong Answer for test 1" }
    check(part2(testInput) == 61229) { "Wrong Answer for test 2" }

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val sizes = listOf(2, 3, 4, 7)

    return input.sumOf { line ->
        line.split(" | ")[1].split(" ").count { sizes.contains(it.length) }
    }
}

private fun part2(input: List<String>) = input.sumOf { getNumbers(it) }

private fun getNumbers(line: String): Int {
    val (left, right) = line.split(" | ").map { getList(it) }
    val mapLeft = mapLeft(left).map { it.value to it.key }.toMap()
    var output = ""

    for (string in right) output += mapLeft[string] ?: 0
    return output.toInt()
}

private fun getList(string: String) =
    string.split(" ").map { it.toCharArray().sorted().joinToString("") }

private fun mapLeft(list: List<String>): Map<Int, String> {
    val map = getEasy(list)

    return map + getDifficult(list, map[1] ?: "", map[4] ?: "")
}

private fun getEasy(list: List<String>): Map<Int, String> {
    val map = mutableMapOf<Int, String>()

    list.forEach { string ->
        when (string.length) {
            2 -> map[1] = string
            3 -> map[7] = string
            4 -> map[4] = string
            7 -> map[8] = string
        }
    }
    return map
}

private fun getDifficult(list: List<String>, one: String, four: String): Map<Int, String> {
    val fiveLength = list.filter { it.length == 5 }.toMutableList()
    val sixLength = list.filter { it.length == 6 }.toMutableList()
    val map = mutableMapOf<Int, String>()

    map[2] = getString(fiveLength, four, 3).also { fiveLength.remove(it) }
    map[3] = getString(fiveLength, one, 3).also { fiveLength.remove(it) }
    map[6] = getString(sixLength, one, 5).also { sixLength.remove(it) }
    map[0] = getString(sixLength, four, 3).also { sixLength.remove(it) }
    map[5] = fiveLength.first()
    map[9] = sixLength.first()
    return map
}

private fun getString(list: List<String>, filter: String, length: Int) = list.first { string ->
    string.count { !filter.contains(it) } == length
}