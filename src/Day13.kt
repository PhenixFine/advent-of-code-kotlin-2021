import utils.readInput
import utils.set
import utils.xyToCoordinate

private const val DOT = "█"
private const val EMPTY = "."

fun main() {
    val testInput = OrigamiInfo.get(readInput("Day13_test"))
    check(part1(testInput) == 17) { "Wrong Answer for test 1" }
    part2(testInput)

    val input = OrigamiInfo.get(readInput("Day13"))
    println(part1(input))
    part2(input)
}

private fun part1(input: OrigamiInfo) = Origami.foldOnce(input).paper.sumOf { line -> line.count { it == DOT } }

private fun part2(input: OrigamiInfo) = Origami.foldAll(input).printPaper()

private object Origami {

    fun foldAll(info: OrigamiInfo): OrigamiInfo {
        var paper = info

        while (paper.folds.isNotEmpty()) {
            paper = foldOnce(paper)
        }
        return paper
    }

    fun foldOnce(info: OrigamiInfo): OrigamiInfo {
        if (info.folds.first().isNotEmpty()) {
            val num = info.folds.first().split("=").last().toInt()

            when (info.folds.first().first()) {
                'y' -> return OrigamiInfo(info.folds.drop(1), foldUp(num, info.paper))
                'x' -> return OrigamiInfo(info.folds.drop(1), foldLeft(num, info.paper))
            }
        }
        return info
    }

    fun foldUp(newSize: Int, paper: List<List<String>>): List<List<String>> {
        val list = MutableList(newSize) { i -> MutableList(paper.first().size) { j -> paper[i][j] } }
        var count = newSize + 1

        for (row in list.lastIndex downTo 0) {
            for (column in list.first().indices) {
                val check = if (count <= paper.lastIndex) paper[count][column] else ""
                if (check == DOT) {
                    list[row][column] = check
                }
            }
            count++
        }
        return list
    }

    fun foldLeft(newSize: Int, paper: List<List<String>>): List<List<String>> {
        val list = MutableList(paper.size) { i -> MutableList(newSize) { j -> paper[i][j] } }

        for (row in list.indices) {
            var count = newSize + 1
            for (column in list.first().lastIndex downTo 0) {
                val check = if (count <= paper.first().lastIndex) paper[row][count] else ""
                if (check == DOT) {
                    list[row][column] = check
                }
                count++
            }
        }
        return list
    }
}

private class OrigamiInfo(val folds: List<String>, val paper: List<List<String>>) {

    fun printPaper() {
        for (line in paper) println(line.joinToString(""))
    }

    companion object {

        fun get(input: List<String>): OrigamiInfo {
            val coordinates = input.filter { it.contains(",") }.map { it.xyToCoordinate() }
            val maxRow = coordinates.maxOf { it.rowY } + 1
            val maxColumn = coordinates.maxOf { it.columnX } + 1
            val folds = input.filter { it.contains("fold") }.map { it.substringAfterLast(" ") }
            val paper = MutableList(maxRow) { MutableList(maxColumn) { EMPTY } }

            coordinates.forEach { paper[it] = DOT }
            return OrigamiInfo(folds, paper)
        }
    }
}