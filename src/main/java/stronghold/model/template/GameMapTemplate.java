package stronghold.model.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import stronghold.context.IntPair;
import stronghold.model.GameMap;

/*
 * Read notes for UnitTemplate
 */
@Data
public class GameMapTemplate {

    private String name;
    private int width, height;
    private List<IntPair> bases = new ArrayList<>();
    private Map<String, Integer> initialResources = new HashMap<>();
    private int initialPopulation = 10;
    private String[][] map;

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

    public GameMapTemplate() {
        // only called by Gson
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
