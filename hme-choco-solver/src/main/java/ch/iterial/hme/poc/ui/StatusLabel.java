package ch.iterial.hme.poc.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;

public class StatusLabel extends JLabel {

    private Status status;

    public StatusLabel() {
        this.status = Status.STOPPED;

        setOpaque(true);
        setPreferredSize(new Dimension(80, 50));
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, false));
        setHorizontalAlignment(CENTER);

        redraw();
    }

    public void updateStatus(final Status status) {
        this.status = status;

        SwingUtilities.invokeLater(this::redraw);
    }

    private void redraw() {
        setText(status.getText());
        setBackground(status.getColor());
    }

}
