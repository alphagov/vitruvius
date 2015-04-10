package uk.gov.prototype.vitruvius.elasticsearch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.test.helper.StandardTestObjects;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.fest.assertions.api.Assertions.assertThat;

public class EmbeddedElasticSearchIntegrationTest extends AbstractIntegrationTest{

    private EmbeddedRepositoryESOperations embeddedRepositoryESOperations;

    @Before
    public void setUp() throws IOException {
        super.setUp();
        embeddedRepositoryESOperations = new EmbeddedRepositoryESOperations(embeddedElasticSearch);
    }

    @After
    public void tearDown (){
        super.tearDown();
    }

    @Test
    public void testCreateMappingForAll() throws Exception {
            embeddedRepositoryESOperations.register(StandardTestObjects.getStandardService());
        TimeUnit.SECONDS.sleep(1);
        String aggregations = embeddedRepositoryESOperations.aggregations("");
        assertThat(aggregations).isNotNull();
        JsonObject aggregationsObject = new JsonObject(aggregations).getField("aggregations");
        JsonArray tags = ((JsonObject) aggregationsObject.getField("tags")).getArray("buckets");
        assertThat(tags.size()).isEqualTo(1);
        JsonArray statuses = ((JsonObject) aggregationsObject.getField("statuses")).getArray("buckets");
        assertThat(statuses.size()).isEqualTo(1);
    }

    @Test
    public void testCreateMappingForService() throws Exception {
        embeddedRepositoryESOperations.register(StandardTestObjects.getStandardService());
        TimeUnit.SECONDS.sleep(1);
        String aggregations = embeddedRepositoryESOperations.aggregations("type: \"service\"");
        assertThat(aggregations).isNotNull();
        assertThat(aggregations).isNotNull();
        JsonObject aggregationsObject = new JsonObject(aggregations).getField("aggregations");
        JsonArray tags = ((JsonObject) aggregationsObject.getField("tags")).getArray("buckets");
        assertThat(tags.size()).isEqualTo(1);
    }

    @Test
    public void canGetAll() throws Exception {
        embeddedRepositoryESOperations.register(StandardTestObjects.getStandardService());
        TimeUnit.SECONDS.sleep(1);
        RepositoryESOperations.RepositoryInfoContext context = new RepositoryESOperations.RepositoryInfoContext();
        context.setSortParam("serviceName");
        context.setTypeParam("all");
        List<RepositoryInformation> listOfRepositoryInformation = embeddedRepositoryESOperations.getListOfRepositoryInformation(context);
        assertThat(listOfRepositoryInformation.size()).isEqualTo(1);

    }

    @Test
    public void canGetComponents() throws Exception {
        embeddedRepositoryESOperations.register(StandardTestObjects.getStandardService());
        embeddedRepositoryESOperations.register(StandardTestObjects.getStandardComponent());
        TimeUnit.SECONDS.sleep(1);
        RepositoryESOperations.RepositoryInfoContext context = new RepositoryESOperations.RepositoryInfoContext();
        context.setSortParam("serviceName");
        context.setTypeParam("component");
        List<RepositoryInformation> listOfRepositoryInformation = embeddedRepositoryESOperations.getListOfRepositoryInformation(context);
        assertThat(listOfRepositoryInformation.size()).isEqualTo(1);

    }
    @Test
    public void canGetServices() throws Exception {
        embeddedRepositoryESOperations.register(StandardTestObjects.getStandardService());
        embeddedRepositoryESOperations.register(StandardTestObjects.getStandardComponent());
        TimeUnit.SECONDS.sleep(1);
        RepositoryESOperations.RepositoryInfoContext context = new RepositoryESOperations.RepositoryInfoContext();
        context.setSortParam("serviceName");
        context.setTypeParam("service");
        List<RepositoryInformation> listOfRepositoryInformation = embeddedRepositoryESOperations.getListOfRepositoryInformation(context);
        assertThat(listOfRepositoryInformation.size()).isEqualTo(1);

    }
}