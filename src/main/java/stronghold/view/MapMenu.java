package stronghold.view;

import java.util.Map;
import java.util.Scanner;

public class MapMenu extends Menu {

    private int x;
    private int y;

    public MapMenu(Scanner scanner, int x, int y) {
        super(scanner);
        this.x = x;
        this.y = y;
    }

    private void move(Map<String, String> input) {
    }

    private void showDetails(Map<String, String> input) {
    }
}