package org.example.stronghold.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Cleanup;
import lombok.Data;

@Data
public class Database implements Serializable {

    private final List<User> users = new ArrayList<>();
    private User stayLoggedInUser = null;

    public static Database fromFile(File file) throws IOException, ClassNotFoundException {
        @Cleanup FileInputStream inputStream = new FileInputStream(file);
        @Cleanup ObjectInputStream objectStream = new ObjectInputStream(inputStream);
        return (Database) objectStream.readObject();
    }

    public static Database fromFileOrDefault(File file) {
        try {
            return fromFile(file);
        } catch (IOException | ClassNotFoundException e) {
            return new Database();
        }
    }

    public void toFile(File file) throws IOException {
        @Cleanup FileOutputStream outputStream = new FileOutputStream(file);
        @Cleanup ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
        objectStream.writeObject(this);
    }

    public User getUserFromUsername(String username) {
        return users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }

    public User getUserFromEmail(String email) {
        return users.stream()
            .filter(user -> user.getEmail().equals(email))
            .findFirst()
            .orElse(null);
    }

    public boolean isStayLoggedInUsername(String username) {
        return stayLoggedInUser != null && username.equals(stayLoggedInUser.getUsername());
    }
}
