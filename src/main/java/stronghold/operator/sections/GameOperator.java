package stronghold.operator.sections;

import static stronghold.context.MapUtils.getReqAs;
import static stronghold.operator.OperatorPreconditions.checkExpression;
import static stronghold.operator.OperatorPreconditions.checkNotNull;
import static stronghold.operator.OperatorPreconditions.checkUserExists;

import java.util.List;
import java.util.Map;
import lombok.Data;
import stronghold.model.Database;
import stronghold.model.Game;
import stronghold.model.User;
import stronghold.model.template.BuildingTemplate;
import stronghold.model.template.GameMapTemplate;
import stronghold.model.template.TemplateDatabase;
import stronghold.model.template.UnitTemplate;
import stronghold.operator.OperatorException;
import stronghold.operator.OperatorException.Type;

@Data
public class GameOperator {

    private final Database database;
    private final TemplateDatabase templateDatabase;


    public Game startGame(Map<String, Object> req) throws OperatorException {
        GameMapTemplate gameMapTemplate = getReqAs(req, "map", GameMapTemplate.class);
        List<User> users = getReqAs(req, "users", List.class);
        checkExpression(users.size() >= 2, Type.INVALID_GAME_PARAMETERS);
        checkExpression(users.size() <= gameMapTemplate.getBases().size(),
                Type.INVALID_GAME_PARAMETERS);
        for (User user : users) {
            checkUserExists(database, user.getUsername());
        }
        UnitTemplate lordTemplate = checkNotNull(templateDatabase.getUnitTemplates().get("Lord"),
                Type.UNIT_NOT_FOUND);
        BuildingTemplate baseTemplate = checkNotNull(templateDatabase.getBuildingTemplates()
                .get("Base"), Type.BUILDING_NOT_FOUND);
        return new Game(users, gameMapTemplate, lordTemplate, baseTemplate);
    }

}
