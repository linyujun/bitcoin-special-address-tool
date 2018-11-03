package com.bitcoin.linyujun.bip.bip39;

import java.text.Normalizer;


final class Normalization {
    static String normalizeNFKD(final String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFKD);
    }

    static char normalizeNFKD(final char c) {
        return normalizeNFKD("" + c).charAt(0);
    }
}
