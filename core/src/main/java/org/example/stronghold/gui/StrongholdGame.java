package org.example.stronghold.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.example.stronghold.gui.sections.LoginScreen;
import org.example.stronghold.gui.sections.MapScreen;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.User;
import org.example.stronghold.model.template.GameMapTemplate;
import org.example.stronghold.model.template.TemplateDatabase;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class StrongholdGame extends Game {

    public final AssetLoader assetLoader = new AssetLoader();
    public Skin skin, craftacularSkin;
    public TemplateDatabase templateDatabase;
    private File databaseFile;
    private File templateDatabaseRoot;
    private Database database;

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
        setLoginScreen();
    }

    private void setLoginScreen() {
        setScreen(new LoginScreen(this));
    }

    private void setMapScreen() {
        try {
            List<User> users = database.getUsers().stream().limit(2).toList();
            GameMapTemplate gameMapTemplate = templateDatabase.getGameMapTemplates().get("test");
            GameData gameData = Operators.game.startGame(new HashMap<>() {{
                put("map", gameMapTemplate);
                put("users", users);
            }});
            setScreen(new MapScreen(this, gameData));
        } catch (OperatorException e) {
            throw new RuntimeException(e);
        }
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
