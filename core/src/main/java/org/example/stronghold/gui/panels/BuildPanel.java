package org.example.stronghold.gui.panels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;

public class BuildPanel extends Panel {

    public static final Map<String, List<String>> CATEGORIES = ImmutableMap.of(
        "Farm", List.of("Quarry", "Wheat Farmer", "Bakery"),
        "Military", List.of("Barracks"),
        "Housing", List.of("Hovel"),
        "Religious", List.of("Church")
    );

    Table categoryTable, buildingsTable;

    public BuildPanel(ControlPanel controlPanel) {
        super(controlPanel);
        create();
    }

    private void create() {
        categoryTable = new Table(game.skin);
        buildingsTable = new Table(game.skin);
        categoryTable.align(Align.left);
        buildingsTable.align(Align.left);
        add(categoryTable).growX().row();
        add(buildingsTable).grow();
        for (String category : CATEGORIES.keySet()) {
            TextButton btn = new TextButton(category, game.skin);
            btn.addListener(new SimpleChangeListener(() -> this.switchCategory(category)));
            categoryTable.add(btn);
        }
    }

    private Texture getBuildingTexture(String building) {
        return game.assetLoader.getTexture("buildings/" + building + ".png");
    }

    private void switchCategory(String category) {
        buildingsTable.clearChildren();
        for (String building : CATEGORIES.get(category)) {
            ImageButton btn = new ImageButton(
                new TextureRegionDrawable(getBuildingTexture(building)));
            btn.setSkin(game.skin);
            btn.row();
            btn.add(building);
            btn.addListener(new SimpleChangeListener(() -> screen.toBeBuiltType = building));
            buildingsTable.add(btn).height(100).width(70).padLeft(20);
        }
    }
}
