package stronghold.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import stronghold.context.IntPair;

@Data
public class Building implements Serializable {

    private final String type;
    private final int maxHitPoints;
    private final boolean isHollow;
    private final Player owner;
    private final IntPair position;
    private int hitPoints;

    @Builder(toBuilder = true)
    public Building(String type, int maxHitPoints, boolean isHollow, Player owner,
            IntPair position) {
        this.type = type;
        this.maxHitPoints = maxHitPoints;
        this.hitPoints = maxHitPoints;
        this.isHollow = isHollow;
        this.owner = owner;
        this.position = position;
    }
}
