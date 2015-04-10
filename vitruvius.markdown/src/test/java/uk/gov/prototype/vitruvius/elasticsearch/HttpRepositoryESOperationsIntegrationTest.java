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

public class HttpRepositoryESOperationsIntegrationTest extends AbstractIntegrationTest {

    private HttpRepositoryESOperations repositoryESOperations;

    @Before
    public void setUp() throws IOException {
        super.setUp();
        repositoryESOperations = new HttpRepositoryESOperations(httpESClient);
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void testRegister() throws Exception {
        RepositoryInformation stub = StandardTestObjects.getStandardService();
        String register = repositoryESOperations.register(stub);
        assertThat(register).contains("\"created\":true");
    }

    @Test
    public void optimize() throws Exception {
        RepositoryInformation newStub = StandardTestObjects.getStandardService();
        String response = httpESClient.index(newStub);
        assertThat(response).isNotEmpty();
        repositoryESOperations.optimise();
    }

    @Test
    public void canGetAggregationWithMultipleWords() throws Exception {
        RepositoryInformation newStub = StandardTestObjects.getStandardServiceWithTagsAndDepartment("\"single\", \"split tag\", \"secondSingle\"", "Ministry of Justice");

        httpESClient.index(newStub);
        TimeUnit.SECONDS.sleep(1);

        String response = repositoryESOperations.aggregations("service");
        assertThat(response.contains("_source")).isFalse();
        assertThat(response.contains("markdown")).isFalse();
        JsonObject jsonResponse = new JsonObject(response);
        JsonObject aggregations = ((JsonObject) jsonResponse.getField("aggregations"));
        JsonArray tags = ((JsonObject) aggregations.getField("tags")).getField("buckets");
        assertThat(tags.size()).isGreaterThanOrEqualTo(3);
        assertThat(tags.contains("split tag"));
        JsonArray status = ((JsonObject) aggregations.getField("statuses")).getField("buckets");
        assertThat(status.size()).isGreaterThanOrEqualTo(1);
        JsonArray departments = ((JsonObject) aggregations.getField("departments")).getField("buckets");
        assertThat(departments.size()).isGreaterThanOrEqualTo(1);
        assertThat(tags.contains("Ministry of Justice"));

    }

    @Test
    public void canGetAll() throws Exception {
        repositoryESOperations.register(StandardTestObjects.getStandardService());
        TimeUnit.SECONDS.sleep(1);
        RepositoryESOperations.RepositoryInfoContext context = new RepositoryESOperations.RepositoryInfoContext();
        context.setTypeParam("all");
        List<RepositoryInformation> listOfRepositoryInformation = repositoryESOperations.getListOfRepositoryInformation(context);
        assertThat(listOfRepositoryInformation.size()).isEqualTo(1);

    }

    @Test
    public void canGetComponents() throws Exception {
        repositoryESOperations.register(StandardTestObjects.getStandardService());
        repositoryESOperations.register(StandardTestObjects.getStandardComponent());
        TimeUnit.SECONDS.sleep(1);
        RepositoryESOperations.RepositoryInfoContext context = new RepositoryESOperations.RepositoryInfoContext();
        context.setTypeParam("component");
        List<RepositoryInformation> listOfRepositoryInformation = repositoryESOperations.getListOfRepositoryInformation(context);
        assertThat(listOfRepositoryInformation.size()).isEqualTo(1);

    }

    @Test
    public void canGetServices() throws Exception {
        repositoryESOperations.register(StandardTestObjects.getStandardService());
        repositoryESOperations.register(StandardTestObjects.getStandardComponent());
        TimeUnit.SECONDS.sleep(1);
        RepositoryESOperations.RepositoryInfoContext context = new RepositoryESOperations.RepositoryInfoContext();
        context.setTypeParam("service");
        List<RepositoryInformation> listOfRepositoryInformation = repositoryESOperations.getListOfRepositoryInformation(context);
        assertThat(listOfRepositoryInformation.size()).isEqualTo(1);

    }
}