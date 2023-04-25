package stronghold.operator.sections;

import java.util.List;
import java.util.Map;
import lombok.Data;
import stronghold.model.Market;
import stronghold.model.TradeRequest;
import stronghold.model.Database;
import stronghold.operator.Result;

@Data
public final class EconomyOperator {
    private final Database database;

    public static Result<List<TradeRequest>> getTradeList(Map<Object, String> req) {
        return null;
    }

    public static Result<TradeRequest> requestTrade(Map<Object, String> req) {
        return null;
    }

    public static Result<Void> acceptTrade(Map<Object, String> req) {
        return null;
    }

    public static Result<Market> getMarket(Map<Object, String> req) {
        return null;
    }

    public static Result<Void> buyMarketItem(Map<Object, String> req) {
        return null;
    }

    public static Result<Void> sellMarketItem(Map<Object, String> req) {
        return null;
    }
}
