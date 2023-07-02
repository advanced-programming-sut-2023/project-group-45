package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.gui.components.PopupWindow;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Player;
import org.example.stronghold.operator.Operators;

public class TaxPanel extends Panel {

    private Table table;
    private Slider rateSlider;
    private Label rateLabel;
    private TextButton updateButton;

    public TaxPanel(ControlPanel controlPanel) {
        super(controlPanel);
        create();
    }

    public Player getPlayer() {
        return screen.getMyself();
    }

    private void create() {
        table = new Table(game.skin);
        table.align(Align.left);
        add(table).grow();
        rateSlider = new Slider(-3, 8, 1, false, game.skin);
        rateSlider.setValue(getPlayer().getTaxRate());
        rateLabel = new Label(String.format("Tax rate: %d", getPlayer().getTaxRate()), game.skin);
        rateLabel.setAlignment(Align.center);
        updateButton = new TextButton("Update", game.skin);
        table.add(rateSlider).growX().row();
        table.add(rateLabel).growX();
        table.add(updateButton).growX().row();
        updateButton.addListener(new SimpleChangeListener(this::update));
    }

    private Map<String, Object> buildMap() {
        return ImmutableMap.of(
            "player", getPlayer(),
            "rate", (int) rateSlider.getValue()
        );
    }

    private void update() {
        try {
            game.conn.sendOperatorRequest("game", "setTaxRate", buildMap());
            rateLabel.setText(String.format("Tax rate: %d", getPlayer().getTaxRate()));
            controlPanel.popup.success("Success");
        } catch (Exception e) {
            controlPanel.popup.error(e.getMessage());
        }
    }
}
