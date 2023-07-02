package org.example.stronghold.gui.panels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.gui.components.PopupWindow;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Player;
import org.example.stronghold.model.TradeRequest;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class TradePanel extends Panel {

    TextButton userListButton, incomingTradesButton, outgoingTradesButton, tradeHistoryButton;
    Table tabsTable, contentTable;

    public TradePanel(ControlPanel controlPanel) {
        super(controlPanel);
        create();
    }

    private void create() {
        tabsTable = new Table(game.skin);
        contentTable = new Table(game.skin);
        tabsTable.align(Align.left);
        contentTable.align(Align.topLeft);
        add(tabsTable).growX().row();
        add(contentTable).grow();
        userListButton = new TextButton("Users", game.skin);
        incomingTradesButton = new TextButton("Incoming", game.skin);
        outgoingTradesButton = new TextButton("Outgoing", game.skin);
        tradeHistoryButton = new TextButton("History", game.skin);
        tabsTable.add(userListButton).growX();
        tabsTable.add(incomingTradesButton).growX();
        tabsTable.add(outgoingTradesButton).growX();
        tabsTable.add(tradeHistoryButton).growX();
        userListButton.addListener(new SimpleChangeListener(this::showUserList));
        incomingTradesButton.addListener(new SimpleChangeListener(this::showIncomingTrades));
        outgoingTradesButton.addListener(new SimpleChangeListener(this::showOutgoingTrades));
        tradeHistoryButton.addListener(new SimpleChangeListener(this::showHistory));
    }

    private void showUserList() {
        contentTable.clearChildren();
        int i = 0;
        for (Player player : screen.gameData.getPlayers()) {
            if (player.getId() != screen.getMyself().getId()) {
                TextButton btn = new TextButton(player.getUser().getUsername(), game.skin);
                btn.addListener(new SimpleChangeListener(() -> {
                    showNewTrade(player);
                }));
                contentTable.add(btn).growX();
            }
            i++;
            if (i == 4) {
                contentTable.row();
                i = 0;
            }
        }
    }

    public Player getPlayer() {
        return screen.getMyself();
    }

    private void showNewTrade(Player player) {
        contentTable.clearChildren();
        TextButton acceptButton = new TextButton("Accept", game.skin);
        TextButton backButton = new TextButton("Back", game.skin);
        TextField amountField = new TextField("Amount", game.skin);
        TextField priceField = new TextField("Price", game.skin);
        TextField itemField = new TextField("Item", game.skin);
        TextField messageField = new TextField("Message", game.skin);
        contentTable.add(itemField).growX();
        contentTable.add(amountField).growX().row();
        contentTable.add(priceField).growX();
        contentTable.add(messageField).growX().row();
        contentTable.add(backButton).growX();
        contentTable.add(acceptButton).growX().row();
        backButton.addListener(new SimpleChangeListener(this::showUserList));
        acceptButton.addListener(new SimpleChangeListener(() -> {
            try {
                Map<String, Object> req = ImmutableMap.of(
                    "player", getPlayer(),
                    "game", screen.gameData,
                    "amount", Integer.parseInt(amountField.getText()),
                    "item", itemField.getText().toLowerCase(),
                    "target", player,
                    "message", messageField.getText(),
                    "price", Integer.parseInt(priceField.getText())
                );
                Operators.economy.requestTrade(req);
                controlPanel.popup.success("Success");
            } catch (Exception e) {
                controlPanel.popup.error(e.getMessage());
            }
        }));
    }

    private void showIncomingTrades() {
        contentTable.clearChildren();
        for (TradeRequest tradeRequest : getPlayer().getIncomingTradeRequests()) {
            Label playerName = new Label(tradeRequest.getSender().getUser().getUsername(),
                game.skin);
            Label item = new Label(tradeRequest.getItem(), game.skin);
            Label amount = new Label("Amount: " + tradeRequest.getAmount(), game.skin);
            Label price = new Label("Price: " + tradeRequest.getPrice(), game.skin);
            Label message = new Label(tradeRequest.getMessage(), game.skin);
            TextButton accept = new TextButton("Accept", game.skin);
            TextButton reject = new TextButton("Reject", game.skin);
            contentTable.add(item).growX();
            contentTable.add(amount).growX();
            contentTable.add(price).growX();
            contentTable.add(accept).growX();
            contentTable.add(reject).growX().row();
            contentTable.add(playerName).growX();
            contentTable.add(message).growX().row();
            accept.addListener(new SimpleChangeListener(() -> {
                try {
                    Operators.economy.acceptTrade(ImmutableMap.of("request", tradeRequest));
                    controlPanel.popup.success("Success");
                } catch (OperatorException e) {
                    controlPanel.popup.error(e.getMessage());
                }
            }));
            reject.addListener(new SimpleChangeListener(() -> {
                try {
                    Operators.economy.deleteTrade(ImmutableMap.of("request", tradeRequest));
                    controlPanel.popup.success("Success");
                } catch (OperatorException e) {
                    controlPanel.popup.error(e.getMessage());
                }
            }));
        }
    }

    private void showOutgoingTrades() {
        contentTable.clearChildren();
        for (TradeRequest tradeRequest : getPlayer().getActiveTradeRequests()) {
            Label playerName = new Label(tradeRequest.getSender().getUser().getUsername(),
                game.skin);
            Label item = new Label(tradeRequest.getItem(), game.skin);
            Label amount = new Label("Amount: " + tradeRequest.getAmount(), game.skin);
            Label price = new Label("Price: " + tradeRequest.getPrice(), game.skin);
            Label message = new Label(tradeRequest.getMessage(), game.skin);
            TextButton cancel = new TextButton("Cancel", game.skin);
            contentTable.add(item).growX();
            contentTable.add(amount).growX();
            contentTable.add(price).growX();
            contentTable.add(cancel).growX().row();
            contentTable.add(playerName).growX();
            contentTable.add(message).growX().row();
            cancel.addListener(new SimpleChangeListener(() -> {
                try {
                    Operators.economy.deleteTrade(ImmutableMap.of("request", tradeRequest));
                    controlPanel.popup.success("Success");
                } catch (OperatorException e) {
                    controlPanel.popup.error(e.getMessage());
                }
            }));
        }
    }

    private void showHistory() {
        contentTable.clearChildren();
        for (TradeRequest tradeRequest : getPlayer().getSuccessfulTradeRequests()) {
            Label playerName = new Label(tradeRequest.getSender().getUser().getUsername(),
                game.skin);
            Label item = new Label(tradeRequest.getItem(), game.skin);
            Label amount = new Label("Amount: " + tradeRequest.getAmount(), game.skin);
            Label price = new Label("Price: " + tradeRequest.getPrice(), game.skin);
            Label message = new Label(tradeRequest.getMessage(), game.skin);
            contentTable.add(item).growX();
            contentTable.add(amount).growX();
            contentTable.add(price).growX();
            contentTable.add(playerName).growX();
            contentTable.add(message).growX().row();
        }
    }
}
