package org.example.stronghold.gui.sections;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import java.util.HashMap;
import java.util.List;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.StrongholdGame;
import org.example.stronghold.gui.components.FormScreen;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Player;
import org.example.stronghold.model.User;

public class StartGameScreen extends FormScreen {

    private final User user;
    TextField mapNameField, opponentField;
    TextButton startGameButton, shareMapButton, refreshButton;
    Table gameTable;

    public StartGameScreen(StrongholdGame game, User user) {
        super(game);
        this.user = user;
    }

    @Override
    public void formShow() {
        mapNameField = new TextField("", game.skin);
        opponentField = new TextField("", game.skin);
        startGameButton = new TextButton("Start game", game.skin);
        shareMapButton = new TextButton("Share map", game.skin);
        refreshButton = new TextButton("Refresh games", game.skin);

        table.defaults().spaceBottom(10).spaceRight(10);
        table.add("Map").align(Align.left);
        table.add(mapNameField).minWidth(400).row();
        table.add("Opponent").align(Align.left);
        table.add(opponentField).minWidth(400).row();
        table.add(startGameButton).colspan(2).minWidth(200).row();

        gameTable = new Table(game.skin);
        table.add(gameTable).colspan(2).align(Align.center).row();
        table.add(refreshButton).colspan(2).minWidth(200).row();

        startGameButton.addListener(new SimpleChangeListener(this::startTheGame));
        shareMapButton.addListener(
            new SimpleChangeListener(() -> game.setScreen(new ShareMapScreen(game, user))));
        refreshButton.addListener(new SimpleChangeListener(this::showGames));

        showGames();
    }

    private void showGames() {
        gameTable.reset();
        try {
            List<GameData> gameData = (List<GameData>) game.conn.sendOperatorRequest("auth",
                "getAllGameData", new HashMap<>());
            if (gameData.isEmpty()) {
                gameTable.add("No game found").growX().row();
            }
            for (GameData data : gameData) {
                StringBuilder builder = new StringBuilder();
                builder.append("Game ID: ").append(data.getId()).append(", ");
                builder.append("Players: ");
                data.getPlayers().stream().map(Player::getUser).forEach(user -> {
                    builder.append(user.getUsername()).append(" (").append(user.getNickname())
                        .append(") ");
                });
                gameTable.add(builder.toString()).growX();
                TextButton joinBtn = new TextButton("Join", game.skin);
                joinBtn.addListener(new SimpleChangeListener(() -> joinTheGame(data.getId())));
                gameTable.add(joinBtn).minWidth(100).row();
            }
        } catch (Exception e) {
            popup.error(e.getMessage());
        }
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

    private void joinTheGame(long gameId) {
        try {
            GameData gameData = (GameData) game.conn.sendObjectRequest("GameData", gameId);
            game.setScreen(new MapScreen(game, user, gameData));
        } catch (Exception e) {
            popup.error(e.getMessage());
        }
    }
}
