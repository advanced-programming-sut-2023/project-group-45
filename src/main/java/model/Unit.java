package model;

import lombok.Data;

@Data
public class Unit {

    private final int maxHitPoints, speed, range, damage;
    private final String type;
    private int hitPoints = getMaxHitPoints();
}
