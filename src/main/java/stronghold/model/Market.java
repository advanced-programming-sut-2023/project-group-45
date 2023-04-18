package stronghold.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class Market implements Serializable {

    private final Map<String, Integer> buyPrices = new HashMap<>();
    private final Map<String, Integer> sellPrices = new HashMap<>();
}
