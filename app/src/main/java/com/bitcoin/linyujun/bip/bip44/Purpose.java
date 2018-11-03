package com.bitcoin.linyujun.bip.bip44;


import com.bitcoin.linyujun.base.CoinTypes;
import com.bitcoin.linyujun.bip.bip32.Index;

public final class Purpose {
    private final M m;
    private final int purpose;
    private final String toString;

    Purpose(final M m, final int purpose) {
        this.m = m;
        if (purpose == 0 || Index.isHardened(purpose))
            throw new IllegalArgumentException();
        this.purpose = purpose;
        toString = String.format("%s/%d'", m, purpose);
    }

    public int getValue() {
        return purpose;
    }

    @Override
    public String toString() {
        return toString;
    }

    /**
     * Create a {@link CoinType} for this purpose.
     *
     * @param coinType The coin type
     * @return A coin type instance for this purpose
     */
    public CoinType coinType(final CoinTypes coinType) {
        return new CoinType(this, coinType);
    }
}
