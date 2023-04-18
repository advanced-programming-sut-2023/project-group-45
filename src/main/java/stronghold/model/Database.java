package stronghold.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Database implements Serializable {
    private static Database ACTIVE_DB = null;

    private final List<User> users = new ArrayList<>();
    private final Market market = new Market();

    public static Database getActive() {
        return ACTIVE_DB;
    }
}
