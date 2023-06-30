package org.example.stronghold.gui.sections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import org.example.stronghold.context.IntPair;
import org.example.stronghold.gui.StrongholdGame;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.panels.BuildingPanel;
import org.example.stronghold.gui.panels.TilePanel;
import org.example.stronghold.model.Building;
import org.example.stronghold.model.GameData;
import org.example.stronghold.model.GameMap;
import org.example.stronghold.model.GuiSetting;
import org.example.stronghold.model.Player;
import org.example.stronghold.model.Tile;
import org.example.stronghold.model.Unit;
import org.example.stronghold.model.template.BuildingTemplate;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class MapScreen implements Screen {

    final StrongholdGame game;
    TiledMap tiledMap;
    IsometricTiledMapRenderer renderer;
    OrthographicCamera camera;
    Viewport mapViewport;
    static final int tilePerUnit = 4;
    @Getter
    public GameData gameData;
    @Getter
    public GameMap gameMap;
    final Random tileRandomizer = new Random(42);
    float hoverX, hoverY;
    int hoverCol = -1, hoverRow = -1;
    int selectCol = -1, selectRow = -1;
    ControlPanel controlPanel;
    ShapeRenderer shapeRenderer;
    public String toBeBuiltType;
    @Getter
    public Player myself;

    public MapScreen(StrongholdGame game, GameData gameData) {
        this.game = game;
        this.gameData = gameData;
        this.gameMap = gameData.getMap();
        // todo: get user in constructor
        this.myself = gameData.getPlayers().get(0);
    }

    private void setTileAt(int column, int row, int id) {
        TiledMapTileSets tileSets = tiledMap.getTileSets();
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tileSets.getTile(id));
        // xy coords in TiledMapTileLayer isn't the same as GameMap
        tileLayer.setCell(row, column, cell);
    }

    private int getTileIdByType(String tileType) {
        if (tileType.equals("farmland")) {
            return 1090 + tileRandomizer.nextInt(1161, 1309);
        }
        if (tileType.equals("water")) {
            return 1 + tileRandomizer.nextInt(649, 742);
        }
        if (tileType.equals("oil")) {
            return 1090 + tileRandomizer.nextInt(37, 148);
        }
        if (tileType.equals("plain")) {
            return 1090 + tileRandomizer.nextInt(908, 1036);
        }
        if (tileType.equals("rock")) {
            return 1090 + tileRandomizer.nextInt(148, 288);
        }
        if (tileType.equals("iron")) {
            return 2459 + tileRandomizer.nextInt(414, 491);
        }
        if (tileType.equals("stone")) {
            return 2459 + tileRandomizer.nextInt(0, 69);
        }
        if (tileType.equals("tree")) {
            return 1090 + tileRandomizer.nextInt(444, 592);
        }
        return 149; // total white as unknown tile
    }

    private void setupTiles() {
        for (int col = 0; col < gameMap.getWidth(); col++) {
            for (int row = 0; row < gameMap.getHeight(); row++) {
                Tile tile = gameMap.getAt(col, row);
                for (int i = 0; i < tilePerUnit; i++) {
                    for (int j = 0; j < tilePerUnit; j++) {
                        setTileAt(tilePerUnit * col + i, tilePerUnit * row + j,
                            getTileIdByType(tile.getType()));
                    }
                }
            }
        }
    }

    // this class can access private members of TestMapScreen, it isn't a static class
    class MapInputProcessor extends InputAdapter {

        private boolean notInMap(int screenY) {
            return screenY >= Gdx.graphics.getHeight() - controlPanel.getHeight();
        }

        private boolean notInMap() {
            return notInMap(Gdx.input.getY());
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            if (notInMap()) {
                return false;
            }
            camera.zoom += amountY * 0.1f;
            camera.zoom = Math.min(Math.max(camera.zoom, 0.5f), 1);
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            hoverCol = -1;
            hoverRow = -1;
            if (notInMap()) {
                return false;
            }
            Vector3 worldVec = mapViewport.unproject(new Vector3(screenX, screenY, 0));
            hoverX = worldVec.x;
            hoverY = worldVec.y;
            IntPair cell = cellAtVec3(worldVec);
            hoverCol = cell.x();
            hoverRow = cell.y();
            return true;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (button != Buttons.LEFT || notInMap(screenY)) {
                return false;
            }
            Vector3 worldVec = mapViewport.unproject(new Vector3(screenX, screenY, 0));
            IntPair cell = cellAtVec3(worldVec);
            selectCol = cell.x();
            selectRow = cell.y();
            if (buildToBeBuilt()) {
                return true;
            }
            setPanelOnSelect();
            return true;
        }
    }

    @Override
    public void show() {
        controlPanel = new ControlPanel(game, this, 140);
        controlPanel.create();

        camera = new OrthographicCamera();
        mapViewport = new ScreenViewport(camera);
        camera.zoom = 1;
        tiledMap = game.assetLoader.getTiledMap("tiled-maps/80x80.tmx");

        setupTiles();

        renderer = new IsometricTiledMapRenderer(tiledMap);
        renderer.setView(camera);

        shapeRenderer = new ShapeRenderer();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new MapInputProcessor());
        multiplexer.addProcessor(controlPanel.getStage());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
            float scale = camera.zoom * 2;
            camera.translate(-Gdx.input.getDeltaX() * scale, Gdx.input.getDeltaY() * scale);
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapViewport.apply();
        camera.update();
        renderer.setView(camera);
        renderer.render();
        drawOverMapLayer();
        controlPanel.render();
    }

    @Override
    public void resize(int width, int height) {
        mapViewport.update(width, height);
        camera.setToOrtho(false, width, height);
        renderer.setView(camera);
        controlPanel.resize(width, height);
    }

    private boolean buildToBeBuilt() {
        if (toBeBuiltType == null) {
            return false;
        }
        try {
            Operators.game.dropBuilding(new HashMap<>() {{
                put("game", gameData);
                put("player", myself);
                put("building", toBeBuiltType);
                put("position", new IntPair(selectCol, selectRow));
            }});
            toBeBuiltType = null;
            return false; // update panel
        } catch (OperatorException e) {
            controlPanel.popup.error(e.getMessage());
            return true; // keep trying
        } finally {
            selectRow = -1;
            selectCol = -1;
        }
    }

    private void setPanelOnSelect() {
        if (notInsideMap(selectCol, selectRow)) {
            controlPanel.setPanel(null);
            return;
        }
        Tile tile = gameMap.getAt(selectCol, selectRow);
        if (tile.getBuilding() != null) {
            controlPanel.setPanel(
                new BuildingPanel(controlPanel, selectCol, selectRow, tile.getBuilding()));
            return;
        }
        controlPanel.setPanel(new TilePanel(controlPanel, selectCol, selectRow, tile));
    }

    private static Vector3 vec3AtSubCell(int column, int row, int i, int j) {
        // returns location of *
        //   /\
        //  /  \
        //  \  /
        // * \/
        return new Vector3(
            15f * (tilePerUnit * (column + row) + i + j),
            8f * (tilePerUnit * (column - row) + i - j),
            0
        );
    }

    private static Vector3 vec3AtCell(int column, int row) {
        return vec3AtSubCell(column, row, 0, 0).sub(0, 8f * (tilePerUnit - 1), 0);
    }

    private static IntPair subCellAtVec3(Vector3 vector) {
        float rowFactor = 8f / 15 * vector.x - vector.y;
        int row = (int) Math.floor((rowFactor + 8) / 16);
        float colFactor = 8f / 15 * vector.x + vector.y;
        int col = (int) Math.floor((colFactor - 8) / 16);
        return new IntPair(col, row);
    }

    private static IntPair cellAtVec3(Vector3 vector) {
        IntPair subCell = subCellAtVec3(vector);
        return new IntPair(
            subCell.x() / tilePerUnit,
            subCell.y() / tilePerUnit
        );
    }

    private static Vector3 vec3AtPoint(float column, float row) {
        // useful for drawing things on top of tiles
        // different from the notion of cells
        //   /\
        //  /  \
        // *    |
        //  \  /
        //   \/
        return new Vector3(
            15f * (tilePerUnit * (column + row)),
            8f * (tilePerUnit * (column - row)) + 8f,
            0
        );
    }

    private void drawOverMapLayer() {
        Batch batch = renderer.getBatch();
        batch.begin();
        drawEntities(batch);
        drawHoverBuildingDetail(batch);
        drawHoverUnitDetail(batch);
        batch.end();
        drawSelectedCell();
    }

    private void drawEntities(Batch batch) {
        // building, tree
        for (int col = gameMap.getWidth() - 1; col >= 0; col--) { // back to front
            for (int row = 0; row < gameMap.getHeight(); row++) {
                Tile tile = gameMap.getAt(col, row);
                if (tile.getBuilding() != null) {
                    drawBuildingAt(batch, tile.getBuilding().getGuiSetting(), col, row);
                    continue;
                }
                if (toBeBuiltType != null && col == hoverCol && row == hoverRow) {
                    drawToBeBuilt(batch, col, row);
                    continue;
                }
                if (tile.getType().startsWith("tree")) {
                    drawTreeAt(batch, game.assetLoader.getTexture("plants/oak.png"), col, row);
                }
            }
        }
        // unit
        for (int col = gameMap.getWidth() - 1; col >= 0; col--) {
            for (int row = 0; row < gameMap.getHeight(); row++) {
                drawUnitsAt(batch, col, row);
            }
        }
    }

    public boolean notInsideMap(int col, int row) {
        return col < 0 || col >= gameMap.getWidth() || row < 0 || row >= gameMap.getHeight();
    }

    private void drawHoverBuildingDetail(Batch batch) {
        if (notInsideMap(hoverCol, hoverRow)) {
            return; // out of map
        }
        Tile tile = gameMap.getAt(hoverCol, hoverRow);
        if (tile.getBuilding() == null) {
            return;
        }
        Building building = tile.getBuilding();
        Label label = new Label(building.getType() + " " + building.getHitPoints(), game.skin);
        Vector3 position = new Vector3(hoverX, hoverY + 10, 0);
        label.setPosition(position.x, position.y, Align.center);
        label.draw(batch, 1);
    }

    private void drawHoverUnitDetail(Batch batch) {
        if (notInsideMap(hoverCol, hoverRow))
            return;
        List<Unit> units = gameData.getUnitsOnPosition(new IntPair(hoverCol, hoverRow)).toList();
        if (units.isEmpty())
            return;
        final int column = hoverCol, row = hoverRow;
        int n = 1;
        if (units.size() > 1) {
            n = 2;
        }
        if (units.size() > 4) {
            n = (int) Math.ceil(Math.sqrt(units.size()));
        }
        int x = 1, y = 1;
        for (Unit unit : units) {
            float uCol = column + (float) x / (n + 1);
            float uRow = row + (float) y / (n + 1);
            Label label = new Label(unit.getType() + " " + unit.getHitPoints(), game.skin);
            Vector3 onScreen = vec3AtPoint(uCol, uRow);
            Vector3 position = new Vector3(onScreen.x, onScreen.y + 50, 0);
            label.setPosition(position.x, position.y, Align.center);
            label.draw(batch, 1);
            x++;
            if (x > n) {
                x = 1;
                y++;
            }
        }
    }

    private void drawSelectedCell() {
        if (notInsideMap(selectCol, selectRow)) {
            return;
        }
        Vector3 cellVec = vec3AtCell(selectCol, selectRow)
            .add(15f * tilePerUnit, 8f * tilePerUnit, 0);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(cellVec.x, cellVec.y, 5);
        shapeRenderer.end();
    }

    private void drawUnitsAt(Batch batch, int column, int row) {
        List<GuiSetting> units = gameData.getUnitsOnPosition(new IntPair(column, row))
            .map(Unit::getGuiSetting)
            .toList();
        if (units.isEmpty()) {
            return;
        }
        int n = 1;
        if (units.size() > 1) {
            n = 2;
        }
        if (units.size() > 4) {
            n = (int) Math.ceil(Math.sqrt(units.size()));
        }
        int x = 1, y = 1;
        for (GuiSetting unit : units) {
            float uCol = column + (float) x / (n + 1);
            float uRow = row + (float) y / (n + 1);
            drawUnitAt(batch, unit, uCol, uRow);
            x++;
            if (x > n) {
                x = 1;
                y++;
            }
        }
    }

    private void drawUnitAt(Batch batch, GuiSetting guiSetting, float column, float row) {
        if (guiSetting.getAsset() == null) {
            return;
        }
        Texture texture = game.assetLoader.getTexture(guiSetting.getAsset());
        Vector3 position = vec3AtPoint(column, row);
        float width = guiSetting.getPrefWidth();
        batch.draw(
            texture,
            position.x - width / 2, position.y,
            width,
            texture.getHeight() * width / texture.getWidth()
        );
    }

    private void drawBuildingAt(Batch batch, GuiSetting guiSetting, int column, int row) {
        if (guiSetting.getAsset() == null) {
            return;
        }
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

    private void drawToBeBuilt(Batch batch, int column, int row) {
        BuildingTemplate building = game.templateDatabase.getBuildingTemplates().get(toBeBuiltType);
        drawBuildingAt(batch, building.getGuiSetting(), column, row);
    }

    private void drawTreeAt(Batch batch, Texture texture, int column, int row) {
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
        controlPanel.dispose();
    }
}
