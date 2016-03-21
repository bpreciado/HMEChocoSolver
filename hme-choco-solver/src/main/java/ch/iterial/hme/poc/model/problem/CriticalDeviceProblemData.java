package ch.iterial.hme.poc.model.problem;

import ch.iterial.hme.poc.model.ProblemData;

public class CriticalDeviceProblemData implements ProblemData {

    public final String type;
    public final int originalUid;
    public final int senderUid;

    public CriticalDeviceProblemData(final String type, final int originalUid, final int senderUid) {
        this.type = type;
        this.originalUid = originalUid;
        this.senderUid = senderUid;
    }

}
