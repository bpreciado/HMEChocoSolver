package ch.iterial.hme.poc.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Dimension;

public class ConstraintLabel extends JLabel {

    private final int index;
    private final String constraint;

    public ConstraintLabel(final int index, final String constraint) {
        super();
        this.index = index;
        this.constraint = constraint;

        setText(String.format("<html><body><b>Constraint #%s</b><br/><i>%s</i></body></html>", index, constraint));

        final Border paddingBorder = BorderFactory.createEmptyBorder(0, 10, 0, 0);
        final Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 1, false);
        setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));
        setBackground(Color.WHITE);
        setOpaque(true);
        setPreferredSize(new Dimension(240, 60));
    }

    public int getIndex() {
        return index;
    }

    public String getConstraint() {
        return constraint;
    }

}
