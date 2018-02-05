package de.ubleipzig.xslt.oai;

import java.util.Optional;
import org.xmlbeam.annotation.XBRead;

public interface OAIData {

    @XBRead("//*[local-name()='resumptionToken']")
    String getResumptionToken();

    @XBRead("//*[local-name()='resumptionToken']/@completeListSize")
    String getCompleteListSize();

    @XBRead("//*[local-name()='resumptionToken']/@cursor")
    Optional<String> getCursor();
}
