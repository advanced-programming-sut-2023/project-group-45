package stronghold.operator;

import java.util.Map;
import stronghold.model.Building;

public final class BuildingOperator {

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

    private BuildingOperator() {
    }
}
