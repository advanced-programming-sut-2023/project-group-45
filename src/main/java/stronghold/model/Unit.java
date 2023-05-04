package stronghold.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
public class Unit implements Serializable {

    private final String type;
    private final int maxHitPoints, speed, range, damage;
    private int hitPoints;

    /* I couldn't find a way to use lombok-generated constructor.
     * It seems there is no way to initialize hitPoints (= maxHitPoints) both in the constructor
     * *and* the builder. Since writing a builder is a huge pain in the ass, I did choose to
     * auto-generate the constructor
     */
    @Builder(toBuilder = true)
    public Unit(String type, int maxHitPoints, int speed, int range, int damage) {
        this.maxHitPoints = maxHitPoints;
        this.speed = speed;
        this.range = range;
        this.damage = damage;
        this.type = type;
        this.hitPoints = maxHitPoints;
    }
}
