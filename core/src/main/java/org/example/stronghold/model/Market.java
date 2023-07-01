package org.example.stronghold.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.example.stronghold.context.IntPair;

@Data
public class Market implements Serializable {

    private final Map<String, IntPair> prices = new HashMap<>();
}
