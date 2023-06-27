package org.example.stronghold.gui.sections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import org.example.stronghold.gui.StrongholdGame;
import org.example.stronghold.model.*;

import java.util.Random;

public class TestMapScreen implements Screen {
    final StrongholdGame game;
    TiledMap tiledMap;
    IsometricTiledMapRenderer renderer;
    OrthographicCamera camera;
    static final int tilePerUnit = 4;
    GameData gameData;
    GameMap gameMap;
    final Random tileRandomizer = new Random(42);

    public TestMapScreen(StrongholdGame game, GameData gameData) {
        this.game = game;
        this.gameData = gameData;
        this.gameMap = gameData.getMap();
    }

    public void setTileAt(int column, int row, int id) {
        TiledMapTileSets tileSets = tiledMap.getTileSets();
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tileSets.getTile(id));
        tileLayer.setCell(row, column, cell); // xy coords in TiledMapTileLayer isn't the same as GameMap
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
        tiledMap = game.assetLoader.getTiledMap("tiled-maps/80x80.tmx");

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
        for (int col = gameMap.getWidth() - 1; col >= 0; col--) { // back to front
            for (int row = 0; row < gameMap.getHeight(); row++) {
                Tile tile = gameMap.getAt(col, row);
                if (tile.getType().startsWith("tree"))
                    drawTreeAt(batch, game.assetLoader.getTexture("plants/oak.png"), col, row);
                if (tile.getBuilding() != null) {
                    drawBuildingAt(batch, tile.getBuilding(), col, row);
                }
            }
        }
        batch.end();
    }

    public Vector3 vec3AtSubCell(int column, int row, int i, int j) {
        return new Vector3(
            15f * (tilePerUnit * (column + row) + i + j),
            8f * (tilePerUnit * (column - row) + i - j),
            0
        );
    }

    public Vector3 vec3AtCell(int column, int row) {
        return vec3AtSubCell(column, row, 0, 0).sub(0, 8f * (tilePerUnit - 1), 0);
    }

    public void drawBuildingAt(Batch batch, Building building, int column, int row) {
        GuiSetting guiSetting = building.getGuiSetting();
        if (guiSetting.getAsset() == null)
            return;
        Texture texture = game.assetLoader.getTexture(guiSetting.getAsset());
        float width = guiSetting.getPrefWidth();
        Vector3 position = vec3AtCell(column, row);
        batch.draw(
            texture,
            position.x + guiSetting.getOffsetX(), position.y + guiSetting.getOffsetY(),
            width,
            texture.getHeight() * width / texture.getWidth()
        );
    }

    public void drawTreeAt(Batch batch, Texture texture, int column, int row) {
        float width = 60f;
        float height = texture.getHeight() * width / texture.getWidth();
        for (int i = tilePerUnit - 2; i >= 0; i -= 2) { // back to front
            for (int j = 0; j < tilePerUnit; j += 2) {
                Vector3 position = vec3AtSubCell(column, row, i, j);
                batch.draw(
                    texture,
                    position.x + 10f,
                    position.y,
                    width,
                    height
                );
            }
        }
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
    }
}
