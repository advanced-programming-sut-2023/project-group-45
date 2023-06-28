package org.example.stronghold.gui.sections;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import java.util.HashMap;
import org.example.stronghold.cli.sections.AuthMenu;
import org.example.stronghold.context.HashMode;
import org.example.stronghold.context.HashedString;
import org.example.stronghold.gui.Captcha;
import org.example.stronghold.gui.FormScreen;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.StrongholdGame;
import org.example.stronghold.model.User;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class RegisterScreen extends FormScreen {

    Label usernameLabel, passwordLabel, passwordConfirmLabel, emailLabel, nicknameLabel, captchaLabel;
    TextField usernameField, passwordField, passwordConfirmField, emailField, nicknameField, captchaField;
    CheckBox randomPassword;
    TextButton registerButton, backButton, reloadCaptchaButton;
    Image captchaImage;
    Captcha captcha;

    public RegisterScreen(StrongholdGame game) {
        super(game);
    }

    private void updateCaptcha() {
        captcha = Captcha.generate(game.assetLoader);
        captchaImage.setDrawable(
            new TextureRegionDrawable(new TextureRegion(captcha.getTexture())));
        captchaField.setText("");
    }

    @Override
    public void formShow() {
        usernameLabel = new Label("Username", game.skin);
        usernameField = new TextField("", game.skin);
        passwordLabel = new Label("Password", game.skin);
        passwordField = new TextField("", game.skin);
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        passwordConfirmLabel = new Label("Confirm password", game.skin);
        passwordConfirmField = new TextField("", game.skin);
        passwordConfirmField.setPasswordCharacter('*');
        passwordConfirmField.setPasswordMode(true);
        emailLabel = new Label("Email", game.skin);
        emailField = new TextField("", game.skin);
        nicknameLabel = new Label("Nickname", game.skin);
        nicknameField = new TextField("", game.skin);
        captchaLabel = new Label("Captcha", game.skin);
        captchaField = new TextField("", game.skin);
        captcha = Captcha.generate(game.assetLoader);
        captchaImage = new Image(captcha.getTexture());

        randomPassword = new CheckBox("Random password", game.skin);
        reloadCaptchaButton = new TextButton("Reload captcha", game.skin);
        registerButton = new TextButton("Register", game.skin);
        backButton = new TextButton("Back", game.skin);

        table.pad(50);
        table.defaults().spaceBottom(10).spaceRight(10);
        table.add(usernameLabel).align(Align.right);
        table.add(usernameField).growX().minWidth(400).row();
        table.add(passwordLabel).align(Align.right);
        table.add(passwordField).growX().row();
        table.add(passwordConfirmLabel).align(Align.right);
        table.add(passwordConfirmField).growX().row();
        table.add(emailLabel).align(Align.right);
        table.add(emailField).growX().row();
        table.add(nicknameLabel).align(Align.right);
        table.add(nicknameField).growX().row();
        table.add(captchaLabel).align(Align.right);
        table.add(captchaField).growX().row();
        table.add(captchaImage).colspan(2).align(Align.center).row();

        table.add(randomPassword).align(Align.right).colspan(2).row();
        table.add(reloadCaptchaButton).align(Align.center).colspan(2).minWidth(200).row();
        table.add(registerButton).align(Align.center).colspan(2).minWidth(200).row();
        table.add(backButton).align(Align.center).colspan(2).minWidth(200).row();

        popup.label.setColor(Color.RED);

        registerButton.addListener(new SimpleChangeListener(this::register));
        randomPassword.addListener(new SimpleChangeListener(this::randomPasswordChanged));
        passwordField.addListener(new SimpleChangeListener(this::passwordChanged));
        passwordConfirmField.addListener(new SimpleChangeListener(this::passwordConfirmChanged));
        backButton.addListener(new SimpleChangeListener(this::back));
        reloadCaptchaButton.addListener(new SimpleChangeListener(this::updateCaptcha));
    }

    private void register() {
        if (!captchaField.getText().equals(captcha.getAnswer())) {
            popup.pop("Captcha mismatch");
            return;
        }
        String password = passwordField.getText();
        String passwordConfirm = passwordConfirmField.getText();
        if (AuthMenu.isPasswordWeak(password)) {
            popup.pop("Password is too weak");
            return;
        }
        if (!password.equals(passwordConfirm)) {
            popup.pop("Passwords do not match");
            return;
        }
        try {
            User user = Operators.auth.register(new HashMap<>() {{
                put("username", usernameField.getText());
                put("password", HashedString.fromPlain(password).withMode(HashMode.SHA256));
                put("email", emailField.getText());
                put("nickname", nicknameField.getText());
            }});
            log("registered as %s", user);
        } catch (OperatorException e) {
            popup.pop(e.getMessage());
        }
    }

    private void randomPasswordChanged() {
        if (randomPassword.isChecked()) {
            passwordField.setDisabled(true);
            passwordField.setPasswordMode(false);
            passwordField.setText(AuthMenu.generatePassword());
            passwordField.setColor(Color.BLACK);
        } else {
            passwordField.setDisabled(false);
            passwordField.setPasswordMode(true);
            passwordField.setText("");
            passwordField.setColor(Color.WHITE);
        }
        passwordConfirmField.setText("");
        passwordConfirmField.setColor(Color.WHITE);
    }

    private void passwordChanged() {
        if (randomPassword.isChecked()) {
            return;
        }
        if (AuthMenu.isPasswordWeak(passwordField.getText())) {
            passwordField.setColor(Color.RED);
        } else {
            passwordField.setColor(Color.GREEN);
        }
        passwordConfirmField.setColor(Color.WHITE);
    }

    private void passwordConfirmChanged() {
        if (!passwordField.getText().equals(passwordConfirmField.getText())) {
            passwordConfirmField.setColor(Color.RED);
        } else {
            passwordConfirmField.setColor(Color.GREEN);
        }
    }

    private void back() {
        game.setScreen(new LoginScreen(game));
    }
}
