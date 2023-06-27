package org.example.stronghold.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.example.stronghold.gui.sections.LoginScreen;
import org.example.stronghold.gui.sections.TestMapScreen;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.template.TemplateDatabase;
import org.example.stronghold.operator.Operators;

import java.io.File;
import java.io.IOException;

public class StrongholdGame extends Game {
    public Skin skin, craftacularSkin;
    private File databaseFile;
    private File templateDatabaseRoot;
    private Database database;
    private TemplateDatabase templateDatabase;
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
        setScreen(new TestMapScreen(this, templateDatabase.getGameMapTemplates().get("test").build()));
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
