package stronghold.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TradeRequest implements Serializable {

    private String type;
    private int amount;
    private int price;
    private String message;
}
