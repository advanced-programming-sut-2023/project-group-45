package org.example.stronghold.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.Data;

@Data
public class SQLExecutor {

    private final String url;
    private Connection conn;

    private Connection connect() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void open() throws SQLException {
        conn = connect();
        conn.setAutoCommit(true);
    }

    public void close() throws SQLException {
        conn.close();
    }

    public void execute(String sql) throws SQLException {
        conn.createStatement().execute(sql);
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        return conn.createStatement().executeQuery(sql);
    }

    public void insertBlob(String sql, byte[] bytes) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setBytes(1, bytes);
        statement.executeUpdate();
    }
}
