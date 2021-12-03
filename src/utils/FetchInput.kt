package utils

import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * adapted from https://curlconverter.com/# Java conversion. I removed things I didn't think was necessary
 * and moved the cookie info to a separate file ( I added an example file of how to set it up ).
 */

internal object FetchInput {
    @Throws(IOException::class)
    @JvmStatic
    fun toFile(day: Int, file: File) {
        val url = URL("https://adventofcode.com/2021/day/$day/input")
        val httpConn = url.openConnection() as HttpURLConnection
        httpConn.requestMethod = "GET"
        httpConn.setRequestProperty("Accept", "text/html")
        httpConn.setRequestProperty("Accept-Language", "en-US,en;q=0.5")
        httpConn.setRequestProperty("Referer", "https://adventofcode.com/2021/day/$day")
        httpConn.setRequestProperty("Connection", "keep-alive")
        httpConn.setRequestProperty("Upgrade-Insecure-Requests", "1")
        httpConn.setRequestProperty("Sec-Fetch-Dest", "document")
        httpConn.setRequestProperty("Sec-Fetch-Mode", "navigate")
        httpConn.setRequestProperty("Sec-Fetch-Site", "same-origin")
        httpConn.setRequestProperty("Cache-Control", "max-age=0")
        httpConn.setRequestProperty("TE", "trailers")
        httpConn.setRequestProperty(COOKIE, "_ga=$GA; _gid=$GID; session=$SESSION")
        val responseStream = if (httpConn.responseCode / 100 == 2) httpConn.inputStream else httpConn.errorStream
        val s = Scanner(responseStream).useDelimiter("\\A")
        val response = if (s.hasNext()) s.next() else ""
        file.writeText(response)
    }
}