package org.example.stronghold.server;

import static org.example.stronghold.context.MapUtils.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class RequestHandler implements Runnable {

    private final ConnectionHandler connectionHandler;
    private final Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            handle();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void handle() throws IOException, ClassNotFoundException {
        try {
            Map<String, Object> request = (Map<String, Object>) input.readObject();
            String what = getReqString(request, "what");
            System.out.println(request);
            sendResponse(new HashMap<>() {{
                put("what", "response");
                put("data", "ok");
            }});
        } catch (IllegalArgumentException e) {
            sendError(e.getMessage());
        }
    }

    private void sendResponse(Map<String, Object> response) throws IOException {
        output.writeObject(new HashMap<>(response));
    }

    private void sendError(String message) throws IOException {
        sendResponse(new HashMap<>() {{
            put("what", "error");
            put("data", new Exception(message));
        }});
    }
}
