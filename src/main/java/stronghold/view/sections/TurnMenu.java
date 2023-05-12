package stronghold.view.sections;

import static com.google.common.base.Preconditions.checkNotNull;
import static stronghold.context.MapUtils.getIntOpt;
import static stronghold.context.MapUtils.getOpt;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import stronghold.context.IntPair;
import stronghold.model.Game;
import stronghold.model.Player;
import stronghold.model.TradeRequest;
import stronghold.operator.OperatorException;
import stronghold.operator.Operators;
import stronghold.view.Menu;

public class TurnMenu extends Menu {

    private final Game game;
    private final Player player;

    public TurnMenu(Scanner scanner, Game game, Player player) {
        super(scanner);
        this.game = game;
        this.player = player;
        addCommand("who-am-i", this::whoAmI);
        addCommand("show-resources", this::showResources);
        addCommand("drop-building", this::dropBuilding);
        addCommand("show-price-list", this::showPriceList);
        addCommand("buy-item", this::buyItem);
        addCommand("sell-item", this::sellItem);
        addCommand("request-trade", this::requestTrade);
        addCommand("accept-trade", this::acceptTrade);
        addCommand("reject-trade", this::rejectTrade);
        addCommand("cancel-trade", this::cancelTrade);
        addCommand("list-trade-requests", this::listTradeRequests);
        addCommand("trade-history", this::tradeHistory);
    }

    private void whoAmI(Map<String, String> input) {
        System.out.println("You are " + player.getUser().getUsername());
    }

    private void showResources(Map<String, String> input) {
        // todo: only for testing purposes
        System.out.println("Your resources: " + player.getResources());
    }

    private void dropBuilding(Map<String, String> input) {
        String type = getOpt(input, "type");
        int x = getIntOpt(input, "x");
        int y = getIntOpt(input, "y");
        try {
            Operators.game.dropBuilding(new HashMap<>() {{
                put("game", game);
                put("player", player);
                put("building", type);
                put("position", new IntPair(x, y));
            }});
            System.out.println("Building added successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showPriceList(Map<String, String> input) {
        for (String item : game.getMarket().getPrices().keySet()) {
            IntPair price = game.getMarket().getPrices().get(item);
            System.out.println("Name       Buy       Sell");
            System.out.printf("%10s%10d%10d", item, price.x(), price.y());
        }
    }

    private void buyItem(Map<String, String> input) {
        String item = getOpt(input, "item");
        Integer amount = getIntOpt(input, "amount");
        try {
            Operators.economy.buyMarketItem(new HashMap<>() {{
                put("game", game);
                put("player", player);
                put("item", item);
                put("amount", amount);
            }});
            System.out.println("Item bought successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sellItem(Map<String, String> input) {
        String item = getOpt(input, "item");
        Integer amount = getIntOpt(input, "amount");
        try {
            Operators.economy.sellMarketItem(new HashMap<>() {{
                put("game", game);
                put("player", player);
                put("item", item);
                put("amount", amount);
            }});
            System.out.println("Item sold successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void requestTrade(Map<String, String> input) {
        String item = getOpt(input, "item");
        Integer amount = getIntOpt(input, "amount");
        Integer price = getIntOpt(input, "price");
        String message = getOpt(input, "message");
        Player target = getPlayerByUsername(getOpt(input, "target"));
        try {
            Operators.economy.requestTrade(new HashMap<>() {{
                put("game", game);
                put("player", player);
                put("item", item);
                put("amount", amount);
                put("price", price);
                put("message", message);
                put("target", target);
            }});
            System.out.println("Trade request sent successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listTradeRequests(Map<String, String> input) {
        System.out.println("Incoming trade requests:");
        int id = 1;
        for (TradeRequest tradeRequest : player.getIncomingTradeRequests()) {
            System.out.printf("%d. %s\n", id++, tradeRequest);
        }
        if (id == 1) {
            System.out.println("No incoming trade requests");
        }
        id = 1;
        System.out.println("Active trade requests:");
        for (TradeRequest tradeRequest : player.getActiveTradeRequests()) {
            System.out.printf("%d. %s\n", id++, tradeRequest);
        }
        if (id == 1) {
            System.out.println("No active trade requests");
        }
    }

    private void tradeHistory(Map<String, String> input) {
        System.out.println("Trade history:");
        int id = 1;
        for (TradeRequest tradeRequest : player.getSuccessfulTradeRequests()) {
            System.out.printf("%d. %s\n", id++, tradeRequest);
        }
        if (id == 1) {
            System.out.println("No trade history");
        }
    }

    private void acceptTrade(Map<String, String> input) {
        int id = getIntOpt(input, "id");
        try {
            Operators.economy.acceptTrade(new HashMap<>() {{
                put("request", checkNotNull(player.getIncomingTradeRequests().get(id - 1),
                        "Trade request " + id + " not found"));
            }});
            System.out.println("Trade request accepted successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void rejectTrade(Map<String, String> input) {
        int id = getIntOpt(input, "id");
        try {
            Operators.economy.deleteTrade(new HashMap<>() {{
                put("request", checkNotNull(player.getIncomingTradeRequests().get(id - 1),
                        "Trade request " + id + " not found"));
            }});
            System.out.println("Trade request rejected successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cancelTrade(Map<String, String> input) {
        int id = getIntOpt(input, "id");
        try {
            Operators.economy.deleteTrade(new HashMap<>() {{
                put("request", checkNotNull(player.getActiveTradeRequests().get(id - 1),
                        "Trade request " + id + " not found"));
            }});
            System.out.println("Trade request cancelled successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private Player getPlayerByUsername(String username) {
        return checkNotNull(game.getPlayers().stream()
                .filter(player -> player.getUser().getUsername().equals(username))
                .findFirst().orElse(null), "Player " + username + " not found");
    }
}




