package ch.iterial.hme.poc.model.problem;

import ch.iterial.hme.poc.model.Solution;
import ch.iterial.hme.poc.model.input.FaultMessage;

import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

public class TimeFrameLatencyProblemDataFactory implements FaultMessageProblemDataFactory<LatencyProblemData> {

    private final TimeFrameCache cache;

    public TimeFrameLatencyProblemDataFactory(final int timeFrameSeconds) {
        cache = new TimeFrameCache(timeFrameSeconds);
    }

    @Override
    public LatencyProblemData buildProblemData(final FaultMessage input) {
        return new LatencyProblemData(
                input.senderUid,
                input.faultComm.actualLatency,
                input.faultComm.promisedLatency
        );
    }

    public TimeFrameCache getCache() {
        return cache;
    }

    public static class TimeFrameCache extends LinkedHashMap<FaultMessage, Solution> {

        private final int timeFrameSeconds;

        public TimeFrameCache(final int timeFrameSeconds) {
            super();

            this.timeFrameSeconds = timeFrameSeconds;
        }

        @Override
        protected boolean removeEldestEntry(final Map.Entry<FaultMessage, Solution> eldest) {
            final FaultMessage newestEntry = keySet().stream()
                    .sorted((messageOne, messageTwo) -> messageTwo.compareTo(messageOne))
                    .findFirst()
                    .get();
            return eldest.getKey().timestamp()
                    .plus(timeFrameSeconds, ChronoUnit.SECONDS)
                    .isBefore(newestEntry.timestamp());
        }

    }

}
