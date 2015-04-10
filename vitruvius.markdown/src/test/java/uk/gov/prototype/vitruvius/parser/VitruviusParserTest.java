package uk.gov.prototype.vitruvius.parser;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import uk.gov.prototype.vitruvius.parser.domain.CollaboratingProject;
import uk.gov.prototype.vitruvius.parser.domain.Meta;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformationBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class VitruviusParserTest {

    private static final String TEST_MARKDOWN_LOCATION = "test-vitruvius.md";

    @Test
    public void canParseGithubServiceLink() throws IOException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(TEST_MARKDOWN_LOCATION).getPath());
        RepositoryInformationBuilder stub = VitruviusParser.parseMarkdownFileToRepoStub(file);
        assertThat(stub).isNotNull();
        RepositoryInformation repositoryInformation = stub.build();
        assertThat(repositoryInformation).isNotNull();
        List<CollaboratingProject> services = repositoryInformation.getServices();

        CollaboratingProject service1 = services.get(0);
        assertThat(service1.getName()).isEqualTo("PeopleLookUpService");
        assertThat(service1.getDescription()).isEqualTo("Used for looking people up");
        assertThat(service1.getLink()).isEqualTo("../repository/PeopleLookUpService");

    }

    @Test
    public void canParseLocalServiceLink() throws IOException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(TEST_MARKDOWN_LOCATION).getPath());
        RepositoryInformationBuilder stub = VitruviusParser.parseMarkdownFileToRepoStub(file);
        assertThat(stub).isNotNull();
        RepositoryInformation repositoryInformation = stub.build();
        assertThat(repositoryInformation).isNotNull();
        List<CollaboratingProject> services = repositoryInformation.getServices();

        CollaboratingProject service1 = services.get(1);
        assertThat(service1.getLink()).isEqualTo("../repository/PaymentService");
    }

    @Test
    public void canParseLocalComponentLink() throws IOException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(TEST_MARKDOWN_LOCATION).getPath());
        RepositoryInformationBuilder stub = VitruviusParser.parseMarkdownFileToRepoStub(file);
        assertThat(stub).isNotNull();
        RepositoryInformation repositoryInformation = stub.build();
        assertThat(repositoryInformation).isNotNull();
        List<CollaboratingProject> components = repositoryInformation.getComponents();
        assertThat(components).isNotEmpty();
        assertThat(components.size()).isEqualTo(1);
        CollaboratingProject component1 = components.get(0);
        assertThat(component1.getLink()).isEqualTo("component1");
    }

    @Test
    public void canParseDataLink() throws IOException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(TEST_MARKDOWN_LOCATION).getPath());
        RepositoryInformationBuilder stub = VitruviusParser.parseMarkdownFileToRepoStub(file);
        assertThat(stub).isNotNull();
        RepositoryInformation repositoryInformation = stub.build();
        assertThat(repositoryInformation).isNotNull();
        List<CollaboratingProject> data = repositoryInformation.getData();
        assertThat(data).isNotEmpty();
        assertThat(data.size()).isEqualTo(2);
        CollaboratingProject data1 = data.get(0);
        assertThat(data1.getLink()).isEqualTo("../data/Banking-Details");
    }

    @Test
    public void canGenerateHtmlFromMD() throws Exception {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(TEST_MARKDOWN_LOCATION).getPath());
        String content = VitruviusParser.getHtmlOfMarkdown(FileUtils.readFileToString(file));
        assertThat(content).isNotEmpty();
    }

    @Test
    public void canGenerateRepositoryInformationFrom() throws Exception {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(TEST_MARKDOWN_LOCATION).getPath());
        RepositoryInformationBuilder repositoryInformationBuilder = VitruviusParser.generateRepositoryInformationFrom(FileUtils.readFileToString(file));
        assertThat(repositoryInformationBuilder).isNotNull();
        RepositoryInformation repositoryInformation = repositoryInformationBuilder.build();
        Meta meta = repositoryInformation.getMeta();
        assertThat(meta).isNotNull();
        assertThat(meta.getAuthor()).isEqualTo("Amin Mohammed-Coleman <amin.mohammed-coleman@digital.cabinet-office.gov.uk>");
        List<String> tags = new ArrayList<>();
        tags.add("income");
        tags.add("tax");
        assertThat(meta.getTags()).isEqualTo(tags);
        assertThat(repositoryInformation.getComponents().size()).isEqualTo(1);
        assertThat(repositoryInformation.getServices().size()).isEqualTo(2);
        assertThat(repositoryInformation.getData().size()).isEqualTo(2);
        assertThat(repositoryInformation.getLink()).isNull();
        assertThat(repositoryInformation.getRepoUri()).isNull();
        assertThat(repositoryInformation.getServiceName()).isNull();
    }

}