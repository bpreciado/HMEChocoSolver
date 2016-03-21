package ch.iterial.hme.poc.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;

public class ResultLabel extends JLabel {

    private Result result;

    public ResultLabel() {
        this.result = Result.TRUE;

        setOpaque(true);
        setPreferredSize(new Dimension(50, 50));
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, false));
        setHorizontalAlignment(CENTER);

        redraw();
    }

    public void updateResult(final Result result) {
        updateResult(result, true);
    }

    public void updateResult(final Result result, final boolean append) {
        this.result = append ? this.result.and(result) : result;

        SwingUtilities.invokeLater(this::redraw);
    }

    private void redraw() {
        setText(result.name());
        setBackground(result.getColor());
    }

}
