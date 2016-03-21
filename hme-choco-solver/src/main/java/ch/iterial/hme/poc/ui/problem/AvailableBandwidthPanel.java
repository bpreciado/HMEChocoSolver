package ch.iterial.hme.poc.ui.problem;

import ch.iterial.hme.poc.model.input.BandwidthInput;
import ch.iterial.hme.poc.model.input.BandwidthReport;
import ch.iterial.hme.poc.model.input.BandwidthStatus;
import ch.iterial.hme.poc.model.input.Input;
import ch.iterial.hme.poc.model.problem.BandwidthProblem;
import ch.iterial.hme.poc.model.problem.BandwidthProblemData;
import ch.iterial.hme.poc.model.problem.BandwidthProblemDataFactory;
import ch.iterial.hme.poc.ui.SolutionPanel;
import ch.iterial.hme.poc.util.UiUtils;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

public class AvailableBandwidthPanel extends SolutionPanel<BandwidthProblemData, BandwidthInput> {

    private final JLabel currentBWValue;
    private final JLabel availableBWValue;

    public AvailableBandwidthPanel(final BandwidthProblem problem) {
        super(1, "Available Bandwidth", problem, new BandwidthProblemDataFactory());

        final JLabel currentBWText = UiUtils.textLabel("Current Available Bandwidth", 220, 25);
        final JLabel availableBWText = UiUtils.textLabel("Assigned Maximum Bandwidth", 220, 25);
        currentBWValue = UiUtils.valueLabel("N/A", 80, 25);
        availableBWValue = UiUtils.valueLabel("N/A", 80, 25);

        final JPanel currentBWPanel = new JPanel(new BorderLayout(10, 0));
        currentBWPanel.add(currentBWText, BorderLayout.CENTER);
        currentBWPanel.add(currentBWValue, BorderLayout.EAST);

        final JPanel availableBWPanel = new JPanel(new BorderLayout(10, 0));
        availableBWPanel.add(availableBWText, BorderLayout.CENTER);
        availableBWPanel.add(availableBWValue, BorderLayout.EAST);

        final JPanel bwPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        bwPanel.setPreferredSize(new Dimension(360, 60));
        bwPanel.add(currentBWPanel);
        bwPanel.add(availableBWPanel);

        add(bwPanel);
    }

    @Override
    public boolean supports(final Input input) {
        return input instanceof BandwidthInput;
    }

    @Override
    public boolean isReadyToSolve() {
        return BandwidthProblemDataFactory.class.cast(this.dataFactory).getCache().containsBothKeys();
    }

    @Override
    public void pushInput(final BandwidthInput input) {
        super.pushInput(input);

        final BandwidthProblemDataFactory dataFactory = BandwidthProblemDataFactory.class.cast(this.dataFactory);
        final BandwidthProblemDataFactory.BandwidthCache cache = dataFactory.getCache();

        if (input instanceof BandwidthStatus) {
            cache.put(BandwidthStatus.class, input);
        } else if (input instanceof BandwidthReport) {
            cache.put(BandwidthReport.class, input);
        } else {
            throw new IllegalArgumentException();
        }

        final BandwidthStatus status = BandwidthStatus.class.cast(cache.get(BandwidthStatus.class));
        final BandwidthReport report = BandwidthReport.class.cast(cache.get(BandwidthReport.class));

        SwingUtilities.invokeLater(() -> {
            currentBWValue.setText(status == null ? "N/A" : Integer.toString(status.getTotalBandwidth()));
            availableBWValue.setText(report == null ? "N/A" : Integer.toString(report.maxBw.intValue()));
        });
    }

    @Override
    public void resetInputs() {
        super.resetInputs();
    }

}
