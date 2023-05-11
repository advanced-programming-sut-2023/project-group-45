package stronghold.model.template;

import lombok.Data;
import stronghold.model.Unit;

@Data
public class UnitTemplate {

    private final String type;
    private final int maxHitPoints, speed, range, damage;
    private final boolean canClimb = false, canDig = false;

    public Unit.UnitBuilder getBuilder() {
        return Unit.builder()
                .type(type)
                .maxHitPoints(maxHitPoints)
                .speed(speed)
                .range(range)
                .damage(damage)
                .canClimb(canClimb)
                .canDig(canDig);
    }
}
