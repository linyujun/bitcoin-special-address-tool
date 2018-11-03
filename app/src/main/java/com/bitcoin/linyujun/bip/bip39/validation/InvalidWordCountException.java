package com.bitcoin.linyujun.bip.bip39.validation;

public class InvalidWordCountException extends Exception {
    public InvalidWordCountException() {
        super("Not a correct number of words");
    }
}
