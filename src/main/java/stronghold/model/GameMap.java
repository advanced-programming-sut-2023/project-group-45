package stronghold.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class GameMap implements Serializable {

    private final int height, width;
    private final Tile[][] map;

    GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        map = new Tile[height][width];
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
}
