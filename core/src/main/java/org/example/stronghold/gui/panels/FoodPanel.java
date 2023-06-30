package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.google.common.collect.ImmutableMap;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.gui.components.PopupWindow;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Player;
import org.example.stronghold.operator.Operators;

import java.util.Map;

public class FoodPanel extends Panel {

    private final GameData gameData;
    private final Player player;
    private final ControlPanel controlPanel;
    private final PopupWindow popupWindow;
    private Table table;
    private TextField rateField;
    private TextButton updateButton;

    public FoodPanel(ControlPanel controlPanel) {
        super(controlPanel);
        this.controlPanel = controlPanel;
        this.gameData = controlPanel.getScreen().getGameData();
        this.player = controlPanel.getScreen().getMyself();
        this.popupWindow = controlPanel.getPopup();
        create();
    }

    private void create() {
        table = new Table(game.skin);
        table.align(Align.left);
        add(table).grow();
        rateField = new TextField(String.valueOf(player.getFoodRate()), game.skin);
        updateButton = new TextButton("Update", game.skin);
        table.add(rateField).growX();
        table.add(updateButton).growX().row();
        updateButton.addListener(new SimpleChangeListener(this::update));
    }

    private Map<String, Object> buildMap() {
        return ImmutableMap.of(
            "player", player,
            "rate", Integer.parseInt(rateField.getText())
        );
    }

    private void update(){
        try {
            Operators.game.setFoodRate(buildMap());
            popupWindow.success("Success");
        } catch (Exception e) {
            popupWindow.error(e.getMessage());
        }
    }
}
