package com.bitcoin.linyujun.bip.bip39;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class WordListMapNormalization implements NFKDNormalizer {
    private final Map<CharSequence, String> normalizedMap = new HashMap<>();

    WordListMapNormalization(final WordList wordList) {
        for (int i = 0; i < 1 << 11; i++) {
            final String word = wordList.getWord(i);
            final String normalized = Normalizer.normalize(word, Normalizer.Form.NFKD);
            normalizedMap.put(word, normalized);
            normalizedMap.put(normalized, normalized);
            normalizedMap.put(Normalizer.normalize(word, Normalizer.Form.NFC), normalized);
        }
    }

    @Override
    public String normalize(final CharSequence charSequence) {
        final String normalized = normalizedMap.get(charSequence);
        if (normalized != null)
            return normalized;
        return Normalizer.normalize(charSequence, Normalizer.Form.NFKD);
    }
}
