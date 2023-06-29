package org.example.stronghold.gui.sections;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import java.util.HashMap;
import org.example.stronghold.context.HashMode;
import org.example.stronghold.context.HashedString;
import org.example.stronghold.gui.FormScreen;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.StrongholdGame;
import org.example.stronghold.model.User;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class ProfileScreen extends FormScreen {
    Label usernameLabel, nicknameLabel, sloganLabel, emailLabel;

    TextField usernameField, passwordField, nicknameField, sloganField, emailField, currentPasswordField;
    TextButton applyButton, logoutButton;
    private final User user;

    public ProfileScreen(StrongholdGame game, User user) {
        super(game);
        this.user = user;
    }

    @Override
    public void formShow() {
        usernameLabel = new Label("Username:", game.skin);
        usernameField = new TextField(user.getUsername(), game.skin);
        passwordField = new TextField("", game.skin);
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        currentPasswordField = new TextField("", game.skin);
        currentPasswordField.setPasswordCharacter('*');
        currentPasswordField.setPasswordMode(true);
        nicknameField = new TextField(user.getNickname(), game.skin);
        sloganField = new TextField(user.getSlogan(), game.skin);
        emailField = new TextField(user.getEmail(), game.skin);
        nicknameLabel = new Label("Nickname:", game.skin);
        sloganLabel = new Label("Slogan:", game.skin);
        emailLabel = new Label("Email:", game.skin);
        applyButton = new TextButton("Apply", game.skin);
        logoutButton = new TextButton("Logout", game.skin);

        table.pad(50);
        table.defaults().spaceBottom(10).spaceRight(10);
        table.add(usernameLabel).align(Align.left);
        table.add(usernameField).growX().minWidth(400).row();
        table.add(nicknameLabel).align(Align.left);
        table.add(nicknameField).growX().row();
        table.add(sloganLabel).align(Align.left);
        table.add(sloganField).growX().row();
        table.add(emailLabel).align(Align.left);
        table.add(emailField).growX().row();
        table.add(currentPasswordField).align(Align.right).minWidth(400);
        table.add(passwordField).minWidth(400).growX().row();
        table.add(logoutButton).align(Align.center).minWidth(400);
        table.add(applyButton).align(Align.center).minWidth(400).row();

        popup.label.setColor(Color.RED);
        logoutButton.addListener(new SimpleChangeListener(() -> game.setScreen(new LoginScreen(game))));
        applyButton.addListener(new SimpleChangeListener(this::apply));
    }

    private void apply() {
        try {
            HashMap<String, Object> req = new HashMap<>() {{
                put("user", user);
                put("old-password", HashedString.fromPlain(currentPasswordField.getText()).withMode(HashMode.SHA256));
                if(passwordField.getText().isEmpty()) {
                    put("new-password", HashedString.fromPlain(currentPasswordField.getText()).withMode(HashMode.SHA256));
                } else {
                    put("new-password", HashedString.fromPlain(passwordField.getText()).withMode(HashMode.SHA256));
                }
                put("new-username", usernameField.getText());
                put("new-email", emailField.getText());
                put("new-nickname", nicknameField.getText());
                put("new-slogan", sloganField.getText());
            }};
            Operators.profile.changePassword(req);
            Operators.profile.changeUsername(req);
            Operators.profile.changeEmail(req);
            Operators.profile.changeNickname(req);
            Operators.profile.changeSlogan(req);
        } catch (OperatorException e) {
            popup.pop(e.getMessage());
        }
    }

}
