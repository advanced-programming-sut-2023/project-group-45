package org.example.stronghold.server;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.example.stronghold.context.MapUtils.getReq;
import static org.example.stronghold.context.MapUtils.getReqAs;
import static org.example.stronghold.context.MapUtils.getReqString;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.example.stronghold.client.Encoder;
import org.example.stronghold.operator.Operators;

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
            synchronized (Operators.class) {
                if (what.equals("object")) {
                    handleObject(request);
                    return;
                }
                handlerOperator(request);
            }
        } catch (RuntimeException e) {
            sendError(e.getMessage());
        }
    }

    private void handleObject(Map<String, Object> req) throws IOException {
        String type = getReqString(req, "type");
        Object id = getReq(req, "id");
        System.out.println(type + " / " + id);
        Object object = Decoder.decodeWithId(type, id);
        checkNotNull(object, "no such object");
        sendResponse(new HashMap<>() {{
            put("what", "object");
            put("data", object);
        }});
    }

    private void handlerOperator(Map<String, Object> req) throws IOException {
        String what = getReqString(req, "what");
        String methodName = getReqString(req, "method");
        Map<String, Object> opReq = new HashMap<>(getReqAs(req, "data", Map.class));
        if (what.matches("^.*[dD]atabase.*$")) {
            throw new RuntimeException("invalid operator or method");
        }
        try {
            Decoder.decodeOperatorRequest(opReq);
        } catch (RuntimeException e) {
            throw new RuntimeException("decoder failed");
        }
        try {
            Object operator = Operators.class.getField(what).get(Operators.class);
            Method method = operator.getClass().getMethod(methodName, Map.class);
            Object result = Encoder.encodeIntoIdOrDefault(method.invoke(operator, opReq));
            sendResponse(new HashMap<>() {{
                put("what", "ok");
                put("data", result);
            }});
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("invalid operator");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("invalid method");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("illegal access");
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception exception) {
                sendError(exception);
            } else {
                sendError("unknown error");
            }
        }
    }

    private void sendResponse(Map<String, Object> response) throws IOException {
        output.writeObject(new HashMap<>(response));
    }

    private void sendError(String message) throws IOException {
        sendResponse(new HashMap<>() {{
            put("what", "error");
            put("message", message);
        }});
    }

    private void sendError(Exception exception) throws IOException {
        sendResponse(new HashMap<>() {{
            put("what", "error");
            put("data", exception);
        }});
    }
}
