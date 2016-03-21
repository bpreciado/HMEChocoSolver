package ch.iterial.hme.poc.util;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;

public abstract class UiUtils {

    public static JLabel valueLabel(final String text, final int width, final int height) {
        final JLabel valueLabel = new JLabel();
        valueLabel.setPreferredSize(new Dimension(width, height));
        valueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));
        valueLabel.setBackground(Color.WHITE);
        valueLabel.setOpaque(true);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setVerticalAlignment(SwingConstants.CENTER);
        valueLabel.setText(text);

        return valueLabel;
    }

    public static JLabel textLabel(final String text, final int width, final int height) {
        final JLabel textLabel = new JLabel(String.format("<html><body><label>%s</label></body></html>", text));
        textLabel.setPreferredSize(new Dimension(width, height));
        textLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        return textLabel;
    }

}
