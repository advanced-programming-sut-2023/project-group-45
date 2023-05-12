package stronghold.operator.sections;

import static stronghold.context.MapUtils.addIntMap;
import static stronghold.context.MapUtils.geqIntMap;
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

    private static final boolean VERBOSE = true;
    private final Database database;
    private final TemplateDatabase templateDatabase;

    public void log(String format, Object... args) {
        if (VERBOSE) {
            System.out.printf(format + "\n", args);
        }
    }

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

    private void assignLabor(Game game) {
        for (Building building : game.getBuildings()) {
            Player player = building.getOwner();
            if (building.getLabors() < building.getMaxLabors()) {
                int workersToAssign = building.getMaxLabors() - building.getLabors();
                int workersToTake = Math.min(workersToAssign, player.getPeasants());
                player.setPeasants(player.getPeasants() - workersToTake);
                building.setLabors(building.getLabors() + workersToTake);
                log("assign labors [count=%s, building=%s]", workersToTake, building);
            }
        }
    }

    private void manageSupplyChain(Game game) {
        for (Building building : game.getBuildings()) {
            if (building.getLabors() < building.getMaxLabors() || building.getSupply().isEmpty()) {
                continue;
            }
            Player player = building.getOwner();
            if (!geqIntMap(player.getResources(), building.getConsume())) {
                continue;
            }
            subtractIntMap(player.getResources(), building.getConsume());
            addIntMap(player.getResources(), building.getSupply());
            log("supply chain [consume=%s, supply=%s, building=%s]",
                    building.getConsume(), building.getSupply(), building);
        }
    }

    private void manageOverpopulate(Game game) {
        for (Player player : game.getPlayers()) {
            int housingSpace = game.getHousingSpace(player);
            int totalPeasants = game.getTotalPeasants(player);
            if (totalPeasants <= housingSpace) {
                continue;
            }
            int notLabor = Math.min(player.getPeasants(), totalPeasants - housingSpace);
            player.setPeasants(player.getPeasants() - notLabor);
            totalPeasants -= notLabor;
            log("overpopulate [notLabor=%s, player=%s]", notLabor, player);
            if (totalPeasants <= housingSpace) {
                continue;
            }
            for (Building building : game.getBuildingsByOwner(player).toList()) {
                while (totalPeasants > housingSpace && building.getLabors() > 0) {
                    totalPeasants--;
                    building.setLabors(building.getLabors() - 1);
                    log("overpopulate [labor=1, building=%s]", building);
                }
            }
        }
    }

    private void manageFood(Game game) {
        for (Player player : game.getPlayers()) {
            int peasants = game.getTotalPeasants(player);
            int totalFoods = (int) (peasants * player.getFoodRation());
            while (totalFoods > 0) {
                int minFood = totalFoods, nonZero = 0;
                for (String food : Game.FOODS) {
                    int count = player.getResources().getOrDefault(food, 0);
                    if (count > 0) {
                        minFood = Math.min(minFood, count);
                        nonZero++;
                    }
                }
                if (nonZero == 0) {
                    player.setFoodRate(-2);
                    log("reset food rate [player=%s]", player);
                    break;
                }
                minFood = Math.min(minFood, (totalFoods + nonZero - 1) / nonZero);
                for (String food : Game.FOODS) {
                    int count = player.getResources().getOrDefault(food, 0);
                    if (count <= 0) {
                        continue;
                    }
                    totalFoods -= minFood;
                    player.getResources().put(food, count - minFood);
                    log("eat food [food=%s, count=%s, player=%s]", food, minFood, player);
                    minFood = Math.min(minFood, totalFoods);
                }
            }
        }
    }

    private void manageTax(Game game) {
        for (Player player : game.getPlayers()) {
            int peasants = game.getTotalPeasants(player);
            int totalTax = (int) (peasants * player.getTaxPerPeasant());
            int golds = player.getResources().getOrDefault("gold", 0);
            if (golds + totalTax < 0) {
                player.setTaxRate(0);
                log("reset tax rate [player=%s]", player);
                continue;
            }
            golds += totalTax;
            player.getResources().put("gold", golds);
            log("collect tax [gold=%s, player=%s]", totalTax, player);
        }
    }

    public void nextFrame(Map<String, Object> req) throws OperatorException {
        Game game = getReqAs(req, "game", Game.class);
        assignLabor(game);
        manageSupplyChain(game);
        manageOverpopulate(game);
        manageFood(game);
        manageTax(game);
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
        checkExpression(geqIntMap(player.getResources(), buildingTemplate.getBuildCost()),
                Type.NOT_ENOUGH_RESOURCE);
        subtractIntMap(player.getResources(), buildingTemplate.getBuildCost());
        Building building = buildingTemplate.getBuilder()
                .owner(player)
                .position(position)
                .build();
        tile.setBuilding(building);
        game.getBuildings().add(building);
    }

    public void setFoodRate(Map<String, Object> req) throws OperatorException {
        Player player = getReqAs(req, "player", Player.class);
        int foodRate = getReqAs(req, "rate", Integer.class);
        checkExpression(foodRate >= -2 && foodRate <= 2, Type.INVALID_GAME_PARAMETERS);
        player.setFoodRate(foodRate);
    }

    public void setTaxRate(Map<String, Object> req) throws OperatorException {
        Player player = getReqAs(req, "player", Player.class);
        int taxRate = getReqAs(req, "rate", Integer.class);
        checkExpression(taxRate >= -3 && taxRate <= 8, Type.INVALID_GAME_PARAMETERS);
        player.setTaxRate(taxRate);
    }
}
