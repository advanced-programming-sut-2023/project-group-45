package org.example.stronghold.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.example.stronghold.model.GameData;
import org.example.stronghold.server.Server;

@Data
public class ClientTest {

    public static void main(String[] args) throws Exception {
        Connection connection = new Connection("localhost", Server.PORT);

        long gameId = (Long) connection.sendOperatorRequest(
            "game", "startGame", new HashMap<>() {{
                put("map", "test");
                put("users", List.of("parsa", "kavi"));
            }}
        );

        GameData gameData = (GameData) connection.sendObjectRequest(
            "GameData", gameId
        );

        System.out.println(gameData);
    }
}
