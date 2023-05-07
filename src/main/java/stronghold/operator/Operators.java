package stronghold.operator;

import stronghold.model.Database;
import stronghold.model.template.TemplateDatabase;
import stronghold.operator.sections.AuthOperator;
import stronghold.operator.sections.BuildingOperator;
import stronghold.operator.sections.EconomyOperator;
import stronghold.operator.sections.MapEditorOperator;
import stronghold.operator.sections.PlayerOperator;
import stronghold.operator.sections.ProfileOperator;
import stronghold.operator.sections.UnitOperator;

/* singleton interface for operators */
public class Operators {

    public static AuthOperator auth;
    public static BuildingOperator building;
    public static EconomyOperator economy;
    public static MapEditorOperator mapEditor;
    public static PlayerOperator player;
    public static ProfileOperator profile;
    public static UnitOperator unit;

    public static void initDatabase(Database database, TemplateDatabase templateDatabase) {
        auth = new AuthOperator(database);
        building = new BuildingOperator(database);
        economy = new EconomyOperator(database);
        mapEditor = new MapEditorOperator(database, templateDatabase);
        player = new PlayerOperator(database);
        profile = new ProfileOperator(database);
        unit = new UnitOperator(database);
    }
}
