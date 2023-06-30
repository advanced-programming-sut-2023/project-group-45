package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import java.util.HashMap;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class OptionPanel extends Panel {

    TextField count;
    TextButton nextFrame;

    public OptionPanel(ControlPanel controlPanel) {
        super(controlPanel);
        create();
    }

    private void create() {
        count = new TextField("1", game.skin);
        nextFrame = new TextButton("Next frame", game.skin);
        nextFrame.addListener(new SimpleChangeListener(this::runNextFrame));
        add(count).width(200);
        add(nextFrame).row();
    }

    private void runNextFrame() {
        try {
            int count = Integer.parseInt(this.count.getText());
            for (int i = 0; i < count; i++) {
                Operators.game.nextFrame(new HashMap<>() {{
                    put("game", screen.gameData);
                }});
            }
        } catch (NumberFormatException e) {
            controlPanel.popup.error("Invalid number");
        } catch (OperatorException e) {
            controlPanel.popup.error(e.getMessage());
        }
    }

}
