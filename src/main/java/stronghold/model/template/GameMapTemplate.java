package stronghold.model.template;

import lombok.Data;
import stronghold.model.GameMap;

@Data
public class GameMapTemplate {

    private final String name;
    private final int width, height;
    private final String[][] map;

    public GameMapTemplate(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        map = new String[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = "plain";
            }
        }
    }

    public GameMap build() {
        GameMap gameMap = new GameMap(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gameMap.getAt(i, j).setType(map[i][j]);
            }
        }
        return gameMap;
    }
}
