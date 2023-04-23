package stronghold.operator;

import java.util.Map;
import lombok.Data;
import stronghold.model.Database;
import stronghold.model.User;

@Data
public class AuthOperator {

    private final Database database;

    public static Result<User> findUser(Map<String, Object> req) {
        return null;
    }

    public static Result<User> register(Map<String, Object> req) {
        return null;
    }

    public static Result<User> login(Map<String, Object> req) {
        return null;
    }
}
