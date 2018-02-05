package de.ubleipzig.xslt.oai;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class OAIClient {

    public static InputStream getApacheClientResponse(String requestUri, String resumptionToken) throws IOException {
        String req;
        if (resumptionToken != null) {
           req = requestUri + resumptionToken;
        } else {
           req = requestUri;
        }
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(req);
        HttpResponse response = client.execute(get);
        HttpEntity out = response.getEntity();
        return out.getContent();
    }

    public static String getResumptionToken(final OAIData oai) {
        return oai.getResumptionToken();
    }

    public static String getCompleteListSize(final OAIData oai) {
        return oai.getCompleteListSize();
    }

    public static String getCursor(final OAIData oai) {
        return oai.getCursor().orElse("0");
    }
}
