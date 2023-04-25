package stronghold.operator.sections;

import java.util.Map;
import lombok.Data;
import stronghold.model.Building;
import stronghold.model.Database;
import stronghold.operator.Result;

@Data
public final class BuildingOperator {
    private final Database database;

    public static Result<Building> dropBuilding(Map<Object, String> req) {
        return null;
    }

    // TODO: return value of trainUnit, (create units inplace or in next turns)
    public static Result<Void> trainUnit(Map<Object, String> req) {
        return null;
    }

    public static Result<Building> repairBuilding(Map<Object, String> req) {
        return null;
    }
}
