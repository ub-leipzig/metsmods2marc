/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

        ByteArrayOutputStream baos;
        byte[] bytes;
        ByteArrayInputStream bais;

        final InputStream in = getApacheClientResponse(requestUri, null);
        baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(in, baos);
        bytes = baos.toByteArray();
        OAIData oai;
        bais = new ByteArrayInputStream(bytes);
        oai = getOAIFromStream(bais);
        String resumptionToken;
        resumptionToken = getToken(in, oai);
        Integer cursor;
        cursor = Integer.parseInt(getCursor(oai));
        final Integer listSize = Integer.parseInt(getCompleteListSize(oai));
        bais.reset();
        File targetFile;
        targetFile = new File(target);
        FileUtils.copyInputStreamToFile(bais, targetFile);

        while (cursor < listSize) {
            targetFile = new File(target + "_" + cursor + ".xml");
            final InputStream append = getApacheClientResponse(resumptionUri, resumptionToken);
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
        return projector.io()
                        .stream(in)
                        .read(OAIData.class);
    }

    private static String getToken(final InputStream in, final OAIData oai) throws IOException {
        return getResumptionToken(oai);
    }
}
