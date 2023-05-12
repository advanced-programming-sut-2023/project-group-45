package stronghold.model;

import static stronghold.context.MapUtils.addIntMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
import stronghold.context.IntPair;
import stronghold.model.template.BuildingTemplate;
import stronghold.model.template.GameMapTemplate;
import stronghold.model.template.UnitTemplate;

@Data
public class Game implements Serializable {

    private final List<Player> players = new ArrayList<>();
    private final GameMapTemplate mapTemplate;
    private final GameMap map;
    private final List<Building> buildings = new ArrayList<>();
    private final List<Unit> units = new ArrayList<>();
    private Market market = new Market();

    public Game(List<User> users, GameMapTemplate gameMapTemplate, UnitTemplate lordTemplate,
            BuildingTemplate baseTemplate) {
        this.mapTemplate = gameMapTemplate;
        this.map = gameMapTemplate.build();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            IntPair position = gameMapTemplate.getBases().get(i);
            Player player = new Player(user);
            Building base = baseTemplate.getBuilder()
                    .owner(player)
                    .position(position)
                    .build();
            Unit lord = lordTemplate.getBuilder()
                    .owner(player)
                    .build();
            lord.setPosition(position);
            players.add(player);
            buildings.add(base);
            units.add(lord);
            this.map.getAt(position).setBuilding(base);
            addIntMap(player.getResources(), gameMapTemplate.getInitialResources());
            player.setPeasants(gameMapTemplate.getInitialPopulation());
        }
    }

    public Stream<Unit> getUnitsOnPosition(IntPair position) {
        return units.stream()
                .filter(unit -> unit.getPosition().equals(position));
    }

    public Stream<Building> getBuildingsByOwner(Player player) {
        return buildings.stream()
                .filter(building -> building.getOwner().equals(player));
    }

    public int getReligion(Player player) {
        return getBuildingsByOwner(player)
                .mapToInt(Building::getReligionFactor)
                .sum();
    }

    public int getHappiness(Player player) {
        return getBuildingsByOwner(player)
                .mapToInt(Building::getHappinessFactor)
                .sum();
    }
}
