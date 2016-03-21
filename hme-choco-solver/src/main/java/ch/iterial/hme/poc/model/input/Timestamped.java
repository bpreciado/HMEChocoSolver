package ch.iterial.hme.poc.model.input;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Common base class for the XML input data that have "ValidDate" and
 * "ValidTime" timestamp information tags at the root level.
 */
public abstract class Timestamped implements Input, Comparable<Timestamped> {

    @XStreamAlias("ValidDate")
    public LocalDate validDate;

    @XStreamAlias("ValidTime")
    public LocalTime validTime;

    public LocalDateTime timestamp() {
        return LocalDateTime.of(validDate, validTime);
    }

    /**
     * Compare to another timestamped object.
     * <br>
     * Produces the sorting from oldest to newest messages.
     *
     * @param that Timestamped instance to compare to
     * @return Comparison result
     */
    @Override
    @SuppressWarnings("NullableProblems")
    public int compareTo(final Timestamped that) {
        final boolean thisHasTimestamp = this.validDate != null && this.validTime != null;
        final boolean thatHasTimestamp = that.validDate != null && that.validTime != null;

        if (thisHasTimestamp && thatHasTimestamp) {
            return this.timestamp().compareTo(that.timestamp());
        } else if (thisHasTimestamp) {
            return 1;
        } else if (thatHasTimestamp) {
            return -1;
        } else {
            return 0;
        }
    }

}
