import utils.readInput

/**
 * I completed part 1 by myself, but I got the solution for part 2 from Jonathan Paulson:
 * https://github.com/jonathanpaulson/AdventOfCode/blob/master/2021/21.py
 * which I saw in his YouTube video: https://www.youtube.com/watch?v=a6ZdJEntKkk
 * I broke up his part two into functions [Dirac.play], [Dirac.countWins], and [Dirac.checkWin]; and also data classes
 * [PlayersEntry] and [Wins] for each player's data and the wins of each player needed for [Dirac.playersMap].
 * I currently can't think of a better way of solving part 2, so I think I'll leave it as is and move on ( I'd need
 * some time to forget his solution to be able to write my own version; and I don't think it would be as efficient
 * if I tried to solve it in a different way ).
 * I also adjusted my part 1 dice after seeing that it wasn't actually necessary to reset the dice when it gets to 100
 * since it's going to get remaindered by 10 when it gets added to the current player's position.
 */
fun main() {
    val testInput = Dirac(readInput("Day21_test"))
    check(part1(testInput) == 739785L) { "Wrong Answer for test 1" }
    check(part2(testInput) == 444356092776315L) { "Wrong Answer for test 2" }

    val input = Dirac(readInput("Day21"))
    println(part1(input))
    println(part2(input))
}

private fun part1(input: Dirac) = input.playPractice()

private fun part2(input: Dirac) = input.play()

private class Dirac(input: List<String>) {
    private val player1Start = getSpace(input.first())
    private val player2Start = getSpace(input.last())
    private val player1 = Player(player1Start, 0)
    private val player2 = Player(player2Start, 0)
    private val playersMap = mutableMapOf<PlayersEntry, Wins>()
    private var isPlayer1Turn = true

    fun playPractice(): Long {
        val dice = Dice()

        while (player1.score < 1000 && player2.score < 1000) practice(dice)
        return player1.score.coerceAtMost(player2.score) * dice.roll
    }

    private fun practice(dice: Dice) {
        val player = getPlayer()

        player.spot = (player.spot + dice.rollThree()) % 10
        player.score += player.spot + 1
        isPlayer1Turn = !isPlayer1Turn
    }

    private fun getPlayer() = if (isPlayer1Turn) player1 else player2

    private fun getSpace(line: String) = line.substringAfterLast(" ").toInt() - 1

    fun play() = countWins(PlayersEntry(player1Start, player2Start, 0, 0)).max()

    private fun countWins(entry: PlayersEntry): Wins {
        var wins = Wins(0L, 0L)
        val dice = 1..3

        for (d1 in dice) {
            for (d2 in dice) {
                for (d3 in dice) {
                    val spot1 = (entry.spot1 + d1 + d2 + d3) % 10
                    val score1 = entry.score1 + spot1 + 1
                    val (wins2, wins1) = checkWin(PlayersEntry(entry.spot2, spot1, entry.score2, score1))
                    wins = Wins(wins.count1 + wins1, wins.count2 + wins2)
                }
            }
        }
        playersMap[entry] = wins
        return wins
    }

    private fun checkWin(entry: PlayersEntry): Wins {
        return when {
            entry.score1 >= 21 -> Wins(1L, 0L)
            entry.score2 >= 21 -> Wins(0L, 1L)
            entry in playersMap -> playersMap[entry] ?: error("map entry in getWin lost")
            else -> countWins(entry)
        }
    }
}

private class Player(var spot: Int, var score: Long)

private class Dice {
    var roll = 0
        private set

    fun rollThree(times: Int = 3): Int {
        var add = 0

        repeat(times) { add += ++roll }
        return add
    }
}

private data class PlayersEntry(val spot1: Int, val spot2: Int, val score1: Long, val score2: Long)

private data class Wins(val count1: Long, val count2: Long) {

    fun max() = count1.coerceAtLeast(count2)
}