package stronghold.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class TradeRequest implements Serializable {

    private Player sender;
    private Player receiver;
    private String item;
    private int amount;
    private int price;
    private String message;
}
