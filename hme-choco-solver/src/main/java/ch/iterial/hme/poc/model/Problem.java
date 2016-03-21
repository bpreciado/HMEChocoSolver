package ch.iterial.hme.poc.model;

/**
 * Interface that describes the generic behaviour of a problem.
 * <br>
 * All problem classes must implement this interface.
 *
 * @param <T> Type of the data that the problem operates.
 * @see ch.iterial.hme.poc.model.ProblemData
 */
public interface Problem<T extends ProblemData> {

    /**
     * Applies the data to the problem logic
     *
     * @param problemData Data to be used for calculating the solution
     * @return Calculation result, true if the problem has a solution, false otherwise
     */
    boolean solve(final T problemData);

}
