package ch.iterial.hme.poc.ui;

import ch.iterial.hme.poc.model.input.Timestamped;
import ch.iterial.hme.poc.service.InputDataReader;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static ch.iterial.hme.poc.util.ImageLoadingUtils.fromFile;

public class ControlsPanel extends JPanel {

    public ControlsPanel(final InputDataReader inputDataReader, final ControlsInputObservable controlsInputBus) {
        super(new FlowLayout(FlowLayout.CENTER, 20, 10));

        final JLabel currentFileLabel = new JLabel();
        currentFileLabel.setPreferredSize(new Dimension(250, 50));
        currentFileLabel.setHorizontalAlignment(SwingConstants.LEFT);
        currentFileLabel.setVerticalAlignment(SwingConstants.CENTER);
        currentFileLabel.setText("<html><body>No open files</body></html>");

        final JButton stopButton = new JButton("Stop", fromFile("stop.png"));
        stopButton.setActionCommand(ControlsAction.STOP.name());
        stopButton.addActionListener(e -> {
            if (ControlsAction.STOP.name().equals(e.getActionCommand())) {
                controlsInputBus.newInputMessage(new ControlsInputObservable.Data(ControlsAction.STOP));
            }
        });

        final JButton nextButton = new JButton("Next", fromFile("next.png"));
        nextButton.setActionCommand(ControlsAction.NEXT.name());
        nextButton.addActionListener(e -> {
            if (ControlsAction.NEXT.name().equals(e.getActionCommand())) {
                if (inputDataReader.hasNext()) {
                    final String currentFile = inputDataReader.currentFile();
                    final int currentIndex = inputDataReader.currentIndex();
                    final int maxIndex = inputDataReader.maxIndex();
                    final Timestamped nextMessage = inputDataReader.next();
                    final Calendar calendar = GregorianCalendar.from(nextMessage.timestamp().atZone(ZoneId.systemDefault()));
                    SwingUtilities.invokeLater(() ->
                            currentFileLabel.setText(String.format("<html><body>" +
                                            "Current file : <u>%s</u> (%s/%s)<br/>Timestamp : <i>%tD %tT</i>" +
                                            "</body></html>",
                                    currentFile, currentIndex, maxIndex, calendar, calendar)));
                    controlsInputBus.newInputMessage(new ControlsInputObservable.Data(nextMessage, ControlsAction.NEXT));
                }
            }
        });

        final JButton resetButton = new JButton("Reset", fromFile("reset.png"));
        resetButton.setActionCommand(ControlsAction.RESET.name());
        resetButton.addActionListener(e -> {
            if (ControlsAction.RESET.name().equals(e.getActionCommand())) {
                controlsInputBus.newInputMessage(new ControlsInputObservable.Data(ControlsAction.RESET));
            }
        });

        add(nextButton);
        add(resetButton);
        add(stopButton);
        add(currentFileLabel);
    }

}
