package stronghold.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Unit implements Serializable {

    private final int maxHitPoints, speed, range, damage;
    private final String type;
    private int hitPoints = getMaxHitPoints();
}
