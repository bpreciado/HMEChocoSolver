package ch.iterial.hme.poc.ui.problem;

import ch.iterial.hme.poc.model.Problem;
import ch.iterial.hme.poc.model.ProblemData;
import ch.iterial.hme.poc.model.input.FaultMessage;
import ch.iterial.hme.poc.model.input.Input;
import ch.iterial.hme.poc.model.problem.FaultMessageProblemDataFactory;
import ch.iterial.hme.poc.ui.SolutionPanel;

public abstract class LatencyRelatedSolutionPanel<D extends ProblemData> extends SolutionPanel<D, FaultMessage> {

    public LatencyRelatedSolutionPanel(final int index,
                                       final String constraint,
                                       final Problem<D> problem,
                                       final FaultMessageProblemDataFactory<D> dataFactory) {
        super(index, constraint, problem, dataFactory);
    }

    @Override
    public boolean supports(final Input input) {
        return input instanceof FaultMessage
                && "Latency".equals(FaultMessage.class.cast(input).faultComm.faultType);
    }

}
