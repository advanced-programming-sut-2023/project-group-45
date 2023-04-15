package model;

public class Unit {

    private final int maxHitPoints, speed, range, damage;
    private final String type;
    private int hitPoints;

    Unit(String type, int maxHitPoints, int speed, int range, int damage) {
        this.maxHitPoints = this.hitPoints = maxHitPoints;
        this.speed = speed;
        this.type = type;
        this.range = range;
        this.damage = damage;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public int getSpeed() {
        return speed;
    }

    public int getRange() {
        return range;
    }

    public int getDamage() {
        return damage;
    }

    public String getType() {
        return type;
    }
}
