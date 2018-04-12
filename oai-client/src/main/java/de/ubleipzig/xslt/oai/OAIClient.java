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

package de.ubleipzig.xslt.oai;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * OAIClient.
 */
public class OAIClient {

    /**
     *
     * @param requestUri String
     * @param resumptionToken String
     * @return InputStream
     * @throws IOException
     */
    public static InputStream getApacheClientResponse(final String requestUri, final String resumptionToken) throws
            IOException {
        final String req;
        if (resumptionToken != null) {
            req = requestUri + resumptionToken;
        } else {
            req = requestUri;
        }
        final CloseableHttpClient client = HttpClients.createDefault();
        final HttpGet get = new HttpGet(req);
        final HttpResponse response = client.execute(get);
        final HttpEntity out = response.getEntity();
        return out.getContent();
    }

    public static String getResumptionToken(final OAIData oai) {
        return oai.getResumptionToken();
    }

    public static String getCompleteListSize(final OAIData oai) {
        return oai.getCompleteListSize();
    }

    public static String getCursor(final OAIData oai) {
        return oai.getCursor()
                  .orElse("0");
    }
}
