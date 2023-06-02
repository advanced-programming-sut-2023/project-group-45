package org.example.stronghold.gui;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class StrongholdGame extends Game {
    @Override
    public void create() {
        setScreen(new FirstScreen());
    }
}
