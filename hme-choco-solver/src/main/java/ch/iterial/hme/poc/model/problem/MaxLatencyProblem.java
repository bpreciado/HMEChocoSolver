package ch.iterial.hme.poc.model.problem;

import ch.iterial.hme.poc.model.Problem;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

public class MaxLatencyProblem implements Problem<LatencyProblemData> {

    private final int criticalDelta;

    public MaxLatencyProblem(final int criticalDelta) {
        this.criticalDelta = criticalDelta;
    }

    @Override
    public boolean solve(final LatencyProblemData problemData) {
        final Solver solver = new Solver(MaxLatencyProblem.class.getSimpleName());
        final IntVar actualLatency = VariableFactory.fixed("ActualLatency", problemData.actualLatency, solver);
        final IntVar promisedLatency = VariableFactory.fixed("PromisedLatency", problemData.promisedLatency, solver);
        final Constraint constraint = IntConstraintFactory.arithm(actualLatency, "-", promisedLatency, ">", criticalDelta);
        solver.post(constraint);
        solver.set(IntStrategyFactory.lexico_Split(actualLatency, promisedLatency));

        return !solver.findSolution();
    }

}
