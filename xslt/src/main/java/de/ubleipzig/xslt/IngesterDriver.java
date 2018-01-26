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

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.xmlbeam.XBProjector;

/**
 * Command-line arguments parser.
 *
 * @author awoods
 * @author escowles
 * @author whikloj
 * @author Christopher Johnson
 * @since 2016-08-29
 */
class IngesterDriver {
    private static final Logger logger = getLogger(IngesterDriver.class);

    private IngesterDriver() {
    }

    /**
     * The main entry point.
     *
     * @param args from the command line
     */
    public static void main(final String[] args) {
        final IngesterDriver driver = new IngesterDriver();

        try {
            driver.run(args);

        } catch (final Exception e) {
            logger.error("Error performing ingest: {}", e.getMessage());
            logger.debug("Stacktrace: ", e);
        }
    }

    /**
     * getRDFProjectionFromURL.
     *
     * @param url String
     * @return XBProjector
     * @throws IOException Exception
     */
    static RDFData getRDFProjectionFromURL(final String url) throws IOException {
        final XBProjector projector = new XBProjector(XBProjector.Flags.TO_STRING_RENDERS_XML);
        return projector.io().url(url).read(RDFData.class);
    }

    /**
     * getResourceURIList.
     *
     * @param rdf RDFData
     * @return ResourceId
     */
    static List<String> getResourceURIList(final RDFData rdf) {
        return rdf.getResourceURIList();
    }

    /**
     * getResources.
     *
     * @param rdf RDFData
     * @return ResourceId
     */
    static List<RDFData.Resource> getResources(final RDFData rdf) {
        return rdf.getResources();
    }

    /**
     * buildRDFDoc.
     *
     * @return XBProjector
     * @throws IOException Exception
     */
    static RDFData buildRDFDoc() throws IOException {
        final XBProjector projector = new XBProjector();
        return projector.projectEmptyDocument(RDFData.class);
    }

    private void run(final String[] args) {
        final ArgParser parser = new ArgParser();
        final TransferProcess processor = parser.parse(args);
        processor.run();
    }


}


