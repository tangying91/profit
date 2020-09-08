package org.profit.util

/**
 * @author TangYing
 */
object IntegerUtils {
    /**
     * Generate min ~ max random number
     *
     * @param min
     * @param max
     * @return
     */
    fun generateRandomSeed(min: Int, max: Int): Int {
        if (min == max) {
            return min
        }
        val seed = Math.random() * (max - min) + min
        return Math.round(seed).toInt()
    }
}