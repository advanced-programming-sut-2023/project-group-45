package org.example.stronghold.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class GuiSetting implements Serializable {
    private String asset = null;
    private float prefWidth = 0;
    private float offsetX = 0;
    private float offsetY = 0;
}
