package org.example.stronghold.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.example.stronghold.server.Server;

@Data
public class ClientTest {

    public static void main(String[] args) {
        Connection connection = new Connection("localhost", Server.PORT);
        Map<String, Object> response = connection.sendRequest(new HashMap<>() {{
            put("what", "game");
            put("method", "startGame");
            put("data", Map.of(
                "map", "test",
                "users", List.of("parsa", "kavi")
            ));
        }});
        System.out.println(response);

        response = connection.sendRequest(new HashMap<>() {{
            put("what", "game");
            put("method", "nextFrame");
            put("data", Map.of(
                "game", 0L
            ));
        }});
        System.out.println(response);
    }
}
