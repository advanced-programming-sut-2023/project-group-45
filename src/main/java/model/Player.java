package model;

import java.util.HashMap;

public class Player {
    private final User user;
    private int happiness, fear;
    private final HashMap<String, Integer> resources;

    Player(User user) {
        this.user = user;
        happiness = 0;
        fear = 0;
        resources = new HashMap<>();
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
}
