package ch.iterial.hme.poc.model;

import ch.iterial.hme.poc.model.input.Input;

/**
 * Data factory that transforms raw program input data into problem input data
 *
 * @param <InputType> Program input data type
 * @param <DataType>  Problem input data type
 * @see ch.iterial.hme.poc.model.input.Input
 * @see ch.iterial.hme.poc.model.ProblemData
 */
public interface ProblemDataFactory<InputType extends Input, DataType extends ProblemData> {

    /**
     * Creates an instance of the problem input data
     *
     * @param input Raw program input data
     * @return New problem input data instance
     */
    DataType buildProblemData(final InputType input);

}
