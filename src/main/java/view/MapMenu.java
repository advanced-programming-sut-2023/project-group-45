package view;

import java.util.HashMap;
import java.util.Scanner;

public class MapMenu extends Menu {

    private final int x;
    private final int y;

    public MapMenu(Scanner scanner, int x, int y) {
        super(scanner);
        this.x = x;
        this.y = y;
    }

    private void move(HashMap<String, String> input) {
    }

    private void showDetails(HashMap<String, String> input) {
    }
}
