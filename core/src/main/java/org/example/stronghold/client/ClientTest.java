package org.example.stronghold.client;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.example.stronghold.server.Server;

@Data
public class ClientTest {

    public static void main(String[] args) {
        Connection connection = new Connection("localhost", Server.PORT);
        connection.open();
        Map<String, Object> response = connection.sendRequest(new HashMap<>() {{
            put("what", "object");
            put("type", "User");
            put("id", "parsa");
        }});
        System.out.println(response);
        connection.close();
    }
}
