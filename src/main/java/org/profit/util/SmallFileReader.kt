package org.profit.util

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

/**
 * @author Kyia
 */
object SmallFileReader {
    @Throws(IOException::class)
    fun readSmallFile(filePath: String?): String? {
        if (filePath == null) {
            return null
        }
        val smallFile = File(filePath)
        return readSmallFile(smallFile)
    }

    @Throws(IOException::class)
    fun readSmallFile(smallFile: File?): String {
        val reader = FileReader(smallFile)
        val bufferedReader = BufferedReader(reader)
        val buf = StringBuffer()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            buf.append(line)
        }
        bufferedReader.close()
        reader.close()
        return buf.toString()
    }
}