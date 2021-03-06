package com.bitcoin.linyujun.base;

public enum CoinTypes {
    Bitcoin(0, "BTC"),
    BitcoinTest(1, "BTC"),
    Litecoin(2, "LTC"),
    Dogecoin(3, "DOGE"),
    Ethereum(60, "ETH"),
    EOS(194, "EOS"),

    ERC20(100089, "ERC20"),

    Default(-1, "DEFAULT");

    private int coinType;
    private String coinName;

    CoinTypes(int i, String name) {
        coinType = i;
        coinName = name;
    }

    public int coinType() {
        return coinType;
    }

    public String coinName() {
        return coinName;
    }

    public static CoinTypes parseCoinType(int type) throws CoinNotFindException {
        for (CoinTypes e : CoinTypes.values()) {
            if (e.coinType == type) {
                return e;
            }
        }
        throw new CoinNotFindException("The currency is not supported for the time being");
    }
}
