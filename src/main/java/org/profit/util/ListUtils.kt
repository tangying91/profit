package org.profit.util

import java.util.*

object ListUtils {
    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     *
     * @param source
     * @return
     */
    fun <T> averageAssign(source: List<T>, n: Int): List<List<T>> {
        val result: MutableList<List<T>> = ArrayList()
        var remaider = source.size % n //(先计算出余数)
        val number = source.size / n //然后是商
        var offset = 0 //偏移量
        for (i in 0 until n) {
            var value: List<T>? = null
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1)
                remaider--
                offset++
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset)
            }
            result.add(value)
        }
        return result
    }

    /**
     * Sub list
     *
     * @return
     */
    fun <T> sublist(list: List<T>?, start: Int, limit: Int): List<T> {
        val fromIndex = Math.min(start, list!!.size)
        val toIndex = Math.min(fromIndex + limit, list.size)
        return list.subList(fromIndex, toIndex)
    }

    /**
     * 从队列里移除所有该单位
     *
     * @param queue
     * @param unit
     */
    fun <T> removeAll(queue: Queue<T>, unit: T) {
        val it = queue.iterator()
        while (it.hasNext()) {
            if (it.next() == unit) {
                it.remove()
            }
        }
    }
}