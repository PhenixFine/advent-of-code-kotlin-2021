import utils.readInput

fun main() {
    val testInput = SyntaxScoring.get(readInput("Day10_test"))
    check(part1(testInput) == 26397) { "Wrong Answer for test 1" }
    check(part2(testInput) == 288957L) { "Wrong Answer for test 2" }

    val input = SyntaxScoring.get(readInput("Day10"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: SyntaxScoring) = input.errorScore

private fun part2(input: SyntaxScoring) = input.middleScore

private class SyntaxScoring private constructor(val middleScore: Long, val errorScore: Int) {

    companion object {
        private val errorMap = mapOf(-1 to 3, -2 to 57, -3 to 1197, -4 to 25137)
        private val syntaxToNum =
            mapOf('(' to 1, ')' to -1, '[' to 2, ']' to -2, '{' to 3, '}' to -3, '<' to 4, '>' to -4)

        fun get(input: List<String>): SyntaxScoring {
            val values = input.map { parseInputLine(it) }
            val incomplete = values.filter { it.second == 0 }.map { it.first }

            return SyntaxScoring(getMiddlePoints(incomplete), values.sumOf { it.second })
        }

        private fun parseInputLine(line: String): Pair<ArrayDeque<Int>, Int> {
            val numbers = line.toCharArray().map { syntaxToNum[it] ?: 0 }
            val deque = ArrayDeque<Int>()

            for (num in numbers) {
                if (num > 0) deque.add(num) else if (num + deque.removeLast() != 0) {
                    return Pair(ArrayDeque(), errorMap[num] ?: 0)
                }
            }
            return Pair(deque, 0)
        }

        private fun getMiddlePoints(incomplete: List<ArrayDeque<Int>>): Long {
            val totalPoints = incomplete.map { getPoints(it) }.sorted()

            return totalPoints[totalPoints.size / 2]
        }

        private fun getPoints(numbers: ArrayDeque<Int>): Long {
            var points = 0L

            while (numbers.isNotEmpty()) {
                points *= 5
                points += numbers.removeLast()
            }
            return points
        }
    }
}