package org.example.stronghold.cli.sections;

import static org.example.stronghold.context.MapUtils.copyOptTo;
import static org.example.stronghold.context.MapUtils.getIntPairOpt;
import static org.example.stronghold.context.MapUtils.getOpt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.example.stronghold.cli.Menu;
import org.example.stronghold.context.IntPair;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Player;
import org.example.stronghold.model.Unit;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class UnitMenu extends Menu {

    private final GameData gameData;
    private final Player player;
    private final List<Unit> selection = new ArrayList<>();

    public UnitMenu(Scanner scanner, GameData gameData, Player player) {
        super(scanner);
        this.gameData = gameData;
        this.player = player;
        addCommand("show-selection", this::showSelection);
        addCommand("select-at", this::selectPosition);
        addCommand("select-type", this::selectType);
        addCommand("clear-selection", this::clearSelection);
        addCommand("move-to", this::moveTo);
        addCommand("attack-to", this::attackTo);
        addCommand("set-mode", this::setMode);
        addCommand("set-patrol", this::setPatrol);
        addCommand("disband", this::disband);
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
        List<Unit> selected = gameData.getUnits().stream()
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
        List<Unit> selected = gameData.getUnits().stream()
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
                put("game", gameData);
                put("units", selection);
                put("position", goal);
            }});
            System.out.println("Units ordered to move");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setPatrol(Map<String, String> input) {
        IntPair[] path = {getIntPairOpt(input, "x1", "y1"), getIntPairOpt(input, "x2", "y2")};
        try {
            Operators.game.setPatrol(new HashMap<>() {{
                put("game", gameData);
                put("units", selection);
                put("path", path);
            }});
            System.out.println("Units ordered to patrol");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void attackTo(Map<String, String> input) {
        IntPair position = getIntPairOpt(input, "x", "y");
        try {
            Unit unit = Operators.game.attackUnit(new HashMap<>() {{
                put("game", gameData);
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

    private void setMode(Map<String, String> input) {
        try {
            Operators.game.setUnitMode(new HashMap<>() {{
                put("units", selection);
                copyOptTo(input, this, "mode");
            }});
            System.out.println("Mode changed successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void disband(Map<String, String> input) {
        try {
            Operators.game.disbandUnits(new HashMap<>() {{
                put("game", gameData);
                put("units", selection);
            }});
            System.out.println("Units disbanded successfully");
            selection.clear();
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }
}
