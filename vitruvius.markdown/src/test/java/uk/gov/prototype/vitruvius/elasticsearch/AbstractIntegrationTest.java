package uk.gov.prototype.vitruvius.elasticsearch;

import org.junit.Ignore;
import org.vertx.java.core.json.JsonObject;

import java.io.IOException;

@Ignore
public abstract class AbstractIntegrationTest {

    protected EmbeddedElasticSearch embeddedElasticSearch;
    protected HttpESClient httpESClient;

    public void setUp() throws IOException {
        JsonObject config = new JsonObject();
        config.putNumber("embedded.search.tcp.port", 9999);
        config.putNumber("search.http.port", 9998);
        config.putString("search.index.dir", "/tmp/");
        config.putString("search.host", "0.0.0.0");
        embeddedElasticSearch = new EmbeddedElasticSearch(config);
        config.putString("search.host", "0.0.0.0:9998");
        embeddedElasticSearch.createIndex();
        embeddedElasticSearch.createMappingForService();
        httpESClient = new HttpESClient(config);
    }

    public void tearDown() {
        httpESClient.shutdown();
        embeddedElasticSearch.shutdown(true);
    }
}
