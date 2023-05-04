package stronghold.model.template;

import lombok.Data;
import stronghold.model.Unit;

@Data
public class UnitTemplate {

    private String type;
    private int maxHitPoints, speed, range, damage;

    public Unit.UnitBuilder getBuilder() {
        return Unit.builder()
                .type(type)
                .maxHitPoints(maxHitPoints)
                .speed(speed)
                .range(range)
                .damage(damage);
    }
}
