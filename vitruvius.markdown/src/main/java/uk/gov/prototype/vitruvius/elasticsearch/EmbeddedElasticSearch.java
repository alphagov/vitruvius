package uk.gov.prototype.vitruvius.elasticsearch;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.vertx.java.core.json.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * @author <a href="hoang281283@gmail.com">Minh Hoang TO</a>
 * @date: 8/1/14
 */
public class EmbeddedElasticSearch {

    private Node node;


    private final String dataDir;
    private final String cluster;

    public EmbeddedElasticSearch(JsonObject jsonObject) throws IOException {

        dataDir = jsonObject.getString("search.index.dir");
        boolean isHttpEnabled = jsonObject.getBoolean("search.http.enabled", true);
        Integer tcpPort = jsonObject.getInteger("embedded.search.tcp.port", 9300);
        String networkHost = jsonObject.getString("search.host", "0.0.0.0");
        Integer httpPort = jsonObject.getInteger("search.http.port", 9200);

        cluster = UUID.randomUUID().toString();
        ImmutableSettings.Builder b = ImmutableSettings.settingsBuilder()
                .put("http.enabled", isHttpEnabled)
                .put("cluster.name", cluster)
                .put("transport.tcp.port", tcpPort)
                .put("network.host", networkHost)
                .put("http.port", httpPort)
                .put("path.data", dataDir);

        node = nodeBuilder().settings(b.build())
                .local(false)
                .node();
    }

    public void createMappingForService() throws IOException {
        PutMappingRequest putMappingRequest = new PutMappingRequest();

        putMappingRequest.indices(new String[]{"services"}).type("service").source(IndexMapping.getServiceMapping());

        PutMappingResponse putMappingResponse = getClient().admin().indices().putMapping(putMappingRequest).actionGet();
    }

    public void createIndex() {
        getClient().admin().indices().create(new CreateIndexRequest("services")).actionGet();
    }

    public Client getClient() {
        return node.client();
    }

    public void shutdown(boolean clearData) {
        node.close();

        if (clearData) {
            File f = new File(dataDir+cluster);
            if (f.exists() && f.isDirectory()) {
                try {
                    FileUtils.deleteDirectory(f);
                } catch (IOException ioEx) {
                    ioEx.printStackTrace();
                }
            }
        }
    }
}