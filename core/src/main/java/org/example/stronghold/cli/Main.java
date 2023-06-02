package org.example.stronghold.cli;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.template.TemplateDatabase;
import org.example.stronghold.operator.Operators;
import org.example.stronghold.cli.sections.AuthMenu;

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
