package ch.iterial.hme.poc.model.problem;

import ch.iterial.hme.poc.model.ProblemDataFactory;
import ch.iterial.hme.poc.model.input.BandwidthInput;
import ch.iterial.hme.poc.model.input.BandwidthReport;
import ch.iterial.hme.poc.model.input.BandwidthStatus;

import java.util.HashMap;

public class BandwidthProblemDataFactory implements ProblemDataFactory<BandwidthInput, BandwidthProblemData> {

    private final BandwidthCache cache = new BandwidthCache();

    @Override
    public BandwidthProblemData buildProblemData(final BandwidthInput input) {
        final BandwidthStatus status = BandwidthStatus.class.cast(cache.get(BandwidthStatus.class));
        final BandwidthReport report = BandwidthReport.class.cast(cache.get(BandwidthReport.class));

        if (status != null && report != null) {
            return new BandwidthProblemData(report.maxBw.intValue(), status.getTotalBandwidth());
        } else {
            return new BandwidthProblemData(0, 0);
        }
    }

    public static final class BandwidthCache extends HashMap<Class<? extends BandwidthInput>, BandwidthInput> {

        public BandwidthCache() {
            super();
        }

        public boolean containsBothKeys() {
            return this.containsKey(BandwidthStatus.class) && this.containsKey(BandwidthReport.class);
        }
    }

    public BandwidthCache getCache() {
        return cache;
    }

}
