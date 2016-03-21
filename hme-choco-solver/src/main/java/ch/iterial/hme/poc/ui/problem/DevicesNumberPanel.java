package ch.iterial.hme.poc.ui.problem;

import ch.iterial.hme.poc.model.Solution;
import ch.iterial.hme.poc.model.SolutionObservable;
import ch.iterial.hme.poc.model.input.FaultMessage;
import ch.iterial.hme.poc.model.problem.LatencyProblemData;
import ch.iterial.hme.poc.model.problem.TimeFrameLatencyProblem;
import ch.iterial.hme.poc.model.problem.TimeFrameLatencyProblemDataFactory;
import ch.iterial.hme.poc.util.UiUtils;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DevicesNumberPanel extends LatencyRelatedSolutionPanel<LatencyProblemData> {

    private final JLabel numberOfDevicesValue;

    private final AtomicInteger numberOfDevices = new AtomicInteger(0);

    public DevicesNumberPanel(final TimeFrameLatencyProblem problem) {
        super(4, "# of Devices with Latency Faults", problem, new TimeFrameLatencyProblemDataFactory(problem.getTimeFrameSeconds()));

        final JLabel numberOfDevicesText = UiUtils.textLabel("# of devices with latency faults", 270, 50);
        numberOfDevicesValue = UiUtils.valueLabel(Integer.toString(numberOfDevices.get()), 80, 50);

        add(numberOfDevicesText);
        add(numberOfDevicesValue);
    }

    @Override
    protected void doWithSolution(final SolutionObservable.Data data) {
        if (data.input instanceof FaultMessage) {
            final TimeFrameLatencyProblem problem = TimeFrameLatencyProblem.class.cast(getProblem());
            final TimeFrameLatencyProblemDataFactory factory = TimeFrameLatencyProblemDataFactory.class.cast(this.dataFactory);

            factory.getCache().put(FaultMessage.class.cast(data.input), data.result);

            numberOfDevices.set(factory.getCache().entrySet().stream()
                    .filter(entry -> Solution.NEGATIVE.equals(entry.getValue()))
                    .map(entry -> entry.getKey().senderUid)
                    .collect(Collectors.toSet()).size());

            if (Solution.INTERRUPTED.equals(data.result) || Solution.ERROR.equals(data.result)) {
                updateSolutionResult(data.result);
            } else {
                if (numberOfDevices.get() < problem.getCriticalDevicesNumber()) {
                    updateSolutionResult(Solution.POSITIVE);
                } else {
                    updateSolutionResult(Solution.NEGATIVE);
                }

                SwingUtilities.invokeLater(() -> numberOfDevicesValue.setText(Integer.toString(numberOfDevices.get())));
            }
        }
    }

}
