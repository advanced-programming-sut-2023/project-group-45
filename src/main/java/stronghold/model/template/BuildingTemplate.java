package stronghold.model.template;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import stronghold.model.Building;
import stronghold.model.Building.BuildingBuilder;

@Data
public class BuildingTemplate {

    private final String type;
    private final int maxHitPoints;
    private final boolean isHollow = false;
    private final String tileType = null;
    private final Map<String, Integer> buildCost = new HashMap<>();

    public BuildingBuilder getBuilder() {
        return Building.builder()
                .type(type)
                .maxHitPoints(maxHitPoints)
                .isHollow(isHollow);
    }

    public boolean canBeBuiltOn(String tileType) {
        return this.tileType == null || this.tileType.equals(tileType);
    }

}
