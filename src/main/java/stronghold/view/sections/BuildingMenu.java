package stronghold.view.sections;

import java.util.Map;
import java.util.Scanner;
import stronghold.model.Building;
import stronghold.view.Menu;

public class BuildingMenu extends Menu {

    private final Building building;

    public BuildingMenu(Scanner scanner, Building building) {
        super(scanner);
        this.building = building;
    }

    private void createUnit(Map<String, String> input) {
    }

    private void repair(Map<String, String> input) {
    }
}
