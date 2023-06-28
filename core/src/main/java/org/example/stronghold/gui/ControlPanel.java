package org.example.stronghold.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Data;

@Data
public class ControlPanel implements Disposable {
    private static final boolean DEBUG = false;
    final int height;
    final StrongholdGame game;
    Viewport viewport;
    Stage stage;
    Table layoutTable, selectTable;
    ScrollPane mainPane;
    Texture dirt;
    public PopupWindow popup;

    public void create() {
        viewport = new FitViewport(Gdx.graphics.getWidth(), height);
        stage = new Stage(viewport);

        dirt = game.assetLoader.getTexture("craftacular/dirt.png");
        dirt.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        layoutTable = new Table();
        layoutTable.setFillParent(true);
        stage.addActor(layoutTable);

        selectTable = new Table();
        layoutTable.add(selectTable).width(300);

        mainPane = new ScrollPane(null);
        layoutTable.add(mainPane).growX().row();

        popup = new PopupWindow(game.craftacularSkin, game.skin, 300);
        stage.addActor(popup);

        selectTable.add(new TextButton("Buildings", game.skin));
        selectTable.add(new TextButton("Popularity", game.skin));
        selectTable.add(new TextButton("Tax", game.skin));

        stage.setDebugAll(DEBUG);
    }

    public void resize(int scrWidth, int scrHeight) {
        viewport.update(scrWidth, height);
    }

    public void render() {
        viewport.apply(true);
        Batch batch = stage.getBatch();
        batch.begin();
        batch.draw(dirt, 0, 0, 0, 0, Gdx.graphics.getWidth(), height);
        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
