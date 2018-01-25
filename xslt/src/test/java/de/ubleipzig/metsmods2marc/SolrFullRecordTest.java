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

package de.ubleipzig.metsmods2marc;

import static de.ubleipzig.metsmods2marc.MarcXMLWriter.removeUTFCharacters;
import static de.ubleipzig.metsmods2marc.MarcXMLWriter.writeMarcXMLtoFile;
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
        String urlString = "https://index.ub.uni-leipzig.de/solr/biblio";
        HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
        SolrQuery query = new SolrQuery();
        query.addSort("id", SolrQuery.ORDER.asc);
        String cursorMark = CursorMarkParams.CURSOR_MARK_START;
        boolean done = false;
        query.set("q", "title:\"[Theologische Sammelhandschrift] - Ms 152\"");
        query.setRows(50);
        while (!done) {
            query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
            QueryResponse response = solr.query(query);
            String nextCursorMark = response.getNextCursorMark();
            for (SolrDocument doc : response.getResults()) {
                String fr = (String) doc.getFieldValue("fullrecord");
                String id = (String) doc.getFieldValue("id");
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
        String path = get(".").toAbsolutePath().normalize().toString();
        String abspath = path + "/src/test/resources/marc21/" + testResource;
        String content = new String(Files.readAllBytes(Paths.get(abspath)));
        StringBuffer sbf = removeUTFCharacters(content);
        byte[] bytes = sbf.toString().getBytes();
        InputStream input = new ByteArrayInputStream(bytes);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final MarcXmlWriter writer = new MarcXmlWriter(out, true);
        MarcReader reader = new MarcStreamReader(input);
        while (reader.hasNext()) {
            Record record = reader.next();
            writer.write(record);
        }
        writer.close();
        FileOutputStream fos = new FileOutputStream(new File("/tmp/out_" + testResource));
        out.writeTo(fos);
        String result = new String(out.toByteArray());
        System.out.println(result);
        //assertXMLEqual(new String(out.toByteArray()), TestUtils.readFileIntoString("/fromsolr
        // .mrc"));
    }
}
