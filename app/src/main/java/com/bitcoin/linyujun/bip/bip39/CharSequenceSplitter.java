package com.bitcoin.linyujun.bip.bip39;

import java.util.LinkedList;
import java.util.List;


final class CharSequenceSplitter {
    private final char separator1;
    private final char separator2;

    CharSequenceSplitter(final char separator1, final char separator2) {
        this.separator1 = separator1;
        this.separator2 = separator2;
    }

    List<CharSequence> split(final CharSequence charSequence) {
        final LinkedList<CharSequence> list = new LinkedList<>();
        int start = 0;
        final int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            final char c = charSequence.charAt(i);
            if (c == separator1 || c == separator2) {
                list.add(charSequence.subSequence(start, i));
                start = i + 1;
            }
        }
        list.add(charSequence.subSequence(start, length));
        return list;
    }
}
