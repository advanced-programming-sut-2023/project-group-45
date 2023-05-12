package stronghold.view.sections;

import static stronghold.context.MapUtils.getOpt;
import static stronghold.context.MapUtils.getIntPairOpt;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;
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

    private boolean isValidPosition(IntPair position) {
        return position.x() >= 0 && position.x() < gameMap.getWidth() && position.y() >= 0 && position.y() < gameMap.getHeight();
    }

    private void saveMap(Map<String, String> input) {
        Map<String, Object> req = new HashMap<>();
        req.put("game-map", gameMap);
        Operators.mapEditor.saveGameMap(req);
        System.out.println("Map saved successfully");
    }

    private void addBase(Map<String, String> input) {
        IntPair position = getIntPairOpt(input, "x", "y");
        if (!isValidPosition(position)) {
            System.out.println("Invalid position for a base");
            return;
        }
        gameMap.getBases().add(position);
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

    private void setTileTexture(Map<String, String> input) {
        IntPair position = getIntPairOpt(input, "x", "y");
        if (!isValidPosition(position)) {
            System.out.println("Invalid position");
            return;
        }
        String type = getOpt(input, "type");
        gameMap.getMap()[position.x()][position.y()] = type;
        System.out.println("Done");
    }

    private void setRangeTexture(Map<String, String> input) {
        IntPair start = getIntPairOpt(input, "x1", "y1");
        IntPair end = getIntPairOpt(input, "x2", "y2");
        if (!isValidPosition(start) || !isValidPosition(end)) {
            System.out.println("Invalid position");
            return;
        }
        if (start.x() > end.x() || start.y() > end.y()) {
            System.out.println("Invalid range");
            return;
        }
        String type = getOpt(input, "type");
        for (int x = start.x(); x <= end.x(); x++) {
            for (int y = start.y(); y <= end.y(); y++) {
                gameMap.getMap()[x][y] = type;
            }
        }
        System.out.println("Done");
    }

    private void clearTile(Map<String, String> input) {
        IntPair position = getIntPairOpt(input, "x", "y");
        if(!isValidPosition(position)){
            System.out.println("Invalid position");
            return;
        }
        gameMap.getMap()[position.x()][position.y()] = "plain";
        System.out.println("Done");
    }

    private void dropRock(Map<String, String> input){
        IntPair position = getIntPairOpt(input, "x", "y");
        if(!isValidPosition(position)){
            System.out.println("Invalid position");
            return;
        }
        String direction = getOpt(input, "direction");
        if(direction.equals("random")){
            Random random = new Random();
            direction = String.valueOf("news".charAt(random.nextInt(4)));
        }
        if(direction.length() > 1 || !"news".contains(direction)){
            System.out.println("Invalid direction");
            return;
        }
        gameMap.getMap()[position.x()][position.y()] = "rock-" + direction;
        System.out.println("Done");
    }

    private void dropTree(Map<String, String> input){
        IntPair position = getIntPairOpt(input, "x", "y");
        if(!isValidPosition(position)){
            System.out.println("Invalid position");
            return;
        }
        String type = getOpt(input, "type");
        gameMap.getMap()[position.x()][position.y()] = "tree-" + type;
        System.out.println("Done");
    }
}
