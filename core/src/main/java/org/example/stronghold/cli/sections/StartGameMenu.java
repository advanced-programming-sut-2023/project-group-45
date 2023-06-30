package org.example.stronghold.cli.sections;

import static org.example.stronghold.context.MapUtils.copyOptTo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.example.stronghold.cli.Menu;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.User;
import org.example.stronghold.model.template.GameMapTemplate;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class StartGameMenu extends Menu {

    private final GameMapTemplate gameMap;
    private final User starter;
    private final List<User> players = new ArrayList<>();

    public StartGameMenu(Scanner scanner, User starter, GameMapTemplate gameMap) {
        super(scanner);
        this.gameMap = gameMap;
        this.starter = starter;
        addCommand("add-opponent", this::addOpponent);
        addCommand("show-opponents", this::showOpponents);
        addCommand("clear-opponents", this::clearOpponents);
        addCommand("start-game", this::startGame);
    }

    private void addOpponent(Map<String, String> input) {
        if (players.size() + 1 >= gameMap.getBases().size()) {
            System.out.println("You cannot add more opponents");
            return;
        }
        try {
            User user = Operators.auth.findUser(new HashMap<>() {{
                copyOptTo(input, this, "username");
            }});
            if (user == starter) {
                System.out.println("You cannot add yourself as an opponent");
                return;
            }
            if (players.contains(user)) {
                System.out.println("This user is already your opponent");
                return;
            }
            players.add(user);
            System.out.println("Added to your opponents");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showOpponents(Map<String, String> input) {
        if (players.isEmpty()) {
            System.out.println("You have no opponents");
            return;
        }
        System.out.println("Your opponents:");
        for (User player : players) {
            System.out.println(" - " + player.getUsername());
        }
    }

    private void clearOpponents(Map<String, String> input) {
        players.clear();
        System.out.println("Cleared opponents");
    }

    private void startGame(Map<String, String> input) {
        try {
            GameData gameData = Operators.game.startGame(new HashMap<>() {{
                put("map", gameMap);
                put("users", new ArrayList<>(players) {{
                    add(starter);
                }});
            }});
            System.out.println("Let's start the game!");
            new FrameMenu(scanner, gameData).run();
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }
}
