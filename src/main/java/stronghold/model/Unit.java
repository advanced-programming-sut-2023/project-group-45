package stronghold.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class Unit implements Serializable {

    private final int maxHitPoints, speed, range, damage;
    private final String type;
    private int hitPoints = getMaxHitPoints();
}
