package dev.rollczi.minecraftlista.util;

import java.util.Collection;
import java.util.Random;

public final class RandomUtil {

    private static final Random RANDOM = new Random();

    private RandomUtil() {}

    public static <T> T getRandom(Collection<T> collection) {
        return collection.stream()
                .skip(RANDOM.nextInt(collection.size()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Collection is empty"));
    }

}
