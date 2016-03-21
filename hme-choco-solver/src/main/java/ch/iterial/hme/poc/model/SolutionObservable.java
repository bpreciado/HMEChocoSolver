package ch.iterial.hme.poc.model;

import ch.iterial.hme.poc.model.input.Input;

import java.util.Observable;

public class SolutionObservable<I extends Input> extends Observable {

    public final class Data {
        public final I input;
        public final Solution result;

        public Data(final I input, final Solution result) {
            this.input = input;
            this.result = result;
        }
    }

    public void newSolution(final I input, final Solution result) {
        setChanged();
        notifyObservers(new Data(input, result));
    }

}
