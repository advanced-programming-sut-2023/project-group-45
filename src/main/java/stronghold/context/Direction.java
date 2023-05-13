package stronghold.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Direction {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static List<Direction> shuffledValues() {
        List<Direction> directions = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(directions);
        return directions;
    }

    public int dx() {
        return dx;
    }

    public int dy() {
        return dy;
    }
}
