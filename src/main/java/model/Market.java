package model;

import java.util.HashMap;
import java.util.Map;

public abstract class Market {

    private static final Map<String, Integer> buyPrices = new HashMap<>(), sellPrices = new HashMap<>();

    public static Map<String, Integer> getBuyPrices() {
        return buyPrices;
    }

    public static Map<String, Integer> getSellPrices() {
        return sellPrices;
    }
}
