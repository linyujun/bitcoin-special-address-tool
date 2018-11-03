package com.bitcoin.linyujun.bip.bip39.validation;

public class InvalidChecksumException extends Exception {
    public InvalidChecksumException() {
        super("Invalid checksum");
    }
}
