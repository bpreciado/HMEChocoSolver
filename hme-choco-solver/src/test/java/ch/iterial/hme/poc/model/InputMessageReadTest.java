package ch.iterial.hme.poc.model;

import ch.iterial.hme.poc.model.input.BandwidthReport;
import ch.iterial.hme.poc.model.input.BandwidthStatus;
import ch.iterial.hme.poc.model.input.FaultMessage;
import ch.iterial.hme.poc.util.XmlReadingUtils;
import com.thoughtworks.xstream.XStream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.time.LocalDate;
import java.time.LocalTime;

@RunWith(JUnit4.class)
public class InputMessageReadTest {

    @Test
    public void testReadingFaultMessages() throws Exception {
        final Resource faultMessageXml = new ClassPathResource("xml/FM02.xml");
        final XStream stream = XmlReadingUtils.createXStreamInstance();

        final Object deSerialized = stream.fromXML(faultMessageXml.getFile());

        Assert.assertNotNull(deSerialized);
        Assert.assertTrue(deSerialized instanceof FaultMessage);

        final FaultMessage faultMessage = FaultMessage.class.cast(deSerialized);
        Assert.assertEquals(777, faultMessage.senderUid.intValue());

        Assert.assertEquals(38, faultMessage.srId.intValue());
        Assert.assertEquals(40, faultMessage.drId.intValue());
        Assert.assertEquals(1, faultMessage.originalUid.intValue());
        Assert.assertEquals("\"V1\"", faultMessage.swtVersion);
        Assert.assertEquals(2, faultMessage.id.intValue());
        Assert.assertEquals(LocalDate.of(2016, 3, 1), faultMessage.validDate);
        Assert.assertEquals(LocalTime.of(2, 4, 30), faultMessage.validTime);

        final FaultMessage.FaultComm faultComm = faultMessage.faultComm;
        Assert.assertEquals(2, faultComm.faultId.intValue());
        Assert.assertEquals("Latency", faultComm.faultType);
        Assert.assertEquals(5, faultComm.actualLatency.intValue());
        Assert.assertEquals(1, faultComm.promisedLatency.intValue());
    }

    @Test
    public void testReadingBandwidthReport() throws Exception {
        final Resource bandwidthReportXml = new ClassPathResource("xml/BWS10.xml");
        final XStream stream = XmlReadingUtils.createXStreamInstance();

        final Object deSerialized = stream.fromXML(bandwidthReportXml.getFile());

        Assert.assertNotNull(deSerialized);
        Assert.assertTrue(deSerialized instanceof BandwidthReport);

        final BandwidthReport bandwidthReport = BandwidthReport.class.cast(deSerialized);
        Assert.assertEquals(LocalDate.of(2016, 3, 1), bandwidthReport.validDate);
        Assert.assertEquals(LocalTime.of(2, 9, 6), bandwidthReport.validTime);

        Assert.assertEquals(Integer.valueOf(34000), bandwidthReport.maxBw);
    }

    @Test
    public void testReadingBandwidthStatus() throws Exception {
        final Resource bandwidthStatusXml = new ClassPathResource("xml/BWR05.xml");
        final XStream stream = XmlReadingUtils.createXStreamInstance();

        final Object deSerialized = stream.fromXML(bandwidthStatusXml.getFile());

        Assert.assertNotNull(deSerialized);
        Assert.assertTrue(deSerialized instanceof BandwidthStatus);

        final BandwidthStatus bandwidthStatus = BandwidthStatus.class.cast(deSerialized);

        Assert.assertEquals(LocalDate.of(2016, 3, 1), bandwidthStatus.validDate);
        Assert.assertEquals(LocalTime.of(2, 6, 6), bandwidthStatus.validTime);

        Assert.assertEquals(2, bandwidthStatus.srId.intValue());
        Assert.assertEquals(65, bandwidthStatus.drId.intValue());
        Assert.assertEquals(1, bandwidthStatus.originalUid.intValue());
        Assert.assertEquals(5, bandwidthStatus.messageId.intValue());
        Assert.assertEquals(2, bandwidthStatus.devices.iterator().next().uid.intValue());
        Assert.assertEquals(1, bandwidthStatus.devices.iterator().next().connections.iterator().next().senderUid.intValue());

        Assert.assertEquals(2501, bandwidthStatus.getTotalBandwidth());
    }

}
