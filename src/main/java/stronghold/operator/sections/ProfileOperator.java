package stronghold.operator.sections;

import java.util.Map;
import lombok.Data;
import stronghold.model.Database;
import stronghold.operator.Result;

@Data
public final class ProfileOperator {
    private final Database database;

    public static Result<String> changePassword(Map<String, Object> req) {
        return null;
    }

    public static Result<String> changeUsername(Map<String, Object> req) {
        return null;
    }

    public static Result<String> changeNickname(Map<String, Object> req) {
        return null;
    }

    public static Result<String> changeEmail(Map<String, Object> req) {
        return null;
    }

    public static Result<String> changeSlogan(Map<String, Object> req) {
        return null;
    }
}
