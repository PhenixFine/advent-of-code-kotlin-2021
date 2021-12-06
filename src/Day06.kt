import utils.readInput

private const val BABY_FISH = 8
private const val ADULT_FISH = 6
private const val FISH_BIRTH = 0

fun main() {
    val testInput = getStartingFish(readInput("Day06_test"))
    check(part1(testInput) == 5934L) { "Wrong Answer for test 1" }
    check(part2(testInput) == 26984457539L) { "Wrong Answer for test 2" }

    val input = getStartingFish(readInput("Day06"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<Long>) = calculateFish(input, 80)

private fun part2(input: List<Long>) = calculateFish(input, 256)

private fun getStartingFish(input: List<String>): List<Long> {
    val fish = LongArray(BABY_FISH + 1)

    input.first().split(",").forEach { fish[it.toInt()]++ }
    return fish.toList()
}

private fun calculateFish(input: List<Long>, days: Int): Long {
    var fish = input

    repeat(days) {
        fish = calculateFishDay(fish)
    }
    return fish.sum()
}

private fun calculateFishDay(input: List<Long>): List<Long> {
    val fish = LongArray(BABY_FISH + 1)

    for ((index, fishCount) in input.withIndex()) {
        when (index) {
            FISH_BIRTH -> {
                fish[BABY_FISH] = fishCount
                fish[ADULT_FISH] = fishCount
            }
            else -> fish[index - 1] += fishCount
        }
    }
    return fish.toList()
}