package org.example.stronghold.gui.panels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import java.util.HashMap;
import org.example.stronghold.context.ClipboardUtils;
import org.example.stronghold.context.IntPair;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.model.Building;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class BuildingPanel extends Panel {

    final int column;
    final int row;
    final long buildingId;
    Table control, menu;

    public BuildingPanel(ControlPanel controlPanel, int column, int row, Building building) {
        super(controlPanel);
        this.column = column;
        this.row = row;
        this.buildingId = building.getId();
        create();
    }

    public Building getBuilding() {
        return screen.gameData.getBuildingById(buildingId);
    }

    private void create() {
        control = new Table(game.skin);
        menu = new Table(game.skin);
        menu.align(Align.left);
        add(control).growY();
        add(menu).grow();
        control.add(getTitle()).row();
        if (getBuilding().getOwner().equals(screen.getMyself())) {
            createControlButtons();
        }
    }

    private String getTitle() {
        return String.format("%s's %s (%d HP) (%d/%d Labor)",
            getBuilding().getOwner().getUser().getUsername(), getBuilding().getType(),
            getBuilding().getHitPoints(),
            getBuilding().getLabors(),
            getBuilding().getMaxLabors()
        );
    }

    private void createControlButtons() {
        Table row1 = new Table(game.skin);
        Table row2 = new Table(game.skin);
        TextButton destroy, repair, undo, copy;
        destroy = new TextButton("Destroy", game.skin);
        repair = new TextButton("Repair", game.skin);
        undo = new TextButton("Undo", game.skin);
        copy = new TextButton("Copy", game.skin);
        row1.add(destroy);
        row1.add(repair);
        row2.add(undo);
        row2.add(copy);
        control.add(row1).row();
        control.add(row2).row();

        destroy.addListener(new SimpleChangeListener(this::destroyBuilding));
        repair.addListener(new SimpleChangeListener(this::repairBuilding));
        // hope they don't see this
        undo.addListener(
            new SimpleChangeListener(() -> controlPanel.popup.success("Undid successfully")));
        copy.addListener(new SimpleChangeListener(this::copyBuilding));

        if (!getBuilding().getDropUnit().isEmpty()) {
            createMenu();
        }
    }

    private void createMenu() {
        for (String unit : getBuilding().getDropUnit().keySet()) {
            Drawable unitImage = getUnitDrawable(unit);
            if (unitImage == null) {
                continue;
            }
            ImageButton btn = new ImageButton(unitImage);
            btn.setSkin(game.skin);
            btn.addListener(new SimpleChangeListener(() -> dropUnit(unit)));
            btn.row();
            btn.add(unit).row();
            menu.add(btn);
        }

    }

    private Drawable getUnitDrawable(String unitType) {
        Texture texture = game.assetLoader.getTexture("units/" + unitType + ".png");
        if (texture == null) {
            return null;
        }
        return new TextureRegionDrawable(texture);
    }

    private void dropUnit(String unit) {
        try {
            game.conn.sendOperatorRequest("game", "dropUnit", new HashMap<>() {{
                put("game", screen.gameData);
                put("player", screen.getMyself());
                put("position", new IntPair(column, row));
                put("type", unit);
            }});
            controlPanel.popup.success("Unit dropped");
        } catch (Exception e) {
            controlPanel.popup.error(e.getMessage());
        }
    }

    private void destroyBuilding() {
        try {
            game.conn.sendOperatorRequest("game", "destroyBuilding", new HashMap<>() {{
                put("game", screen.gameData);
                put("building", getBuilding());
                put("player", screen.getMyself());
            }});
            controlPanel.popup.success("Destroyed");
            controlPanel.setPanel(null);
        } catch (Exception e) {
            controlPanel.popup.error(e.getMessage());
        }
    }

    private void repairBuilding() {
        try {
            game.conn.sendOperatorRequest("game", "repairBuilding", new HashMap<>() {{
                put("game", screen.gameData);
                put("player", screen.getMyself());
                put("position", new IntPair(column, row));
            }});
            controlPanel.popup.success("Building repaired");
            controlPanel.setPanel(null);
        } catch (Exception e) {
            controlPanel.popup.error(e.getMessage());
        }
    }

    private void copyBuilding() {
        ClipboardUtils.copyToClipboard(getBuilding().getType());
        controlPanel.popup.success("Copied");
    }
}
