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
    private int happiness = 0, fear = 0;
    private int peasants = 0;

    public int getGold() {
        return resources.get("gold");
    }

    public void setGold(int gold) {
        resources.put("gold", gold);
    }
}
