package org.example.stronghold.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import lombok.Data;

@Data
public class Chat implements Serializable {

    public static final Map<Long, Chat> OBJECTS = new TreeMap<>();
    private static long NEXT_ID = 0;
    private final long id = NEXT_ID++;
    private final List<Message> messages;
    private final Set<User> users;

    {
        OBJECTS.put(id, this);
    }

    public Chat() {
        messages = new ArrayList<>();
        users = new HashSet<>();
    }

    public Chat(Set<User> users) {
        this.users = users;
        messages = new ArrayList<>();
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addUsers(User user) {
        users.add(user);
    }

    public String getName() {
        return users.stream()
            .map(User::getUsername)
            .reduce((a, b) -> a + ", " + b)
            .orElse("No users");
    }
}
