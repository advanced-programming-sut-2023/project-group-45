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
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class StartGameScreen extends FormScreen {

    private final User user;
    TextField mapNameField, opponentField;
    TextButton startGameButton;

    public StartGameScreen(StrongholdGame game, User user) {
        super(game);
        this.user = user;
    }

    @Override
    public void formShow() {
        mapNameField = new TextField("", game.skin);
        opponentField = new TextField("", game.skin);
        startGameButton = new TextButton("Start game", game.skin);

        table.defaults().spaceBottom(10).spaceRight(10);
        table.add("Map").align(Align.left);
        table.add(mapNameField).minWidth(400).row();
        table.add("Opponent").align(Align.left);
        table.add(opponentField).minWidth(400).row();
        table.add(startGameButton).colspan(2).minWidth(200).row();

        startGameButton.addListener(new SimpleChangeListener(this::startTheGame));
    }

    private void startTheGame() {
        try {
            User opponent = Operators.auth.findUser(new HashMap<>() {{
                put("username", opponentField.getText());
            }});
            List<User> users = List.of(user, opponent);
            GameMapTemplate gameMapTemplate = game.templateDatabase.getGameMapTemplates()
                .get(mapNameField.getText());
            if (gameMapTemplate == null) {
                popup.error("Map not found");
                return;
            }
            GameData gameData = Operators.game.startGame(new HashMap<>() {{
                put("map", gameMapTemplate);
                put("users", users);
            }});
            game.setScreen(new MapScreen(game, gameData));
        } catch (OperatorException e) {
            popup.error(e.getMessage());
        }
    }
}
