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

import static de.ubleipzig.xslt.Constants.MARC_MODS_STYLESHEET;
import static de.ubleipzig.xslt.Constants.METS_MODS_STYLESHEET;
import static de.ubleipzig.xslt.Constants.MODS_BIBFRAME_STYLESHEET;
import static de.ubleipzig.xslt.Constants.MODS_MARC_STYLESHEET;
import static de.ubleipzig.xslt.Constants.OUTPUT_DIR;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.apache.commons.rdf.api.RDFSyntax.JSONLD;
import static org.apache.commons.rdf.api.RDFSyntax.RDFXML;
import static org.slf4j.LoggerFactory.getLogger;

import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.JAXBException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmValue;
import org.apache.commons.rdf.api.Graph;
import org.apache.commons.rdf.jena.JenaRDF;
import org.slf4j.Logger;
import org.trellisldp.api.IOService;
import org.trellisldp.io.JenaIOService;
import org.xmlbeam.XBProjector;

/**
 * Ingester.
 *
 * @author Christopher Johnson
 */
public class Ingester implements TransferProcess {
    private static final Logger logger = getLogger(Ingester.class);
    private static final JenaRDF rdf = new JenaRDF();
    private static final IOService ioService = new JenaIOService(null);
    private final Config config;


    Ingester(final Config config) {
        this.config = config;
    }

    private static Graph getGraph(final InputStream stream) {
        final Graph graph = rdf.createGraph();
        ioService.read(stream, null, RDFXML).forEachOrdered(graph::add);
        return graph;
    }

    @Override
    public void run() {
        logger.info("Running importer...");

        processImport();

    }

    private void processImport() {
        System.setProperty(
                "javax.xml.transform.TransformerFactory",
                "net.sf.saxon.TransformerFactoryImpl");

        final URI resource = config.getResource();
        final URI repositoryBaseUri = config.getRepositoryBaseUri();
        final Boolean bibFrame = config.isBibFrame();
        final File baseDir = config.getBaseDirectory();

        if (!bibFrame) {
            final LocalTime now = LocalTime.now();
            final String resultFile = baseDir + "/marc-output_" + now + ".xml";
            final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            final InputStream xsltfile1 = classloader.getResourceAsStream(
                    "de.ubleipzig.xslt/" + METS_MODS_STYLESHEET);
            final InputStream xsltfile2 = classloader.getResourceAsStream(
                    "de.ubleipzig.xslt/" + MODS_MARC_STYLESHEET);
            final SAXTransformerFactory stf = (SAXTransformerFactory) TransformerFactory
                    .newInstance();

            try {
                final TransformerHandler th1 =
                        stf.newTransformerHandler(stf.newTemplates(
                                new StreamSource(xsltfile1)));
                final TransformerHandler th2 = stf.newTransformerHandler(
                        stf.newTemplates(new StreamSource(xsltfile2)));
                th1.setResult(new SAXResult(th2));
                final StreamResult result = new StreamResult(new File(resultFile));
                th2.setResult(result);
                final Transformer t = stf.newTransformer();
                th2.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
                th2.getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                t.transform(new StreamSource(new File(resource.toString())), new SAXResult(th1));
                logger.info("Writing transformed output to " + resultFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            final XdmValue baseUriValue = new XdmAtomicValue(repositoryBaseUri.toString());
            final LocalTime now = LocalTime.now();
            final String resultFile = baseDir + "/bibframe-output_" + now + ".rdf";
            final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            final InputStream xsltfile1 = classloader.getResourceAsStream(
                    "de.ubleipzig.xslt/" + MARC_MODS_STYLESHEET);
            final InputStream xsltfile2 = classloader.getResourceAsStream(
                    "de.ubleipzig.xslt/" + MODS_BIBFRAME_STYLESHEET);
            final SAXTransformerFactory stf = (SAXTransformerFactory) TransformerFactory
                    .newInstance();

            try {
                final TransformerHandler th1 =
                        stf.newTransformerHandler(stf.newTemplates(
                                new StreamSource(xsltfile1)));
                final TransformerHandler th2 = stf.newTransformerHandler(
                        stf.newTemplates(new StreamSource(xsltfile2)));
                th1.setResult(new SAXResult(th2));
                final StreamResult result = new StreamResult(new File(resultFile));
                th2.setResult(result);
                final Transformer t = stf.newTransformer();
                th2.getTransformer().setParameter("BASEURI", baseUriValue);
                th2.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
                th2.getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                t.transform(new StreamSource(new File(resource.toString())), new SAXResult(th1));
                convertRdfXmltoJsonLdGraph(resultFile);
                logger.info("Writing transformed output to " + resultFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void convertRdfXmltoJsonLdGraph(final String resultFile) throws IOException {
        final File result = new File(resultFile);
        final InputStream fs = new FileInputStream(result);
        final Graph g = getGraph(fs);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ioService.write(g.stream(), out, JSONLD);
        final UUID uuid = UUID.randomUUID();
        final FileOutputStream fos = new FileOutputStream(
                new File(OUTPUT_DIR + "/bibframe_jsonld_out_" + uuid + ".jsonld"));
        out.writeTo(fos);
    }

    private void putResult(final String resultFile) throws IOException, JAXBException {
        final XBProjector projector = new XBProjector(XBProjector.Flags.TO_STRING_RENDERS_XML);
        final RDFData rdf = projector.io().url(resultFile).read(RDFData.class);
        final List<RDFData.Resource> graph = rdf.getGraph();
        rdf.setResource(graph);
        final String contentType = "application/rdf+xml";
        final URI destinationURI = rdf.getResourceURI();
        final ByteArrayInputStream is = new ByteArrayInputStream(rdf.toString().getBytes());
        try {
            ModellerClient.doStreamPut(destinationURI, is, contentType);
        } catch (final ModellerClientFailedException e) {
            System.out.println(getMessage(e));
        }
    }


}
