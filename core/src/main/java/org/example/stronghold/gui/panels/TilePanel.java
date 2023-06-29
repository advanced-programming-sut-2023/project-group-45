package org.example.stronghold.gui.panels;

import com.badlogic.gdx.utils.Align;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.model.Building;
import org.example.stronghold.model.Tile;

public class TilePanel extends Panel {

    private final int column, row;
    private final Tile tile;

    public TilePanel(ControlPanel controlPanel, int column, int row, Tile tile) {
        super(controlPanel);
        this.column = column;
        this.row = row;
        this.tile = tile;
        create();
    }

    private void addLabel(String text) {
        add(text).align(Align.left).row();
    }

    private String getBuildingText() {
        Building building = tile.getBuilding();
        if (building == null) {
            return "No building";
        }
        return String.format("%s HP:%d Owner:%s", building.getType(), building.getHitPoints(),
            building.getOwner().getUser().getUsername());
    }

    private void create() {
        align(Align.left);
        addLabel(String.format("(%d,%d) %s", column, row, tile.getType()));
        addLabel(getBuildingText());
    }

}
