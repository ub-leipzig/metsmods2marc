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

package de.ubleipzig.xslt;

import static de.ubleipzig.xslt.MarcXMLWriter.removeUTFCharacters;
import static de.ubleipzig.xslt.MarcXMLWriter.writeMarcXMLtoFile;
import static java.nio.file.Paths.get;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CursorMarkParams;
import org.junit.Test;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;

public class SolrFullRecordTest {
    private String testResource = "fr-ruhnken.mrc";

    @Test
    public void testGetDocumentFromSolr() throws IOException, SolrServerException {
        final String urlString = "https://index.ub.uni-leipzig.de/solr/biblio";
        final HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
        final SolrQuery query = new SolrQuery();
        query.addSort("id", SolrQuery.ORDER.asc);
        String cursorMark = CursorMarkParams.CURSOR_MARK_START;
        boolean done = false;
        query.set("q", "title:\"[Theologische Sammelhandschrift] - Ms 152\"");
        query.setRows(50);
        while (!done) {
            query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
            final QueryResponse response = solr.query(query);
            final String nextCursorMark = response.getNextCursorMark();
            for (SolrDocument doc : response.getResults()) {
                final  String fr = (String) doc.getFieldValue("fullrecord");
                final String id = (String) doc.getFieldValue("id");
                writeMarcXMLtoFile(fr, id);
            }
            if (cursorMark.equals(nextCursorMark)) {
                done = true;
            }
            cursorMark = nextCursorMark;
        }
    }

    @Test
    public void testMarcReaderXmlWriter() throws Exception {
        final String path = get(".").toAbsolutePath().normalize().toString();
        final String abspath = path + "/src/test/resources/marc21/" + testResource;
        final String content = new String(Files.readAllBytes(Paths.get(abspath)));
        final StringBuffer sbf = removeUTFCharacters(content);
        final byte[] bytes = sbf.toString().getBytes();
        final InputStream input = new ByteArrayInputStream(bytes);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final MarcXmlWriter writer = new MarcXmlWriter(out, true);
        final MarcReader reader = new MarcStreamReader(input);
        while (reader.hasNext()) {
            final Record record = reader.next();
            writer.write(record);
        }
        writer.close();
        final FileOutputStream fos = new FileOutputStream(new File("/tmp/out_" + testResource));
        out.writeTo(fos);
        final String result = new String(out.toByteArray());
        System.out.println(result);
        //assertXMLEqual(new String(out.toByteArray()), TestUtils.readFileIntoString("/fromsolr
        // .mrc"));
    }
}
