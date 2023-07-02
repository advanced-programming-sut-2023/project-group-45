package org.example.stronghold.model;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class TradeRequest implements Serializable {

    public static final Map<Long, TradeRequest> OBJECTS = new TreeMap<>();
    private static long NEXT_ID = 0;
    private final long id = NEXT_ID++;
    @ToString.Exclude
    private Player sender;
    @ToString.Exclude
    private Player receiver;
    private String item;
    private int amount;
    private int price;
    private String message;

    {
        OBJECTS.put(id, this);
    }
}
