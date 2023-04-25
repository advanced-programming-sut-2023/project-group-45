package stronghold.operator.sections;

import java.util.Map;
import lombok.Data;
import stronghold.model.Database;
import stronghold.model.GameMap;
import stronghold.model.Tile;
import stronghold.operator.Result;

@Data
public final class GameMapOperator {

    private final Database database;

    public static Result<GameMap> getGameMap(Map<Object, String> req) {
        return null;
    }

    public static Result<Tile> changeTileTexture(Map<Object, String> req) {
        return null;
    }

    public static Result<Tile> clearTile(Map<Object, String> req) {
        return null;
    }

    public static Result<Tile> dropRock(Map<Object, String> req) {
        return null;
    }

    public static Result<Tile> dropTree(Map<Object, String> req) {
        return null;
    }
}
