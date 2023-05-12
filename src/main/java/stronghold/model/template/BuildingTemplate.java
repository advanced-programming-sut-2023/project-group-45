package stronghold.model.template;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import stronghold.model.Building;
import stronghold.model.Building.BuildingBuilder;

/*
 * Read notes for UnitTemplate
 */
@Data
public class BuildingTemplate {

    private String type;
    private int maxHitPoints;
    private boolean isHollow = false;
    private String tileType = null;
    private Map<String, Integer> buildCost = new HashMap<>();
    private Map<String, Integer> consume = new HashMap<>();
    private Map<String, Integer> supply = new HashMap<>();
    private int maxLabors = 0;
    private int religionFactor = 0;
    private int happinessFactor = 0;
    private int housingSpace = 0;

    public BuildingBuilder getBuilder() {
        return Building.builder()
                .type(type)
                .maxHitPoints(maxHitPoints)
                .isHollow(isHollow)
                .maxLabors(maxLabors)
                .consume(consume)
                .supply(supply)
                .religionFactor(religionFactor)
                .happinessFactor(happinessFactor);
    }

    public boolean canBeBuiltOn(String tileType) {
        return this.tileType == null || tileType.startsWith(this.tileType);
    }

}
