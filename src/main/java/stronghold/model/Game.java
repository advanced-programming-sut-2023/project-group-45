package stronghold.model;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import stronghold.model.template.GameMapTemplate;

@Data
public class Game implements Serializable {

    private final List<Player> players;
    private int turn = 1;
    private int playerTurn = 0;
    private Market market;

    @Builder(toBuilder = true)
    public Game(List<User> users, GameMapTemplate gameMapTemplate) {
        // todo
        players = null;
    }

    public int getPlayerCount() {
        return players.size();
    }
}
