package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import java.util.HashMap;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.gui.sections.LoginScreen;
import org.example.stronghold.model.GameData;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class OptionPanel extends Panel {

    Table playTable;
    TextField count;
    TextButton nextFrame, resumeButton, stopButton, quitButton;

    public OptionPanel(ControlPanel controlPanel) {
        super(controlPanel);
        create();
    }

    private void create() {
        count = new TextField("1", game.skin);
        nextFrame = new TextButton("Next frame", game.skin);
        nextFrame.addListener(new SimpleChangeListener(this::runNextFrame));
        add("Game ID: " + screen.gameData.getId()).colspan(2).row();
        add(count).width(200);
        add(nextFrame).row();

        playTable = new Table(game.skin);
        resumeButton = new TextButton("Resume", game.skin);
        stopButton = new TextButton("Stop", game.skin);
        quitButton = new TextButton("Quit", game.skin);
        resumeButton.addListener(new SimpleChangeListener(() -> screen.running = true));
        stopButton.addListener(new SimpleChangeListener(() -> screen.running = false));
        quitButton.addListener(new SimpleChangeListener(this::quitTheGame));
        playTable.add(resumeButton, stopButton, quitButton);
        add(playTable).colspan(2).row();
    }

    private void runNextFrame() {
        try {
            int count = Integer.parseInt(this.count.getText());
            screen.updateGameData(count);
        } catch (NumberFormatException e) {
            controlPanel.popup.error("Invalid number");
        }
    }

    private void quitTheGame() {
        screen.running = false;
        screen.stopped = true;
        try {
            screen.updater.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        game.setScreen(new LoginScreen(game));
    }

}
