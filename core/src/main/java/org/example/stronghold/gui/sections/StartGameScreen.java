package org.example.stronghold.gui.sections;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import java.util.HashMap;
import java.util.List;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.StrongholdGame;
import org.example.stronghold.gui.components.FormScreen;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.User;
import org.example.stronghold.model.template.GameMapTemplate;

public class StartGameScreen extends FormScreen {

    private final User user;
    TextField mapNameField, opponentField, gameIdField;
    TextButton startGameButton, joinGameButton, shareMapButton;

    public StartGameScreen(StrongholdGame game, User user) {
        super(game);
        this.user = user;
    }

    @Override
    public void formShow() {
        mapNameField = new TextField("", game.skin);
        opponentField = new TextField("", game.skin);
        gameIdField = new TextField("", game.skin);
        startGameButton = new TextButton("Start game", game.skin);
        joinGameButton = new TextButton("Join game", game.skin);
        shareMapButton = new TextButton("Share map", game.skin);

        table.defaults().spaceBottom(10).spaceRight(10);
        table.add("Map").align(Align.left);
        table.add(mapNameField).minWidth(400).row();
        table.add("Opponent").align(Align.left);
        table.add(opponentField).minWidth(400).row();
        table.add(startGameButton).colspan(2).minWidth(200).row();
        table.add("Game ID").align(Align.left);
        table.add(gameIdField).minWidth(400).row();
        table.add(joinGameButton).colspan(2).minWidth(200).row();
        table.add(shareMapButton).colspan(2).minWidth(200).row();

        startGameButton.addListener(new SimpleChangeListener(this::startTheGame));
        joinGameButton.addListener(new SimpleChangeListener(this::joinTheGame));
        shareMapButton.addListener(new SimpleChangeListener(() -> game.setScreen(new ShareMapScreen(game, user))));
    }

    private void startTheGame() {
        try {
            User opponent = (User) game.conn.sendObjectRequest("User", opponentField.getText());
            List<User> users = List.of(user, opponent);
            long gameId = (Long) game.conn.sendOperatorRequest("game", "startGame",
                new HashMap<>() {{
                    put("map", mapNameField.getText());
                    put("users", users);
                }});
            GameData gameData = (GameData) game.conn.sendObjectRequest("GameData", gameId);
            game.setScreen(new MapScreen(game, user, gameData));
        } catch (Exception e) {
            popup.error(e.getMessage());
        }
    }

    private void joinTheGame() {
        try {
            long gameId = Long.parseLong(gameIdField.getText());
            GameData gameData = (GameData) game.conn.sendObjectRequest("GameData", gameId);
            game.setScreen(new MapScreen(game, user, gameData));
        } catch (Exception e) {
            popup.error(e.getMessage());
        }
    }
}
