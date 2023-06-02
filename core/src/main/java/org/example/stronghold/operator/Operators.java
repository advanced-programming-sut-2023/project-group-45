package org.example.stronghold.operator;

import org.example.stronghold.model.Database;
import org.example.stronghold.model.template.TemplateDatabase;
import org.example.stronghold.operator.sections.AuthOperator;
import org.example.stronghold.operator.sections.EconomyOperator;
import org.example.stronghold.operator.sections.GameOperator;
import org.example.stronghold.operator.sections.MapEditorOperator;
import org.example.stronghold.operator.sections.ProfileOperator;

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
