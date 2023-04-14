package model;

public class GameMap {
    private final int height, width;
    private final Tile[][] map;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Tile[][] getMap() {
        return map;
    }

    GameMap(int height, int width) {
        this.height = height;
        this.width = width;
        map = new Tile[height][width];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                map[i][j] = new Tile();
            }
        }
    }
}
