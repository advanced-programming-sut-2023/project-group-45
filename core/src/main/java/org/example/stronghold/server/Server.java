package org.example.stronghold.server;

import java.io.File;
import java.io.IOException;
import lombok.Data;
import org.example.stronghold.model.Chat;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.Message;
import org.example.stronghold.model.template.TemplateDatabase;
import org.example.stronghold.operator.Operators;

@Data
public class Server {
    public static final int PORT = 2222;
    private static final File databaseFile = new File("./assets/data/database.ser");
    private static final File templateDatabaseRoot = new File("./assets/data");
    private static Database database;
    private static TemplateDatabase templateDatabase;

    public static void main(String[] args) throws IOException {
        database = Database.fromFileOrDefault(databaseFile);
        Chat.fixObjects(database);
        templateDatabase = new TemplateDatabase();
        templateDatabase.updateFromPath(templateDatabaseRoot);
        Operators.initDatabase(database, templateDatabase);

        Runtime.getRuntime().addShutdownHook(new Thread(Server::shutdown));

        ConnectionHandler connectionHandler = new ConnectionHandler(PORT);
        connectionHandler.run();

    }

    private static void shutdown() {
        try {
            database.toFile(databaseFile);
            templateDatabase.saveToPath(templateDatabaseRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
