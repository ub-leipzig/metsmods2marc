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

package de.ubleipzig.xslt.api;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_METHOD;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.apache.camel.LoggingLevel.INFO;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.main.Main;
import org.apache.camel.main.MainListenerSupport;
import org.apache.camel.main.MainSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transformer {
    public static void main(String[] args) throws Exception {
        Transformer transformer = new Transformer();
        transformer.init();
    }

    private void init() throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new TransformRoute());
        main.addMainListener(new Events());
        main.run();
    }

    public static class Events extends MainListenerSupport {

        @Override
        public void afterStart(MainSupport main) {
            System.out.println("Transformer is now started!");
        }

        @Override
        public void beforeStop(MainSupport main) {
            System.out.println("Transformer is now being stopped!");
        }
    }

    public static class TransformRoute extends RouteBuilder {
        private static final Logger LOGGER = LoggerFactory.getLogger(TransformRoute.class);
        private static final String REPO_BASE_URL = "CamelRepoBaseUrl";
        private static final String HTTP_QUERY_CONTEXT = "context";
        private static final String FORMAT = "format";
        private static final String ACCEPT = "Accept";

        public void configure() {

            final PropertiesComponent pc =
                    getContext().getComponent("properties", PropertiesComponent.class);
            pc.setLocation("classpath:application.properties");

            from("jetty:http://{{rest.host}}:{{rest.port}}{{rest.prefix}}?"
                    + "optionsEnabled=true&matchOnUriPrefix=true&sendServerVersion=false"
                    + "&httpMethodRestrict=GET,OPTIONS")
                    .routeId("XmlAccept")
                    .process(e -> e.getIn().setHeader(
                            Exchange.HTTP_URI,
                            e.getIn().getHeader(HTTP_QUERY_CONTEXT)))
                    .log(INFO, LOGGER, "Setting Resource Header : ${headers[CamelHttpUri]}")
                    .removeHeader(ACCEPT)
                    .setHeader(REPO_BASE_URL).simple("{{repo.baseUrl}}")
                    .choice()
                    .when(header(HTTP_METHOD).isEqualTo("OPTIONS"))
                    .to("direct:options")
                    .when(header(FORMAT).isEqualTo("mods"))
                    .to("direct:mods")
                    .when(header(FORMAT).isEqualTo("marc21"))
                    .to("direct:mods")
                    .to("direct:marc21")
                    .when(header(FORMAT).isEqualTo("bibframe"))
                    .to("direct:mods")
                    .to("direct:bibframe")
                    .otherwise()
                    .to("direct:choice");

            from("direct:mods")
                    .routeId("XmlModsXslt")
                    .to("direct:getResource")
                    //.filter(header(HTTP_RESPONSE_CODE).isEqualTo(200))
                    .setHeader(CONTENT_TYPE).constant("application/xml")
                    .convertBodyTo(String.class)
                    .log(INFO, LOGGER,
                            "Converting resource to MODS/XML: ${headers[CamelHttpUri]}")
                    .to("xslt:{{mods.xslt}}?saxon=true&output=string");

            from("direct:marc21")
                    .routeId("XmlMarc21Xslt")
                    .filter(header(HTTP_RESPONSE_CODE).isEqualTo(200))
                    .setHeader(CONTENT_TYPE).constant("application/xml")
                    .convertBodyTo(String.class)
                    .log(INFO, LOGGER,
                            "Converting resource to MARC21/XML: ${headers[CamelHttpUri]}")
                    .to("xslt:{{marc21.xslt}}?saxon=true");

            from("direct:bibframe")
                    .routeId("XmlBibframeXslt")
                    .filter(header(HTTP_RESPONSE_CODE).isEqualTo(200))
                    .setHeader(CONTENT_TYPE).constant("application/xml")
                    .convertBodyTo(String.class)
                    .log(INFO, LOGGER,
                            "Converting resource to RDF/XML: ${headers[CamelHttpUri]}")
                    .to("xslt:{{bibframe.xslt}}?saxon=true");

            from("direct:options")
                    .routeId("XmlOptions")
                    .setHeader(CONTENT_TYPE).constant("text/turtle")
                    .setHeader("Allow").constant("GET,OPTIONS")
                    .to("language:simple:resource:classpath:options.ttl");

            from("direct:choice")
                    .routeId("ChooseFormat")
                    .setHeader(CONTENT_TYPE).constant("text/html")
                    .setHeader("Allow").constant("GET,OPTIONS")
                    .to("language:simple:resource:classpath:index.html");

            from("direct:getResource")
                    .routeId("XmlTransformationCommon")
                    .removeHeader(HTTP_QUERY_CONTEXT)
                    .removeHeader(FORMAT)
                    .to("http4:localhost:8080?throwExceptionOnFailure=false");

        }
    }
}

