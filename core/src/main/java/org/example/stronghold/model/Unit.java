package org.example.stronghold.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.stronghold.context.IntPair;

@Data
public class Unit implements Serializable {

    public static final double DEFENSIVE_VISION_RATE = 0.5;

    private final String type;
    private final int maxHitPoints, speed, range, damage;
    private final boolean canClimb, canDig;
    private final Player owner;
    private final int vision;
    private int hitPoints;
    private IntPair position;
    private IntPair navigationGoal = null;
    private IntPair[] patrol = null;
    @ToString.Exclude   // avoid recursive call
    @EqualsAndHashCode.Exclude
    private Unit attackGoal = null;
    private String mode = "standing";
    private GuiSetting guiSetting;

    /* I couldn't find a way to use lombok-generated constructor.
     * It seems there is no way to initialize hitPoints (= maxHitPoints) both in the constructor
     * *and* the builder. Since writing a builder is a huge pain in the ass, I did choose to
     * auto-generate the constructor
     */
    @Builder(toBuilder = true)
    public Unit(String type, int maxHitPoints, int speed, int range, int damage, boolean canClimb,
            boolean canDig, Player owner, int vision, GuiSetting guiSetting) {
        this.maxHitPoints = maxHitPoints;
        this.speed = speed;
        this.range = range;
        this.damage = damage;
        this.type = type;
        this.canClimb = canClimb;
        this.canDig = canDig;
        this.hitPoints = maxHitPoints;
        this.owner = owner;
        this.vision = vision;
        this.guiSetting = guiSetting;
    }

    public void die(GameData gameData) {
        hitPoints = 0;
        gameData.getUnits().remove(this);
    }

    public IntPair getGoal() {
        if (attackGoal != null && attackGoal.hitPoints > 0 && attackGoal.owner.isAlive()) {
            return attackGoal.position;
        }
        attackGoal = null;
        if (patrol != null) {
            if (navigationGoal.equals(patrol[0])) {
                navigationGoal = patrol[1];
            } else if (navigationGoal.equals(patrol[1])) {
                navigationGoal = patrol[0];
            }
        }
        return navigationGoal;
    }

    public void unsetGoal() {
        attackGoal = null;
        patrol = null;
        navigationGoal = null;
    }

    public int getEffectiveVision() {
        if (mode.equals("standing")) {
            return 0;
        }
        if (mode.equals("defensive")) {
            return (int) (vision * DEFENSIVE_VISION_RATE);
        }
        return vision;
    }

    public int getDamage(GameData gameData) {
        return (int) (damage * (1 + 0.05 * gameData.getHappiness(owner)));
    }
}
