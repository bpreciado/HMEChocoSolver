package ch.iterial.hme.poc.model.problem;

import ch.iterial.hme.poc.model.ProblemData;

public class LatencyProblemData implements ProblemData {

    public final int senderId;
    public final int actualLatency;
    public final int promisedLatency;

    public LatencyProblemData(final int senderId, final int actualLatency, final int promisedLatency) {
        this.senderId = senderId;
        this.actualLatency = actualLatency;
        this.promisedLatency = promisedLatency;
    }

}
