package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import java.util.HashMap;
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

    public BuildingPanel(ControlPanel controlPanel, int column, int row, Building building) {
        super(controlPanel);
        this.column = column;
        this.row = row;
        this.building = building;
        create();
    }

    private void create() {
        align(Align.left);
        add(getTitle()).row();
        if (building.getOwner().equals(screen.myself)) {
            createControlButtons();
            row();
        }
    }

    private String getTitle() {
        return String.format("%s's %s (%d HP) (%d Labor)",
            building.getOwner().getUser().getUsername(), building.getType(),
            building.getHitPoints(),
            building.getLabors()
        );
    }

    private void createControlButtons() {
        TextButton destroy, repair, undo;
        destroy = new TextButton("Destroy", game.skin);
        repair = new TextButton("Repair", game.skin);
        undo = new TextButton("Undo", game.skin);
        Table table = new Table(game.skin);
        table.add(destroy, repair, undo);
        add(table).align(Align.left);

        destroy.addListener(new SimpleChangeListener(this::destroyBuilding));
        repair.addListener(new SimpleChangeListener(this::repairBuilding));
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
}
