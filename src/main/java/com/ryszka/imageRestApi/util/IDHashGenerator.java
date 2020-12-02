package com.ryszka.imageRestApi.util;

import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class IDHashGenerator {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();

    public static String generateHash(int length) {
        StringBuilder output = new StringBuilder();
        IntStream.range(0, length)
                .forEach(i -> output.append(ALPHABET.charAt(random.nextInt(ALPHABET.length()))));
        return output.toString();
    }

    public static void getHash(Consumer<String> setHash) {
        setHash.accept(generate30CharHash());
    }

    public static String generate30CharHash() {
        return generateHash(30);
    }
}
