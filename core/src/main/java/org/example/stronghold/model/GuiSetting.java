package org.example.stronghold.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class GuiSetting implements Serializable {

    private String asset = null;
    private float prefWidth = 0;
    private float offsetX = 0;
    private float offsetY = 0;
}
