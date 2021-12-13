import utils.readInput

fun main() {
    val testInput = PassagePathing.get(readInput("Day12_test"))
    check(part1(testInput) == 226) { "Wrong Answer for test 1" }
    check(part2(testInput) == 3509) { "Wrong Answer for test 2" }

    val input = PassagePathing.get(readInput("Day12"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: PassagePathing) = input.oneVisit.size

private fun part2(input: PassagePathing) = input.twoVisits.size

private class PassagePathing private constructor(val start: List<String>, val caves: Map<String, List<String>>) {
    val oneVisit: List<List<String>> by lazy { findPaths() }
    val twoVisits: List<List<String>> by lazy { twoVisits() }

    private fun twoVisits(): List<List<String>> {
        val paths = oneVisit.toMutableList()
        val smallCaves = caves.keys.filter { it.first().isLowerCase() && it != END }

        for (cave in smallCaves) {
            findPaths(cave).filter { list -> list.count { it == cave } == 2 }.forEach { paths.add(it) }
        }
        return paths
    }

    private fun findPaths(twoTurns: String = ""): List<List<String>> {
        val paths = mutableListOf<List<String>>()

        for (cave in start) {
            val visited = if (isSmallCave(cave, twoTurns)) listOf(cave) else emptyList()
            val toVisit = caves[cave] ?: emptyList()
            followPath(visited, toVisit, listOf(cave), twoTurns).filter { it.contains(END) }.forEach { paths.add(it) }
        }
        return paths
    }

    private fun followPath(
        visited: List<String>,
        toVisit: List<String>,
        paths: List<String>,
        twoVisits: String,
        isTwoVisits: Boolean = false
    ): List<List<String>> {

        val list = mutableListOf<List<String>>()
        val isTwo = if (twoVisits.isNotEmpty() && !isTwoVisits) paths.count { it == twoVisits } == 2 else isTwoVisits
        val add = if (isTwo && !visited.contains(twoVisits)) visited + twoVisits else visited

        for (cave in toVisit) {
            val checked = (if (isSmallCave(cave, twoVisits)) listOf(cave) else emptyList()) + add
            val check = (caves[cave] ?: emptyList()).filterNot { checked.contains(it) }
            if (check.isEmpty()) list.add(paths + cave) else {
                list.addAll(followPath(checked, check, paths + cave, twoVisits, isTwo))
            }
        }
        return list
    }

    private fun isSmallCave(cave: String, twoTurns: String) = cave.first().isLowerCase() && cave != twoTurns

    companion object {
        const val START = "start"
        const val END = "end"
        const val SPLIT = "-"

        fun get(input: List<String>): PassagePathing {
            val start = input.filter { it.contains(START) }.map { it.removeSplitAnd(START) }
            val end = input.filter { it.contains(END) }.map { it.removeSplitAnd(END) }
            val list = input.filterNot { it.contains(START) || it.contains(END) }
                .map { it.split(SPLIT) }.map { it.first() to it.last() }
            val map = mutableMapOf<String, List<String>>()

            for ((first, second) in list) {
                map[first] = (map[first] ?: emptyList()) + listOf(second)
                map[second] = (map[second] ?: emptyList()) + listOf(first)
            }
            map[END] = emptyList()
            end.forEach { map[it] = (map[it] ?: emptyList()) + listOf(END) }
            return PassagePathing(start, map)
        }

        private fun String.removeSplitAnd(string: String) =
            replace(SPLIT, "").replace(string, "")
    }
}