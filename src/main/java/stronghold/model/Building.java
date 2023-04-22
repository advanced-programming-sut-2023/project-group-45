package stronghold.model;

import lombok.Data;

@Data
public class Building {

    private final String type;
    private final int maxHitPoints;
    private int hitPoints = getMaxHitPoints();
}
