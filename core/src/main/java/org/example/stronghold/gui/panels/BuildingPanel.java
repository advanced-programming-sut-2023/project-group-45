package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    final Building building;
    Table control, menu;

    public BuildingPanel(ControlPanel controlPanel, int column, int row, Building building) {
        super(controlPanel);
        this.column = column;
        this.row = row;
        this.building = building;
        create();
    }

    private void create() {
        control = new Table(game.skin);
        menu = new Table(game.skin);
        menu.align(Align.left);
        add(control).growY();
        add(menu).grow();
        control.add(getTitle()).row();
        if (building.getOwner().equals(screen.myself)) {
            createControlButtons();
        }
    }

    private String getTitle() {
        return String.format("%s's %s (%d HP) (%d/%d Labor)",
            building.getOwner().getUser().getUsername(), building.getType(),
            building.getHitPoints(),
            building.getLabors(),
            building.getMaxLabors()
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
    }

    private void destroyBuilding() {
        try {
            Operators.game.destroyBuilding(new HashMap<>() {{
                put("game", screen.gameData);
                put("building", building);
                put("player", screen.myself);
            }});
            controlPanel.popup.success("Destroyed");
            controlPanel.setPanel(null);
        } catch (OperatorException e) {
            controlPanel.popup.error(e.getMessage());
        }
    }

    private void repairBuilding() {
        try {
            Operators.game.repairBuilding(new HashMap<>() {{
                put("game", screen.gameData);
                put("player", screen.myself);
                put("position", new IntPair(column, row));
                controlPanel.popup.success("Building repaired");
                controlPanel.setPanel(null);
            }});
        } catch (OperatorException e) {
            controlPanel.popup.error(e.getMessage());
        }
    }

    private void copyBuilding() {
        ClipboardUtils.copyToClipboard(building.getType());
        controlPanel.popup.success("Copied");
    }
}
