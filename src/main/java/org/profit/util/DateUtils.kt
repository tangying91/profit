package org.profit.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"
    private const val DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private val dateFormat = SimpleDateFormat(DEFAULT_DATE_FORMAT)
    private val timeFormat = SimpleDateFormat(DEFAULT_TIME_FORMAT)

    const val MILLISECOND_PER_DAY = 24 * 60 * 60 * 1000.toLong()
    const val HOUR_MILLISECONDS = 3600000L
    /**
     * Format time with default time pattern
     *
     * @param time
     * @return
     */
    fun formatTime(time: Long): String {
        return timeFormat.format(Date(time))
    }

    /**
     * Format time with default date pattern
     *
     * @param time
     * @return
     */
    fun formatDate(time: Long): String {
        return dateFormat.format(Date(time))
    }

    /**
     * Format time with pattern
     *
     * @param time
     * @param pattern
     * @return
     */
    fun formatTime(time: Long, pattern: String?): String {
        val format = SimpleDateFormat(pattern)
        return format.format(Date(time))
    }

    /**
     * Parse time
     *
     * @param str
     * @return
     */
    @Synchronized
    fun parseTime(str: String?): Long {
        var time = 0L
        try {
            val date = dateFormat.parse(str)
            time = date.time
        } catch (e: ParseException) { // e...
        }
        return time
    }

    /**
     * Parse time
     *
     * @param str
     * @param pattern
     * @return
     */
    fun parseTime(str: String?, pattern: String?): Long {
        var time = System.currentTimeMillis()
        try {
            val format = SimpleDateFormat(pattern)
            val date = format.parse(str)
            time = date.time
        } catch (e: ParseException) { // e...
        }
        return time
    }

    /**
     * 获取零点
     *
     * @return
     */
    val zeroTime: Long
        get() = getZeroTime(System.currentTimeMillis())

    /**
     * Get zero time, a day's begin
     *
     * @param time
     * @return
     */
    fun getZeroTime(time: Long): Long { // Get date string
        val str = dateFormat.format(Date(time))
        var zeroTime = time
        try {
            val date = dateFormat.parse(str)
            zeroTime = date.time
        } catch (e: ParseException) { // e....
        }
        return zeroTime
    }

    /**
     * Get day interval
     * end must bigger than start
     * end > start
     *
     * @param start
     * @param end
     * @return
     */
    fun getDayInterval(start: Long, end: Long): Int {
        val startZeroTime = getZeroTime(start)
        val endZeroTime = getZeroTime(end)
        val day = ((endZeroTime - startZeroTime) / (24 * 60 * 60 * 1000)).toInt()
        return Math.abs(day)
    }

    /**
     * 是否是周末
     *
     * @return
     */
    fun isWeek(str: String?): Boolean {
        try {
            val date = dateFormat.parse(str)
            val day = date.day
            if (day == 0 || day == 6) {
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }
}