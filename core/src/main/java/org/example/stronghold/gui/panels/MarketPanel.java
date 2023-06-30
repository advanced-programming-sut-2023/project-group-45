package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.gui.components.PopupWindow;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Market;
import org.example.stronghold.model.Player;
import org.example.stronghold.operator.Operators;

public class MarketPanel extends Panel {

    public static List<String> RESOURCES;
    Table resourceTable, economyTable;
    TextField amountField;
    TextButton buyButton, sellButton;
    private GameData gameData;
    private Market market;
    private Player player;
    private ControlPanel controlPanel;
    private PopupWindow popupWindow;
    private String resource;

    public MarketPanel(ControlPanel controlPanel) {
        super(controlPanel);
        this.controlPanel = controlPanel;
        this.gameData = controlPanel.getScreen().getGameData();
        this.market = gameData.getMarket();
        this.player = controlPanel.getScreen().getMyself();
        this.popupWindow = controlPanel.getPopup();
        create();
    }

    private void create() {
        RESOURCES = market.getPrices().keySet().stream().toList();
        resourceTable = new Table(game.skin);
        economyTable = new Table(game.skin);
        resourceTable.align(Align.left);
        economyTable.align(Align.left);
        add(resourceTable).growX().row();
        add(economyTable).grow();
        for (String resource : RESOURCES) {
            TextButton btn = new TextButton(resource, game.skin);
            btn.addListener(new SimpleChangeListener(() -> this.resource = resource));
            resourceTable.add(btn);
        }
        amountField = new TextField("0", game.skin);
        buyButton = new TextButton("Buy", game.skin);
        sellButton = new TextButton("Sell", game.skin);
        economyTable.add(amountField).align(Align.center).growX().row();
        economyTable.add(buyButton).growX();
        economyTable.add(sellButton).growX().row();
        buyButton.addListener(new SimpleChangeListener(this::buy));
        sellButton.addListener(new SimpleChangeListener(this::sell));
    }

    private Map<String, Object> buildMap() {
        return ImmutableMap.of(
            "player", player,
            "game", gameData,
            "amount", Integer.parseInt(amountField.getText()),
            "item", resource
        );
    }

    private void buy() {
        try {
            Operators.economy.buyMarketItem(buildMap());
            popupWindow.success("Success");
        } catch (Exception e) {
            popupWindow.error(e.getMessage());
        }
    }

    private void sell() {
        try {
            Operators.economy.sellMarketItem(buildMap());
            popupWindow.success("Success");
        } catch (Exception e) {
            popupWindow.error(e.getMessage());
        }
    }
}
