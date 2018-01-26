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

import static java.nio.file.Paths.get;

import org.junit.Test;

public class IngesterTest {

    @Test
    public void testIngester() {
        String path = get(".").toAbsolutePath().normalize().getParent().toString();
        String xsl = path
                + "/xslt/src/main/resources/de.ubleipzig"
                + ".xslt/MODS3-4_MARC21slim_XSLT1-0.xsl";
        Config config = new Config();
        config.setXsltResource(xsl);
        new Ingester(config);
    }
}
