package de.ubleipzig.metsmods2marc;

import static java.nio.file.Paths.get;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;

public class SolrFullRecordToMarcXMLTest {
    private String testResource = "ms152.mrc";
    @Test
    public void testMarcReaderXmlWriter() throws Exception {
        String path = get(".").toAbsolutePath().normalize().toString();
        String abspath = path + "/src/test/resources/" + testResource;
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
        FileOutputStream fos = new FileOutputStream (new File("/tmp/out_" + testResource));
        out.writeTo(fos);
        String result = new String(out.toByteArray());
        System.out.println(result);
        //assertXMLEqual(new String(out.toByteArray()), TestUtils.readFileIntoString("/fromsolr.mrc"));
    }

    public static StringBuffer removeUTFCharacters(String data){
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
