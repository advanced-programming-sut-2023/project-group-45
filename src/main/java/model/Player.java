package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {

    private final User user;
    private final Map<String, Integer> resources;
    private final List<TradeRequest> tradeRequests;
    private int happiness, fear;

    Player(User user) {
        this.user = user;
        happiness = 0;
        fear = 0;
        resources = new HashMap<>();
        tradeRequests = new ArrayList<>();
    }

    public User getUser() {
        return user;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public int getFear() {
        return fear;
    }

    public void setFear(int fear) {
        this.fear = fear;
    }

    public Map<String, Integer> getResources() {
        return resources;
    }

    public List<TradeRequest> getTradeRequests() {
        return tradeRequests;
    }
}
