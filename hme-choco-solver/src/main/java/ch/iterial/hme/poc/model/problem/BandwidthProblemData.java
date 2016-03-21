package ch.iterial.hme.poc.model.problem;

import ch.iterial.hme.poc.model.ProblemData;

public class BandwidthProblemData implements ProblemData {

    public final int availableBandwidth;
    public final int totalBandwidth;

    public BandwidthProblemData(final int availableBandwidth, final int totalBandwidth) {
        this.availableBandwidth = availableBandwidth;
        this.totalBandwidth = totalBandwidth;
    }

}
