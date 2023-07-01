package org.example.stronghold.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class TradeRequest implements Serializable {

    @ToString.Exclude
    private Player sender;
    @ToString.Exclude
    private Player receiver;
    private String item;
    private int amount;
    private int price;
    private String message;
}
