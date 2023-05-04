package stronghold;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import stronghold.model.Database;
import stronghold.model.template.TemplateDatabase;
import stronghold.operator.Operators;
import stronghold.view.sections.AuthMenu;

public class Main {

    private static final File databaseFile = new File("./data/database.ser");
    private static final File templateDatabaseRoot = new File("./data");

    public static void main(String[] args) throws IOException {
        Database database = Database.fromFileOrDefault(databaseFile);
        TemplateDatabase templateDatabase = new TemplateDatabase();
        templateDatabase.updateFromPath(templateDatabaseRoot);
        Operators.initDatabase(database, templateDatabase);

        Scanner scanner = new Scanner(System.in);
        new AuthMenu(scanner).run();

        database.toFile(databaseFile);
        templateDatabase.saveToPath(templateDatabaseRoot);
    }
}
