import utils.readInput

fun main() {
    val testInput = SnailFishMath.process(readInput("Day18_test"))
    check(part1(testInput) == 4140) { "Wrong Answer for test 1" }
    check(part2(testInput) == 3993) { "Wrong Answer for test 2" }

    val input = SnailFishMath.process(readInput("Day18"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: SnailFishMath) = input.finalSum

private fun part2(input: SnailFishMath) = input.bestOfTwo

private data class SnailFishMath(val finalSum: Int, val bestOfTwo: Int) {

    companion object {
        private const val OLD_START = '['
        private const val START = '('
        private const val OLD_END = ']'
        private const val END = ')'
        private const val COMMA = ','
        private const val COMBO = "$START$END$COMMA"
        private const val EXPLODE = "0"
        private const val MAX = '9'
        private const val MAX_NUM = 9
        private const val MINUS = 48 // used to return a char greater than 9 to its Int value
        private val PATTERN = Regex("[$START]\\d+[$COMMA]\\d+[$END]")

        fun process(input: List<String>): SnailFishMath {
            val converted = input.map { it.replace(OLD_START, START).replace(OLD_END, END) }
            val reduced = converted.reduce { acc, s -> addTwoLines(acc, s) }
            val maxOfTwo = converted.maxOf { line ->
                converted.filterNot { it == line }.maxOf { getMagnitude(addTwoLines(line, it)) }
            }
            return SnailFishMath(getMagnitude(reduced), maxOfTwo)
        }

        private fun addTwoLines(line1: String, line2: String): String {
            var oldLine: String
            var line = "$START$line1$COMMA$line2$END"

            do {
                oldLine = line
                line = explode(line)
                if (line == oldLine) line = split(line)
            } while (line != oldLine)
            return line
        }

        private fun explode(line: String): String {
            val deque = ArrayDeque<Char>()

            for ((index, char) in line.withIndex()) {
                when (char) {
                    START -> deque.add(char)
                    END -> deque.removeLast()
                    COMMA -> continue
                    else -> if (deque.size > 4) return explodeWork(line, index, char)
                }
            }
            return line
        }

        private fun explodeWork(line: String, index: Int, number: Char): String {
            var copy = line
            val numbers = line.mapIndexed { i, c -> if (!COMBO.contains(c)) i to c else null }.filterNotNull()
            val numbersIndex = numbers.indexOfFirst { it.first == index }

            if (numbersIndex != 0) {
                val previous = numbers[numbersIndex - 1]
                val replace = addExplosion(previous.second, number)
                copy = copy.replaceRange(previous.first..previous.first, replace)
            }
            if (numbersIndex + 2 <= numbers.lastIndex) {
                val next = numbers[numbersIndex + 2]
                val replace = addExplosion(numbers[numbersIndex + 1].second, next.second)
                copy = copy.replaceRange(next.first..next.first, replace)
            }
            return copy.replaceRange((index - 1)..(index + 3), EXPLODE)
        }

        private fun addExplosion(num1: Char, num2: Char) = getChar(getInt(num1) + getInt(num2)).toString()

        private fun split(line: String): String {
            for ((index, char) in line.withIndex()) {
                if (char > MAX) {
                    val num = getInt(char).let { getChar(it / 2) to getChar(it / 2 + it % 2) }
                    return line.replaceRange(index..index, "$START${num.first}$COMMA${num.second}$END")
                }
            }
            return line
        }

        private fun getMagnitude(converted: String): Int {
            var line = magnitudeReplaceEach(converted)

            while (line.contains(START)) line = magnitudeReplaceEach(line)
            return line.toInt()
        }

        private fun magnitudeReplaceEach(line: String) = line.replace(PATTERN) { magnitudeCalculate(it.value) }

        private fun magnitudeCalculate(string: String): String {
            val numbers = string.substring(1, string.lastIndex).split(COMMA).map { it.toInt() }

            return (numbers.first() * 3 + numbers.last() * 2).toString()
        }

        private fun getInt(num: Char) = if (num > MAX) num.code - MINUS else num.digitToInt()

        private fun getChar(num: Int) = if (num > MAX_NUM) MAX + num - MAX_NUM else num.digitToChar()
    }
}