package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.gui.components.PopupWindow;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Player;

public class ReportPanel extends Panel {

    private final GameData gameData;
    private final Player player;
    private final ControlPanel controlPanel;
    private final PopupWindow popupWindow;
    private Table table;

    public ReportPanel(ControlPanel controlPanel) {
        super(controlPanel);
        this.controlPanel = controlPanel;
        this.gameData = controlPanel.getScreen().getGameData();
        this.player = controlPanel.getScreen().getMyself();
        this.popupWindow = controlPanel.getPopup();
        create();
    }

    private void addRow(String name, int value) {
        Label nameLabel = new Label(name, game.skin);
        nameLabel.setAlignment(Align.center);
        Label valueLabel = new Label(String.valueOf(value), game.skin);
        valueLabel.setAlignment(Align.center);
        table.add(nameLabel).grow();
        table.add(valueLabel).grow().row();
    }

    private void create() {
        table = new Table(game.skin);
        table.align(Align.center);
        add(table).grow();
        addRow("Popularity", player.getPopularity());
        addRow("Peasants", player.getPeasants());
        for (String resource : player.getResources().keySet()) {
            addRow(resource, player.getResources().get(resource));
        }
    }
}
