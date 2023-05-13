package stronghold.view.sections;

import static stronghold.context.MapUtils.getIntPairOpt;
import static stronghold.context.MapUtils.getOpt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import stronghold.context.IntPair;
import stronghold.model.Game;
import stronghold.model.Player;
import stronghold.model.Unit;
import stronghold.operator.OperatorException;
import stronghold.operator.Operators;
import stronghold.view.Menu;

public class UnitMenu extends Menu {

    private final Game game;
    private final Player player;
    private final List<Unit> selection = new ArrayList<>();

    public UnitMenu(Scanner scanner, Game game, Player player) {
        super(scanner);
        this.game = game;
        this.player = player;
        addCommand("show-selection", this::showSelection);
        addCommand("select-position", this::selectPosition);
        addCommand("select-type", this::selectType);
        addCommand("clear-selection", this::clearSelection);
        addCommand("move-to", this::moveTo);
        addCommand("attack-to", this::attackTo);
    }

    private void showSelection(Map<String, String> input) {
        if (selection.isEmpty()) {
            System.out.println("No units selected.");
            return;
        }
        System.out.println("Selected units:");
        for (Unit unit : selection) {
            System.out.printf("- %s at (%d,%d)\n", unit.getType(), unit.getPosition().x(),
                    unit.getPosition().y());
        }
    }

    private void selectPosition(Map<String, String> input) {
        IntPair position = getIntPairOpt(input, "x", "y");
        List<Unit> selected = game.getUnits().stream()
                .filter(u -> u.getPosition().equals(position))
                .filter(u -> u.getOwner() == player)
                .toList();
        if (selected.isEmpty()) {
            System.out.println("No units at this position.");
            return;
        }
        selection.addAll(selected);
        System.out.printf("Selected %d units at (%d,%d)\n", selected.size(), position.x(),
                position.y());
    }

    private void selectType(Map<String, String> input) {
        String type = getOpt(input, "type");
        List<Unit> selected = game.getUnits().stream()
                .filter(u -> u.getType().equals(type))
                .filter(u -> u.getOwner() == player)
                .toList();
        if (selected.isEmpty()) {
            System.out.println("No units of this type.");
            return;
        }
        selection.addAll(selected);
        System.out.printf("Selected %d units of type %s\n", selected.size(), type);
    }

    private void clearSelection(Map<String, String> input) {
        selection.clear();
        System.out.println("Selection cleared");
    }

    private void moveTo(Map<String, String> input) {
        IntPair goal = getIntPairOpt(input, "x", "y");
        try {
            Operators.game.setNavigationGoal(new HashMap<>() {{
                put("game", game);
                put("units", selection);
                put("position", goal);
            }});
            System.out.println("Units ordered to move");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void attackTo(Map<String, String> input) {
        IntPair position = getIntPairOpt(input, "x", "y");
        try {
            Unit unit = Operators.game.attackUnit(new HashMap<>() {{
                put("game", game);
                put("player", player);
                put("units", selection);
                put("position", position);
            }});
            System.out.println(
                    "Units ordered to attack " + unit.getType() + " at (" + position.x() + ","
                            + position.y() + ")");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }
}
