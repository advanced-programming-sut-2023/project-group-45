package stronghold.model.template;

import lombok.Data;
import stronghold.model.Unit;

/*
 * Note for Template classes:
 * Gson needs a no-arg constructor to instantiate the class and set the provided default values
 * Make sure all the fields are non-final, so lombok will generate a no-arg constructor
 */
@Data
public class UnitTemplate {

    private String type;
    private int maxHitPoints, speed, range, damage;
    private boolean canClimb = false, canDig = false;
    private int vision = 5;

    public Unit.UnitBuilder getBuilder() {
        return Unit.builder()
                .type(type)
                .maxHitPoints(maxHitPoints)
                .speed(speed)
                .range(range)
                .damage(damage)
                .canClimb(canClimb)
                .canDig(canDig)
                .vision(vision);
    }
}
