package com.bitcoin.linyujun.base;


import com.bitcoin.linyujun.bip.bip32.ValidationException;

public interface BtcEthTransaction {
    byte[] sign(ECKeyPair key) throws ValidationException;

    public byte[] getSignBytes();

    /**
     * Eth 使用的方法
     *
     * @return
     */
    public byte[] getData();


}
