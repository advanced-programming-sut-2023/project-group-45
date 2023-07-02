package org.example.stronghold.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class Connection {

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean enabled = false;

    private synchronized void open() throws IOException {
        if (enabled) {
            return;
        }
        socket = new Socket(host, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        enabled = true;
    }

    private synchronized void close() {
        if (!enabled) {
            return;
        }
        try {
            socket.close();
            output.close();
            input.close();
            enabled = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Map<String, Object> sendRequest(Map<String, Object> request) {
        try {
            open();
            output.writeObject(new HashMap<>(request));
            return (Map<String, Object>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }
}
