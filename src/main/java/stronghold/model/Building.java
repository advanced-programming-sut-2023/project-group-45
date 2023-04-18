package stronghold.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class Building implements Serializable {

    private final String type;
    private final int maxHitPoints;
    private int hitPoints = getMaxHitPoints();
}
