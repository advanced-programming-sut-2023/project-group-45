package org.example.stronghold.gui.panels;

import org.example.stronghold.gui.components.ControlPanel;
import org.example.stronghold.gui.components.Panel;

public class BuildPanel extends Panel {

    public BuildPanel(ControlPanel controlPanel) {
        super(controlPanel);
        create();
    }

    private void create() {
        add("Build Panel");
    }
}
