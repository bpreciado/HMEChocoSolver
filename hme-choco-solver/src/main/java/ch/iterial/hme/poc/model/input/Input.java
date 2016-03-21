package ch.iterial.hme.poc.model.input;

import java.io.Serializable;

/**
 * Marker interface for the program input data.
 * <br>
 * Raw input that will be transformed into problem data using the corresponding factory.
 * All classes bound to different types of XML input files must implement this interface.
 *
 * @see ch.iterial.hme.poc.model.ProblemData
 * @see ch.iterial.hme.poc.model.ProblemDataFactory
 */
public interface Input extends Serializable {
}
