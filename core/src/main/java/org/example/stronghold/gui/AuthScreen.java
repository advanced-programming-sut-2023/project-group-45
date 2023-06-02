package org.example.stronghold.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class AuthScreen implements Screen {
    final StrongholdGame game;
    Stage stage;
    Texture dirt;
    Table titleTable, formTable;
    Label usernameLabel, passwordLabel;
    TextField usernameField, passwordField;
    TextButton loginButton, registerButton, forgotPasswordButton;

    AuthScreen(StrongholdGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        titleTable = new Table();
        formTable = new Table();
        stage.addActor(titleTable);

        titleTable.setFillParent(true);
        titleTable.add(new Label("STRONGHOLD", game.skin, "title")).expandX().row();
        titleTable.add(formTable).row();

        usernameLabel = new Label("Username", game.skin);
        usernameField = new TextField("", game.skin);
        passwordLabel = new Label("Password", game.skin);
        passwordField = new TextField("", game.skin);
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        loginButton = new TextButton("Login", game.skin);
        registerButton = new TextButton("Register", game.skin);

        formTable.pad(50);
        formTable.defaults().spaceBottom(10).spaceRight(10);
        formTable.add(usernameLabel).align(Align.right);
        formTable.add(usernameField).align(Align.left).growX().minWidth(400).row();
        formTable.add(passwordLabel).align(Align.right);
        formTable.add(passwordField).align(Align.left).growX().spaceBottom(20).row();
        formTable.add(loginButton).align(Align.center).colspan(2).row();
        formTable.add(registerButton).align(Align.center).colspan(2).row();
        formTable.add(forgotPasswordButton).align(Align.center).colspan(2).row();

        dirt = new Texture(Gdx.files.internal("dirt.png"));
        dirt.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        formTable.pack();
        titleTable.pack();
    }

    private void drawBackground() {
        stage.getBatch().begin();
        stage.getBatch().draw(dirt, 0, 0, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

    }

    @Override
    public void render(float delta) {
        drawBackground();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
