package ch.iterial.hme.poc.ui;

import ch.iterial.hme.poc.model.Problem;
import ch.iterial.hme.poc.model.ProblemData;
import ch.iterial.hme.poc.model.Solution;
import ch.iterial.hme.poc.model.SolutionObservable;
import ch.iterial.hme.poc.model.input.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SwingWorker;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class ProblemSolverWorker<D extends ProblemData, I extends Input> extends SwingWorker<Boolean, Status> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProblemSolverWorker.class);

    private final Problem<D> problem;
    private final D data;
    private final I input;
    private final SolutionObservable<I> observable;

    public ProblemSolverWorker(final Problem<D> problem, final D data, final I input, final SolutionObservable<I> observable) {
        this.problem = problem;
        this.data = data;
        this.input = input;
        this.observable = observable;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        Thread.sleep(100 + Double.valueOf(StrictMath.random() * 150).longValue());

        return problem.solve(data);
    }

    protected void done() {
        try {
            observable.newSolution(input, get() ? Solution.POSITIVE : Solution.NEGATIVE);
        } catch (final InterruptedException | CancellationException e) {
            observable.newSolution(input, Solution.INTERRUPTED);
        } catch (final ExecutionException e) {
            LOGGER.error(e.getMessage(), e);

            observable.newSolution(input, Solution.ERROR);
        }
    }

}
