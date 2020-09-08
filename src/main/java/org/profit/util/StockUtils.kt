package org.profit.util

object StockUtils {

    /**
     * 修正股票代码
     */
    fun code(code: Int): String {
        val sCode = code.toString()
        val sb = StringBuffer()
        for (i in 0 until 6 - sCode.length) {
            sb.append("0")
        }
        sb.append(sCode)
        return sb.toString()
    }
}