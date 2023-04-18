package model;

import lombok.Data;

@Data
public class Tile {

    private String type = "plain";
    private Building building = null;
}
