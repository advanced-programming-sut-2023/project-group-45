package org.example.stronghold.cli.sections;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.example.stronghold.context.MapUtils.copyOptTo;
import static org.example.stronghold.context.MapUtils.getIntOpt;
import static org.example.stronghold.context.MapUtils.getIntPairOpt;
import static org.example.stronghold.context.MapUtils.getOpt;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.example.stronghold.context.IntPair;
import org.example.stronghold.context.ListPair;
import org.example.stronghold.model.Game;
import org.example.stronghold.model.Player;
import org.example.stronghold.model.TradeRequest;
import org.example.stronghold.model.Unit;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;
import org.example.stronghold.cli.Menu;

public class TurnMenu extends Menu {

    private final Game game;
    private final Player player;

    public TurnMenu(Scanner scanner, Game game, Player player) {
        super(scanner);
        this.game = game;
        this.player = player;
        addCommand("who-am-i", this::whoAmI);
        addCommand("show-info", this::showInfo);
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
        addCommand("map-view", this::mapView);
        addCommand("show-food-list", this::showFoodList);
        addCommand("food-rate", this::setFoodRate);
        addCommand("show-food-rate", this::showFoodRate);
        addCommand("tax-rate", this::setTaxRate);
        addCommand("show-tax-rate", this::showTaxRate);
        addCommand("show-popularity", this::showPopularity);
        addCommand("unit-menu", this::unitMenu);
        addCommand("drop-unit", this::dropUnit);
        addCommand("build-equipment", this::buildEquipment);
        addCommand("repair-building", this::repairBuilding);
    }

    private void whoAmI(Map<String, String> input) {
        System.out.println("You are " + player.getUser().getUsername());
    }

    private void showInfo(Map<String, String> input) {
        ListPair<String, String> info = new ListPair<>() {{
            put("Resources", "" + player.getResources());
            put("Peasants", "" + player.getPeasants());
            put("Religion", "" + game.getReligion(player));
            put("Happiness", "" + game.getHappiness(player));
            put("Population", "" + game.getTotalPeasants(player));
            put("Housing", "" + game.getHousingSpace(player));
            put("Food rate", "" + player.getFoodRate());
            put("Tax rate", "" + player.getTaxRate());
            put("Gold", "" + player.getGold());
            put("Popularity", "" + player.getPopularity());
        }};
        info.forEach((k, v) -> System.out.printf("%15s: %s\n", k, v));
    }

    private void dropBuilding(Map<String, String> input) {
        String type = getOpt(input, "type");
        IntPair position = getIntPairOpt(input, "x", "y");
        try {
            Operators.game.dropBuilding(new HashMap<>() {{
                put("game", game);
                put("player", player);
                put("building", type);
                put("position", position);
            }});
            System.out.println("Building added successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showPriceList(Map<String, String> input) {
        if (game.getMarket().getPrices().isEmpty()) {
            System.out.println("No items in the market");
            return;
        }
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

    private void mapView(Map<String, String> input) {
        System.out.println("Switched to map view");
        new MapViewMenu(scanner, game).run();
    }

    private void showFoodList(Map<String, String> input) {
        player.getResources().forEach((k, v) -> {
            if (Game.FOODS.contains(k)) {
                System.out.println(" - " + k + ": " + v);
            }
        });
    }

    private void setFoodRate(Map<String, String> input) {
        int rate = getIntOpt(input, "rate");
        try {
            Operators.game.setFoodRate(new HashMap<>() {{
                put("player", player);
                put("rate", rate);
            }});
            System.out.println("Food rate updated successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showFoodRate(Map<String, String> input) {
        System.out.printf("Food rate is: %d\n", player.getFoodRate());
    }

    private void setTaxRate(Map<String, String> input) {
        int rate = getIntOpt(input, "rate");
        try {
            Operators.game.setTaxRate(new HashMap<>() {{
                put("player", player);
                put("rate", rate);
            }});
            System.out.println("Tax rate updated successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showTaxRate(Map<String, String> input) {
        System.out.printf("Tax rate is: %d\n", player.getTaxRate());
    }

    private void showPopularity(Map<String, String> input) {
        ListPair<String, String> info = new ListPair<>() {{
            put("Food", "" + player.getFoodDeltaPopularity());
            put("Tax", "" + player.getTaxDeltaPopularity());
            put("Religion", "" + game.getReligion(player));
            put("Happiness", "" + game.getHappiness(player));
            put("Delta popularity", "" + player.getDeltaPopularity());
            put("Total popularity", "" + player.getPopularity());
        }};
        System.out.println("Factors:");
        info.forEach((k, v) -> System.out.printf(" - %20s: %s\n", k, v));
    }

    private void unitMenu(Map<String, String> input) {
        System.out.println("Switched to unit menu");
        new UnitMenu(scanner, game, player).run();
    }

    private void dropUnit(Map<String, String> input) {
        IntPair position = getIntPairOpt(input, "x", "y");
        int count = (input.containsKey("count") ? getIntOpt(input, "count") : 1);
        Map<String, Object> req = new HashMap<>() {{
            put("game", game);
            put("player", player);
            put("position", position);
            copyOptTo(input, this, "type");
        }};
        try {
            for (int i = 0; i < count; i++) {
                Operators.game.dropUnit(req);
            }
            System.out.println("Units dropped successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void buildEquipment(Map<String, String> input) {
        IntPair position = getIntPairOpt(input, "x", "y");
        try {
            Unit unit = Operators.game.buildEquipment(new HashMap<>() {{
                put("game", game);
                put("player", player);
                put("position", position);
                copyOptTo(input, this, "type");
            }});
            System.out.println("Equipment " + unit.getType() +
                    " built at (" + position.x() + "," + position.y() + ")");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void repairBuilding(Map<String, String> input) {
        IntPair position = getIntPairOpt(input, "x", "y");
        try {
            Operators.game.repairBuilding(new HashMap<>() {{
                put("game", game);
                put("player", player);
                put("position", position);
            }});
            System.out.println("Building at (" + position.x() + "," + position.y() + ") repaired");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }
}

