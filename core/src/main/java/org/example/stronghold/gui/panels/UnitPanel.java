package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.example.stronghold.context.IntPair;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.model.Unit;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class UnitPanel extends Panel {

    public final List<Unit> units = new ArrayList<>();
    Label info;
    Table banners;
    ImageButton defensiveBtn, offensiveBtn, standingBtn, moveBtn, disbandBtn, attackBtn;
    String targetMode;

    public UnitPanel(ControlPanel controlPanel) {
        super(controlPanel);
        create();
    }

    private void toggleUnit(Unit unit) {
        // equality of object is wrong, it comes from a bigger bug in the model
        if (units.stream().anyMatch(u -> u == unit)) {
            units.remove(unit);
        } else {
            units.add(unit);
        }
    }

    public void toggleCell(int col, int row) {
        screen.gameData.getUnitsOnPosition(new IntPair(col, row))
            .filter(u -> u.getOwner().equals(screen.myself))
            .forEach(this::toggleUnit);
        updateInfo();
    }

    private TextureRegionDrawable getBanner(String banner) {
        return new TextureRegionDrawable(game.assetLoader.getTexture("banners/" + banner + ".png"));
    }

    private ImageButton newButton(String banner, Runnable runnable) {
        ImageButton btn = new ImageButton(getBanner(banner));
        btn.setSkin(game.skin);
        if (runnable != null) {
            btn.addListener(new SimpleChangeListener(runnable));
        }
        return btn;
    }

    private ImageButton newModeButton(String mode) {
        return newButton(mode, () -> this.setMode(mode));
    }

    private void create() {
        info = new Label("", game.skin);
        info.setWrap(true);
        add(info).growX().row();

        defensiveBtn = newModeButton("defensive");
        standingBtn = newModeButton("standing");
        offensiveBtn = newModeButton("offensive");
        disbandBtn = newButton("disband", this::disbandUnits);
        moveBtn = newButton("move", () -> this.startToBeTargeted("move"));
        attackBtn = newButton("attack", () -> this.startToBeTargeted("attack"));

        banners = new Table(game.skin);
        banners.add(defensiveBtn, standingBtn, offensiveBtn, disbandBtn, moveBtn, attackBtn);
        add(banners).align(Align.left).row();
    }

    private void updateInfo() {
        if (units.isEmpty()) {
            info.setText("No selected units");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for (Unit unit : units) {
            if (!first) {
                stringBuilder.append(" - ");
            }
            first = false;
            stringBuilder.append(String.format("%s %s %d",
                unit.getMode(),
                unit.getType(),
                unit.getHitPoints()
            ));
        }
        info.setText(stringBuilder);
    }

    private void setMode(String mode) {
        try {
            Operators.game.setUnitMode(new HashMap<>() {{
                put("units", units);
                put("mode", mode);
            }});
            controlPanel.popup.success("Changed mode to " + mode);
            updateInfo();
        } catch (OperatorException e) {
            controlPanel.popup.error(e.getMessage());
        }
    }

    private void disbandUnits() {
        try {
            Operators.game.disbandUnits(new HashMap<>() {{
                put("units", units);
                put("game", screen.gameData);
            }});
            controlPanel.popup.success("Disbanded units");
            controlPanel.setPanel(null);
        } catch (OperatorException e) {
            controlPanel.popup.error(e.getMessage());
        }
    }

    private void startToBeTargeted(String mode) {
        targetMode = mode;
        screen.toBeTargeted = true;
        controlPanel.popup.info("Select target");
    }

    public void setUnitTarget(int col, int row) {
        if (targetMode == null) {
            return;
        }
        if (targetMode.equals("move")) {
            setNavigationGoal(col, row);
        } else if (targetMode.equals("attack")) {
            setAttackGoal(col, row);
        }
    }

    private void setNavigationGoal(int col, int row) {
        try {
            Operators.game.setNavigationGoal(new HashMap<>() {{
                put("units", units);
                put("game", screen.gameData);
                put("position", new IntPair(col, row));
            }});
            controlPanel.popup.success("Set navigation goal");
        } catch (OperatorException e) {
            controlPanel.popup.error(e.getMessage());
        }
    }

    private void setAttackGoal(int col, int row) {
        try {
            Operators.game.attackUnit(new HashMap<>() {{
                put("game", screen.gameData);
                put("player", screen.myself);
                put("position", new IntPair(col, row));
                put("units", units);
            }});
            controlPanel.popup.success("Set attack goal");
        } catch (OperatorException e) {
            controlPanel.popup.error(e.getMessage());
        }
    }
}
