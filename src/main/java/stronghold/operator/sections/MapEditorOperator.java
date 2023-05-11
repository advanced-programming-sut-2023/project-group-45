package stronghold.operator.sections;

import static stronghold.context.MapUtils.getReqAs;
import static stronghold.context.MapUtils.getReqString;
import static stronghold.operator.OperatorPreconditions.checkNotNull;

import java.util.Map;
import lombok.Data;
import stronghold.model.Database;
import stronghold.model.template.GameMapTemplate;
import stronghold.model.template.TemplateDatabase;
import stronghold.operator.OperatorException;
import stronghold.operator.OperatorException.Type;

@Data
public final class MapEditorOperator {

    private final Database database;
    private final TemplateDatabase templateDatabase;

    public GameMapTemplate getGameMap(Map<String, Object> req) throws OperatorException {
        String name = getReqString(req, "name");
        return checkNotNull(templateDatabase.getGameMapTemplates().get(name), Type.MAP_NOT_FOUND);
    }

    public void saveGameMap(Map<String, Object> req) {
        GameMapTemplate gameMap = getReqAs(req, "game-map", GameMapTemplate.class);
        templateDatabase.getGameMapTemplates().put(gameMap.getName(), gameMap);
    }
}
