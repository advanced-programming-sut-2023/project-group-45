package org.example.stronghold.model.template;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.example.stronghold.model.Building;
import org.example.stronghold.model.Building.BuildingBuilder;
import org.example.stronghold.model.GuiSetting;

/*
 * Read notes for UnitTemplate
 */
@Data
public class BuildingTemplate implements Serializable {

    private String type;
    private int maxHitPoints;
    private boolean isHollow = false;
    private String tileType = "plain";
    private Map<String, Integer> buildCost = new HashMap<>();
    private Map<String, Integer> consume = new HashMap<>();
    private Map<String, Integer> supply = new HashMap<>();
    private int maxLabors = 0;
    private int religionFactor = 0;
    private int happinessFactor = 0;
    private int housingSpace = 0;
    private Map<String, Map<String, Integer>> dropUnit = new HashMap<>();
    private GuiSetting guiSetting = new GuiSetting();

    public BuildingBuilder getBuilder() {
        return Building.builder()
            .type(type)
            .maxHitPoints(maxHitPoints)
            .isHollow(isHollow)
            .maxLabors(maxLabors)
            .consume(consume)
            .supply(supply)
            .religionFactor(religionFactor)
            .happinessFactor(happinessFactor)
            .housingSpace(housingSpace)
            .dropUnit(dropUnit)
            .guiSetting(guiSetting);
    }

    public boolean canBeBuiltOn(String tileType) {
        return tileType.startsWith(this.tileType);
    }

}
