import utils.readInput

fun main() {
    val testInput = VelocityProcess.get(readInput("Day17_test"))
    check(part1(testInput) == 45) { "Wrong Answer for test 1" }
    check(part2(testInput) == 112) { "Wrong Answer for test 2" }

    val input = VelocityProcess.get(readInput("Day17"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: VelocityProcess) = input.trickShot

private fun part2(input: VelocityProcess) = input.distinctVelocity

private class VelocityProcess private constructor(val trickShot: Int, val distinctVelocity: Int) {

    companion object {
        fun get(input: List<String>): VelocityProcess {
            val ranges = getRanges(input)
            val y1 = ranges.y.first
            var maxY = 0
            var count = 0

            for (x in 1..ranges.x.last) {
                for (y in y1 until -y1) {
                    val velocity = testVelocity(x, y, ranges.x, ranges.y)
                    if (velocity.passedThrough) {
                        count++
                        if (velocity.maxY > maxY) maxY = velocity.maxY
                    }
                }
            }
            return VelocityProcess(maxY, count)
        }

        private fun testVelocity(_x: Int, _y: Int, xRange: IntRange, yRange: IntRange): VelocityMaxY {
            val x = VelocityInfo(_x, _x)
            val y = VelocityInfo(_y, _y)
            var max = _y
            var passedThrough = false

            while (y.position >= yRange.first && x.position <= xRange.last) {
                if (!passedThrough && xRange.contains(x.position) && yRange.contains(y.position)) passedThrough = true
                if (y.position > max) max = y.position
                if (x.velocity > 0) x.position += --x.velocity
                y.position += --y.velocity
            }
            return if (passedThrough) VelocityMaxY(max, true) else VelocityMaxY()
        }

        private fun getRanges(input: List<String>) = input.first().replace("target area: ", "")
            .split(", ").map { it.split("=").last().split("..") }
            .map { it.first().toInt()..it.last().toInt() }.let { RangeXY(it.first(), it.last()) }
    }
}

private class RangeXY(val x: IntRange, val y: IntRange)

private class VelocityInfo(var position: Int, var velocity: Int)

private class VelocityMaxY(val maxY: Int = 0, val passedThrough: Boolean = false)