package org.example.stronghold.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Data;

@Data
public class AssetLoader implements Disposable {

    private final Map<String, Disposable> assets = new HashMap<>();

    public AssetLoader() {

    }

    public void loadAll() {
        loadFromRoots(this::loadTexture,
            "buildings/", "plants/", "captcha/", "units/", "banners/", "others/"
        );
        loadFromRoots(this::loadTiledMap, "tiled-maps/");
        loadTexture(Gdx.files.internal("craftacular/dirt.png"));
    }

    private void loadTexture(FileHandle file) {
        assets.put(file.path(), new Texture(file));
    }

    private void loadFromRoots(Consumer<FileHandle> loader, String... roots) {
        for (String root : roots) {
            FileHandle[] files = Gdx.files.internal(root).list();
            for (FileHandle file : files) {
                loader.accept(file);
            }
        }
    }

    private void loadTiledMap(FileHandle file) {
        assets.put(file.path(), new TmxMapLoader().load(file.path()));
    }

    public <T extends Disposable> T get(String path, Class<T> clazz) {
        return (T) assets.get(path);
    }

    public Texture getTexture(String path) {
        return get(path, Texture.class);
    }

    public TiledMap getTiledMap(String path) {
        return get(path, TiledMap.class);
    }

    @Override
    public void dispose() {
        for (Disposable value : assets.values()) {
            value.dispose();
        }
        assets.clear();
    }
}
