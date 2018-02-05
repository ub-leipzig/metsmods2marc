package de.ubleipzig.xslt.oai;

import static de.ubleipzig.xslt.oai.OAIClient.getApacheClientResponse;
import static de.ubleipzig.xslt.oai.OAIClient.getCompleteListSize;
import static de.ubleipzig.xslt.oai.OAIClient.getCursor;
import static de.ubleipzig.xslt.oai.OAIClient.getResumptionToken;
import static org.xmlbeam.XBProjector.Flags.TO_STRING_RENDERS_XML;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.xmlbeam.XBProjector;

public class OAIClientTest {
    private String requestUri = "http://www.kenom.de/oai/?verb=ListRecords&metadataPrefix=oai_dc&set=institution:DE-15";
    private String resumptionUri = "http://www.kenom.de/oai/?verb=ListRecords&resumptionToken=";
    private String target = "/tmp/oai/OAItargetFile.xml";

    @Test
    public void testGetOAIPagedResponse() throws IOException {
        String resumptionToken;
        OAIData oai;
        Integer cursor;
        File targetFile;
        ByteArrayOutputStream baos;
        byte[] bytes;
        ByteArrayInputStream bais;
        InputStream in = getApacheClientResponse(requestUri,null);
        baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(in, baos);
        bytes = baos.toByteArray();
        bais = new ByteArrayInputStream(bytes);
        oai = getOAIFromStream(bais);
        resumptionToken = getToken(in, oai);
        Integer listSize = Integer.parseInt(getCompleteListSize(oai));
        cursor = Integer.parseInt(getCursor(oai));

        bais.reset();
        targetFile = new File(target);
        FileUtils.copyInputStreamToFile(bais, targetFile);

        while (cursor < listSize) {
            targetFile = new File(target + "_" + cursor + ".xml");
            InputStream append = getApacheClientResponse(resumptionUri, resumptionToken);
            baos = new ByteArrayOutputStream();
            org.apache.commons.io.IOUtils.copy(append, baos);
            bytes = baos.toByteArray();
            bais = new ByteArrayInputStream(bytes);
            oai = getOAIFromStream(bais);
            resumptionToken = getToken(bais, oai);
            cursor = Integer.parseInt(getCursor(oai));
            bais.reset();
            FileUtils.copyInputStreamToFile(bais, targetFile);
            if (cursor == 0) {
                break;
            }
        }
    }

    private static OAIData getOAIFromStream(final InputStream in) throws IOException {
        final XBProjector projector = new XBProjector(TO_STRING_RENDERS_XML);
        return projector.io().stream(in).read(OAIData.class);
    }

    private static String getToken(InputStream in, OAIData oai) throws IOException {
        return getResumptionToken(oai);
    }
}
