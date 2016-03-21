package ch.iterial.hme.poc.ui;

import java.awt.Color;

public enum Result {

    TRUE(Color.GREEN, true), FALSE(Color.RED, false);

    private final Color color;
    private final boolean value;

    Result(final Color color, final boolean value) {
        this.color = color;
        this.value = value;
    }

    public Result and(final Result result) {
        return value && result.value ? TRUE : FALSE;
    }

    public Color getColor() {
        return color;
    }

}
