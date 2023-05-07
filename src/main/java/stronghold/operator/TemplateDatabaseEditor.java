package stronghold.operator;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import stronghold.model.template.BuildingTemplate;
import stronghold.model.template.TemplateDatabase;
import stronghold.model.template.UnitTemplate;

public class TemplateDatabaseEditor {

    private static final File templateDatabaseRoot = new File("./data");

    public static void main(String[] args) throws IOException {
        TemplateDatabase templateDatabase = new TemplateDatabase();
        templateDatabase.updateFromPath(templateDatabaseRoot);
        Map<String, UnitTemplate> unitTemplates = templateDatabase.getUnitTemplates();
        Map<String, BuildingTemplate> buildingTemplates = templateDatabase.getBuildingTemplates();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("exit")) {
            String[] tokens = input.split(" ");
            switch (tokens[0]) {
                case "addUnit" -> {
                    String type = tokens[1];
                    int maxHitPoints = Integer.parseInt(tokens[3]);
                    int speed = Integer.parseInt(tokens[4]);
                    int damage = Integer.parseInt(tokens[2]);
                    UnitTemplate unitTemplate = new UnitTemplate(type, maxHitPoints * 10,
                            speed, 1, damage, false, false);
                    unitTemplates.put(type, unitTemplate);
                }
                case "removeUnit" -> {
                    String type = tokens[1];
                    unitTemplates.remove(type);
                }
                case "list" -> {
                    if (tokens[1].equals("units")) {
                        unitTemplates.forEach((type, unitTemplate) -> System.out.println(
                                type + " " + unitTemplate));
                    } else if (tokens[1].equals("buildings")) {
                        buildingTemplates.forEach((type, buildingTemplate) -> System.out.println(
                                type + " " + buildingTemplate));
                    }
                }
                case "addBuilding" -> {
                    String type = tokens[1];
                    int maxHitPoints = Integer.parseInt(tokens[2]);
                    BuildingTemplate buildingTemplate = new BuildingTemplate(type, maxHitPoints);
                    buildingTemplates.put(type, buildingTemplate);
                }
                case "removeBuilding" -> {
                    String type = tokens[1];
                    buildingTemplates.remove(type);
                }
                default -> System.out.println("Invalid command");
            }
            input = scanner.nextLine();
        }
        templateDatabase.saveToPath(templateDatabaseRoot, true);
    }
}
