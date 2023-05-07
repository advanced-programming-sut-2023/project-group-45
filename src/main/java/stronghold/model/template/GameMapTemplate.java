package stronghold.model.template;

import lombok.Data;
import stronghold.model.GameMap;

@Data
public class GameMapTemplate {
    private final String name;
    private final int width, height;
    private final String[][] map;

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
