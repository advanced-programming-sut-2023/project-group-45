package org.example.stronghold.gui.panels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.gui.components.PopupWindow;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Player;

public class PopularityPanel extends Panel {

    private final GameData gameData;
    private final Player player;
    private final ControlPanel controlPanel;
    private final PopupWindow popupWindow;
    private Table table;
    private TextButton updateButton;

    public PopularityPanel(ControlPanel controlPanel) {
        super(controlPanel);
        this.controlPanel = controlPanel;
        this.gameData = controlPanel.getScreen().getGameData();
        this.player = controlPanel.getScreen().getMyself();
        this.popupWindow = controlPanel.getPopup();
        create();
    }

    private void addLabel(String factor, int value) {
        Label label = new Label(String.format("%s: %d", factor, value), game.skin);
        label.setAlignment(Align.center);
        table.add(label).growX();
    }

    private Texture getTexture(int value){
        if(value < 0){
            return game.assetLoader.getTexture("others/sad.png");
        } else if(value == 0){
            return game.assetLoader.getTexture("others/normal.png");
        } else {
            return game.assetLoader.getTexture("others/happy.png");
        }
    }

    private void addImage(int value) {
        Image image = new Image(new TextureRegionDrawable(getTexture(value)));
        table.add(image).growX();
    }

    private void create() {
        table = new Table(game.skin);
        table.align(Align.center);
        add(table).grow();
        addLabel("Total Popularity", player.getPopularity());
        addLabel("Delta Popularity", player.getDeltaPopularity());
        addLabel("Food", player.getFoodDeltaPopularity());
        addLabel("Tax", player.getTaxDeltaPopularity());
        addLabel("Religion", gameData.getReligion(player));
        addLabel("Happiness", gameData.getHappiness(player));
        table.row();
        addImage(player.getPopularity());
        addImage(player.getDeltaPopularity());
        addImage(player.getFoodDeltaPopularity());
        addImage(player.getTaxDeltaPopularity());
        addImage(gameData.getReligion(player));
        addImage(gameData.getHappiness(player));
        table.row();
    }
}
