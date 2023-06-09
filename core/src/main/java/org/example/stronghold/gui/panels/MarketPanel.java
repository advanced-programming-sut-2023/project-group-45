package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.example.stronghold.context.IntPair;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Market;
import org.example.stronghold.model.Player;
import org.example.stronghold.operator.Operators;

public class MarketPanel extends Panel {

    public static List<String> RESOURCES;
    Table resourceTable, economyTable;
    TextField amountField;
    TextButton buyButton, sellButton;
    Label resourceInfo;
    private String resource;

    public MarketPanel(ControlPanel controlPanel) {
        super(controlPanel);
        create();
    }

    public Market getMarket() {
        return screen.gameData.getMarket();
    }

    private void create() {
        RESOURCES = getMarket().getPrices().keySet().stream().toList();
        resourceTable = new Table(game.skin);
        economyTable = new Table(game.skin);
        resourceTable.align(Align.left);
        economyTable.align(Align.left);
        add(resourceTable).growX().row();
        add(economyTable).grow();
        for (String resource : RESOURCES) {
            TextButton btn = new TextButton(resource, game.skin);
            btn.addListener(new SimpleChangeListener(() -> this.changeResource(resource)));
            resourceTable.add(btn);
        }
        amountField = new TextField("0", game.skin);
        buyButton = new TextButton("Buy", game.skin);
        sellButton = new TextButton("Sell", game.skin);
        resourceInfo = new Label("", game.skin);
        economyTable.add(amountField).align(Align.center).growX();
        economyTable.add(resourceInfo).align(Align.center).fill().row();
        economyTable.add(buyButton).growX();
        economyTable.add(sellButton).growX().row();
        buyButton.addListener(new SimpleChangeListener(this::buy));
        sellButton.addListener(new SimpleChangeListener(this::sell));
    }

    private void changeResource(String resource) {
        this.resource = resource;
        IntPair status = getMarket().getPrices().get(resource);
        resourceInfo.setText(String.format(
            "Buy: %d, Sell %d",
            status.x(),
            status.y()
        ));
    }

    private Map<String, Object> buildMap() {
        return ImmutableMap.of(
            "player", screen.getMyself(),
            "game", screen.gameData,
            "amount", Integer.parseInt(amountField.getText()),
            "item", resource
        );
    }

    private void buy() {
        try {
            game.conn.sendOperatorRequest("economy", "buyMarketItem", buildMap());
            controlPanel.popup.success("Success");
        } catch (Exception e) {
            controlPanel.popup.error(e.getMessage());
        }
    }

    private void sell() {
        try {
            game.conn.sendOperatorRequest("economy", "sellMarketItem", buildMap());
            controlPanel.popup.success("Success");
        } catch (Exception e) {
            controlPanel.popup.error(e.getMessage());
        }
    }
}
