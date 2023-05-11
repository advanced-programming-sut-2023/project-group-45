package stronghold.model;

import java.io.Serializable;
import lombok.Data;
import stronghold.context.IntPair;

@Data
public class GameMap implements Serializable {

    private final int width, height;
    private final Tile[][] map;

    /*
     * let's assume this convention:
     * (0,0) -----------------> (w-1,0)
     *   |
     *   |
     *   |
     *   v
     * (0,h-1)
     */
    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        map = new Tile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = new Tile();
            }
        }
    }

    public Tile getAt(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return map[x][y];
    }

    public Tile getAt(IntPair position) {
        return getAt(position.x(), position.y());
    }
}
