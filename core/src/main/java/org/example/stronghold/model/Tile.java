package org.example.stronghold.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class Tile implements Serializable {

    private String type = "plain";
    private Building building = null;
}
