package model;

public class Tile {
    private String type;
    private Building building;

    public Tile() {
        this.type = "plain";
        this.building = null;
    }

    public Tile(String type) {
        this.type = type;
        this.building = null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
