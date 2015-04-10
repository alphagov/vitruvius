package uk.gov.prototype.vitruvius.elasticsearch;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Json;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.*;

public class HttpESClient {

    Logger LOGGER = LoggerFactory.getLogger(HttpESClient.class);

    private final Client client;
    private final String baseUrl;
    private String services = "services";
    private String searchUrl = "_search";
    private String service = "service";
    private Logger logger = LoggerFactory.getLogger(HttpESClient.class);

    public HttpESClient(JsonObject jsonObject) {
        String networkHost = jsonObject.getString("search.host", "0.0.0.0:9200");

        baseUrl = "http://" + networkHost + '/';

        ClientConfig clientConfig = new DefaultClientConfig();
        client = Client.create(clientConfig);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void shutdown() {
        client.destroy();
    }

    public String search(String query) {
        return search(query, services, service);
    }

    public String search(String query, String collection, String object) {
        WebResource webResource = client.resource(baseUrl + collection + '/' + object + "/" + searchUrl);
        ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, query);
        int status = response.getStatus();
        if (status == BAD_REQUEST.getStatusCode() || status == NOT_FOUND.getStatusCode() ||
                status == INTERNAL_SERVER_ERROR.getStatusCode())
            throw new RuntimeException("Search failure \n" + status + " : " + response + "\nreason : " + generateResponse(response));
        return generateResponse(response);
    }

    public String index(RepositoryInformation stub) throws Exception {
        String encodedRi = Json.encode(stub);
        LOGGER.info("trying to index : " + stub.getServiceName());
        return index(encodedRi, services, service, UUID.nameUUIDFromBytes(stub.getRepoUri().getBytes()).toString());
    }

    public String index(String json, String collection, String objectType, String id) {
        WebResource webResource = client.resource(baseUrl + collection + '/' + objectType + '/' + id);
        ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, json);
        return generateResponse(response);
    }

    public String generateResponse(ClientResponse response) {
        InputStream in = response.getEntityInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }

    public Client getClient() {
        return client;
    }

    public String createMappingForService() {
        try {
            String mapping = IndexMapping.getServiceMapping();
            WebResource webResource = client.resource(baseUrl + services + '/' + service + "/_mapping");
            ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, mapping);
            if (response.getStatus() == BAD_REQUEST.getStatusCode() || response.getStatus() == NOT_FOUND.getStatusCode() || response.getStatus() == INTERNAL_SERVER_ERROR.getStatusCode()) {
                throw new RuntimeException("failed to create mapping : " + response.getStatus() + " : " + response);
            }
            return response.toString();
        } catch (Exception e) {
            throw new RuntimeException("failed to create mapping");
        }
    }

    public void createIndex() {
        WebResource webResource = client.resource(baseUrl + services);
        ClientResponse response = webResource.head();
        if (response.getStatus() == 404) {
            logger.info("index does not exist - creating now");
            webResource.put();
        }
    }

}
