package ch.iterial.hme.poc.ui;

import ch.iterial.hme.poc.model.Problem;
import ch.iterial.hme.poc.model.ProblemData;
import ch.iterial.hme.poc.model.ProblemDataFactory;
import ch.iterial.hme.poc.model.Solution;
import ch.iterial.hme.poc.model.SolutionObservable;
import ch.iterial.hme.poc.model.input.Input;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;

/**
 * Base class for the solution panels
 *
 * @param <D> Problem data type
 * @param <I> Program input type
 */
public abstract class SolutionPanel<D extends ProblemData, I extends Input> extends JPanel implements Observer {

    /**
     * Problem to which this panel displays the solution
     */
    protected final Problem<D> problem;

    /**
     * Data factory for the problem
     */
    protected final ProblemDataFactory<I, D> dataFactory;

    /**
     * Displays the current status of the solution calculation
     *
     * @see ch.iterial.hme.poc.ui.Status
     */
    protected final StatusLabel statusLabel;

    /**
     * Displays the solver output for the current problem data
     *
     * @see ch.iterial.hme.poc.ui.Result
     */
    protected final ResultLabel resultLabel;

    /**
     * Bus for monitoring the solver outputs
     */
    private final SolutionObservable<I> solutionsBus = new SolutionObservable<>();

    /**
     * Constructs new panel instance
     *
     * @param index       Index of the panel to be displayed on the constraint (leftmost) label
     * @param constraint  Constraint name to be displayed on the constraint (leftmost) label
     * @param problem     Problem instance
     * @param dataFactory Data factory instance
     */
    public SolutionPanel(final int index,
                         final String constraint,
                         final Problem<D> problem,
                         final ProblemDataFactory<I, D> dataFactory) {

        super(new FlowLayout(FlowLayout.LEFT, 10, 0));

        this.problem = problem;
        this.dataFactory = dataFactory;

        solutionsBus.addObserver(this);

        final ConstraintLabel constraintLabel = new ConstraintLabel(index, constraint);

        statusLabel = new StatusLabel();
        resultLabel = new ResultLabel();

        add(constraintLabel);
        add(statusLabel);
        add(resultLabel);

        setBorder(BorderFactory.createEtchedBorder());
    }

    /**
     * Called whenever a new solution arrives on the bus
     *
     * @param bus  Solver output
     * @param data New solution
     */
    @Override
    public void update(final Observable bus, final Object data) {
        if (data instanceof SolutionObservable.Data) {
            doWithSolution(SolutionObservable.Data.class.cast(data));
        }
    }

    /**
     * Creates a new instance of the worker that calculates the solution in a dedicated thread
     *
     * @param input Program input data
     * @return Worker instance
     * @see ch.iterial.hme.poc.ui.ProblemSolverWorker
     * @see ch.iterial.hme.poc.ui.SolutionPanel#runSolverFor(Input)
     */
    private ProblemSolverWorker<D, I> launchWorker(final I input) {
        SwingUtilities.invokeLater(() -> statusLabel.updateStatus(Status.RUNNING));

        final ProblemSolverWorker<D, I> worker = new ProblemSolverWorker<>(problem, dataFactory.buildProblemData(input), input, solutionsBus);

        worker.execute();

        return worker;
    }

    /**
     * Tells whether this panel can operate the given input
     *
     * @param input Program input data
     * @return Boolean
     * @see ch.iterial.hme.poc.ui.SolutionPanel#runSolverFor(Input)
     */
    protected abstract boolean supports(final Input input);

    /**
     * Tells whether the collected data is sufficient and consistent to run the solver on it
     *
     * @return Boolean
     * @see ch.iterial.hme.poc.ui.SolutionPanel#runSolverFor(Input)
     */
    protected boolean isReadyToSolve() {
        return true;
    }

    /**
     * Executes when to a new program input pushed
     *
     * @param input Program input data
     * @see ch.iterial.hme.poc.ui.SolutionPanel#runSolverFor(Input)
     */
    protected void pushInput(final I input) {
    }

    /**
     * Executes when to a new solution arrives
     *
     * @param data Solution data
     * @see ch.iterial.hme.poc.ui.SolutionPanel#update(Observable, Object)
     */
    protected void doWithSolution(final SolutionObservable.Data data) {
        updateSolutionResult(data.result);
    }

    /**
     * Updates the panel UI according to the solution result
     *
     * @param result Solution result
     * @see ch.iterial.hme.poc.model.Solution
     */
    protected final void updateSolutionResult(final Solution result) {
        switch (result) {
            case POSITIVE:
                SwingUtilities.invokeLater(() -> {
                    statusLabel.updateStatus(Status.STOPPED);
                    resultLabel.updateResult(Result.TRUE);
                });
                break;
            case NEGATIVE:
                SwingUtilities.invokeLater(() -> {
                    statusLabel.updateStatus(Status.STOPPED);
                    resultLabel.updateResult(Result.FALSE);
                });
                break;
            case INTERRUPTED:
                SwingUtilities.invokeLater(() -> statusLabel.updateStatus(Status.STOPPED));
                break;
            case ERROR:
                SwingUtilities.invokeLater(() -> statusLabel.updateStatus(Status.ERROR));
                break;
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Reset the panel UI according to defaults
     */
    public void resetInputs() {
        SwingUtilities.invokeLater(() -> {
            statusLabel.updateStatus(Status.STOPPED);
            resultLabel.updateResult(Result.TRUE, false);
        });
    }

    /**
     * Main solver starter
     *
     * @param input Program input data
     * @return Worker instance
     */
    public final ProblemSolverWorker<D, I> runSolverFor(final I input) {
        if (supports(input)) {
            pushInput(input);
            if (isReadyToSolve()) {
                return launchWorker(input);
            }
        }
        return null;
    }

    /**
     * Getter
     *
     * @return Problem instance
     */
    public Problem<D> getProblem() {
        return problem;
    }

}
