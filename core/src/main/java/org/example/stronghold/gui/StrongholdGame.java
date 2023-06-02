package org.example.stronghold.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class StrongholdGame extends Game {
    public Skin skin;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));
        setScreen(new AuthScreen(this));
    }

    @Override
    public void dispose() {
        skin.dispose();
    }
}
