package ch.iterial.hme.poc.model.input;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BWReport")
public class BandwidthReport extends Timestamped implements BandwidthInput {

    @XStreamAlias("MaxBW")
    public Integer maxBw;

}
