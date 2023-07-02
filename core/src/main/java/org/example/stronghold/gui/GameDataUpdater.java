package org.example.stronghold.gui;

import lombok.Data;
import org.example.stronghold.gui.sections.MapScreen;

@Data
public class GameDataUpdater implements Runnable {

    private final MapScreen mapScreen;

    @Override
    public void run() {
        while (!mapScreen.stopped) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!mapScreen.running)
                continue;
            mapScreen.updateGameData(1);
        }
    }
}
