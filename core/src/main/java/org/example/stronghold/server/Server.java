package org.example.stronghold.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.Data;
import org.example.stronghold.model.Chat;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.SQLExecutor;
import org.example.stronghold.model.template.TemplateDatabase;
import org.example.stronghold.operator.Operators;

@Data
public class Server {

    public static final int PORT = 2222;
    private static final File databaseFile = new File("./assets/data/database.ser");
    private static final File templateDatabaseRoot = new File("./assets/data");
    private static final String sqliteUrl = "jdbc:sqlite:assets/data/database.sqlite";
    private static Database database;
    private static TemplateDatabase templateDatabase;
    private static SQLExecutor sqlExecutor;

    private static void createTable() throws SQLException {
        String sql =
            "create table if not exists objects(" +
                "   id integer primary key," +
                "   object blob not null" +
                ");";
        sqlExecutor.execute(sql);
    }

    private static Database queryDatabaseOrDefault() throws Exception {
        String sql = "select object from objects where id = 1;";
        try (ResultSet resultSet = sqlExecutor.executeQuery(sql)) {
            if (resultSet.next()) {
                byte[] bytes = resultSet.getBytes("object");
                return Database.fromBytes(bytes);
            }
        }
        return Database.fromFileOrDefault(databaseFile);
    }

    private static void saveDatabase() throws Exception {
        String sql = "insert into objects(id, object) values(1, ?);";
        try (ResultSet resultSet = sqlExecutor.executeQuery(
            "select * from objects where id = 1;")) {
            if (resultSet.next()) {
                sqlExecutor.execute("delete from objects where id = 1");
            }
        }
        sqlExecutor.insertBlob(sql, toBytes(database));
    }

    public static byte[] toBytes(Serializable object) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
        objectStream.writeObject(object);
        objectStream.flush();
        return outputStream.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        sqlExecutor = new SQLExecutor(sqliteUrl);
        sqlExecutor.open();
        createTable();
        database = queryDatabaseOrDefault();
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
            saveDatabase();
            sqlExecutor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
