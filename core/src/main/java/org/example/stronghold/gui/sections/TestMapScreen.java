package org.example.stronghold.gui.sections;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import org.example.stronghold.model.GameMap;
import org.example.stronghold.model.Tile;

import java.util.Random;

public class TestMapScreen implements Screen {
    final Game game;
    TiledMap tiledMap;
    IsometricTiledMapRenderer renderer;
    OrthographicCamera camera;
    Texture barracks;
    TiledMapTileSets tileSets;
    TiledMapTileLayer tileLayer;
    static final int tilePerUnit = 4;
    GameMap gameMap;
    final Random tileRandomizer = new Random(42);

    public TestMapScreen(Game game, GameMap gameMap) {
        this.game = game;
        this.gameMap = gameMap;
    }

    public void setTileAt(int column, int row, int id) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tileSets.getTile(id));
        tileLayer.setCell(column, row, cell);
    }

    public int getTileIdByType(String tileType) {
        if (tileType.equals("farmland"))
            return 1090 + tileRandomizer.nextInt(1161, 1309);
        if (tileType.equals("water"))
            return 1 + tileRandomizer.nextInt(649, 742);
        if (tileType.equals("oil"))
            return 1090 + tileRandomizer.nextInt(37, 148);
        if (tileType.equals("plain"))
            return 1090 + tileRandomizer.nextInt(908, 1036);
        if (tileType.equals("rock"))
            return 1090 + tileRandomizer.nextInt(148, 288);
        if (tileType.equals("iron"))
            return 2459 + tileRandomizer.nextInt(414, 491);
        if (tileType.equals("stone"))
            return 2459 + tileRandomizer.nextInt(0, 69);
        if (tileType.equals("tree"))
            return 1090 + tileRandomizer.nextInt(444, 592);
        return 149; // total white as unknown tile
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.zoom = 1;
        tiledMap = new TmxMapLoader().load("tiled-maps/80x80.tmx");
        tileSets = tiledMap.getTileSets();
        tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);

        for (int col = 0; col < gameMap.getWidth(); col++) {
            for (int row = 0; row < gameMap.getHeight(); row++) {
                Tile tile = gameMap.getAt(col, row);
                for (int i = 0; i < tilePerUnit; i++) {
                    for (int j = 0; j < tilePerUnit; j++) {
                        setTileAt(tilePerUnit * col + i, tilePerUnit * row + j, getTileIdByType(tile.getType()));
                    }
                }
            }
        }

        renderer = new IsometricTiledMapRenderer(tiledMap);
        renderer.setView(camera);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                camera.zoom += amountY * 0.1f;
                camera.zoom = Math.min(Math.max(camera.zoom, 0.5f), 1);
                return false;
            }
        });

        barracks = new Texture(Gdx.files.internal("buildings/Barracks.png"));
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            float scale = camera.zoom * 2;
            camera.translate(-Gdx.input.getDeltaX() * scale, Gdx.input.getDeltaY() * scale);
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();

        Batch batch = renderer.getBatch();
        batch.begin();
        drawTextureAt(batch, barracks, 4, 2);
        drawTextureAt(batch, barracks, 3, 1);
        drawTextureAt(batch, barracks, 3, 3);
        batch.end();
    }

    public Vector3 vec3AtCell(int column, int row) {
        return new Vector3(15f * tilePerUnit * (column + row), 8f * tilePerUnit * (column - row) - 8f * (tilePerUnit - 1), 0);
    }

    public void drawTextureAt(Batch batch, Texture texture, int column, int row) {
        float margin = 10f;
        float width = 30f * tilePerUnit - 2 * margin;
        Vector3 position = vec3AtCell(column, row);
        batch.draw(
            texture,
            position.x + margin, position.y + margin / 2,
            width,
            texture.getHeight() * width / texture.getWidth()
        );
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        renderer.setView(camera);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        renderer.dispose();
        tiledMap.dispose();
    }
}
