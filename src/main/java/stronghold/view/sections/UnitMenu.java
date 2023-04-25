package stronghold.view.sections;

import java.util.Map;
import java.util.Scanner;
import stronghold.model.Unit;
import stronghold.view.Menu;

public class UnitMenu extends Menu {

    private final Unit unit;

    public UnitMenu(Scanner scanner, Unit unit) {
        super(scanner);
        this.unit = unit;
    }

    private void moveUnit(Map<String, String> input) {
    }

    private void patrolUnit(Map<String, String> input) {
    }

    private void changeUnitMode(Map<String, String> input) {
    }

    private void attack(Map<String, String> input) {
    }

    private void pourOil(Map<String, String> input) {
    }

    private void digTunnel(Map<String, String> input) {
    }

    private void buildEquipment(Map<String, String> input) {
    }

    private void disbandUnit(Map<String, String> input) {
    }
}
