package view;

import model.Building;

import java.util.HashMap;
import java.util.Scanner;

public class BuildingMenu extends Menu{
    private final Building building;

    public BuildingMenu(Scanner scanner, Building building){
        super(scanner);
        this.building = building;
    }

    private void createUnit(HashMap<String, String> input){
        return;
    }

    private void repair(HashMap<String, String> input){
        return;
    }
}
