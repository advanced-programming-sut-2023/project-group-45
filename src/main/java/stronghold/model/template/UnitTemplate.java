package stronghold.model.template;

import lombok.Data;
import stronghold.model.Unit;

@Data
public class UnitTemplate {

    private int maxHitPoints, speed, range, damage;
    private String type;

    public Unit.UnitBuilder getBuilder() {
        return Unit.builder()
                .maxHitPoints(maxHitPoints)
                .speed(speed)
                .range(range)
                .damage(damage)
                .type(type);
    }
}
