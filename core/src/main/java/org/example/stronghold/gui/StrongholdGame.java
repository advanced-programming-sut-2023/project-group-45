package org.example.stronghold.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class StrongholdGame extends Game {
    public Skin skin, craftacularSkin;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("default/uiskin.json"));
        craftacularSkin = new Skin(Gdx.files.internal("craftacular/craftacular-ui.json"));
        setScreen(new LoginScreen(this));
    }

    @Override
    public void dispose() {
        skin.dispose();
        craftacularSkin.dispose();
    }
}
