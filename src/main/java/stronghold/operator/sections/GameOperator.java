package stronghold.operator.sections;

import static stronghold.context.MapUtils.getReqAs;
import static stronghold.context.MapUtils.getReqString;
import static stronghold.context.MapUtils.subtractIntMap;
import static stronghold.operator.OperatorPreconditions.checkExpression;
import static stronghold.operator.OperatorPreconditions.checkNotNull;
import static stronghold.operator.OperatorPreconditions.checkUserExists;

import java.util.List;
import java.util.Map;
import lombok.Data;
import stronghold.context.IntPair;
import stronghold.model.Building;
import stronghold.model.Database;
import stronghold.model.Game;
import stronghold.model.Player;
import stronghold.model.Tile;
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

    public void nextFrame(Map<String, Object> req) throws OperatorException {
        Game game = getReqAs(req, "game", Game.class);
        // todo: update game, expect pretty long procedure
    }

    public void dropBuilding(Map<String, Object> req) throws OperatorException {
        Game game = getReqAs(req, "game", Game.class);
        Player player = getReqAs(req, "player", Player.class);
        String buildingType = getReqString(req, "building");
        checkExpression(!buildingType.equals("Base"), Type.BUILDING_NOT_FOUND);
        BuildingTemplate buildingTemplate = checkNotNull(templateDatabase.getBuildingTemplates()
                .get(buildingType), Type.BUILDING_NOT_FOUND);
        IntPair position = getReqAs(req, "position", IntPair.class);
        Tile tile = checkNotNull(game.getMap().getAt(position), Type.INVALID_POSITION);
        checkExpression(tile.getBuilding() == null,
                Type.INVALID_POSITION);
        checkExpression(buildingTemplate.canBeBuiltOn(tile.getType()), Type.INVALID_POSITION);
        for (String resource : buildingTemplate.getBuildCost().keySet()) {
            checkExpression(
                    player.getResources().get(resource) >= buildingTemplate.getBuildCost()
                            .get(resource), Type.NOT_ENOUGH_RESOURCE);
        }
        subtractIntMap(player.getResources(), buildingTemplate.getBuildCost());
        Building building = buildingTemplate.getBuilder()
                .owner(player)
                .position(position)
                .build();
        tile.setBuilding(building);
        game.getBuildings().add(building);
    }
}