package utils

import java.util.*

private val DAY = String.format("%02d", Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
private const val FOLDER = "text"
private val INPUT = getFile("$FOLDER/Day$DAY.txt")
private val TEST = getFile("$FOLDER/Day${DAY}_test.txt")
private val TEMPLATE = getFile("$FOLDER/template.txt")
private val KT_FILE = getFile("Day$DAY.kt")

fun main() {
    TEST.createNewFile()
    if (!INPUT.exists()) FetchInput.toFile(DAY.toInt(), INPUT)
    if (!KT_FILE.exists()) KT_FILE.writeText(String.format(TEMPLATE.readText(), DAY, DAY))
}