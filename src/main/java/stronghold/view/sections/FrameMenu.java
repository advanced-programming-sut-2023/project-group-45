package stronghold.view.sections;

import static stronghold.context.MapUtils.getIntOpt;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import stronghold.model.Game;
import stronghold.model.Player;
import stronghold.model.Tile;
import stronghold.operator.OperatorException;
import stronghold.operator.Operators;
import stronghold.view.Menu;

public class FrameMenu extends Menu {

    private final Game game;

    public FrameMenu(Scanner scanner, Game game) {
        super(scanner);
        this.game = game;
        addCommand("next-frame", this::nextFrame);
        addCommand("show-map", this::showMap);
        addCommand("next-turn", this::nextTurn);
    }

    private void nextFrame(Map<String, String> input) {
        int count = 1;
        if (input.containsKey("count")) {
            count = getIntOpt(input, "count");
            if (count <= 0) {
                System.out.println("Count must be positive");
                return;
            }
        }
        for (int i = 0; i < count; i++) {
            try {
                Operators.game.nextFrame(new HashMap<>() {{
                    put("game", game);
                }});
            } catch (OperatorException e) {
                System.out.println("Frame " + (i + 1) + " failed: " + e.getMessage());
            }
        }
        System.out.println("Frames passed");
    }

    private void showMap(Map<String, String> input) {
        // todo: currently just for test
        for (int y = 0; y < game.getMap().getHeight(); y++) {
            for (int x = 0; x < game.getMap().getWidth(); x++) {
                Tile tile = game.getMap().getAt(x, y);
                if (tile.getBuilding() != null) {
                    System.out.print(tile.getBuilding().getType().charAt(0));
                } else if (tile.getType().equals("plain")) {
                    System.out.print("~");
                } else if (tile.getType().equals("tree")) {
                    System.out.print("$");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    private void nextTurn(Map<String, String> input) {
        for (Player player : game.getPlayers()) {
            System.out.println("Turn for " + player.getUser().getUsername());
            new TurnMenu(scanner, game, player).run();
        }
    }
}
