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
import static de.ubleipzig.xslt.Constants.REPOSITORY_BASE_URI;
import static java.nio.file.Paths.get;

import java.io.File;
import org.junit.Test;

/**
 * ArgParserTest.
 *
 * @author christopher-johnson
 */
public class ArgParserTest {
    private ArgParser parser;
    private String testFileName = "mods_src/MS_526.xml";
    private String marc21XmlFile = "out_feaadd2e-9f9f-5797-ac2b-c401dd49e8b1.xml";

    @Test
    public void testArgs() {
        parser = new ArgParser();
        final String[] args;
        if (!new File(OUTPUT_DIR).exists()) {
            String path = get(".").toAbsolutePath().normalize().getParent().toString();
            args = new String[]{"-r", path + "/xslt/src/test/resources/" + testFileName,
                    "-d", "/tmp"};
        } else {
            String path = get(".").toAbsolutePath().normalize().getParent().toString();
            args = new String[]{"-r", path + "/xslt/src/test/resources/" + testFileName,
                    "-d", OUTPUT_DIR};
        }
        final TransferProcess processor = parser.init(args);
        processor.run();
    }

    @Test
    public void testOptions() {
        parser = new ArgParser();
        final String[] args;
        String path = get(".").toAbsolutePath().normalize().getParent().toString();
        args = new String[]{"-r", path + "/xslt/src/test/resources/marc21xml/" + marc21XmlFile,
                "-b", "-p", REPOSITORY_BASE_URI, "-d", OUTPUT_DIR};
        final TransferProcess processor = parser.init(args);
        processor.run();
    }
}
