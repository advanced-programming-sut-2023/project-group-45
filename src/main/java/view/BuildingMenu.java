package view;

import java.util.HashMap;
import java.util.Scanner;
import model.Building;

public class BuildingMenu extends Menu {

    private final Building building;

    public BuildingMenu(Scanner scanner, Building building) {
        super(scanner);
        this.building = building;
    }

    private void createUnit(HashMap<String, String> input) {
    }

    private void repair(HashMap<String, String> input) {
    }
}
