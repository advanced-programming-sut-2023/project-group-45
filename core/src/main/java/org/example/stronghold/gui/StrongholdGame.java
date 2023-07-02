package org.example.stronghold.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.example.stronghold.client.Connection;
import org.example.stronghold.gui.sections.LoginScreen;
import org.example.stronghold.gui.sections.MapScreen;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.User;
import org.example.stronghold.model.template.GameMapTemplate;
import org.example.stronghold.model.template.TemplateDatabase;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;
import org.example.stronghold.server.Server;

public class StrongholdGame extends Game {

    public final AssetLoader assetLoader = new AssetLoader();
    public Skin skin, craftacularSkin;
    public TemplateDatabase templateDatabase;
    public Connection conn;
    private File templateDatabaseRoot;

    @Override
    public void create() {
        templateDatabaseRoot = Gdx.files.internal("data").file();
        templateDatabase = new TemplateDatabase();
        try {
            templateDatabase.updateFromPath(templateDatabaseRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        skin = new Skin(Gdx.files.internal("default/uiskin.json"));
        craftacularSkin = new Skin(Gdx.files.internal("craftacular/craftacular-ui.json"));
        assetLoader.loadAll();
        conn = new Connection("localhost", Server.PORT);
        setLoginScreen();
    }

    private void setLoginScreen() {
        setScreen(new LoginScreen(this));
    }

    @Override
    public void dispose() {
        skin.dispose();
        craftacularSkin.dispose();
        assetLoader.dispose();

        try {
            templateDatabase.saveToPath(templateDatabaseRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
