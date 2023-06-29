package org.example.stronghold.gui.components;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

public class PopupWindow extends Window {
    public final Label label;
    private boolean visible = false;
    private long lastPopped = 0;

    public PopupWindow(Skin windowSkin, Skin labelSkin, int width) {
        super("", windowSkin);
        setMovable(false);
        setResizable(false);
        setPosition(20, 20);
        setVisible(false);
        label = new Label("", labelSkin);
        label.setAlignment(Align.center);
        add(label).minWidth(width).row();
        pack();
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ignore();
                return false;
            }
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        this.visible = visible;
    }

    public void pop(String message) {
        label.setText(message);
        pack();
        setVisible(true);
        lastPopped = TimeUtils.nanoTime();
    }

    public void error(String message) {
        label.setColor(Color.RED);
        pop(message);
    }

    public void success(String message) {
        label.setColor(Color.GREEN);
        pop(message);
    }

    public void info(String message) {
        label.setColor(Color.WHITE);
        pop(message);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (visible && TimeUtils.nanoTime() - lastPopped >= 5L*1000*1000*1000) {
            setVisible(false);
        }
    }

    private void ignore() {
        setVisible(false);
    }
}
