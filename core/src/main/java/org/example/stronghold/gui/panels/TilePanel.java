package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import java.util.HashMap;
import org.example.stronghold.context.ClipboardUtils;
import org.example.stronghold.context.IntPair;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.model.Building;
import org.example.stronghold.model.Tile;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class TilePanel extends Panel {

    private final int column, row;
    TextField equipmentType;
    TextButton buildEquipment;

    public TilePanel(ControlPanel controlPanel, int column, int row) {
        super(controlPanel);
        this.column = column;
        this.row = row;
        create();
    }

    public Tile getTile() {
        return screen.getMap().getAt(column, row);
    }

    private void addLabel(String text) {
        add(text).align(Align.left).row();
    }

    private String getBuildingText() {
        Building building = getTile().getBuilding();
        if (building == null) {
            return "No building";
        }
        return String.format("%s HP:%d Owner:%s", building.getType(), building.getHitPoints(),
            building.getOwner().getUser().getUsername());
    }

    private void create() {
        align(Align.left);
        addLabel(String.format("(%d,%d) %s", column, row, getTile().getType()));
        addLabel(getBuildingText());
        TextButton pasteBtn = new TextButton("Paste building", game.skin);
        add(pasteBtn).align(Align.left).row();
        pasteBtn.addListener(new SimpleChangeListener(this::pasteBuilding));

        if (hasEngineers()) {
            equipmentType = new TextField("", game.skin);
            buildEquipment = new TextButton("Build equipment", game.skin);

            add(equipmentType).align(Align.left).width(200);
            add(buildEquipment).align(Align.left).row();

            buildEquipment.addListener(new SimpleChangeListener(this::buildTheEquipment));
        }
    }

    private void pasteBuilding() {
        String type;
        try {
            type = ClipboardUtils.pasteFromClipboard().trim();
        } catch (Exception e) {
            controlPanel.popup.error("Clipboard is invalid");
            return;
        }
        if (type.isEmpty()) {
            controlPanel.popup.error("Clipboard is empty");
            return;
        }
        try {
            game.conn.sendOperatorRequest("game", "dropBuilding", new HashMap<>() {{
                put("game", screen.gameData);
                put("player", screen.getMyself());
                put("building", type);
                put("position", new IntPair(column, row));
            }});
            controlPanel.popup.success("Pasted");
            controlPanel.setPanel(null);
        } catch (Exception e) {
            controlPanel.popup.error(e.getMessage());
        }
    }

    private boolean hasEngineers() {
        return screen.gameData.getUnitsOnPosition(new IntPair(column, row))
            .anyMatch(u -> u.getOwner().equals(screen.getMyself()) && u.getType().equals("Engineer"));
    }

    private void buildTheEquipment() {
        String type = equipmentType.getText().trim();
        if (type.isEmpty()) {
            controlPanel.popup.error("Equipment type is empty");
            return;
        }
        try {
            game.conn.sendOperatorRequest("game", "buildEquipment", new HashMap<>() {{
                put("game", screen.gameData);
                put("player", screen.getMyself());
                put("position", new IntPair(column, row));
                put("type", type);
            }});
            controlPanel.popup.success("Equipment built");
        } catch (Exception e) {
            controlPanel.popup.error(e.getMessage());
        }
    }

}
