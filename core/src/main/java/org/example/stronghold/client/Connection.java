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

    public synchronized void open() {
        if (enabled)
            return;
        try {
            socket = new Socket(host, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            enabled = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void close() {
        if (!enabled)
            return;
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
        if (!enabled)
            throw new RuntimeException("Connection is not enabled");
        try {
            output.writeObject(new HashMap<>(request));
            return (Map<String, Object>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
