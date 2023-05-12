package stronghold.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class Player implements Serializable {

    private final User user;
    private final Map<String, Integer> resources = new HashMap<>();
    private final List<TradeRequest> tradeRequests = new ArrayList<>();
    private int foodRate = -2, taxRate = 0;
    private int peasants = 0;

    public double getFoodRation(){
        return 0.5 * (foodRate + 2);
    }
}
