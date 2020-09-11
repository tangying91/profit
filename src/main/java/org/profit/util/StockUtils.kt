package org.profit.util

import java.math.RoundingMode
import java.text.DecimalFormat

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

    /**
     * 对入参保留最多两位小数(舍弃末尾的0)，如:
     * 3.345->3.34
     * 3.40->3.4
     * 3.0->3
     */
    fun twoDigits(number: Double): String {
        val format = DecimalFormat("0.##")
        //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
        format.roundingMode = RoundingMode.FLOOR
        return format.format(number)
    }
}