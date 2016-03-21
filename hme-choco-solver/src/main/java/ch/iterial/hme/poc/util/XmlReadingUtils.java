package ch.iterial.hme.poc.util;

import ch.iterial.hme.poc.model.input.BandwidthReport;
import ch.iterial.hme.poc.model.input.BandwidthStatus;
import ch.iterial.hme.poc.model.input.FaultMessage;
import ch.iterial.hme.poc.model.input.Identified;
import ch.iterial.hme.poc.model.input.Timestamped;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

public abstract class XmlReadingUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static XStream createXStreamInstance() {
        final XStream stream = new XStream(new StaxDriver());
        stream.ignoreUnknownElements();
        stream.processAnnotations(Timestamped.class);
        stream.processAnnotations(Identified.class);
        stream.processAnnotations(FaultMessage.FaultComm.class);
        stream.processAnnotations(FaultMessage.class);
        stream.processAnnotations(BandwidthReport.class);
        stream.processAnnotations(BandwidthStatus.Device.Connection.class);
        stream.processAnnotations(BandwidthStatus.Device.class);
        stream.processAnnotations(BandwidthStatus.class);

        stream.registerConverter(new LocalDateConverter());
        stream.registerConverter(new LocalTimeConverter());

        return stream;
    }

    public static <T> Stream<T> notNullStreamOf(final Collection<T> collection) {
        return collection == null ? Collections.<T>emptyList().stream() : collection.stream();
    }

    private static class LocalDateConverter implements SingleValueConverter {

        @Override
        public String toString(final Object object) {
            return LocalDate.class.cast(object).format(DATE_FORMATTER);
        }

        @Override
        public Object fromString(final String string) {
            return LocalDate.parse(string, DATE_FORMATTER);
        }

        @Override
        public boolean canConvert(final Class type) {
            return type.equals(LocalDate.class);
        }

    }

    private static class LocalTimeConverter implements SingleValueConverter {

        @Override
        public String toString(final Object object) {
            return LocalTime.class.cast(object).format(TIME_FORMATTER);
        }

        @Override
        public Object fromString(final String string) {
            return LocalTime.parse(string, TIME_FORMATTER);
        }

        @Override
        public boolean canConvert(final Class type) {
            return type.equals(LocalTime.class);
        }

    }

}
