package stronghold.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class Game implements Serializable {

    private final List<Player> players;
    private int turn = 1;
    private int playerTurn = 0;
    private Market market;

    public int getPlayerCount() {
        return players.size();
    }
}
