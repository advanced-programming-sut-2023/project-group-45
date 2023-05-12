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
    private final List<TradeRequest> incomingTradeRequests = new ArrayList<>(),
            activeTradeRequests = new ArrayList<>(), successfulTradeRequests = new ArrayList<>();
    private int peasants = 0;
    private int foodRate = -2, taxRate = 0;

    public int getGold() {
        return resources.get("gold");
    }

    public void setGold(int gold) {
        resources.put("gold", gold);
    }
  
    public double getFoodRation(){
        return 0.5 * (foodRate + 2);
    }

    public double getTaxPerPeasant(){
        if (taxRate == 0){
            return 0;
        }
        if (taxRate < 0){
            return -0.4 + 0.2 * taxRate;
        }
        return 0.4 + 0.2 * taxRate;
    }
}
