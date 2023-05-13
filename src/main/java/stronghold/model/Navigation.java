package stronghold.model;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import lombok.Data;
import stronghold.context.Direction;
import stronghold.context.IntPair;

@Data
public class Navigation {

    private static final List<String> WALKABLE_TILES = List.of("plain", "farmland");
    private final Game game;

    /*
     * Returns null, when start == end, or when there is no path from start to end.
     */
    public IntPair nextStep(IntPair start, IntPair end, int speed) {
        if (start.equals(end)) {
            return null;
        }
        int w = game.getMap().getWidth(), h = game.getMap().getHeight();
        IntPair[][] next = new IntPair[w][h];
        next[end.x()][end.y()] = end;
        Queue<IntPair> queue = new ArrayDeque<>();
        queue.add(end);
        while (!queue.isEmpty()) {
            IntPair u = queue.poll();
            for (Direction dir : Direction.shuffledValues()) {
                IntPair v = u.add(dir.dx(), dir.dy());
                Tile tile = game.getMap().getAt(v);
                if (tile == null || next[v.x()][v.y()] != null) {
                    continue;
                }
                if (!WALKABLE_TILES.contains(tile.getType())) {
                    continue;
                }
                next[v.x()][v.y()] = u;
                queue.add(v);
            }
        }
        if (next[start.x()][start.y()] == null) {
            return null;
        }
        IntPair step = start;
        for (int i = 0; i < speed; i++) {
            step = next[step.x()][step.y()];
        }
        return step;
    }
}
