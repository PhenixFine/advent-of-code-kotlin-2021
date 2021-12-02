import java.util.*

private val DAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
private const val FOLDER = "textFiles"
private val INPUT = getFile(String.format("$FOLDER/Day%02d.txt", DAY))
private val TEST = getFile(String.format("$FOLDER/Day%02d_test.txt", DAY))
private val TEMPLATE = getFile("$FOLDER/template.txt")
private val KT_FILE = getFile(String.format("Day%02d.kt", DAY))

fun main() {
    INPUT.createNewFile()
    TEST.createNewFile()
    if (!KT_FILE.exists()) KT_FILE.writeText(String.format(TEMPLATE.readText(), DAY, DAY))
}

