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
    private Table table;

    public ReportPanel(ControlPanel controlPanel) {
        super(controlPanel);
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

    public Player getPlayer() {
        return screen.getMyself();
    }

    private void create() {
        table = new Table(game.skin);
        table.align(Align.center);
        add(table).grow();
        addRow("Popularity", getPlayer().getPopularity());
        addRow("Peasants", getPlayer().getPeasants());
        for (String resource : getPlayer().getResources().keySet()) {
            addRow(resource, getPlayer().getResources().get(resource));
        }
    }
}
