package org.example.stronghold.model;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import lombok.Data;

@Data
public class Message implements Serializable {

    public static final Map<Long, Message> OBJECTS = new TreeMap<>();
    private static long NEXT_ID = 0;
    private final long id = NEXT_ID++;
    private final User sender;
    private final long timestamp;
    private String content;
    private boolean seen;

    {
        OBJECTS.put(id, this);
    }

    public Message(User sender, String content) {
        this.sender = sender;
        this.content = content;
        this.seen = false;
        timestamp = System.currentTimeMillis();
    }

    public String getDate() {
        return new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(timestamp));
    }
}
