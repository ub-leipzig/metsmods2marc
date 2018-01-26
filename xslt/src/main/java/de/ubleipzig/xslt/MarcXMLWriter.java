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

import static de.ubleipzig.xslt.Constants.OUTPUT_DIR;
import static de.ubleipzig.xslt.UUIDType5.NAMESPACE_URL;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;

public class MarcXMLWriter {

    public static void writeMarcXMLtoFile(String content, String id) throws IOException {
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
        UUID marcUUID = UUIDType5.nameUUIDFromNamespaceAndString(NAMESPACE_URL, id);
        FileOutputStream fos = new FileOutputStream(
                new File(OUTPUT_DIR + "/marc21xmlout_" + marcUUID + ".xml"));
        out.writeTo(fos);
    }

    public static StringBuffer removeUTFCharacters(String data) {
        Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
        Matcher m = p.matcher(data);
        StringBuffer buf = new StringBuffer(data.length());
        while (m.find()) {
            String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
            m.appendReplacement(buf, Matcher.quoteReplacement(ch));
        }
        m.appendTail(buf);
        return buf;
    }
}
