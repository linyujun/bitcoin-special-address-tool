package com.bitcoin.linyujun.bip.bip39;


public interface PBKDF2WithHmacSHA512 {
    byte[] hash(final char[] chars, final byte[] salt);
}
