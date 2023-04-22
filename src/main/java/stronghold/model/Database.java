package stronghold.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public final class Database {

    @Getter
    private static final List<User> users = new ArrayList<>();

    @Getter
    private static final Market market = new Market();

    // no instance creation for this class
    private Database() {
    }
}
