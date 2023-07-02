package org.example.stronghold.server;

import lombok.Data;

@Data
public class Server {
    public static final int PORT = 2222;

    public static void main(String[] args) {
        ConnectionHandler connectionHandler = new ConnectionHandler(PORT);
        connectionHandler.run();
    }
}
