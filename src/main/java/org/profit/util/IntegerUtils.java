package org.profit.util;

/**
 * @author TangYing
 */
public class IntegerUtils {

    /**
     * Generate min ~ max random number
     *
     * @param min
     * @param max
     * @return
     */
    public static int generateRandomSeed(int min, int max) {
        if (min == max) {
            return min;
        }

        double seed = Math.random() * (max - min) + min;
        return (int) Math.round(seed);
    }
}
