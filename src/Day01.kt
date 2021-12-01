fun main() {
    val testInput = readInput("Day01_test").map { it.toInt() }
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<Int>) = input.zipWithNext { a, b -> b - a }.count { it > 0 }

fun part2(input: List<Int>): Int {
    val sumsOfThree = mutableListOf<Int>()

    for (i in 0..input.lastIndex - 2) {
        val sum = input[i] + input[i + 1] + input[i + 2]
        sumsOfThree.add(sum)
    }
    return part1(sumsOfThree)
}