package stronghold.view.sections;

import static stronghold.context.ANSIColor.BLACK;
import static stronghold.context.ANSIColor.BLUE;
import static stronghold.context.ANSIColor.GREEN;
import static stronghold.context.ANSIColor.RESET;
import static stronghold.context.ANSIColor.WHITE;
import static stronghold.context.ANSIColor.YELLOW;

import java.util.Map;
import java.util.Scanner;
import stronghold.model.Game;
import stronghold.model.Tile;
import stronghold.view.Menu;

public class MapViewMenu extends Menu {

    private static final int MAX_HEIGHT = 5, MAX_WIDTH = 5;

    private final Game game;
    private final int yStart = 0;
    private final int xStart = 0;

    public MapViewMenu(Scanner scanner, Game game) {
        super(scanner);
        this.game = game;
        addCommand("show-map", this::showMap);
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
            return WHITE.background();
        }
        if (type.equals("iron")) {
            return YELLOW.background() + BLACK.foreground();
        }
        if (type.startsWith("rock")) {
            return BLACK.brightBackground() + WHITE.foreground();
        }
        return "";
    }

    private void setColorByTile(Tile tile) {
        System.out.print(getColorByType(tile.getType()));
    }

    private void resetColor() {
        System.out.print(RESET);
    }

    private void showMap(Map<String, String> input) {
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
