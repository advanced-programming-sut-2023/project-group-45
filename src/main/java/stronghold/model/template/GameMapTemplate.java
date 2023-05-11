package stronghold.model.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import stronghold.context.IntPair;
import stronghold.model.GameMap;

@Data
public class GameMapTemplate {

    private final String name;
    private final int width, height;
    // TIL: gson doesn't care about this default value
    private final List<IntPair> bases = new ArrayList<>();
    private final Map<String, Integer> initialResources = new HashMap<>();
    private final int initialPopulation = 0;
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
