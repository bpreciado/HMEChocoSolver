package ch.iterial.hme.poc.ui.problem;

import ch.iterial.hme.poc.model.input.FaultMessage;
import ch.iterial.hme.poc.model.problem.LatencyProblemData;
import ch.iterial.hme.poc.model.problem.MaxLatencyProblem;
import ch.iterial.hme.poc.util.UiUtils;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.util.concurrent.atomic.AtomicInteger;

public class MaxLatencyPanel extends LatencyRelatedSolutionPanel<LatencyProblemData> {

    private final JLabel maxReportedLatencyValue;

    private final AtomicInteger maxReportedLatency = new AtomicInteger(0);

    public MaxLatencyPanel(final MaxLatencyProblem problem) {
        super(2, "Max Latency", problem, faultMessage -> new LatencyProblemData(
                faultMessage.senderUid,
                faultMessage.faultComm.actualLatency,
                faultMessage.faultComm.promisedLatency
        ));

        final JLabel maxReportedLatencyText = UiUtils.textLabel("Maximum Reported Latency", 270, 50);
        maxReportedLatencyValue = UiUtils.valueLabel(Integer.toString(maxReportedLatency.get()), 80, 50);

        add(maxReportedLatencyText);
        add(maxReportedLatencyValue);
    }

    @Override
    public void pushInput(final FaultMessage faultMessage) {
        super.pushInput(faultMessage);

        final int actualLatency = faultMessage.faultComm.actualLatency;
        if (actualLatency > maxReportedLatency.get()) {
            maxReportedLatency.set(actualLatency);
        }

        SwingUtilities.invokeLater(this::redraw);
    }

    @Override
    public void resetInputs() {
        super.resetInputs();

        maxReportedLatency.set(0);

        SwingUtilities.invokeLater(this::redraw);
    }

    private void redraw() {
        maxReportedLatencyValue.setText(Integer.toString(maxReportedLatency.get()));
    }

}
