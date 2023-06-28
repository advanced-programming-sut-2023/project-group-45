package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import org.example.stronghold.model.Building;
import org.example.stronghold.model.Tile;

public class TilePanel extends Table {

    private final Skin skin;
    private final int column, row;
    private final Tile tile;

    public TilePanel(Skin skin, int column, int row, Tile tile) {
        super(skin);
        this.skin = skin;
        this.column = column;
        this.row = row;
        this.tile = tile;

        create();
    }

    private void addLabel(String text) {
        add(new Label(text, skin)).align(Align.left).row();
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
        pad(10);
        addLabel(String.format("(%d,%d) %s", column, row, tile.getType()));
        addLabel(getBuildingText());
    }

}
