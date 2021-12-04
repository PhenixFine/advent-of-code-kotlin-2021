import utils.readInput

private const val GAME_ERROR = "Something went wrong with %s"

fun main() {
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val winNumbers = winningNumbers(input)
    val boards = getBoards(input)

    for (num in winNumbers) {
        for (board in boards) {
            if (board.isWin(num)) {
                return board.calculate() * num
            }
        }
    }
    error(String.format(GAME_ERROR, "part1"))
}

private fun part2(input: List<String>): Int {
    val winNumbers = winningNumbers(input)
    val boards = getBoards(input)
    val boardsRemoved = boards.toMutableList()

    for (num in winNumbers) {
        for (board in boards) {
            if (boardsRemoved.contains(board) && board.isWin(num)) {
                if (boardsRemoved.size == 1) {
                    return board.calculate() * num
                } else boardsRemoved.remove(board)
            }
        }
    }
    error(String.format(GAME_ERROR, "part2"))
}

private fun winningNumbers(input: List<String>) = input.first().split(",").map { it.toInt() }

private fun getBoards(input: List<String>): List<Board> {
    val boards = mutableListOf<Board>()
    var numberMap = mutableMapOf<Int, NumberInfo>()
    var row = 0

    for (i in 2..input.lastIndex) {
        val line = input[i].trim().replace("  ", " ")

        if (line.isNotEmpty()) {
            numberMap += getNumbers(line, row)
            if (++row == 5) {
                boards.add(Board(numberMap))
                numberMap = mutableMapOf()
                row = 0
            }
        }
    }
    return boards
}

private fun getNumbers(number: String, row: Int): Map<Int, NumberInfo> {
    val numbers = number.split(" ").map { it.toInt() }
    val numberMap = mutableMapOf<Int, NumberInfo>()

    for ((column, num) in numbers.withIndex()) {
        numberMap[num] = NumberInfo(row, column)
    }
    return numberMap
}

private class NumberInfo(val row: Int, val column: Int, var marked: Boolean = false)

private class Board(val numbersMap: Map<Int, NumberInfo>) {
    private val rows = Array(5) { 0 }
    private val columns = Array(5) { 0 }

    fun isWin(num: Int): Boolean {
        val number = numbersMap.getOrDefault(num, null) ?: return false

        if (!number.marked) {
            number.marked = true
            rows[number.row] += 1
            columns[number.column] += 1
        }
        return rows.any { it == 5 } || columns.any { it == 5 }
    }

    fun calculate() = numbersMap.map { if (!it.value.marked) it.key else 0 }.sum()
}