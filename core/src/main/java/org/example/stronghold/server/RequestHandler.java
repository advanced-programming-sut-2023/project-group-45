package org.example.stronghold.server;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.example.stronghold.context.MapUtils.getReq;
import static org.example.stronghold.context.MapUtils.getReqString;

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
            if (what.equals("object")) {
                handleObject(request);
                return;
            }
            sendError("Unknown request: " + what);
        } catch (RuntimeException e) {
            sendError(e.getMessage());
        }
    }

    private void handleObject(Map<String, Object> req) throws IOException {
        String type = getReqString(req, "type");
        Object id = getReq(req, "id");
        Object object = Decoder.decodeWithId(type, id);
        checkNotNull(object, "no such object");
        sendResponse(new HashMap<>() {{
            put("what", "object");
            put("data", object);
        }});
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
