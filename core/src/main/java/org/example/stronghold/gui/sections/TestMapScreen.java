package org.example.stronghold.gui.sections;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

public class TestMapScreen implements Screen {
    private final Game game;
    private TiledMap tiledMap;
    private IsometricTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private static final float unitScale = 2.0f;

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
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            float scale = unitScale * camera.zoom;
            camera.translate(-Gdx.input.getDeltaX() * scale, Gdx.input.getDeltaY() * scale);
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
            Vector3 touched = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            camera.position.set(touched);
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
        tiledMap.dispose();
    }
}
