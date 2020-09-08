package org.profit.util

import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object FileUtils {

    private val chatRecDir = System.getProperty("user.dir") + "/stocks/"

    fun writeHistory(code: String, content: String) {
        val stockPath = "${chatRecDir}/$code"
        checkPath(Paths.get(stockPath))

        // 组装目标路径
        val path = Paths.get(stockPath, "history")
        try {
            // 先清空
            Files.delete(path)
            // 再写入
            Files.write(path, content.toByteArray(charset("UTF-8")), StandardOpenOption.WRITE, StandardOpenOption.CREATE)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            println("$code 历史数据写入完成.")
        }
    }

    fun readHistory(code: String): List<String> {
        val codePath = "${chatRecDir}/$code"
        val path = Paths.get(codePath, "history")
        return Files.readAllLines(path)
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