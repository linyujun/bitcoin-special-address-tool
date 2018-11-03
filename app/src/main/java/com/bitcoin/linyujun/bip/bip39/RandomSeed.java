package com.bitcoin.linyujun.bip.bip39;

import java.security.SecureRandom;
import java.util.Random;

public class RandomSeed {
    public static byte[] random(WordCount words) {
        return random(words, new SecureRandom());
    }

    public static byte[] random(WordCount words, Random random) {
        byte[] randomSeed = new byte[words.byteLength()];
        random.nextBytes(randomSeed);
        return randomSeed;
    }
}
