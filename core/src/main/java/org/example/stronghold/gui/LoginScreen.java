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

public class LoginScreen implements Screen {
    final StrongholdGame game;
    Stage stage;
    Texture dirt;
    Table titleTable, formTable;
    Label usernameLabel, passwordLabel, errorLabel;
    TextField usernameField, passwordField;
    TextButton loginButton, registerButton, forgotPasswordButton;
    CheckBox randomPassword;

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
        randomPassword = new CheckBox("Random Password", game.skin);
        errorLabel = new Label("", game.skin); // todo: pop-up error
        errorLabel.setAlignment(Align.center);
        errorLabel.setColor(Color.RED);

        formTable.pad(50);
        formTable.defaults().spaceBottom(10).spaceRight(10);
        formTable.add(usernameLabel).align(Align.right);
        formTable.add(usernameField).growX().minWidth(400).row();
        formTable.add(passwordLabel).align(Align.right);
        formTable.add(passwordField).growX().row();
        formTable.add(randomPassword).align(Align.center).colspan(2).spaceBottom(20).row();
        formTable.add(loginButton).align(Align.center).colspan(2).minWidth(200).row();
        formTable.add(registerButton).align(Align.center).colspan(2).minWidth(200).row();
        formTable.add(forgotPasswordButton).align(Align.center).colspan(2).minWidth(200).row();
        formTable.add(errorLabel).colspan(2).growX().row();

        formTable.pack();
        titleTable.pack();

        randomPassword.addListener(new SimpleChangeListener(this::updateRandomPassword));
        passwordField.addListener(new SimpleChangeListener(this::updatePasswordStrength));
        loginButton.addListener(new SimpleChangeListener(this::login));
    }

    private void drawBackground() {
        Batch batch = stage.getBatch();
        batch.begin();
        batch.draw(dirt, 0, 0, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    private void updateRandomPassword() {
        if (randomPassword.isChecked()) {
            String password = AuthMenu.generatePassword();
            passwordField.setText(password);
            passwordField.setDisabled(true);
            passwordField.setPasswordMode(false);
        } else {
            passwordField.setText("");
            passwordField.setDisabled(false);
            passwordField.setPasswordMode(true);
        }
    }

    private void updatePasswordStrength() {
        String password = passwordField.getText();
        Color color;
        if (AuthMenu.isPasswordWeak(password)) {
            color = Color.RED;
        } else {
            color = Color.GREEN;
        }
        passwordField.setColor(color);
    }

    private void login() {
        errorLabel.setText("");
        String password = passwordField.getText();
        if (AuthMenu.isPasswordWeak(password)) {
            errorLabel.setText("Password is too weak");
            return;
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
