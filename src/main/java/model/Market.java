package model;

import java.util.HashMap;

public abstract class Market {

    private static final HashMap<String, Integer> buyPrices = new HashMap<>(), sellPrices = new HashMap<>();

    public static HashMap<String, Integer> getBuyPrices() {
        return buyPrices;
    }

    public static HashMap<String, Integer> getSellPrices() {
        return sellPrices;
    }
}
