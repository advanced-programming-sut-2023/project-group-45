package stronghold.operator;

import java.util.Map;
import lombok.Data;
import stronghold.model.Database;
import stronghold.model.Unit;

@Data
public final class UnitOperator {
    private final Database database;

    public static Result<Unit> dropUnit(Map<Object, String> req) {
        return null;
    }

    public static Result<Unit> moveUnit(Map<Object, String> req) {
        return null;
    }

    public static Result<Unit> patrolUnit(Map<Object, String> req) {
        return null;
    }

    public static Result<Unit> changeUnitMode(Map<Object, String> req) {
        return null;
    }

    public static Result<Unit> pourOil(Map<Object, String> req) {
        return null;
    }

    public static Result<Unit> digTunnel(Map<Object, String> req) {
        return null;
    }

    public static Result<Unit> buildEquipment(Map<Object, String> req) {
        return null;
    }

    public static Result<Unit> disbandUnit(Map<Object, String> req) {
        return null;
    }
}
