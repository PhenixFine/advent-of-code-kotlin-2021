import utils.Coordinate
import utils.Direction
import utils.Direction.*
import utils.getOrNull
import utils.readInput

fun main() {
    val testInput = ImageEnhancement.get(readInput("Day20_test"))
    check(part1(testInput) == 35) { "Wrong Answer for test 1" }
    check(part2(testInput) == 3351) { "Wrong Answer for test 2" }

    val input = ImageEnhancement.get(readInput("Day20"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: ImageEnhancement) = pixelCount(input, 2)

private fun part2(input: ImageEnhancement) = pixelCount(input, 50)

private fun pixelCount(input: ImageEnhancement, steps: Int): Int {
    input.process(steps)

    return input.countPixels()
}

private class ImageEnhancement(val algorithm: List<Int>, val image: List<List<Int>>, val blink: Boolean) {
    private var currentImage = image
    private var step = 1
    private var padding = 0

    fun countPixels() = currentImage.flatten().count { it == 1 }

    fun process(steps: Int) {
        if ((blink && steps % 2 != 0) || steps < 1) error("Invalid number of steps") else if (steps >= step) {
            do {
                if (blink) padding = if (step % 2 == 0) 1 else 0
                padImage()
                enhance()
            } while (++step <= steps)
        } else {
            currentImage = image
            step = 1
            process(steps)
        }
    }

    private fun padImage() {
        currentImage = List(currentImage.size + 2) { i ->
            if (i > 0 && i < currentImage.size + 1) {
                listOf(padding) + currentImage[i - 1] + listOf(padding)
            } else List(currentImage.first().size + 2) { padding }
        }
    }

    private fun enhance() {
        currentImage = List(currentImage.size) { i ->
            List(currentImage.first().size) { j ->
                getResult(currentImage[i][j], Coordinate(i, j))
            }
        }
    }

    private fun getResult(num: Int, coordinate: Coordinate): Int {
        val part1 = getDirections(listOf(UP_LEFT, UP, UP_RIGHT, LEFT), coordinate)
        val part2 = "$num" + getDirections(listOf(RIGHT, DOWN_LEFT, DOWN, DOWN_RIGHT), coordinate)

        return algorithm[(part1 + part2).toInt(2)]
    }

    private fun getDirections(directions: List<Direction>, coordinate: Coordinate): String {
        var result = ""

        directions.forEach { direction ->
            result += currentImage.getOrNull(direction.coordinate + coordinate) ?: padding
        }
        return result
    }

    companion object {

        fun get(input: List<String>): ImageEnhancement {
            val algorithm = convert(input.first()).map { it.digitToInt() }
            val image = input.subList(2, input.size).map { string -> convert(string).map { it.digitToInt() } }

            return ImageEnhancement(algorithm, image, algorithm.first() == 1)
        }

        private fun convert(string: String) = string.replace(".", "0").replace("#", "1")
    }
}