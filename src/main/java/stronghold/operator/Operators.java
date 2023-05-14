package stronghold.operator;

import stronghold.model.Database;
import stronghold.model.template.TemplateDatabase;
import stronghold.operator.sections.AuthOperator;
import stronghold.operator.sections.EconomyOperator;
import stronghold.operator.sections.GameOperator;
import stronghold.operator.sections.MapEditorOperator;
import stronghold.operator.sections.ProfileOperator;

/* singleton interface for operators */
public class Operators {

    public static AuthOperator auth;
    public static EconomyOperator economy;
    public static MapEditorOperator mapEditor;
    public static ProfileOperator profile;
    public static GameOperator game;

    public static void initDatabase(Database database, TemplateDatabase templateDatabase) {
        auth = new AuthOperator(database);
        economy = new EconomyOperator(database);
        mapEditor = new MapEditorOperator(database, templateDatabase);
        profile = new ProfileOperator(database);
        game = new GameOperator(database, templateDatabase);
    }
}
