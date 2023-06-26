package org.example.stronghold.gui.sections;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

public class TestMapScreen implements Screen {
    final Game game;
    TiledMap tiledMap;
    IsometricTiledMapRenderer renderer;
    OrthographicCamera camera;
    static final float unitScale = 2.0f;
    Texture barracks;

    public TestMapScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.zoom = 1 / unitScale;
        tiledMap = new TmxMapLoader().load("tiled-maps/test.tmx");
        renderer = new IsometricTiledMapRenderer(tiledMap);
        renderer.setView(camera);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                camera.zoom += amountY * 0.1f;
                camera.zoom = Math.min(Math.max(camera.zoom, 0.1f), 1 / unitScale);
                return false;
            }
        });

        barracks = new Texture(Gdx.files.internal("buildings/Barracks.png"));
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            float scale = unitScale * camera.zoom;
            camera.translate(-Gdx.input.getDeltaX() * scale, Gdx.input.getDeltaY() * scale);
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
            Vector3 touched = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            camera.position.set(touched);
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();

        Batch batch = renderer.getBatch();
        batch.begin();
        drawTextureAt(batch, barracks, 4, 2);
        drawTextureAt(batch, barracks, 3, 2);
        drawTextureAt(batch, barracks, 3, 3);
        batch.end();
    }

    public Vector3 vec3AtCell(int column, int row) {
        return new Vector3(15f * (column + row), 8f * (column - row), 0);
    }

    public void drawTextureAt(Batch batch, Texture texture, int column, int row) {
        int margin = 2;
        Vector3 position = vec3AtCell(column, row);
        batch.draw(
            texture,
            position.x + margin, position.y + margin,
            30f - 2*margin,
            texture.getHeight() * (30f - 2*margin) / texture.getWidth()
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
