package stronghold.model;

import java.io.Serializable;
import java.util.Map;
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
    private final int maxLabors;
    private final Map<String, Integer> consume;
    private final Map<String, Integer> supply;
    private int hitPoints;
    private int labors = 0;
    private final int religionFactor;
    private final int happinessFactor;

    @Builder(toBuilder = true)
    public Building(String type, int maxHitPoints, boolean isHollow, Player owner,
            IntPair position, int maxLabors, Map<String, Integer> consume,
            Map<String, Integer> supply, int religionFactor, int happinessFactor) {
        this.type = type;
        this.maxHitPoints = maxHitPoints;
        this.hitPoints = maxHitPoints;
        this.isHollow = isHollow;
        this.owner = owner;
        this.position = position;
        this.maxLabors = maxLabors;
        this.consume = consume;
        this.supply = supply;
        this.religionFactor = religionFactor;
        this.happinessFactor = happinessFactor;
    }
}
