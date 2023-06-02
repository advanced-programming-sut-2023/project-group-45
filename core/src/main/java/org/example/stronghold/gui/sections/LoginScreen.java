package org.example.stronghold.gui.sections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import org.example.stronghold.context.HashMode;
import org.example.stronghold.context.HashedString;
import org.example.stronghold.gui.FormScreen;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.StrongholdGame;
import org.example.stronghold.model.User;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

import java.util.HashMap;

public class LoginScreen extends FormScreen {
    Label usernameLabel, passwordLabel;
    TextField usernameField, passwordField;
    TextButton loginButton, registerButton, forgotPasswordButton;
    CheckBox stayLoggedIn;

    public LoginScreen(StrongholdGame game) {
        super(game);
    }

    @Override
    public void formShow() {
        usernameLabel = new Label("Username", game.skin);
        usernameField = new TextField("", game.skin);
        passwordLabel = new Label("Password", game.skin);
        passwordField = new TextField("", game.skin);
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        loginButton = new TextButton("Login", game.skin);
        registerButton = new TextButton("Register", game.skin);
        forgotPasswordButton = new TextButton("Forgot password", game.skin);
        stayLoggedIn = new CheckBox("Stay logged in", game.skin);
        stayLoggedIn.setChecked(true);

        table.pad(50);
        table.defaults().spaceBottom(10).spaceRight(10);
        table.add(usernameLabel).align(Align.right);
        table.add(usernameField).growX().minWidth(400).row();
        table.add(passwordLabel).align(Align.right);
        table.add(passwordField).growX().row();
        table.add(stayLoggedIn).align(Align.right).colspan(2).row();
        table.add(loginButton).align(Align.center).colspan(2).minWidth(200).row();
        table.add(registerButton).align(Align.center).colspan(2).minWidth(200).row();
        table.add(forgotPasswordButton).align(Align.center).colspan(2).minWidth(200).row();

        popup.label.setColor(Color.RED);

        loginButton.addListener(new SimpleChangeListener(this::login));
        registerButton.addListener(new SimpleChangeListener(() -> game.setScreen(new RegisterScreen(game))));
    }

    private void login() {
        String password = passwordField.getText();
        try {
            User user = Operators.auth.login(new HashMap<>() {{
                put("username", usernameField.getText());
                put("password", HashedString.fromPlain(password).withMode(HashMode.SHA256));
                put("stay-logged-in", stayLoggedIn.isChecked());
            }});
            log("Logged in as %s", user);
            // todo: switch to profile screen
        } catch (OperatorException e) {
            popup.pop(e.getMessage());
        }
    }
}
