import utils.readInput
import kotlin.math.abs

fun main() {
    val testInput = Scanners.get(readInput("Day19_test")).process()
    check(part1(testInput) == 79) { "Wrong Answer for test 1" }
    check(part2(testInput) == 3621) { "Wrong Answer for test 2" }

    val input = Scanners.get(readInput("Day19")).process()
    println(part1(input))
    println(part2(input))
}

private fun part1(input: ProbeInfo) = input.beacons.size

private fun part2(input: ProbeInfo) =
    input.scanners.maxOf { point -> input.scanners.maxOf { point.distanceManhattan(it) } }

private class ProbeInfo(val scanners: List<Point3d>, val beacons: List<Point3d>)

private class Scanners(data: List<List<Point3d>>) {
    private val beacons = mutableMapOf<Point3d, List<Int>>()
    private val unprocessedScanners = mutableListOf<Map<Point3d, List<Int>>>()
    private val scanners = mutableListOf<Point3d>()

    init {
        beacons.putAll(getDistances(data.first()))
        data.subList(1, data.size).forEach { unprocessedScanners.add(getDistances(it)) }
        scanners.add(Point3d(0, 0, 0))
    }

    private fun getDistances(scanner: List<Point3d>): Map<Point3d, List<Int>> {
        val beaconsMap = mutableMapOf<Point3d, List<Int>>()
        val checked = mutableListOf<Int>()

        scanner.forEachIndexed { i, beacon1 ->
            checked.add(i)
            scanner.forEachIndexed { j, beacon2 ->
                if (!checked.contains(j)) {
                    val distance = beacon1.distanceSquared(beacon2)
                    beaconsMap[beacon1] = (beaconsMap[beacon1] ?: emptyList()) + distance
                    beaconsMap[beacon2] = (beaconsMap[beacon2] ?: emptyList()) + distance
                }
            }
        }
        return beaconsMap
    }

    fun process(): ProbeInfo {
        while (unprocessedScanners.isNotEmpty()) {
            for ((index, scanner) in unprocessedScanners.withIndex()) {
                val beaconMatches = scanner.map { beaconsMatch(it.key, it.value) }.filterNotNull()
                if (beaconMatches.size >= 12) {
                    mergeScanners(index, beaconMatches)
                    break
                }
            }
        }
        return ProbeInfo(scanners, beacons.map { it.key })
    }

    private fun beaconsMatch(beacon: Point3d, distance: List<Int>): Pair<Point3d, Point3d>? {
        beacons.forEach { (beaconOrigin, distanceOrigin) ->
            val combine = (distance + distanceOrigin).toHashSet()
            if (combine.size + 10 < distance.size + distanceOrigin.size) {
                beacons[beaconOrigin] = combine.toList()
                return Pair(beaconOrigin, beacon)
            }
        }
        return null
    }

    private fun mergeScanners(index: Int, beaconMatches: List<Pair<Point3d, Point3d>>) {
        val notMatches = unprocessedScanners[index].filterNot { match -> beaconMatches.any { match.key == it.second } }
        val shift = scannerShift(beaconMatches)

        notMatches.forEach { beaconMap ->
            beaconMap.key.rotate(shift)
            val coordinate = beaconMap.key.currentPoint.plus(scanners.last())
            beacons[coordinate] = ((beacons[coordinate] ?: emptyList()) + beaconMap.value).distinct()
        }
        unprocessedScanners.removeAt(index)
    }

    private fun scannerShift(beaconMatches: List<Pair<Point3d, Point3d>>): Int {
        repeat(24) { num ->
            val scanner = beaconMatches.map { it.first.minus(it.second.currentPoint) }.toHashSet()
            if (scanner.size == 1) {
                scanners.add(scanner.first())
                return num + 1
            } else {
                beaconMatches.forEach { it.second.rotate() }
            }
        }
        error("Something went wrong with getShift")
    }


    companion object {

        fun get(input: List<String>): Scanners {
            var scanner = mutableListOf<String>()
            val scanners = mutableListOf<List<Point3d>>()
            var index = 1

            while (index <= input.lastIndex) {
                val line = input[index]
                if (line.isEmpty()) {
                    index += 2
                    scanners.add(processInput(scanner))
                    scanner = mutableListOf()
                } else {
                    index++
                    scanner.add(line)
                }
            }
            scanners.add(processInput(scanner))
            return Scanners(scanners)
        }

        private fun processInput(scanner: List<String>): List<Point3d> {
            val beacons = mutableListOf<Point3d>()
            scanner.forEach { beacon ->
                val (x, y, z) = beacon.split(",").map { it.toInt() }
                beacons.add(Point3d(x, y, z))
            }
            return beacons
        }
    }
}

/**
 * based on javax Point3d with rotating as an option
 */
private data class Point3d(val x: Int, val y: Int, val z: Int) {
    var currentPoint = this
        private set
    private var rotatePosition = 1
        set(value) {
            field = if (value == 25) 1 else value
        }

    operator fun minus(point: Point3d) = Point3d(x - point.x, y - point.y, z - point.z)

    operator fun plus(point: Point3d) = Point3d(x + point.x, y + point.y, z + point.z)

    fun distanceSquared(point: Point3d) = square(x - point.x) + square(y - point.y) + square(z - point.z)

    private fun square(num: Int) = num * num

    fun distanceManhattan(point: Point3d) = abs(x - point.x) + abs(y - point.y) + abs(z - point.z)

    fun rotate(position: Int = ++rotatePosition) {
        currentPoint = when (position) {
            1 -> Point3d(x, y, z)
            2 -> Point3d(x, -y, -z)
            3 -> Point3d(x, z, -y)
            4 -> Point3d(x, -z, y)
            5 -> Point3d(-x, y, -z)
            6 -> Point3d(-x, -y, z)
            7 -> Point3d(-x, z, y)
            8 -> Point3d(-x, -z, -y)
            9 -> Point3d(y, x, -z)
            10 -> Point3d(y, -x, z)
            11 -> Point3d(y, z, x)
            12 -> Point3d(y, -z, -x)
            13 -> Point3d(-y, x, z)
            14 -> Point3d(-y, -x, -z)
            15 -> Point3d(-y, z, -x)
            16 -> Point3d(-y, -z, x)
            17 -> Point3d(z, y, -x)
            18 -> Point3d(z, -y, x)
            19 -> Point3d(z, x, y)
            20 -> Point3d(z, -x, -y)
            21 -> Point3d(-z, x, -y)
            22 -> Point3d(-z, -x, y)
            23 -> Point3d(-z, y, x)
            24 -> Point3d(-z, -y, -x)
            else -> error("Invalid Position")
        }
    }
}