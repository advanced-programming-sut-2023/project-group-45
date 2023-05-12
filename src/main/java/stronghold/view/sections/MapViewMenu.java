package stronghold.view.sections;

import static stronghold.context.ANSIColor.BLACK;
import static stronghold.context.ANSIColor.BLUE;
import static stronghold.context.ANSIColor.GREEN;
import static stronghold.context.ANSIColor.PURPLE;
import static stronghold.context.ANSIColor.RESET;
import static stronghold.context.ANSIColor.WHITE;
import static stronghold.context.ANSIColor.YELLOW;
import static stronghold.context.MapUtils.getIntOpt;

import java.util.Map;
import java.util.Scanner;
import stronghold.model.Game;
import stronghold.model.Tile;
import stronghold.view.Menu;

public class MapViewMenu extends Menu {

    private static final int MAX_HEIGHT = 7, MAX_WIDTH = 20;

    private final Game game;
    private int yStart = 0;
    private int xStart = 0;

    public MapViewMenu(Scanner scanner, Game game) {
        super(scanner);
        this.game = game;
        addCommand("show", x -> this.showMap());
        addCommand("move", this::moveMap);
        this.showMap();
    }

    private void moveMap(Map<String, String> input) {
        int dy = 0, dx = 0;
        if (input.containsKey("up")) {
            dy -= getIntOpt(input, "up");
        }
        if (input.containsKey("down")) {
            dy += getIntOpt(input, "down");
        }
        if (input.containsKey("left")) {
            dx -= getIntOpt(input, "left");
        }
        if (input.containsKey("right")) {
            dx += getIntOpt(input, "right");
        }
        if (yStart + dy < 0 || yStart + dy + MAX_HEIGHT > game.getMap().getHeight()
                || xStart + dx < 0 || xStart + dx + MAX_WIDTH > game.getMap().getWidth()) {
            System.out.println("Cannot move that way");
            return;
        }
        yStart += dy;
        xStart += dx;
        this.showMap();
    }

    private String getColorByType(String type) {
        if (type.startsWith("tree")) {
            return GREEN.background() + BLACK.foreground();
        }
        if (type.equals("farmland")) {
            return GREEN.brightBackground() + BLACK.foreground();
        }
        if (type.equals("water")) {
            return BLUE.brightBackground() + BLACK.foreground();
        }
        if (type.equals("stone")) {
            return WHITE.background() + BLACK.foreground();
        }
        if (type.equals("iron")) {
            return YELLOW.background() + BLACK.foreground();
        }
        if (type.startsWith("rock")) {
            return BLACK.brightBackground();
        }
        if (type.equals("oil")) {
            return PURPLE.background() + BLACK.foreground();
        }
        return "";
    }

    private void setColorByTile(Tile tile) {
        System.out.print(getColorByType(tile.getType()));
    }

    private void resetColor() {
        System.out.print(RESET);
    }

    private void showMap() {
        int length = 4 * MAX_WIDTH - 1;
        String startPos = "(" + xStart + "," + yStart + ")";
        System.out.println("┌" + startPos + "─".repeat(length - startPos.length()) + "┐");
        for (int y = yStart; y < yStart + MAX_HEIGHT; y++) {
            System.out.print("│");
            for (int x = xStart; x < xStart + MAX_WIDTH; x++) {
                Tile tile = game.getMap().getAt(x, y);
                setColorByTile(tile);
                System.out.print(tile.getType().equals("plain") ? "." : tile.getType().charAt(0));
                System.out.print("..");
                resetColor();
                if (x + 1 != xStart + MAX_WIDTH) {
                    System.out.print("│");
                }
            }
            System.out.print("│\n│");
            for (int x = xStart; x < xStart + MAX_WIDTH; x++) {
                Tile tile = game.getMap().getAt(x, y);
                setColorByTile(tile);
                System.out.print(tile.getBuilding() == null ? "..."
                        : tile.getBuilding().getType().substring(0, 3));
                resetColor();
                if (x + 1 != xStart + MAX_WIDTH) {
                    System.out.print("│");
                }
            }
            System.out.println("│");
            if (y + 1 == yStart + MAX_HEIGHT) {
                continue;
            }
            System.out.print("├");
            for (int x = xStart; x < xStart + MAX_WIDTH; x++) {
                System.out.print("───");
                if (x + 1 != xStart + MAX_WIDTH) {
                    System.out.print("┼");
                }
            }
            System.out.println("┤");
        }
        String endPos = "(" + (xStart + MAX_WIDTH - 1) + "," + (yStart + MAX_HEIGHT - 1) + ")";
        System.out.println("└" + "─".repeat(length - endPos.length()) + endPos + "┘");
    }
}
