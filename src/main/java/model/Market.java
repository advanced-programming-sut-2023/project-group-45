package model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class Market {

    private final Map<String, Integer> buyPrices = new HashMap<>();
    private final Map<String, Integer> sellPrices = new HashMap<>();
}
