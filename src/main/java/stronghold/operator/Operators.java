package stronghold.operator;

import stronghold.model.Database;

/* singleton interface for operators */
public class Operators {

    public static AuthOperator auth;
    public static BuildingOperator building;
    public static EconomyOperator economy;
    public static GameMapOperator gameMap;
    public static PlayerOperator player;
    public static ProfileOperator profile;
    public static UnitOperator unit;

    public static void initDatabase(Database database) {
        auth = new AuthOperator(database);
        building = new BuildingOperator(database);
        economy = new EconomyOperator(database);
        gameMap = new GameMapOperator(database);
        player = new PlayerOperator(database);
        profile = new ProfileOperator(database);
        unit = new UnitOperator(database);
    }
}
