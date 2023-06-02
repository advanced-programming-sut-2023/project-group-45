package org.example.stronghold.cli.sections;

import static org.example.stronghold.context.MapUtils.getIntOpt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.example.stronghold.model.Game;
import org.example.stronghold.model.Player;
import org.example.stronghold.model.User;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;
import org.example.stronghold.cli.Menu;

public class FrameMenu extends Menu {

    private final Game game;

    public FrameMenu(Scanner scanner, Game game) {
        super(scanner);
        this.game = game;
        addCommand("next-frame", this::nextFrame);
        addCommand("next-turn", this::nextTurn);
        addCommand("map-view", this::mapView);
    }

    private boolean gameEnded() {
        if (game.getPlayers().size() == 0) {
            System.out.println("Game ended with no absolute winners :/");
            return true;
        }
        if (game.getPlayers().size() == 1) {
            Player player = game.getPlayers().get(0);
            System.out.println("Game ended with " + player.getUser().getUsername()
                    + " as the absolute winner!");
            return true;
        }
        return false;
    }

    private void nextFrame(Map<String, String> input) {
        if (gameEnded()) {
            return;
        }
        int count = 1;
        if (input.containsKey("count")) {
            count = getIntOpt(input, "count");
            if (count <= 0) {
                System.out.println("Count must be positive");
                return;
            }
        }
        List<User> beforeFrame = game.getPlayers().stream().map(Player::getUser).toList();
        for (int i = 0; i < count; i++) {
            try {
                Operators.game.nextFrame(new HashMap<>() {{
                    put("game", game);
                }});
            } catch (OperatorException e) {
                System.out.println("Frame " + (i + 1) + " failed: " + e.getMessage());
            }
        }
        List<User> afterFrame = game.getPlayers().stream().map(Player::getUser).toList();
        for (User user : beforeFrame) {
            if (!afterFrame.contains(user)) {
                System.out.println("Lord " + user.getUsername() + " has been eliminated");
            }
        }
        System.out.println("Frames passed");
    }

    private void nextTurn(Map<String, String> input) {
        if (gameEnded()) {
            return;
        }
        for (Player player : game.getPlayers()) {
            System.out.println("Turn for " + player.getUser().getUsername());
            new TurnMenu(scanner, game, player).run();
        }
    }

    private void mapView(Map<String, String> input) {
        System.out.println("Switched to map view");
        new MapViewMenu(scanner, game).run();
    }
}
