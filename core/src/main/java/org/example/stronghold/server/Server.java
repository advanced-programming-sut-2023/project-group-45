package org.example.stronghold.server;

import java.io.File;
import java.io.IOException;
import lombok.Data;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.template.TemplateDatabase;
import org.example.stronghold.operator.Operators;

@Data
public class Server {
    public static final int PORT = 2222;
    private static final File databaseFile = new File("./assets/data/database.ser");
    private static final File templateDatabaseRoot = new File("./assets/data");

    public static void main(String[] args) throws IOException {
        Database database = Database.fromFileOrDefault(databaseFile);
        TemplateDatabase templateDatabase = new TemplateDatabase();
        templateDatabase.updateFromPath(templateDatabaseRoot);
        Operators.initDatabase(database, templateDatabase);

        ConnectionHandler connectionHandler = new ConnectionHandler(PORT);
        connectionHandler.run();

        database.toFile(databaseFile);
        templateDatabase.saveToPath(templateDatabaseRoot);
    }
}
