package stronghold.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
public class Building implements Serializable {

    private final String type;
    private final int maxHitPoints;
    private int hitPoints;

    @Builder(toBuilder = true)
    public Building(String type, int maxHitPoints) {
        this.type = type;
        this.maxHitPoints = maxHitPoints;
        this.hitPoints = maxHitPoints;
    }
}
