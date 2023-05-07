package stronghold.view.sections;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import stronghold.model.template.GameMapTemplate;
import stronghold.operator.Operators;
import stronghold.view.Menu;

public class MapEditorMenu extends Menu {

    private final GameMapTemplate gameMap;

    public MapEditorMenu(Scanner scanner, GameMapTemplate gameMap) {
        super(scanner);
        this.gameMap = gameMap;
        addCommand("save-map", this::saveMap);
    }

    private void saveMap(Map<String, String> input) {
        Map<String, Object> req = new HashMap<>();
        req.put("game-map", gameMap);
        Operators.mapEditor.saveGameMap(req);
        System.out.println("Map saved successfully");
    }
}
