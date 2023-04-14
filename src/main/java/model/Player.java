package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private final User user;
    private int happiness, fear;
    private final HashMap<String, Integer> resources;
    private final ArrayList<TradeRequest> tradeRequests;

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

    public HashMap<String, Integer> getResources() {
        return resources;
    }

    public ArrayList<TradeRequest> getTradeRequests() {
        return tradeRequests;
    }
}
