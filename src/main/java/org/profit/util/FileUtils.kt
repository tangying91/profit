package org.profit.util

import java.io.*
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object FileUtils {

    private val stocks = System.getProperty("user.dir") + "/stocks/"

    fun writeHistory(code: String, content: String) {
        val stockPath = "${stocks}/$code"
        checkPath(Paths.get(stockPath))

        // 组装目标路径
        val path = Paths.get(stockPath, "history")
        try {
            // 先清空
            Files.deleteIfExists(path)
            // 再写入
            Files.write(path, content.toByteArray(charset("UTF-8")), StandardOpenOption.WRITE, StandardOpenOption.CREATE)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            println("$code 历史数据写入完成.")
        }
    }

    fun readHistory(code: String): List<String> {
        val codePath = "${stocks}/$code"
        val path = Paths.get(codePath, "history")
        return try {
            Files.readAllLines(path)
        } catch (e: Exception) {
            listOf()
        }
    }

    fun readStocks(): List<String> {
        val confPath = System.getProperty("user.dir") + "/conf/"
        val path = Paths.get(confPath, "stocks")
        return Files.readAllLines(path)
    }

    fun readLogs(): List<String> {
        val logPath = "${stocks}/logs"
        val path = Paths.get(logPath, DateUtils.formatDate(System.currentTimeMillis()))
        return try {
            Files.readAllLines(path)
        } catch (e: Exception) {
            listOf()
        }
    }

    fun writeLog(code: String) {
        val logPath = "${stocks}/logs"
        checkPath(Paths.get(logPath))

        // 组装目标路径
        val path = Paths.get(logPath, DateUtils.formatDate(System.currentTimeMillis()))
        try {
            // 写入
            Files.write(path, "$code\r\n".toByteArray(charset("UTF-8")), StandardOpenOption.APPEND, StandardOpenOption.CREATE)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 检查路径
     */
    private fun checkPath(path: Path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}