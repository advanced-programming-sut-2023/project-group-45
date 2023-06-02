package org.example.stronghold.operator.sections;

import static org.example.stronghold.context.MapUtils.getReqAs;
import static org.example.stronghold.context.MapUtils.getReqString;
import static org.example.stronghold.operator.OperatorPreconditions.checkNotNull;

import java.util.Map;
import lombok.Data;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.template.GameMapTemplate;
import org.example.stronghold.model.template.TemplateDatabase;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.OperatorException.Type;

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
