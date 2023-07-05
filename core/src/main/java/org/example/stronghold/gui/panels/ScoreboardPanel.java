package org.example.stronghold.gui.panels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;
import org.example.stronghold.model.User;

import java.text.SimpleDateFormat;
import java.util.*;

public class ScoreboardPanel extends Panel {

    private Table table;

    public ScoreboardPanel(ControlPanel controlPanel) {
        super(controlPanel);
        create();
    }

    private void addLabel(String value){
        Label label = new Label(value, game.skin);
        label.setAlignment(Align.center);
        table.add(label).growX();
    }

    private void create(){
        table = new Table(game.skin);
        table.align(Align.center);
        add(table).grow();
        addLabel("Avatar");
        addLabel("Username");
        addLabel("Score");
        addLabel("Last Visit");
        table.row();
        updateScoreboard();
    }

    private String getLastVisit(User user){
        if(user.isOnline()){
            return "Online now";
        }
        long lastVisit = user.getLastVisit();
        Date date = new Date(lastVisit * 1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3:30"));
        return sdf.format(date);
    }

    private void updateScoreboard(){
        try {
            List<User> users = new ArrayList<>((List<User>) game.conn.sendOperatorRequest("auth", "getUsers",
                new HashMap<>()));
            users.sort(Comparator.comparing(User::getScore).reversed());
            for(User user : users){
                table.add(new Image(new Texture(user.getAvatarFileHandle()))).growX();
                addLabel(user.getUsername());
                addLabel(String.valueOf(user.getScore()));
                addLabel(getLastVisit(user));
                table.row();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
