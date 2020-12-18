package com.tinkerforge;

import java.util.Random;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Base58Utils {

    private Base58Utils() {
    }

    public static String base58Random() {
        return base58Encode(System.currentTimeMillis() + new Random().nextInt(1234567890));
    }

    public static String base58Encode(final long value) {
        return IPConnectionBase.base58Encode(value);
    }

    public static long base58Decode(final String encoded) {
        return IPConnectionBase.base58Decode(encoded);
    }
}