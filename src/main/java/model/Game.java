package model;

import java.util.List;
import lombok.Data;

@Data
public class Game {

    private final List<Player> players;
    private int turn = 1;
    private int playerTurn = 0;

    public int getPlayerCount() {
        return players.size();
    }
}
