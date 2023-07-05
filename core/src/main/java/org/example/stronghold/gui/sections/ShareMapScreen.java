package org.example.stronghold.gui.sections;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.StrongholdGame;
import org.example.stronghold.gui.components.FormScreen;
import org.example.stronghold.model.User;
import org.example.stronghold.model.template.GameMapTemplate;
import org.example.stronghold.model.template.TemplateDatabase;

public class ShareMapScreen extends FormScreen {

    private final User user;
    TextButton exitBtn, shareBtn;
    TextField mapFileField;

    public ShareMapScreen(StrongholdGame game, User user) {
        super(game);
        this.user = user;
    }

    private void getAndShowMaps() throws Exception {
        List<GameMapTemplate> maps = (List) game.conn.sendOperatorRequest("auth", "getMaps", new HashMap<>());
        for (GameMapTemplate map : maps) {
            table.add(map.getName()).minWidth(300);
            table.add(map.getAuthor()).minWidth(300);
            table.row();
        }
    }

    private void shareMap() {
        String filename = mapFileField.getText().trim();
        if (filename.isEmpty()) {
            popup.error("Map file name cannot be empty");
            return;
        }
        GameMapTemplate map = null;
        try {
            map = TemplateDatabase.fromFile(new File(filename), GameMapTemplate.class);
        } catch (IOException e) {
            popup.error("File content is invalid");
            return;
        }
        map.setAuthor(user.getUsername());
        try {
            GameMapTemplate finalMap = map;
            game.conn.sendOperatorRequest("auth", "addMap", new HashMap<>() {{
                put("name", finalMap.getName());
                put("map-object", finalMap);
            }});
            popup.success("Map shared successfully");
        } catch (Exception e) {
            popup.error(e.getMessage());
        }
    }

    @Override
    public void formShow() {
        try {
            getAndShowMaps();
        } catch (Exception e) {
            popup.error(e.getMessage());
        }

        Table mapShareTable = new Table(game.skin);
        mapFileField = new TextField("", game.skin);
        mapShareTable.add("Map file");
        mapShareTable.add(mapFileField).growX().row();

        table.add(mapShareTable).colspan(2).growX().row();

        shareBtn = new TextButton("Share", game.skin);
        table.add(shareBtn).colspan(2).row();

        exitBtn = new TextButton("Exit", game.skin);
        table.add(exitBtn).colspan(2).row();

        exitBtn.addListener(new SimpleChangeListener(() -> game.setScreen(new StartGameScreen(game, user))));
        shareBtn.addListener(new SimpleChangeListener(this::shareMap));
    }
}
