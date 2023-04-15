package model;

public class Building {

    private final String type;
    private final int maxHitPoints;
    private int hitPoints;

    Building(String type, int maxHitPoints) {
        this.type = type;
        this.maxHitPoints = this.hitPoints = maxHitPoints;
    }

    public String getType() {
        return type;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }
}
