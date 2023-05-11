package stronghold.view.sections;

import static stronghold.context.MapUtils.getIntOpt;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import stronghold.context.IntPair;
import stronghold.model.template.GameMapTemplate;
import stronghold.operator.Operators;
import stronghold.view.Menu;

public class MapEditorMenu extends Menu {

    private final GameMapTemplate gameMap;

    public MapEditorMenu(Scanner scanner, GameMapTemplate gameMap) {
        super(scanner);
        this.gameMap = gameMap;
        addCommand("save-map", this::saveMap);
        addCommand("add-base", this::addBase);
        addCommand("show-bases", this::showBases);
        addCommand("clear-bases", this::clearBases);
    }

    private void saveMap(Map<String, String> input) {
        Map<String, Object> req = new HashMap<>();
        req.put("game-map", gameMap);
        Operators.mapEditor.saveGameMap(req);
        System.out.println("Map saved successfully");
    }

    private void addBase(Map<String, String> input) {
        int x = getIntOpt(input, "x");
        int y = getIntOpt(input, "y");
        if (x < 0 || x >= gameMap.getWidth() || y < 0 || y >= gameMap.getHeight()) {
            System.out.println("Invalid position for a base");
            return;
        }
        gameMap.getBases().add(new IntPair(x, y));
        System.out.println("Added base successfully");
    }

    private void showBases(Map<String, String> input) {
        if (gameMap.getBases().isEmpty()) {
            System.out.println("No bases added yet");
            return;
        }
        System.out.println("Bases:");
        for (IntPair base : gameMap.getBases()) {
            System.out.println(" - x=" + base.x() + ", y=" + base.y());
        }
    }

    private void clearBases(Map<String, String> input) {
        gameMap.getBases().clear();
        System.out.println("Cleared previous bases");
    }
}
