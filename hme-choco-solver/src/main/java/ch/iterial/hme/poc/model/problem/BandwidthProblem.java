package ch.iterial.hme.poc.model.problem;

import ch.iterial.hme.poc.model.Problem;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

public class BandwidthProblem implements Problem<BandwidthProblemData> {

    @Override
    public boolean solve(final BandwidthProblemData problemData) {
        if (problemData.availableBandwidth == 0 && problemData.totalBandwidth == 0) {
            return true;
        }

        final Solver solver = new Solver(BandwidthProblem.class.getSimpleName());
        final IntVar availableBandwidth = VariableFactory.fixed("AvailableBandwidth", problemData.availableBandwidth, solver);
        final IntVar totalBandwidth = VariableFactory.fixed("TotalBandwidth", problemData.totalBandwidth, solver);
        final Constraint constraint = IntConstraintFactory.arithm(availableBandwidth, ">", VariableFactory.scale(totalBandwidth, 2));
        solver.post(constraint);
        solver.set(IntStrategyFactory.lexico_Split(availableBandwidth, totalBandwidth));

        return solver.findSolution();
    }

}
