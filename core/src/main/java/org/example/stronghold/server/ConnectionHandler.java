package org.example.stronghold.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Data;

@Data
public class ConnectionHandler implements Runnable {

    private final int port;

    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try (ServerSocket socket = new ServerSocket(port)) {
            while (true) {
                executor.execute(new RequestHandler(this, socket.accept()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
