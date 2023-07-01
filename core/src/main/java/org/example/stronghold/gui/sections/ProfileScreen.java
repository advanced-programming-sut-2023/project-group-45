package org.example.stronghold.gui.sections;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import org.example.stronghold.cli.sections.AuthMenu;
import org.example.stronghold.cli.sections.ProfileMenu;
import org.example.stronghold.context.HashMode;
import org.example.stronghold.context.HashedString;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.StrongholdGame;
import org.example.stronghold.gui.components.FormScreen;
import org.example.stronghold.model.User;
import org.example.stronghold.operator.Operators;

public class ProfileScreen extends FormScreen {

    private final User user;
    Label usernameLabel, nicknameLabel, sloganLabel, emailLabel;
    TextField usernameField, passwordField, nicknameField, sloganField, emailField, currentPasswordField, avatarPath;
    TextButton applyButton, logoutButton, startGameButton;
    Table profileTable, scoreboardTable;
    ScrollPane scrollPane;
    Image avatarImage;
    CheckBox randomSlogan;

    public ProfileScreen(StrongholdGame game, User user) {
        super(game);
        this.user = user;
    }

    @Override
    public void formShow() {
        profileTable = new Table();
        avatarImage = new Image(new Texture(user.getAvatar()));
        usernameLabel = new Label("Username:", game.skin);
        usernameField = new TextField(user.getUsername(), game.skin);
        passwordField = new TextField("", game.skin);
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        avatarPath = new TextField("", game.skin);
        currentPasswordField = new TextField("", game.skin);
        currentPasswordField.setPasswordCharacter('*');
        currentPasswordField.setPasswordMode(true);
        nicknameField = new TextField(user.getNickname(), game.skin);
        sloganField = new TextField(user.getSlogan(), game.skin);
        randomSlogan = new CheckBox("Random famous slogan", game.skin);
        emailField = new TextField(user.getEmail(), game.skin);
        nicknameLabel = new Label("Nickname:", game.skin);
        sloganLabel = new Label("Slogan:", game.skin);
        emailLabel = new Label("Email:", game.skin);
        applyButton = new TextButton("Apply", game.skin);
        logoutButton = new TextButton("Logout", game.skin);
        startGameButton = new TextButton("Start game", game.skin);

        scoreboardTable = new Table();
        Operators.auth.getUsers().sort((x, y) -> Integer.compare(y.getScore(), x.getScore()));
        int i = 0;
        for (User user : Operators.auth.getUsers()) {
            scoreboardTable.add(new Label(++i + ".", game.skin)).align(Align.left);
            scoreboardTable.add(new Label(user.getUsername(), game.skin)).align(Align.left);
            scoreboardTable.add(new Label(String.valueOf(user.getScore()), game.skin))
                .align(Align.right).minWidth(200).row();
        }
        scrollPane = new ScrollPane(scoreboardTable);

        profileTable.pad(50);
        profileTable.defaults().spaceBottom(10).spaceRight(10);
        profileTable.add(avatarImage);
        profileTable.add(avatarPath).growX().align(Align.bottom).row();
        profileTable.add(usernameLabel).align(Align.left);
        profileTable.add(usernameField).growX().minWidth(400).row();
        profileTable.add(nicknameLabel).align(Align.left);
        profileTable.add(nicknameField).growX().row();
        profileTable.add(sloganLabel).align(Align.left);
        profileTable.add(sloganField).growX();
        profileTable.add(randomSlogan).growX().row();
        profileTable.add(emailLabel).align(Align.left);
        profileTable.add(emailField).growX().row();
        profileTable.add(currentPasswordField).align(Align.right).minWidth(400);
        profileTable.add(passwordField).minWidth(400).growX().row();
        profileTable.add(logoutButton).align(Align.center).minWidth(400);
        profileTable.add(applyButton).align(Align.center).minWidth(400);
        profileTable.add(startGameButton).align(Align.center).minWidth(400).row();
        table.add(profileTable).growX();
        table.add(scrollPane).maxHeight(500).growX().padTop(20).align(Align.topLeft).row();

        randomSlogan.addListener(new SimpleChangeListener(this::generateSlogan));
        logoutButton.addListener(
            new SimpleChangeListener(() -> game.setScreen(new LoginScreen(game))));
        applyButton.addListener(new SimpleChangeListener(this::apply));
        startGameButton.addListener(
            new SimpleChangeListener(() -> game.setScreen(new StartGameScreen(game, user))));
    }

    private void generateSlogan() {
        if (randomSlogan.isChecked()) {
            sloganField.setDisabled(true);
            sloganField.setText(ProfileMenu.generateSlogan());
            sloganField.setColor(Color.BLACK);
        } else {
            sloganField.setDisabled(false);
            sloganField.setText("");
            sloganField.setColor(Color.WHITE);
        }
    }

    private void apply() {
        try {
            HashMap<String, Object> req = new HashMap<>() {{
                put("user", user);
                put("old-password", HashedString.fromPlain(currentPasswordField.getText())
                    .withMode(HashMode.SHA256));
                if (passwordField.getText().isEmpty()) {
                    put("new-password", HashedString.fromPlain(currentPasswordField.getText())
                        .withMode(HashMode.SHA256));
                } else {
                    if (AuthMenu.isPasswordWeak(passwordField.getText())) {
                        throw new Exception("Password is too weak!");
                    }
                    put("new-password",
                        HashedString.fromPlain(passwordField.getText()).withMode(HashMode.SHA256));
                }
                put("new-username", usernameField.getText());
                put("new-email", emailField.getText());
                put("new-nickname", nicknameField.getText());
                put("new-slogan", sloganField.getText());
            }};
            Operators.profile.changePassword(req);
            if (!req.get("new-username").equals(user.getUsername())) {
                Operators.profile.changeUsername(req);
            }
            if (!req.get("new-email").equals(user.getEmail())) {
                Operators.profile.changeEmail(req);
            }
            Operators.profile.changeNickname(req);
            Operators.profile.changeSlogan(req);
            if (!avatarPath.getText().isEmpty()) {
                Files.copy(new File(avatarPath.getText()).toPath(),
                    user.getAvatar().file().toPath());
                avatarImage = new Image(new Texture(user.getAvatar()));
            }
            popup.success("Done!");
        } catch (Exception e) {
            popup.error(e.getMessage());
        }
    }

}
