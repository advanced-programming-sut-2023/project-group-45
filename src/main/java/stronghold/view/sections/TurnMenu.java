package stronghold.view.sections;

import static stronghold.context.MapUtils.getIntOpt;
import static stronghold.context.MapUtils.getOpt;

import java.nio.file.LinkPermission;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import stronghold.context.IntPair;
import stronghold.model.Game;
import stronghold.model.Market;
import stronghold.model.Player;
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
            Operators.economy.buyMarketItem(new HashMap<String, Object>() {{
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
            Operators.economy.sellMarketItem(new HashMap<String, Object>() {{
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
}


