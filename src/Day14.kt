import utils.readInput

fun main() {
    val testInput = Polymerization.get(readInput("Day14_test"))
    check(part1(testInput) == 1588L) { "Wrong Answer for test 1" }
    check(part2(testInput) == 2188189693529L) { "Wrong Answer for test 2" }

    val input = Polymerization.get(readInput("Day14"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: Polymerization) = input.stepGrowth(10)

private fun part2(input: Polymerization) = input.stepGrowth(40)

private class Polymerization(val template: String, val insertion: Map<String, Char>) {
    private val letters = insertion.values.toSet().toList()
    private val pairs = insertion.keys.toList()

    fun stepGrowth(steps: Int): Long {
        val charCount = charMap()
        var oldPairCount = pairsMap()

        repeat(steps) {
            val newPairCount = pairs.associateWith { Count(0L) }

            oldPairCount.forEach { (pair, num) ->
                if (num.count > 0) {
                    val add = insertion[pair] ?: error("key: $pair, not found.")
                    newPairCount[pair.first().toString() + add]?.let { it.count += num.count }
                    newPairCount[add.toString() + pair.last()]?.let { it.count += num.count }
                    charCount[add]?.let { it.count += num.count }
                }
            }
            oldPairCount = newPairCount
        }
        return charCount.maxOf { map -> map.value.count } - charCount.minOf { map -> map.value.count }
    }

    private fun charMap(): Map<Char, Count> {
        val map = letters.associateWith { Count(0L) }.toMutableMap()

        template.forEach { map[it]?.let { num -> num.count++ } }
        return map
    }

    private fun pairsMap(): Map<String, Count> {
        val zip = template.zipWithNext { a, b -> "$a$b" }
        val map = pairs.associateWith { Count(0L) }

        zip.forEach { map[it]?.let { num -> num.count++ } }
        return map
    }

    companion object {

        fun get(input: List<String>): Polymerization {
            return Polymerization(input.first(), input.drop(2)
                .associate { it.substringBefore(" ") to it.substringAfterLast(" ").first() })
        }
    }
}

private data class Count(var count: Long)