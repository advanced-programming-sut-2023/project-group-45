package stronghold.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import lombok.Data;
import stronghold.context.Direction;
import stronghold.context.IntPair;

@Data
public class Navigation {

    private static final List<String> WALKABLE_TILES = List.of("plain", "farmland");
    private final Game game;

    public boolean isWalkable(IntPair position) {
        Tile tile = game.getMap().getAt(position);
        return tile != null && WALKABLE_TILES.contains(tile.getType()) &&
                (tile.getBuilding() == null || tile.getBuilding().isHollow()
                        || tile.getBuilding().getHitPoints() <= 0);
    }

    /*
     * Returns null, when start == end, or when there is no path from start to end.
     */
    public List<IntPair> getPath(IntPair start, IntPair end) {
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
                if (tile == null || next[v.x()][v.y()] != null || !isWalkable(v)) {
                    continue;
                }
                next[v.x()][v.y()] = u;
                queue.add(v);
            }
        }
        if (next[start.x()][start.y()] == null) {
            return null;
        }
        List<IntPair> path = new ArrayList<>();
        IntPair u = start;
        while (!u.equals(end)) {
            path.add(u);
            u = next[u.x()][u.y()];
        }
        path.add(end);
        return path;
    }

    public static IntPair getBySpeed(List<IntPair> path, int speed) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        if (speed >= path.size()) {
            return path.get(path.size() - 1);
        }
        return path.get(speed);
    }
}
