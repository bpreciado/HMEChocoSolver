package ch.iterial.hme.poc.model.input;

import ch.iterial.hme.poc.util.XmlReadingUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@XStreamAlias("MPAPL")
public class BandwidthStatus extends Identified implements BandwidthInput {

    @XStreamAlias("BWListMessageID")
    public Integer messageId;

    @XStreamImplicit
    public List<Device> devices;

    @XStreamAlias("DEV")
    public class Device implements Serializable {

        @XStreamAlias("UID")
        public Integer uid;

        @XStreamImplicit
        public List<Connection> connections;

        @XStreamAlias("AP")
        public class Connection implements Serializable {

            @XStreamAlias("BW")
            public Integer bandwidth;

            @XStreamAlias("SendUID")
            public Integer senderUid;

        }

    }

    public int getTotalBandwidth() {
        return XmlReadingUtils.notNullStreamOf(devices)
                .map(device -> device.connections)
                .flatMap(XmlReadingUtils::notNullStreamOf)
                .collect(Collectors.summingInt(connection -> connection.bandwidth));
    }

}
