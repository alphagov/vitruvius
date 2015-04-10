package uk.gov.prototype.vitruvius.parser.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class RepositoryInformationBuilderTest {

    private static final String TEST_LINK = "link.link.com";
    private static final String TEST_COLLABORATING_PROJECT_NAME = "Test Service";
    private static final String TEST_API_URI = "https://api.github.com/repos/owner/service";
    private static final String TEST_NON_API_URI = "https://github.com/owner/service";
    private static final String TEST_META_STATUS = "Draft";
    private static final String TEST_META_DESCRIPTION = "Very brief description of this service.";
    private static final String TEST_META_AUTHOR = "Amin Mohammed-Coleman <amin.mohammed-coleman@digital.cabinet-office.gov.uk>";
    private static final String TEST_META_LAST_UPDATED = "22/08/2014";
    private static final String TEST_META_VERSION = "0.1";
    public static final String TEST_META_STRING = "{\"status\":\"" + TEST_META_STATUS + "\"," +
            "\"description\":\"" + TEST_META_DESCRIPTION + "\"," +
            "\"author\":\"" + TEST_META_AUTHOR + "\"," +
            "\"lastUpdated\":\"" + TEST_META_LAST_UPDATED + "\"," +
            "\"version\":\"" + TEST_META_VERSION + "\"," +
            "\"department\":\"" + "department" + "\"," +
            "\"tags\":" + "[\"test\",\"test\"]" + "," +
            "\"type\":\"" + "Service" + "\"" +
            "}";
    private static final String TEST_COLLABORATING_PROJECT_DESCRIPTION = "Used for looking people up";
    private static final String TEST_COLLABORATING_PROJECT_LINK = "link for the web";
    private static final String TEST_COLLABORATING_PROJECT_STRING = "{\"name\":\"" + TEST_COLLABORATING_PROJECT_NAME + "\"," +
            "\"description\":\"" + TEST_COLLABORATING_PROJECT_DESCRIPTION + "\"," +
            "\"link\":\"" + TEST_COLLABORATING_PROJECT_LINK + "\"" +
            "}";

    private RepositoryInformationBuilder repositoryInformationBuilder;

    @Before
    public void setup() {
        repositoryInformationBuilder = new RepositoryInformationBuilder();
        repositoryInformationBuilder.repoUri(TEST_API_URI);
    }

    @Test
    public void testTransformAndAddMeta() throws Exception {
        repositoryInformationBuilder.parseAndAddMeta(TEST_META_STRING);
        RepositoryInformation repositoryInformation = repositoryInformationBuilder.build();
        assertThat(repositoryInformation).isNotNull();
        Meta meta = repositoryInformation.getMeta();
        assertThat(meta).isNotNull();
        assertThat(meta.getStatus()).isEqualTo(TEST_META_STATUS);
        assertThat(meta.getDescription()).isEqualTo(TEST_META_DESCRIPTION);
        assertThat(meta.getAuthor()).isEqualTo(TEST_META_AUTHOR);
        assertThat(meta.getLastUpdated()).isEqualTo(TEST_META_LAST_UPDATED);
        assertThat(meta.getVersion()).isEqualTo(TEST_META_VERSION);
        assertThat(meta.getDepartment()).isEqualTo("department");
        List<String> tags = new ArrayList<>();
        tags.add("test");
        tags.add("test");
        assertThat(meta.getTags()).isEqualTo(tags);
        assertThat(meta.getType()).isEqualTo("Service");
    }

    @Test
    public void testParseAndAddService() throws Exception {
        repositoryInformationBuilder.parseAndAddService(TEST_COLLABORATING_PROJECT_STRING);
        RepositoryInformation repositoryInformation = repositoryInformationBuilder.build();
        assertThat(repositoryInformation).isNotNull();
        List<CollaboratingProject> services = repositoryInformation.getServices();
        assertThat(services).isNotNull();
        assertThat(services.size()).isEqualTo(1);
        CollaboratingProject collaboratingProject = services.get(0);
        assertThat(collaboratingProject.getName()).isEqualTo(TEST_COLLABORATING_PROJECT_NAME);
        assertThat(collaboratingProject.getDescription()).isEqualTo(TEST_COLLABORATING_PROJECT_DESCRIPTION);
        assertThat(collaboratingProject.getLink()).isEqualTo(TEST_COLLABORATING_PROJECT_LINK);
    }

    @Test
    public void testParseAndAddTwoServices() throws Exception {
        repositoryInformationBuilder.parseAndAddService(TEST_COLLABORATING_PROJECT_STRING);
        repositoryInformationBuilder.parseAndAddService(TEST_COLLABORATING_PROJECT_STRING);
        RepositoryInformation repositoryInformation = repositoryInformationBuilder.build();
        assertThat(repositoryInformation).isNotNull();
        List<CollaboratingProject> services = repositoryInformation.getServices();
        assertThat(services).isNotNull();
        assertThat(services.size()).isEqualTo(2);
        assertThat(repositoryInformation.getComponents()).isEmpty();
    }

    @Test
    public void testParseAndAddTwoComponents() throws Exception {
        repositoryInformationBuilder.parseAndAddComponent(TEST_COLLABORATING_PROJECT_STRING);
        repositoryInformationBuilder.parseAndAddComponent(TEST_COLLABORATING_PROJECT_STRING);
        RepositoryInformation repositoryInformation = repositoryInformationBuilder.build();
        assertThat(repositoryInformation).isNotNull();
        List<CollaboratingProject> services = repositoryInformation.getServices();
        assertThat(services).isEmpty();
        List<CollaboratingProject> components = repositoryInformation.getComponents();
        assertThat(components).isNotEmpty();
        assertThat(components.size()).isEqualTo(2);
    }

    @Test
    public void testRepoUri() throws Exception {
        RepositoryInformation repositoryInformation = repositoryInformationBuilder.build();
        assertThat(repositoryInformation.getRepoUri()).isEqualTo(TEST_API_URI);
    }

    @Test
    public void testRepoUriForNonGitHubUri() throws Exception {
        repositoryInformationBuilder.repoUri(TEST_NON_API_URI);
        RepositoryInformation repositoryInformation = repositoryInformationBuilder.build();
        assertThat(repositoryInformation.getRepoUri()).isEqualTo(TEST_API_URI);
    }

    @Test
    public void testServiceName() throws Exception {
        repositoryInformationBuilder.serviceName(TEST_COLLABORATING_PROJECT_NAME);
        RepositoryInformation repositoryInformation = repositoryInformationBuilder.build();
        assertThat(repositoryInformation.getServiceName()).isEqualTo(TEST_COLLABORATING_PROJECT_NAME);
    }

    @Test
    public void testLink() throws Exception {
        repositoryInformationBuilder.link(TEST_LINK);
        RepositoryInformation repositoryInformation = repositoryInformationBuilder.build();
        assertThat(repositoryInformation.getLink()).isEqualTo(TEST_LINK);
    }

}