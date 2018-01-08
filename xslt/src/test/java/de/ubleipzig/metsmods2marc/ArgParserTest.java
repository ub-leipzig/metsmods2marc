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

import static de.ubleipzig.metsmods2marc.Constants.OUTPUT_DIR;
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

    @Test
    public void testArgs() {
        parser = new ArgParser();
        final String[] args;
        if (!new File(OUTPUT_DIR).exists()) {
            String path = get(".").toAbsolutePath().normalize().getParent().toString();
            args = new String[]{"-r", path + "/xslt/src/test/resources/MS_152.xml",
                    "-x",
                    path + "/xslt/src/main/resources/de.ubleipzig.metsmods2marc/extract-mods.xsl",
                    "-d", "/tmp"};
        } else {
            String path = get(".").toAbsolutePath().normalize().getParent().toString();
            args = new String[]{"-r", path + "/xslt/src/test/resources/MS_152.xml",
                    "-x",
                    path + "/xslt/src/main/resources/de.ubleipzig.metsmods2marc/extract-mods.xsl",
                    "-d", OUTPUT_DIR};
        }
        final TransferProcess processor = parser.init(args);
        processor.run();
    }
}
