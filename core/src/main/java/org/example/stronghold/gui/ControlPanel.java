package org.example.stronghold.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Data;

@Data
public class ControlPanel implements Disposable {
    final int height;
    final StrongholdGame game;
    Viewport viewport;
    Stage stage;
    Table layoutTable, selectTable;
    ScrollPane mainPane;

    public void create() {
        viewport = new FitViewport(Gdx.graphics.getWidth(), height);
        stage = new Stage(viewport);

        layoutTable = new Table();
        layoutTable.setFillParent(true);
        layoutTable.setDebug(true);
        stage.addActor(layoutTable);

        selectTable = new Table();
        selectTable.setDebug(true);
        layoutTable.add(selectTable).width(300);

        mainPane = new ScrollPane(null);
        layoutTable.add(mainPane).growX().row();

        selectTable.add(new TextButton("Test", game.skin));
    }

    public void resize(int width) {
        viewport.update(width, height);
    }

    public void render() {
        viewport.apply(true);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
