package stronghold;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import stronghold.model.Database;
import stronghold.operator.Operators;
import stronghold.view.sections.AuthMenu;

public class Main {

    public static void main(String[] args) throws IOException {
        File databaseFile = new File("./data/database.ser");
        Database database = Database.fromFileOrDefault(databaseFile);
        Operators.initDatabase(database);

        Scanner scanner = new Scanner(System.in);
        new AuthMenu(scanner).run();

        database.toFile(databaseFile);
    }
}
