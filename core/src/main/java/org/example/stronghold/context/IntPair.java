package org.example.stronghold.context;

import java.io.Serializable;

public record IntPair(int x, int y) implements Serializable {

    public IntPair add(int dx, int dy) {
        return new IntPair(x + dx, y + dy);
    }
}
