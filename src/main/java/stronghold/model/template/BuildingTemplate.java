package stronghold.model.template;

import lombok.Data;
import stronghold.model.Building;
import stronghold.model.Building.BuildingBuilder;

@Data
public class BuildingTemplate {

    private final String type;
    private final int maxHitPoints;
    private final boolean isHollow = false;

    public BuildingBuilder getBuilder() {
        return Building.builder()
                .type(type)
                .maxHitPoints(maxHitPoints)
                .isHollow(isHollow);
    }

}
