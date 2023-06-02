package org.example.stronghold.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.stronghold.cli.sections.AuthMenu;
import org.example.stronghold.context.HashMode;
import org.example.stronghold.context.HashedString;
import org.example.stronghold.model.User;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

import java.util.HashMap;

public class LoginScreen implements Screen {
    final StrongholdGame game;
    Stage stage;
    Texture dirt;
    Table titleTable, formTable;
    Label usernameLabel, passwordLabel;
    TextField usernameField, passwordField;
    TextButton loginButton, registerButton, forgotPasswordButton;
    CheckBox stayLoggedIn;
    PopupWindow popUp;

    LoginScreen(StrongholdGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        dirt = new Texture(Gdx.files.internal("craftacular/dirt.png"));
        dirt.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        titleTable = new Table();
        formTable = new Table();
        stage.addActor(titleTable);

        titleTable.setFillParent(true);
        titleTable.add(new Label("STRONGHOLD", game.craftacularSkin, "title")).expandX().row();
        titleTable.add(formTable).row();

        usernameLabel = new Label("Username", game.skin);
        usernameField = new TextField("", game.skin);
        passwordLabel = new Label("Password", game.skin);
        passwordField = new TextField("", game.skin);
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        loginButton = new TextButton("Login", game.skin);
        registerButton = new TextButton("Register", game.skin);
        stayLoggedIn = new CheckBox("Stay logged in", game.skin);

        formTable.pad(50);
        formTable.defaults().spaceBottom(10).spaceRight(10);
        formTable.add(usernameLabel).align(Align.right);
        formTable.add(usernameField).growX().minWidth(400).row();
        formTable.add(passwordLabel).align(Align.right);
        formTable.add(passwordField).growX().row();
        formTable.add(stayLoggedIn).align(Align.right).colspan(2).row();
        formTable.add(loginButton).align(Align.center).colspan(2).minWidth(200).row();
        formTable.add(registerButton).align(Align.center).colspan(2).minWidth(200).row();
        formTable.add(forgotPasswordButton).align(Align.center).colspan(2).minWidth(200).row();

        formTable.pack();
        titleTable.pack();

        popUp = new PopupWindow(game.craftacularSkin, game.skin, 300);
        popUp.label.setColor(Color.RED);
        stage.addActor(popUp);

        loginButton.addListener(new SimpleChangeListener(this::login));
    }

    private void drawBackground() {
        Batch batch = stage.getBatch();
        batch.begin();
        batch.draw(dirt, 0, 0, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    private void login() {
        String password = passwordField.getText();
        try {
            User user = Operators.auth.login(new HashMap<>() {{
                put("username", usernameField.getText());
                put("password", HashedString.fromPlain(password).withMode(HashMode.SHA256));
                put("stay-logged-in", stayLoggedIn.isChecked());
            }});
            Gdx.app.log("LoginScreen", "Logged in as " + user.getUsername());
            // todo: switch to profile screen
        } catch (OperatorException e) {
            popUp.pop(e.getMessage());
        }
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
