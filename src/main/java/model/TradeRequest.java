package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TradeRequest {

    private String type;
    private int amount;
    private int price;
    private String message;
}
