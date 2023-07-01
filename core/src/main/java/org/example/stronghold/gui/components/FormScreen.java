package org.example.stronghold.gui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.stronghold.gui.StrongholdGame;

public abstract class FormScreen implements Screen {

    private final static boolean DEBUG = true;
    final protected StrongholdGame game;
    protected Stage stage;
    protected PopupWindow popup;
    protected Table table;
    Texture dirt;
    Table titleTable;

    public FormScreen(StrongholdGame game) {
        this.game = game;
    }

    protected void log(String format, Object... objects) {
        Gdx.app.log(getClass().getSimpleName(), String.format(format, objects));
    }

    @Override
    public final void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(DEBUG);

        dirt = game.assetLoader.getTexture("craftacular/dirt.png");
        dirt.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        titleTable = new Table();
        table = new Table(game.skin);
        stage.addActor(titleTable);
        titleTable.setFillParent(true);
        titleTable.add(new Label("STRONGHOLD", game.craftacularSkin, "title")).expandX().row();
        titleTable.add(table).row();

        popup = new PopupWindow(game.craftacularSkin, game.skin, 300);
        stage.addActor(popup);

        formShow();

        table.pack();
        titleTable.pack();
    }

    public abstract void formShow();

    private void drawBackground() {
        Batch batch = stage.getBatch();
        batch.begin();
        batch.draw(dirt, 0, 0, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public void render(float delta) {
        drawBackground();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
