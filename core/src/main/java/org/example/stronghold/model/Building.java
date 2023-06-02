package org.example.stronghold.model;

import java.io.Serializable;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.example.stronghold.context.IntPair;

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
    private final int religionFactor;
    private final int happinessFactor;
    private final int housingSpace;
    private final Map<String, Map<String, Integer>> dropUnit;
    private int hitPoints;
    private int labors = 0;

    @Builder(toBuilder = true)
    public Building(String type, int maxHitPoints, boolean isHollow, Player owner,
            IntPair position, int maxLabors, Map<String, Integer> consume,
            Map<String, Integer> supply, int religionFactor, int happinessFactor,
            int housingSpace, Map<String, Map<String, Integer>> dropUnit) {
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
        this.housingSpace = housingSpace;
        this.dropUnit = dropUnit;
    }

    public void destroy(Game game) {
        owner.setPeasants(owner.getPeasants() + labors);
        labors = 0;
    }
}
