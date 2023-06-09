package org.example.stronghold.model;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import lombok.Builder;
import lombok.Data;
import org.example.stronghold.context.IntPair;

@Data
public class Building implements Serializable {

    public static final Map<Long, Building> OBJECTS = new TreeMap<>();
    private static long NEXT_ID = 0;
    private final long id = NEXT_ID++;
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
    private GuiSetting guiSetting;

    {
        OBJECTS.put(id, this);
    }

    @Builder(toBuilder = true)
    public Building(String type, int maxHitPoints, boolean isHollow, Player owner,
        IntPair position, int maxLabors, Map<String, Integer> consume,
        Map<String, Integer> supply, int religionFactor, int happinessFactor,
        int housingSpace, Map<String, Map<String, Integer>> dropUnit, GuiSetting guiSetting) {
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
        this.guiSetting = guiSetting;
    }

    public void destroy(GameData gameData) {
        owner.setPeasants(owner.getPeasants() + labors);
        labors = 0;
    }
}
