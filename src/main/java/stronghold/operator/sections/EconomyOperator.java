package stronghold.operator.sections;

import static stronghold.context.MapUtils.*;

import java.util.List;
import java.util.Map;
import lombok.Data;
import stronghold.model.Database;
import stronghold.model.Game;
import stronghold.model.Market;
import stronghold.model.Player;
import stronghold.model.TradeRequest;
import stronghold.operator.OperatorException;
import stronghold.operator.OperatorException.Type;
import stronghold.operator.Result;

@Data
public final class EconomyOperator {

    private final Database database;

    public Result<List<TradeRequest>> getTradeList(Map<String, Object> req) {
        return null;
    }

    public Result<TradeRequest> requestTrade(Map<String, Object> req) {
        return null;
    }

    public Result<Void> acceptTrade(Map<String, Object> req) {
        return null;
    }

    public void buyMarketItem(Map<String, Object> req) throws OperatorException {
        String item = getReqString(req, "item");
        int amount = getReqAs(req, "amount", Integer.class);
        Player player = getReqAs(req, "player", Player.class);
        Game game = getReqAs(req, "game", Game.class);
        Market market = game.getMarket();
        if (player.getGold() < amount * market.getPrices().get(item).x()) {
            throw new OperatorException(Type.NOT_ENOUGH_GOLD);
        } else {
            addIntMap(player.getResources(), item, amount);
            player.setGold(player.getGold() - amount * market.getPrices().get(item).x());
        }
    }

    public void sellMarketItem(Map<String, Object> req) throws OperatorException {
        String item = getReqString(req, "item");
        int amount = getReqAs(req, "amount", Integer.class);
        Player player = getReqAs(req, "player", Player.class);
        Game game = getReqAs(req, "game", Game.class);
        Market market = game.getMarket();
        if (player.getResources().get(item) < amount) {
            throw new OperatorException(Type.NOT_ENOUGH_RESOURCE);
        } else {
            addIntMap(player.getResources(), item, -amount);
            player.setGold(player.getGold() + amount * market.getPrices().get(item).y());
        }
    }
}
