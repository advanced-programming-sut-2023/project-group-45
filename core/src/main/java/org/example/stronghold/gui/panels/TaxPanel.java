package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.*;
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

public class TaxPanel extends Panel {

    private final GameData gameData;
    private final Player player;
    private final ControlPanel controlPanel;
    private final PopupWindow popupWindow;
    private Table table;
    private Slider rateSlider;
    private Label rateLabel;
    private TextButton updateButton;

    public TaxPanel(ControlPanel controlPanel) {
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
        rateSlider = new Slider(-3, 8, 1, false, game.skin);
        rateSlider.setValue(player.getTaxRate());
        rateLabel = new Label(String.format("Tax rate: %d", player.getTaxRate()), game.skin);
        rateLabel.setAlignment(Align.center);
        updateButton = new TextButton("Update", game.skin);
        table.add(rateSlider).growX().row();
        table.add(rateLabel).growX();
        table.add(updateButton).growX().row();
        updateButton.addListener(new SimpleChangeListener(this::update));
    }

    private Map<String, Object> buildMap() {
        return ImmutableMap.of(
            "player", player,
            "rate", (int) rateSlider.getValue()
        );
    }

    private void update(){
        try {
            Operators.game.setTaxRate(buildMap());
            rateLabel.setText(String.format("Tax rate: %d", player.getTaxRate()));
            popupWindow.success("Success");
        } catch (Exception e) {
            popupWindow.error(e.getMessage());
        }
    }
}