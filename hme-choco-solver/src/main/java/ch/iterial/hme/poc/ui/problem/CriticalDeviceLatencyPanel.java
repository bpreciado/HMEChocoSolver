package ch.iterial.hme.poc.ui.problem;

import ch.iterial.hme.poc.model.SolutionObservable;
import ch.iterial.hme.poc.model.input.FaultMessage;
import ch.iterial.hme.poc.model.input.Input;
import ch.iterial.hme.poc.model.problem.CriticalDeviceProblem;
import ch.iterial.hme.poc.model.problem.CriticalDeviceProblemData;
import ch.iterial.hme.poc.util.UiUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CriticalDeviceLatencyPanel extends LatencyRelatedSolutionPanel<CriticalDeviceProblemData> {

    private final JLabel maxReportedLatencyValue;

    private AtomicInteger maxReportedLatency = new AtomicInteger(0);

    public CriticalDeviceLatencyPanel(final CriticalDeviceProblem problem) {
        super(3, "Critical Device Latency", problem, faultMessage -> new CriticalDeviceProblemData(
                faultMessage.faultComm.faultType,
                faultMessage.originalUid,
                faultMessage.senderUid
        ));

        final JLabel criticalDeviceUidText = UiUtils.textLabel("Device(s) UID", 110, 50);
        final JLabel criticalDeviceUidValue = UiUtils.valueLabel(StringUtils.join(problem.getIds(), ';'), 100, 50);

        final JLabel maxReportedLatencyText = UiUtils.textLabel("Max", 40, 50);
        maxReportedLatencyValue = UiUtils.valueLabel(Integer.toString(maxReportedLatency.get()), 80, 50);

        add(criticalDeviceUidText);
        add(criticalDeviceUidValue);
        add(maxReportedLatencyText);
        add(maxReportedLatencyValue);
    }

    @Override
    public boolean supports(final Input input) {
        return input instanceof FaultMessage && (
                "Latency".equals(FaultMessage.class.cast(input).faultComm.faultType)
                        || "Unknown".equals(FaultMessage.class.cast(input).faultComm.faultType));
    }

    @Override
    protected void doWithSolution(final SolutionObservable.Data data) {
        super.doWithSolution(data);

        if (data.input instanceof FaultMessage) {
            final FaultMessage faultMessage = FaultMessage.class.cast(data.input);
            final int actualLatency = faultMessage.faultComm.actualLatency;
            final CriticalDeviceProblem problem = CriticalDeviceProblem.class.cast(getProblem());
            final List<Integer> ids = problem.getIds();
            if ((ids.contains(faultMessage.senderUid) || ids.contains(faultMessage.originalUid))
                    && actualLatency > maxReportedLatency.get()) {
                maxReportedLatency.set(actualLatency);
            }

            SwingUtilities.invokeLater(this::redraw);
        }
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
