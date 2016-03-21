package ch.iterial.hme.poc.model.input;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("FaultMessage")
public class FaultMessage extends Identified {

    @XStreamAlias("FaultMessageID")
    public Integer id;

    @XStreamAlias("SWTVer")
    public String swtVersion;

    @XStreamAlias("Faultcomm")
    public FaultComm faultComm;

    @XStreamAlias("SenderUID")
    public Integer senderUid;

    @XStreamAlias("Faultcomm")
    public static class FaultComm implements Serializable {

        @XStreamAlias("FaultID")
        public Integer faultId;

        @XStreamAlias("FaultType")
        public String faultType;

        @XStreamAlias("ActualLatency")
        public Integer actualLatency;

        @XStreamAlias("PromisedLatency")
        public Integer promisedLatency;

    }
}
