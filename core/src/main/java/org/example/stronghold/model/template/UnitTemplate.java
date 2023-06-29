package org.example.stronghold.model.template;

import lombok.Data;
import org.example.stronghold.model.GuiSetting;
import org.example.stronghold.model.Unit;

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
    private int engineers = 0;
    private GuiSetting guiSetting = new GuiSetting();

    public Unit.UnitBuilder getBuilder() {
        return Unit.builder()
                .type(type)
                .maxHitPoints(maxHitPoints)
                .speed(speed)
                .range(range)
                .damage(damage)
                .canClimb(canClimb)
                .canDig(canDig)
                .vision(vision)
                .guiSetting(guiSetting);
    }
}
