package stronghold.model;

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
import lombok.Getter;
import lombok.Setter;

@Data
public class Database implements Serializable {

    private final List<User> users = new ArrayList<>();
    private final Market market = new Market();

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
}
