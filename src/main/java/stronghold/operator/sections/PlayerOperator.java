package stronghold.operator.sections;

import java.util.Map;
import lombok.Data;
import stronghold.model.Database;
import stronghold.model.Player;
import stronghold.operator.Result;

@Data
public final class PlayerOperator {

    private final Database database;

    public static Result<Player> getPlayer(Map<Object, String> req) {
        return null;
    }

    public static Result<Integer> changeFoodRate(Map<Object, String> req) {
        return null;
    }

    public static Result<Integer> changeTaxRate(Map<Object, String> req) {
        return null;
    }

    public static Result<Integer> changeFearRate(Map<Object, String> req) {
        return null;
    }
}
