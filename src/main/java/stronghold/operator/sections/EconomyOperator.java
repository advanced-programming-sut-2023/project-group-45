package stronghold.operator.sections;

import static stronghold.context.MapUtils.*;
import static stronghold.operator.OperatorPreconditions.checkExpression;

import java.util.Map;
import lombok.Data;
import stronghold.model.Database;
import stronghold.model.Game;
import stronghold.model.Market;
import stronghold.model.Player;
import stronghold.model.TradeRequest;
import stronghold.operator.OperatorException;
import stronghold.operator.OperatorException.Type;

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
        checkExpression(player.getResources().get(item) < amount, Type.NOT_ENOUGH_RESOURCE);
        TradeRequest tradeRequest = new TradeRequest(player, target, item, amount, price, message);
        addIntMap(player.getResources(), item, -amount);
        player.getActiveTradeRequests().add(tradeRequest);
        target.getIncomingTradeRequests().add(tradeRequest);
    }

    public void acceptTrade(Map<String, Object> req) throws OperatorException {
        TradeRequest tradeRequest = getReqAs(req, "request", TradeRequest.class);
        Player player = tradeRequest.getReceiver();
        Player sender = tradeRequest.getSender();
        checkExpression(player.getGold() < tradeRequest.getPrice(), Type.NOT_ENOUGH_GOLD);
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
        Game game = getReqAs(req, "game", Game.class);
        Market market = game.getMarket();
        checkExpression(player.getGold() < amount * market.getPrices().get(item).x(), Type.NOT_ENOUGH_GOLD);
        addIntMap(player.getResources(), item, amount);
        player.setGold(player.getGold() - amount * market.getPrices().get(item).x());
    }

    public void sellMarketItem(Map<String, Object> req) throws OperatorException {
        String item = getReqString(req, "item");
        int amount = getReqAs(req, "amount", Integer.class);
        Player player = getReqAs(req, "player", Player.class);
        Game game = getReqAs(req, "game", Game.class);
        Market market = game.getMarket();
        checkExpression(player.getResources().get(item) < amount, Type.NOT_ENOUGH_RESOURCE);
        addIntMap(player.getResources(), item, -amount);
        player.setGold(player.getGold() + amount * market.getPrices().get(item).y());
    }
}
