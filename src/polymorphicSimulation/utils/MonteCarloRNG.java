package polymorphicSimulation.utils;

import java.util.Random;

public class MonteCarloRNG {
    private static final Random RANDOM = new Random();

    public static int getInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Max must be greater than or equal to min");
        }
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public static boolean chance(double probability) {
        return RANDOM.nextDouble() < probability;
    }

    public static <T> T getItem(T[] array) {
        if (array == null || array.length == 0)
            return null;
        return array[RANDOM.nextInt(array.length)];
    }
}
