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

import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * XmlFileWriter.
 *
 * @author Christopher Johnson
 */
public class XmlFileWriter extends ByteArrayOutputStream {
    protected XmlFileWriter() {
    }

    /**
     * write.
     *
     * @return XMLWriterBuilder
     */
    public static XMLWriterBuilder write() {
        return new XMLWriterBuilder();
    }

    /**
     * marshal.
     *
     * @param resDescriptor ResourceDescriptor
     * @return marshalled output
     * @throws JAXBException Exception
     */
    private static ByteArrayOutputStream marshal(final ResourceDescriptor resDescriptor) throws
            JAXBException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final JAXBContext jaxbContext = JAXBContext.newInstance(ResourceDescriptor.class);
        final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(resDescriptor, out);
        return out;
    }

    /**
     * XMLWriterBuilder.
     */
    public static class XMLWriterBuilder {
        private String rdfBody;

        /**
         * rdfBody.
         *
         * @param rdfBody String
         * @return this
         */
        public XmlFileWriter.XMLWriterBuilder rdfBody(final String rdfBody) {
            this.rdfBody = rdfBody;
            return this;
        }

        /**
         * build.
         *
         * @return marshalled output
         * @throws JAXBException Exception
         */
        public ByteArrayOutputStream build() throws JAXBException {
            final String descriptor = this.rdfBody;
            final ResourceDescriptor resDescriptor = new ResourceDescriptor();
            resDescriptor.setBody(descriptor);
            return XmlFileWriter.marshal(resDescriptor);
        }
    }
}
