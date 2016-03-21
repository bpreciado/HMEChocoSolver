package ch.iterial.hme.poc.model.problem;

import ch.iterial.hme.poc.model.Problem;
import org.apache.commons.lang.ArrayUtils;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class CriticalDeviceProblem implements Problem<CriticalDeviceProblemData> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CriticalDeviceProblem.class);

    private final int[] ids;

    public CriticalDeviceProblem(final List<Integer> ids) {
        this.ids = ArrayUtils.toPrimitive(new LinkedHashSet<>(ids).toArray(new Integer[ids.size()]));
    }

    @Override
    public boolean solve(final CriticalDeviceProblemData problemData) {
        if ("Unknown".equals(problemData.type)) {
            try {
                Thread.sleep(1000000L);
            } catch (final InterruptedException e) {
                LOGGER.warn("Problem solving interrupted!");
            }
        }

        final Solver solver = new Solver(CriticalDeviceProblem.class.getSimpleName());
        final IntVar currentIds = VariableFactory.enumerated("InputIds",
                new int[]{problemData.originalUid, problemData.senderUid},
                solver);
        final Constraint constraint = IntConstraintFactory.member(currentIds, ids);
        solver.post(constraint);
        solver.set(IntStrategyFactory.lexico_Split(currentIds));

        return !solver.findSolution();
    }

    public List<Integer> getIds() {
        return Arrays.asList(ArrayUtils.toObject(ids));
    }

}
