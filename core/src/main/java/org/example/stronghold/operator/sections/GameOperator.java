package org.example.stronghold.operator.sections;

import static java.util.stream.Collectors.groupingBy;
import static org.example.stronghold.context.MapUtils.addIntMap;
import static org.example.stronghold.context.MapUtils.geqIntMap;
import static org.example.stronghold.context.MapUtils.getReqAs;
import static org.example.stronghold.context.MapUtils.getReqString;
import static org.example.stronghold.context.MapUtils.randomChoice;
import static org.example.stronghold.context.MapUtils.subtractIntMap;
import static org.example.stronghold.operator.OperatorPreconditions.checkExpression;
import static org.example.stronghold.operator.OperatorPreconditions.checkNotNull;
import static org.example.stronghold.operator.OperatorPreconditions.checkUserExists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.example.stronghold.context.IntPair;
import org.example.stronghold.model.Building;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.Game;
import org.example.stronghold.model.Navigation;
import org.example.stronghold.model.Player;
import org.example.stronghold.model.Tile;
import org.example.stronghold.model.TradeRequest;
import org.example.stronghold.model.Unit;
import org.example.stronghold.model.User;
import org.example.stronghold.model.template.BuildingTemplate;
import org.example.stronghold.model.template.GameMapTemplate;
import org.example.stronghold.model.template.TemplateDatabase;
import org.example.stronghold.model.template.UnitTemplate;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.OperatorException.Type;
import org.example.stronghold.operator.Operators;

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
            if (building.getLabors() < building.getMaxLabors() && building.getHitPoints() > 0) {
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
            if (building.getLabors() < building.getMaxLabors() || building.getHitPoints() <= 0
                    || building.getSupply().isEmpty()) {
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
            if (totalTax == 0) {
                continue;
            }
            golds += totalTax;
            player.getResources().put("gold", golds);
            log("collect tax [gold=%s, player=%s]", totalTax, player);
        }
    }

    private void managePopularity(Game game) {
        for (Player player : game.getPlayers()) {
            int deltaPopularity = 0;
            deltaPopularity += player.getFoodDeltaPopularity();
            deltaPopularity += player.getTaxDeltaPopularity();
            deltaPopularity += game.getReligion(player);
            deltaPopularity += game.getHappiness(player);
            player.setDeltaPopularity(deltaPopularity);
            int popularity = player.getPopularity();
            popularity = Math.max(0, Math.min(100, popularity + deltaPopularity));
            player.setPopularity(popularity);
            log("popularity [delta=%s, popularity=%s, player=%s]", deltaPopularity, popularity,
                    player);

            if (popularity >= 80 && game.getTotalPeasants(player) < game.getHousingSpace(player)) {
                player.setPeasants(player.getPeasants() + 1);
                log("peasant birth [popularity=%s, player=%]", popularity, player);
            }
            if (popularity <= 20 && player.getPeasants() > 0) {
                player.setPeasants(player.getPeasants() - 1);
                log("peasant death [popularity=%s, player=%s]", popularity, player);
            }
        }
    }

    private void manageNavigation(Game game) {
        Navigation navigation = new Navigation(game);
        List<Unit> unitsToMove = new ArrayList<>();
        List<IntPair> positionsToMoveTo = new ArrayList<>();
        for (int y = 0; y < game.getMap().getWidth(); y++) {
            for (int x = 0; x < game.getMap().getHeight(); x++) {
                final int finalX = x;
                final int finalY = y;
                game.getUnitsOnPosition(new IntPair(finalX, finalY))
                        .filter(u -> u.getGoal() != null)
                        .collect(groupingBy(Unit::getGoal))
                        .forEach((goal, units) -> {
                            IntPair start = new IntPair(finalX, finalY);
                            List<IntPair> path = navigation.getPath(start, goal);
                            if (path == null) {
                                log("no path for units [units=%s]", units);
                                units.forEach(Unit::unsetGoal);
                            } else {
                                for (Unit unit : units) {
                                    unitsToMove.add(unit);
                                    positionsToMoveTo.add(
                                            Navigation.getBySpeed(path, unit.getSpeed()));
                                }
                            }
                        });
            }
        }
        for (int i = 0; i < unitsToMove.size(); i++) {
            Unit unit = unitsToMove.get(i);
            IntPair position = positionsToMoveTo.get(i);
            log("move unit [position=%s, unit=%s]", position, unit);
            unit.setPosition(position);
        }
    }

    private void manageBuildingDamages(Game game, Map<Building, Integer> toDamage) {
        List<Building> destroyed = new ArrayList<>();
        for (Map.Entry<Building, Integer> entry : toDamage.entrySet()) {
            Building target = entry.getKey();
            int damage = entry.getValue();
            int hp = target.getHitPoints();
            hp = Math.max(0, hp - damage);
            target.setHitPoints(hp);
            log("damage building [damage=%s, hp=%s, target=%s]", damage, hp, target);
            if (hp == 0) {
                destroyed.add(target);
            }
        }
        for (Building building : destroyed) {
            log("destroy building [building=%s]", building);
            building.destroy(game);
        }
    }

    private void manageUnitDamages(Game game, Map<Unit, Integer> toDamage) {
        List<Unit> died = new ArrayList<>();
        for (Map.Entry<Unit, Integer> entry : toDamage.entrySet()) {
            Unit target = entry.getKey();
            int damage = entry.getValue();
            int hp = target.getHitPoints();
            hp = Math.max(0, hp - damage);
            target.setHitPoints(hp);
            log("damage unit [damage=%s, hp=%s, target=%s]", damage, hp, target);
            if (hp == 0) {
                died.add(target);
            }
        }
        for (Unit unit : died) {
            log("kill unit [unit=%s]", unit);
            unit.die(game);
            if (unit.getType().equals("Lord")) {
                terminatePlayer(game, unit.getOwner());
            }
        }
    }

    private void manageAttacks(Game game) {
        Map<Unit, Integer> unitsToDamage = new HashMap<>();
        Map<Building, Integer> buildingsToDamage = new HashMap<>();
        for (Unit unit : game.getUnits()) {
            if (unit.getDamage(game) == 0) {
                continue;
            }
            List<Object> targetCandidates = game.getTargetsAround(unit, unit.getRange());
            if (targetCandidates.isEmpty()) {
                continue;
            }
            Object target = (targetCandidates.contains(unit.getAttackGoal()) ? unit.getAttackGoal()
                    : randomChoice(targetCandidates));
            if (target instanceof Unit targetUnit) {
                addIntMap(unitsToDamage, targetUnit, unit.getDamage(game));
            } else if (target instanceof Building targetBuilding) {
                addIntMap(buildingsToDamage, targetBuilding, unit.getDamage(game));
            }
        }
        manageBuildingDamages(game, buildingsToDamage);
        manageUnitDamages(game, unitsToDamage);
    }

    private void manageVision(Game game) {
        for (Unit unit : game.getUnits()) {
            if (unit.getAttackGoal() != null) {
                continue;
            }
            int vision = unit.getEffectiveVision();
            if (vision == 0) {
                continue;
            }
            List<Unit> visibleTargets = game.getTargetsAround(unit, vision).stream()
                    .filter(obj -> obj instanceof Unit)
                    .map(obj -> (Unit) obj)
                    .toList();
            if (visibleTargets.isEmpty()) {
                continue;
            }
            Unit target = randomChoice(visibleTargets);
            log("target in vision [vision=%s, unit=%s, target=%s]", vision, unit, target);
            unit.setAttackGoal(target);
        }
    }

    public void nextFrame(Map<String, Object> req) throws OperatorException {
        Game game = getReqAs(req, "game", Game.class);
        assignLabor(game);
        manageSupplyChain(game);
        manageFood(game);
        manageTax(game);
        managePopularity(game);
        manageVision(game);
        manageNavigation(game);
        manageAttacks(game);
        manageOverpopulate(game);
    }

    private void terminatePlayer(Game game, Player player) {
        log("terminate player [player=%s]", player);
        for (Building building : game.getBuildingsByOwner(player).toList()) {
            game.getMap().getAt(building.getPosition()).setBuilding(null);
        }
        for (TradeRequest request : player.getIncomingTradeRequests()) {
            try {
                Operators.economy.deleteTrade(new HashMap<>() {{
                    put("request", request);
                }});
            } catch (OperatorException e) {
                e.printStackTrace();
            }
        }
        game.getUnits().removeIf(u -> u.getOwner().equals(player));
        game.getBuildings().removeIf(b -> b.getOwner().equals(player));
        game.getPlayers().remove(player);
        player.setAlive(false);
        // todo: add scores to player.user
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

    public void setNavigationGoal(Map<String, Object> req) throws OperatorException {
        List<Unit> units = getReqAs(req, "units", List.class);
        if (units.isEmpty()) {
            return;
        }
        Game game = getReqAs(req, "game", Game.class);
        IntPair position = getReqAs(req, "position", IntPair.class);
        checkExpression(new Navigation(game).isWalkable(position), Type.INVALID_POSITION);
        units.forEach(unit -> unit.setNavigationGoal(position));
    }

    public void setPatrol(Map<String, Object> req) throws OperatorException {
        List<Unit> units = getReqAs(req, "units", List.class);
        if (units.isEmpty()) {
            return;
        }
        Game game = getReqAs(req, "game", Game.class);
        IntPair[] path = getReqAs(req, "path", IntPair[].class);
        checkExpression(new Navigation(game).isWalkable(path), Type.INVALID_POSITION);
        units.forEach(unit -> {
            unit.setNavigationGoal(path[0]);
            unit.setPatrol(path);
        });
    }

    public Unit attackUnit(Map<String, Object> req) throws OperatorException {
        Game game = getReqAs(req, "game", Game.class);
        Player player = getReqAs(req, "player", Player.class);
        IntPair position = getReqAs(req, "position", IntPair.class);
        List<Unit> units = getReqAs(req, "units", List.class);
        checkExpression(game.getMap().getAt(position) != null, Type.INVALID_GAME_PARAMETERS);
        Unit target = game.getUnitsOnPosition(position)
                .filter(u -> !u.getOwner().equals(player))
                .findFirst()
                .orElse(null);
        checkExpression(target != null, Type.INVALID_POSITION);
        units.forEach(unit -> unit.setAttackGoal(target));
        return target;
    }

    public void repairBuilding(Map<String, Object> req) throws OperatorException {
        Game game = getReqAs(req, "game", Game.class);
        Player player = getReqAs(req, "player", Player.class);
        IntPair position = getReqAs(req, "position", IntPair.class);
        Building building = game.getBuildingsByOwner(player)
                .filter(b -> b.getPosition().equals(position))
                .findFirst()
                .orElse(null);
        checkExpression(building != null, Type.INVALID_POSITION);
        int cost = (int) ((building.getMaxHitPoints() - building.getHitPoints()) * 0.1);
        if (building.getOwner() != player && building.getHitPoints() == building.getMaxHitPoints() || !game.getTargetsAround(player,
                position, 2).isEmpty() || cost > player.getResources().get("stone")) {
            throw new OperatorException(Type.CANT_REPAIR);
        }
        addIntMap(player.getResources(), "stone", -cost);
        building.setHitPoints(building.getMaxHitPoints());
    }

    public void disbandUnits(Map<String, Object> req) throws OperatorException {
        List<Unit> units = getReqAs(req, "units", List.class);
        Game game = getReqAs(req, "game", Game.class);
        if (units.isEmpty()) {
            return;
        }
        units.forEach(unit -> {
            unit.getOwner().setPeasants(unit.getOwner().getPeasants() + 1);
            unit.die(game);
        });
    }

    public void setUnitMode(Map<String, Object> req) throws OperatorException {
        List<Unit> units = getReqAs(req, "units", List.class);
        String mode = getReqString(req, "mode");
        checkExpression(List.of("standing", "defensive", "offensive").contains(mode),
                Type.INVALID_GAME_PARAMETERS);
        units.forEach(unit -> unit.setMode(mode));
    }

    public void dropUnit(Map<String, Object> req) throws OperatorException {
        Game game = getReqAs(req, "game", Game.class);
        Player player = getReqAs(req, "player", Player.class);
        IntPair position = getReqAs(req, "position", IntPair.class);
        String unitType = getReqString(req, "type");
        UnitTemplate unitTemplate = checkNotNull(templateDatabase.getUnitTemplates().get(unitType),
                Type.UNIT_NOT_FOUND);
        Tile tile = checkNotNull(game.getMap().getAt(position), Type.INVALID_POSITION);
        Building building = checkNotNull(tile.getBuilding(), Type.INVALID_POSITION);
        Map<String, Integer> cost = checkNotNull(building.getDropUnit().get(unitType),
                Type.UNIT_NOT_FOUND);
        checkExpression(geqIntMap(player.getResources(), cost), Type.NOT_ENOUGH_RESOURCE);
        subtractIntMap(player.getResources(), cost);
        Unit unit = unitTemplate.getBuilder()
                .owner(player)
                .build();
        unit.setPosition(position);
        game.getUnits().add(unit);
    }

    public Unit buildEquipment(Map<String, Object> req) throws OperatorException {
        Game game = getReqAs(req, "game", Game.class);
        Player player = getReqAs(req, "player", Player.class);
        IntPair position = getReqAs(req, "position", IntPair.class);
        String unitType = getReqString(req, "type");
        UnitTemplate unitTemplate = checkNotNull(templateDatabase.getUnitTemplates().get(unitType),
                Type.UNIT_NOT_FOUND);
        checkExpression(unitTemplate.getEngineers() > 0, Type.UNIT_NOT_FOUND);
        List<Unit> engineers = game.getUnitsOnPosition(position)
                .filter(u -> u.getType().equals("Engineer"))
                .limit(unitTemplate.getEngineers())
                .toList();
        checkExpression(engineers.size() == unitTemplate.getEngineers(), Type.NOT_ENOUGH_ENGINEER);
        engineers.forEach(u -> u.die(game));
        Unit unit = unitTemplate.getBuilder()
                .owner(player)
                .build();
        unit.setPosition(position);
        game.getUnits().add(unit);
        return unit;
    }
}
