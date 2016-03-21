package ch.iterial.hme.poc.ui;

import ch.iterial.hme.poc.model.Problem;
import ch.iterial.hme.poc.model.ProblemData;
import ch.iterial.hme.poc.model.input.Input;
import ch.iterial.hme.poc.model.problem.BandwidthProblem;
import ch.iterial.hme.poc.model.problem.CriticalDeviceProblem;
import ch.iterial.hme.poc.model.problem.MaxLatencyProblem;
import ch.iterial.hme.poc.model.problem.TimeFrameLatencyProblem;
import ch.iterial.hme.poc.service.InputDataReader;
import ch.iterial.hme.poc.ui.problem.AvailableBandwidthPanel;
import ch.iterial.hme.poc.ui.problem.CriticalDeviceLatencyPanel;
import ch.iterial.hme.poc.ui.problem.DevicesNumberPanel;
import ch.iterial.hme.poc.ui.problem.MalwareReportsPanel;
import ch.iterial.hme.poc.ui.problem.MaxLatencyPanel;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import static ch.iterial.hme.poc.util.ImageLoadingUtils.fromFile;

public class SolverFrame extends JFrame implements Observer {

    private static final String TITLE = "Constraints Solver PoC";

    private final Set<SolutionPanel> solutionPanels = new HashSet<>();
    private final Set<ProblemSolverWorker> currentWorkers = new HashSet<>();

    public SolverFrame(final InputDataReader inputDataReader, final Map<Class<? extends Problem>, Problem> problems) {
        super(TITLE);

        final Container container = getContentPane();
        container.setLayout(new BorderLayout(0, 25));

        final ControlsInputObservable controlsBus = new ControlsInputObservable();
        controlsBus.addObserver(this);

        final JLabel titleLabel = new JLabel(TITLE, fromFile("logo.png"), SwingConstants.LEFT);
        final Font titleLabelFont = titleLabel.getFont();
        titleLabel.setFont(new Font(titleLabelFont.getName(), Font.BOLD, 32));
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setOpaque(true);

        container.add(titleLabel, BorderLayout.NORTH);
        container.add(new ControlsPanel(inputDataReader, controlsBus), BorderLayout.SOUTH);

        final JPanel solutionsPanel = new JPanel();
        solutionsPanel.setLayout(new BoxLayout(solutionsPanel, BoxLayout.Y_AXIS));
        solutionsPanel.add(addSolutionPanel(new AvailableBandwidthPanel(get(problems, BandwidthProblem.class))));
        solutionsPanel.add(addSolutionPanel(new MaxLatencyPanel(get(problems, MaxLatencyProblem.class))));
        solutionsPanel.add(addSolutionPanel(new CriticalDeviceLatencyPanel(get(problems, CriticalDeviceProblem.class))));
        solutionsPanel.add(addSolutionPanel(new DevicesNumberPanel(get(problems, TimeFrameLatencyProblem.class))));
        solutionsPanel.add(addSolutionPanel(new MalwareReportsPanel()));
        container.add(solutionsPanel, BorderLayout.CENTER);
    }

    private <D extends ProblemData, I extends Input> SolutionPanel<D, I> addSolutionPanel(final SolutionPanel<D, I> solutionPanel) {
        this.solutionPanels.add(solutionPanel);
        return solutionPanel;
    }

    private <P extends Problem<D>, D extends ProblemData> P get(final Map<Class<? extends Problem>, ? extends Problem> map,
                                                                final Class<P> type) {
        return type.cast(map.get(type));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(final Observable observable, final Object data) {
        solutionPanels.forEach(panel -> {
            final ControlsInputObservable.Data inputData = ControlsInputObservable.Data.class.cast(data);
            switch (inputData.controlsAction) {
                case RESET:
                    panel.resetInputs();
                    break;
                case NEXT:
                    clearCurrentWorkers();
                    addToCurrentWorkers(panel.runSolverFor(inputData.inputMessage));
                    break;
                case STOP:
                    clearCurrentWorkers();
                    currentWorkers.forEach(worker -> worker.cancel(true));
                    break;
                default:
                    throw new IllegalStateException();
            }
        });
    }

    private void clearCurrentWorkers() {
        currentWorkers.removeIf(worker -> worker.isDone() || worker.isCancelled());
    }

    private boolean addToCurrentWorkers(final ProblemSolverWorker worker) {
        return worker != null && currentWorkers.add(worker);
    }

}
