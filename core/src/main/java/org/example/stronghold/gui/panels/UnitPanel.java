package org.example.stronghold.gui.panels;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import java.util.ArrayList;
import java.util.List;
import org.example.stronghold.context.IntPair;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.model.Unit;

public class UnitPanel extends Panel {

    public final List<Unit> units = new ArrayList<>();
    Label info;
    Table banners;
    ImageButton defensiveBtn, offensiveBtn, standingBtn, moveBtn, disbandBtn, attackBtn;

    public UnitPanel(ControlPanel controlPanel) {
        super(controlPanel);
        create();
    }

    private void toggleUnit(Unit unit) {
        if (units.contains(unit))
            units.remove(unit);
        else
            units.add(unit);
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

    private ImageButton getButton(String banner) {
        ImageButton btn = new ImageButton(getBanner(banner));
        btn.setSkin(game.skin);
        return btn;
    }

    private void create() {
        info = new Label("", game.skin);
        info.setWrap(true);
        add(info).growX().row();

        defensiveBtn = getButton("defensive");
        offensiveBtn = getButton("offensive");
        standingBtn = getButton("standing");
        moveBtn = getButton("move");
        disbandBtn = getButton("disband");
        attackBtn = getButton("attack");

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
            if (!first)
                stringBuilder.append(" - ");
            first = false;
            stringBuilder.append(String.format("%s %d",
                unit.getType(),
                unit.getHitPoints()
            ));
        }
        info.setText(stringBuilder);
    }
}
