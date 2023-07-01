package org.example.stronghold.gui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.function.Supplier;
import lombok.Data;
import org.example.stronghold.gui.SimpleChangeListener;
import org.example.stronghold.gui.StrongholdGame;
import org.example.stronghold.gui.panels.BuildPanel;
import org.example.stronghold.gui.panels.FoodPanel;
import org.example.stronghold.gui.panels.MarketPanel;
import org.example.stronghold.gui.panels.OptionPanel;
import org.example.stronghold.gui.panels.PopularityPanel;
import org.example.stronghold.gui.panels.ReportPanel;
import org.example.stronghold.gui.panels.TaxPanel;
import org.example.stronghold.gui.panels.TradePanel;
import org.example.stronghold.gui.sections.MapScreen;

@Data
public class ControlPanel implements Disposable {

    private static final boolean DEBUG = false;
    final StrongholdGame game;
    final MapScreen screen;
    final int height;
    public PopupWindow popup;
    Viewport viewport;
    Stage stage;
    Table layoutTable, selectTable, lastRow;
    Container<Panel> mainPane;
    Texture dirt;

    public Panel getPanel() {
        return mainPane.getActor();
    }

    public void setPanel(Panel panel) {
        mainPane.setActor(panel);
    }

    public void switchPanelOnChange(Actor actor, Supplier<Panel> supplier) {
        actor.addListener(new SimpleChangeListener(() -> setPanel(supplier.get())));
    }

    private void addPanelButton(String text, Supplier<Panel> supplier) {
        TextButton button = new TextButton(text, game.skin);
        switchPanelOnChange(button, supplier);
        lastRow.add(button);
    }

    private void finishSelectRow() {
        selectTable.add(lastRow).row();
        lastRow = new Table();
    }

    public void create() {
        viewport = new FitViewport(Gdx.graphics.getWidth(), height);
        stage = new Stage(viewport);

        dirt = game.assetLoader.getTexture("craftacular/dirt.png");
        dirt.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        layoutTable = new Table();
        layoutTable.setFillParent(true);
        stage.addActor(layoutTable);

        selectTable = new Table();
        layoutTable.add(selectTable).width(300);

        lastRow = new Table(); // select rows

        mainPane = new Container<>();
        mainPane.pad(5);
        mainPane.fill();
        layoutTable.add(mainPane).grow();

        Image minimap = new Image(game.assetLoader.getTexture("banners/minimap.png"));
        layoutTable.add(minimap).width(2 * height);

        popup = new PopupWindow(game.craftacularSkin, game.skin, 300);
        stage.addActor(popup);

        addPanelButton("Report", () -> new ReportPanel(this));
        addPanelButton("Build", () -> new BuildPanel(this));
        addPanelButton("Food", () -> new FoodPanel(this));
        addPanelButton("Popularity", () -> new PopularityPanel(this));
        finishSelectRow();
        addPanelButton("Market", () -> new MarketPanel(this));
        addPanelButton("Tax", () -> new TaxPanel(this));
        addPanelButton("Trade", () -> new TradePanel(this));
        finishSelectRow();
        addPanelButton("Options", () -> new OptionPanel(this));
        finishSelectRow();

        stage.setDebugAll(DEBUG);
    }

    public void resize(int scrWidth, int scrHeight) {
        viewport.update(scrWidth, height);
    }

    public void render() {
        viewport.apply(true);
        Batch batch = stage.getBatch();
        batch.begin();
        batch.draw(dirt, 0, 0, 0, 0, Gdx.graphics.getWidth(), height);
        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
