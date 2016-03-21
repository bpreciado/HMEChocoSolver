package ch.iterial.hme.poc.model.problem;

import ch.iterial.hme.poc.model.ProblemData;
import ch.iterial.hme.poc.model.ProblemDataFactory;
import ch.iterial.hme.poc.model.input.FaultMessage;

/**
 * Common interface for the data factories taking the fault message object as the input
 *
 * @param <T> Resulting problem data type
 * @see ch.iterial.hme.poc.model.ProblemData
 * @see ch.iterial.hme.poc.model.input.FaultMessage
 */
public interface FaultMessageProblemDataFactory<T extends ProblemData> extends ProblemDataFactory<FaultMessage, T> {
}
