package ch.iterial.hme.poc.model.problem;

public class TimeFrameLatencyProblem extends MaxLatencyProblem {

    private final int timeFrameSeconds;
    private final int criticalDevicesNumber;

    public TimeFrameLatencyProblem(final int criticalDelta, final int timeFrameSeconds, final int criticalDevicesNumber) {
        super(criticalDelta);
        this.timeFrameSeconds = timeFrameSeconds;
        this.criticalDevicesNumber = criticalDevicesNumber;
    }

    public int getTimeFrameSeconds() {
        return timeFrameSeconds;
    }

    public int getCriticalDevicesNumber() {
        return criticalDevicesNumber;
    }

}
