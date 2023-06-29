package org.example.stronghold.gui.components;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.example.stronghold.gui.StrongholdGame;
import org.example.stronghold.gui.sections.TestMapScreen;

public abstract class Panel extends Table {

    protected StrongholdGame game;
    protected TestMapScreen screen;
    protected ControlPanel controlPanel;

    public Panel(ControlPanel controlPanel) {
        super(controlPanel.game.skin);
        this.controlPanel = controlPanel;
        this.screen = controlPanel.getScreen();
        this.game = controlPanel.getGame();
    }
}
