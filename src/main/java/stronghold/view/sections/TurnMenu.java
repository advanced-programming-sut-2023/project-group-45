package stronghold.view.sections;

import java.util.Map;
import java.util.Scanner;
import stronghold.model.Game;
import stronghold.model.Player;
import stronghold.view.Menu;

public class TurnMenu extends Menu {

    private final Game game;
    private final Player player;

    public TurnMenu(Scanner scanner, Game game, Player player) {
        super(scanner);
        this.game = game;
        this.player = player;
        addCommand("who-am-i", this::whoAmI);
        addCommand("show-resources", this::showResources);
    }

    private void whoAmI(Map<String, String> input) {
        System.out.println("You are " + player.getUser().getUsername());
    }

    private void showResources(Map<String, String> input) {
        // todo: only for testing purposes
        System.out.println("Your resources: " + player.getResources());
    }
}
