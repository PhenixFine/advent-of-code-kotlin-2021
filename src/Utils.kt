import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


/**
 * Get a file.
 */
fun getFile(name: String) = File("src", name)

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = getFile("textFiles/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
