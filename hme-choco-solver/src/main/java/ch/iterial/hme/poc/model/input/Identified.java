package ch.iterial.hme.poc.model.input;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Common base class for the timestamped XML input data that have IDs information tags
 */
public abstract class Identified extends Timestamped {

    @XStreamAlias("SRID")
    public Integer srId;

    @XStreamAlias("DRID")
    public Integer drId;

    @XStreamAlias("OriginaUID")
    public Integer originalUid;

}
