package org.example.stronghold.client;

import java.util.HashMap;
import java.util.List;
import lombok.Data;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.User;
import org.example.stronghold.server.Server;

@Data
public class ClientTest {

    public static void main(String[] args) throws Exception {
        Connection connection = new Connection("localhost", Server.PORT);

        User parsa = (User) connection.sendObjectRequest(
            "User", "parsa"
        );
        User kavi = (User) connection.sendObjectRequest(
            "User", "kavi"
        );

        long gameId = (Long) connection.sendOperatorRequest(
            "game", "startGame", new HashMap<>() {{
                put("map", "test");
                put("users", List.of(parsa, kavi));
            }}
        );

        GameData gameData = (GameData) connection.sendObjectRequest(
            "GameData", gameId
        );

        System.out.println(gameData);
    }
}
