import utils.readInput

fun main() {
    val testInput = Decoder(readInput("Day16_test").first()).transmission
    check(part1(testInput) == 20) { "Wrong Answer for test 1" }
    check(part2(testInput) == 1L) { "Wrong Answer for test 2" }

    val input = Decoder(readInput("Day16").first()).transmission
    println(part1(input))
    println(part2(input))
}

private fun part1(input: Transmission) = input.versionSum

private fun part2(input: Transmission) = input.number

private class Decoder(input: String) {
    private var versionNum = 0
    val transmission: Transmission

    init {
        val list = getAll(hexToBinary(input))
        check(list.size == 1) { "wrong list size for init" }
        transmission = Transmission(versionNum, list.first())
    }

    private fun hexToBinary(hex: String) = hex.map {
        String.format("%4s", it.digitToInt(16).toString(2)).replace(' ', '0')
    }.joinToString("")

    private fun getAll(_binary: String): List<Long> {
        val numbers = mutableListOf<Long>()
        var binary = _binary

        while (!binary.all { it == '0' } && binary.isNotEmpty()) {
            val hold = getNext(binary)
            binary = hold.first
            numbers.add(hold.second)
        }
        return numbers
    }

    private fun getNext(_binary: String): Pair<String, Long> {
        versionNum += _binary.substring(0, 3).toInt(2)
        val typeID = _binary.substring(3, 6).toInt(2)
        val binary = _binary.drop(6)

        return when (typeID) {
            4 -> getLiteral(binary)
            else -> operator(binary, typeID)
        }
    }

    private fun getLiteral(_binary: String): Pair<String, Long> {
        var binary = _binary
        var num = ""

        while (true) {
            val first = binary.first()
            num += binary.substring(1, 5)
            binary = binary.drop(5)
            if (first == '0') return Pair(binary, num.toLong(2))
        }
    }

    private fun operator(_binary: String, typeID: Int): Pair<String, Long> {
        val num = if (_binary.first() == '0') 15 else 11
        val length = _binary.substring(1, num + 1).toInt(2)
        var binary = _binary.drop(num + 1)
        val numbers = mutableListOf<Long>()

        if (num == 15) {
            numbers.addAll(getAll(binary.substring(0, length)))
            binary = binary.drop(length)
        } else repeat(length) {
            val hold = getNext(binary)
            binary = hold.first
            numbers.add(hold.second)
        }
        return Pair(binary, operatorCalculate(typeID, numbers))
    }

    private fun operatorCalculate(typeID: Int, numbers: List<Long>) = when (typeID) {
        0 -> numbers.sum()
        1 -> numbers.reduce { acc, num -> acc * num }
        2 -> numbers.minOrNull() ?: error("no min")
        3 -> numbers.maxOrNull() ?: error("no max")
        5, 6, 7 -> if (numbers.size == 2) compare(typeID, numbers) else error("problem with compare Id: $typeID")
        else -> error("something went wrong with typeID $typeID in operatorCalculate")
    }

    private fun compare(typeID: Int, numbers: List<Long>) = when (typeID) {
        5 -> if (numbers.first() > numbers.last()) 1L else 0L
        6 -> if (numbers.first() < numbers.last()) 1L else 0L
        else -> if (numbers.first() == numbers.last()) 1L else 0L
    }
}

private data class Transmission(val versionSum: Int, val number: Long)