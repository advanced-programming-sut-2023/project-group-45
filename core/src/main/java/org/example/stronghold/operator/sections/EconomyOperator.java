package org.example.stronghold.operator.sections;

import static org.example.stronghold.context.MapUtils.addIntMap;
import static org.example.stronghold.context.MapUtils.getReqAs;
import static org.example.stronghold.context.MapUtils.getReqString;
import static org.example.stronghold.operator.OperatorPreconditions.checkExpression;

import java.util.Map;
import lombok.Data;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.Market;
import org.example.stronghold.model.Player;
import org.example.stronghold.model.TradeRequest;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.OperatorException.Type;

@Data
public final class EconomyOperator {

    private final Database database;

    public void requestTrade(Map<String, Object> req) throws OperatorException {
        Player player = getReqAs(req, "player", Player.class);
        Player target = getReqAs(req, "target", Player.class);
        String item = getReqString(req, "item");
        int amount = getReqAs(req, "amount", Integer.class);
        int price = getReqAs(req, "price", Integer.class);
        String message = getReqString(req, "message");
        checkExpression(amount > 0 && price >= 0, Type.INVALID_GAME_PARAMETERS);
        checkExpression(player.getResources().get(item) >= amount, Type.NOT_ENOUGH_RESOURCE);
        TradeRequest tradeRequest = new TradeRequest(player, target, item, amount, price, message);
        addIntMap(player.getResources(), item, -amount);
        player.getActiveTradeRequests().add(tradeRequest);
        target.getIncomingTradeRequests().add(tradeRequest);
    }

    public void acceptTrade(Map<String, Object> req) throws OperatorException {
        TradeRequest tradeRequest = getReqAs(req, "request", TradeRequest.class);
        Player player = tradeRequest.getReceiver();
        Player sender = tradeRequest.getSender();
        checkExpression(player.getGold() >= tradeRequest.getPrice(), Type.NOT_ENOUGH_GOLD);
        addIntMap(player.getResources(), tradeRequest.getItem(), tradeRequest.getAmount());
        player.setGold(player.getGold() - tradeRequest.getPrice());
        sender.setGold(tradeRequest.getSender().getGold() + tradeRequest.getPrice());
        player.getIncomingTradeRequests().remove(tradeRequest);
        sender.getActiveTradeRequests().remove(tradeRequest);
        player.getSuccessfulTradeRequests().add(tradeRequest);
        sender.getSuccessfulTradeRequests().add(tradeRequest);
    }

    public void deleteTrade(Map<String, Object> req) throws OperatorException {
        TradeRequest tradeRequest = getReqAs(req, "request", TradeRequest.class);
        Player player = tradeRequest.getReceiver();
        Player sender = tradeRequest.getSender();
        player.getIncomingTradeRequests().remove(tradeRequest);
        sender.getActiveTradeRequests().remove(tradeRequest);
        addIntMap(sender.getResources(), tradeRequest.getItem(), tradeRequest.getAmount());
    }

    public void buyMarketItem(Map<String, Object> req) throws OperatorException {
        String item = getReqString(req, "item");
        int amount = getReqAs(req, "amount", Integer.class);
        Player player = getReqAs(req, "player", Player.class);
        GameData gameData = getReqAs(req, "game", GameData.class);
        Market market = gameData.getMarket();
        checkExpression(player.getGold() < amount * market.getPrices().get(item).x(),
                Type.NOT_ENOUGH_GOLD);
        addIntMap(player.getResources(), item, amount);
        player.setGold(player.getGold() - amount * market.getPrices().get(item).x());
    }

    public void sellMarketItem(Map<String, Object> req) throws OperatorException {
        String item = getReqString(req, "item");
        int amount = getReqAs(req, "amount", Integer.class);
        Player player = getReqAs(req, "player", Player.class);
        GameData gameData = getReqAs(req, "game", GameData.class);
        Market market = gameData.getMarket();
        checkExpression(player.getResources().get(item) < amount, Type.NOT_ENOUGH_RESOURCE);
        addIntMap(player.getResources(), item, -amount);
        player.setGold(player.getGold() + amount * market.getPrices().get(item).y());
    }
}
