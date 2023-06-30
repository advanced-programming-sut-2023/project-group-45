package org.example.stronghold.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SimpleChangeListener extends ChangeListener {

    private final Runnable runnable;

    public SimpleChangeListener(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        runnable.run();
    }
}
