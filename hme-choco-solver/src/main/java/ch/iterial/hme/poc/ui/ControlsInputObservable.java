package ch.iterial.hme.poc.ui;

import ch.iterial.hme.poc.model.input.Timestamped;

import java.util.Observable;

public class ControlsInputObservable extends Observable {

    public static final class Data {
        public final Timestamped inputMessage;
        public final ControlsAction controlsAction;

        public Data(final ControlsAction controlsAction) {
            this(null, controlsAction);
        }

        public Data(final Timestamped inputMessage, final ControlsAction controlsAction) {
            this.inputMessage = inputMessage;
            this.controlsAction = controlsAction;
        }
    }

    public void newInputMessage(final Data data) {
        setChanged();
        notifyObservers(data);
    }

}
