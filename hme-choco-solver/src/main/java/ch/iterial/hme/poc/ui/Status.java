package ch.iterial.hme.poc.ui;

import java.awt.Color;

public enum Status {

    STOPPED("Stopped", Color.YELLOW), RUNNING("Running", Color.GREEN), ERROR("Error", Color.RED);

    private final String text;
    private final Color color;

    Status(final String text, final Color color) {
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }

}
