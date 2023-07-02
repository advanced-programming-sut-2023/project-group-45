package org.example.stronghold.gui.panels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.gui.components.PopupWindow;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Player;

public class PopularityPanel extends Panel {

    private Table table;

    public PopularityPanel(ControlPanel controlPanel) {
        super(controlPanel);
        this.controlPanel = controlPanel;
        create();
    }

    private void addLabel(String factor, int value) {
        Label label = new Label(String.format("%s: %d", factor, value), game.skin);
        label.setAlignment(Align.center);
        table.add(label).growX();
    }

    private Texture getTexture(int value) {
        if (value < 0) {
            return game.assetLoader.getTexture("others/sad.png");
        } else if (value == 0) {
            return game.assetLoader.getTexture("others/normal.png");
        } else {
            return game.assetLoader.getTexture("others/happy.png");
        }
    }

    private void addImage(int value) {
        Image image = new Image(new TextureRegionDrawable(getTexture(value)));
        table.add(image).growX();
    }

    public Player getPlayer() {
        return screen.getMyself();
    }

    private void create() {
        table = new Table(game.skin);
        table.align(Align.center);
        add(table).grow();
        addLabel("Total Popularity", getPlayer().getPopularity());
        addLabel("Delta Popularity", getPlayer().getDeltaPopularity());
        addLabel("Food", getPlayer().getFoodDeltaPopularity());
        addLabel("Tax", getPlayer().getTaxDeltaPopularity());
        addLabel("Religion", screen.gameData.getReligion(getPlayer()));
        addLabel("Happiness", screen.gameData.getHappiness(getPlayer()));
        table.row();
        addImage(getPlayer().getPopularity());
        addImage(getPlayer().getDeltaPopularity());
        addImage(getPlayer().getFoodDeltaPopularity());
        addImage(getPlayer().getTaxDeltaPopularity());
        addImage(screen.gameData.getReligion(getPlayer()));
        addImage(screen.gameData.getHappiness(getPlayer()));
        table.row();
    }
}
