package stronghold.view.sections;

import static stronghold.context.MapUtils.getIntOpt;
import static stronghold.context.MapUtils.getOpt;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import stronghold.context.IntPair;
import stronghold.model.Game;
import stronghold.model.Player;
import stronghold.operator.OperatorException;
import stronghold.operator.Operators;
import stronghold.view.Menu;

public class TurnMenu extends Menu {

    private final Game game;
    private final Player player;

    public TurnMenu(Scanner scanner, Game game, Player player) {
        super(scanner);
        this.game = game;
        this.player = player;
        addCommand("who-am-i", this::whoAmI);
        addCommand("show-info", this::showInfo);
        addCommand("drop-building", this::dropBuilding);
        addCommand("map-view", this::mapView);
    }

    private void whoAmI(Map<String, String> input) {
        System.out.println("You are " + player.getUser().getUsername());
    }

    private void showInfo(Map<String, String> input) {
        System.out.println("Your resources: " + player.getResources());
        System.out.println("Peasants: " + player.getPeasants());
        System.out.println("Religion: " + game.getReligion(player));
        System.out.println("Happiness: " + game.getHappiness(player));
    }

    private void dropBuilding(Map<String, String> input) {
        String type = getOpt(input, "type");
        int x = getIntOpt(input, "x");
        int y = getIntOpt(input, "y");
        try {
            Operators.game.dropBuilding(new HashMap<>() {{
                put("game", game);
                put("player", player);
                put("building", type);
                put("position", new IntPair(x, y));
            }});
            System.out.println("Building added successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void mapView(Map<String, String> input) {
        System.out.println("Switched to map view");
        new MapViewMenu(scanner, game).run();
    }
}
