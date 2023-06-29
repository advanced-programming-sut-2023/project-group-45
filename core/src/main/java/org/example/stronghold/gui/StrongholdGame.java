package org.example.stronghold.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.example.stronghold.gui.sections.TestMapScreen;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.User;
import org.example.stronghold.model.template.BuildingTemplate;
import org.example.stronghold.model.template.GameMapTemplate;
import org.example.stronghold.model.template.TemplateDatabase;
import org.example.stronghold.model.template.UnitTemplate;
import org.example.stronghold.operator.Operators;

public class StrongholdGame extends Game {

    public Skin skin, craftacularSkin;
    private File databaseFile;
    private File templateDatabaseRoot;
    private Database database;
    public TemplateDatabase templateDatabase;
    public final AssetLoader assetLoader = new AssetLoader();

    @Override
    public void create() {
        databaseFile = Gdx.files.internal("data/database.ser").file();
        templateDatabaseRoot = Gdx.files.internal("data").file();
        database = Database.fromFileOrDefault(databaseFile);
        templateDatabase = new TemplateDatabase();
        try {
            templateDatabase.updateFromPath(templateDatabaseRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Operators.initDatabase(database, templateDatabase);

        skin = new Skin(Gdx.files.internal("default/uiskin.json"));
        craftacularSkin = new Skin(Gdx.files.internal("craftacular/craftacular-ui.json"));
        assetLoader.loadAll();
//        setScreen(new LoginScreen(this));
        List<User> users = database.getUsers().stream().limit(2).toList();
        GameMapTemplate gameMapTemplate = templateDatabase.getGameMapTemplates().get("test");
        UnitTemplate lordTemplate = templateDatabase.getUnitTemplates().get("Lord");
        BuildingTemplate buildingTemplate = templateDatabase.getBuildingTemplates().get("Base");
        GameData gameData = new GameData(users, gameMapTemplate, lordTemplate, buildingTemplate);
        setScreen(new TestMapScreen(this, gameData));
    }

    @Override
    public void dispose() {
        skin.dispose();
        craftacularSkin.dispose();
        assetLoader.dispose();

        try {
            database.toFile(databaseFile);
            templateDatabase.saveToPath(templateDatabaseRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
