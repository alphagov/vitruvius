package uk.gov.prototype.vitruvius.listener;


import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.eventbus.impl.JsonObjectMessage;
import org.vertx.java.core.json.JsonObject;
import uk.gov.prototype.vitruvius.RepositoryInformationCache;
import uk.gov.prototype.vitruvius.elasticsearch.RepositoryESOperations;
import uk.gov.prototype.vitruvius.parser.VitruviusParser;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformationBuilder;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryUri;
import uk.gov.prototype.vitruvius.parser.github.GitHubRepositoryInformationExtractor;
import uk.gov.prototype.vitruvius.parser.github.GitHubUriParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RegisterRepositoryEventListenerTest {


    private RegisterRepositoryEventListener underTest;
    @Mock
    private RepositoryESOperations repositoryESOperations;
    @Mock
    private RepositoryInformationCache cache;
    @Mock
    private GitHubRepositoryInformationExtractor gitHubRepositoryInformationExtractor;
    private static final String OWNER = "owner";

    private String vitruviusContent;
    private static final String NAME = "repoName";
    private static final String HTTP_URL = "https://github.com/" + OWNER + "/" + NAME;


    RepositoryUri repositoryUri = new RepositoryUri(NAME, HTTP_URL);

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTest = new RegisterRepositoryEventListener(repositoryESOperations, cache, gitHubRepositoryInformationExtractor);
        URL resource = this.getClass().getClassLoader().getResource("vitruvius.md");
        vitruviusContent = FileUtils.readFileToString(new File(resource.getFile()));
    }

    @Test
    public void canRegisterWithNoErrors() throws Exception {
        JsonObject eventPayload = new JsonObject();
        eventPayload.putString("address.content", "githuburl");
        eventPayload.putString("serviceName.content", "serviceName");
        RepositoryInformation repositoryInformation =  createRepositoryInformationFromMarkdown(repositoryUri,vitruviusContent);
        when(gitHubRepositoryInformationExtractor.get(any(RepositoryUri.class))).thenReturn(repositoryInformation);
        Message<JsonObject> event = new JsonObjectMessage(false, "", eventPayload);

        underTest.handle(event);
        verify(repositoryESOperations).register(any(RepositoryInformation.class));

    }


    public RepositoryInformation createRepositoryInformationFromMarkdown(RepositoryUri repositoryUri, String vitruviusMarkdownAsString) throws IOException {
        RepositoryInformation repositoryInformation;
        RepositoryInformationBuilder repositoryInformationBuilder = VitruviusParser.generateRepositoryInformationFrom(vitruviusMarkdownAsString);
        GitHubUriParser gitHubUriParser = new GitHubUriParser(repositoryUri.getUri());
        repositoryInformationBuilder.repoUri(makeRepoUri(gitHubUriParser));
        repositoryInformationBuilder.serviceName(repositoryUri.getName());
        repositoryInformationBuilder.link(makeGitHubUri(gitHubUriParser
        ));
        repositoryInformation = repositoryInformationBuilder.build();
        return repositoryInformation;
    }

    private String makeGitHubUri(GitHubUriParser repositoryUri) {
        return repositoryUri.clientHost();
    }
    private String makeRepoUri(GitHubUriParser gitHubUriParser) {
        return gitHubUriParser.repoUri();
    }


    @Test
    public void validationErrorDoesNotRegisterMDFile() throws Exception {
        URL resource = this.getClass().getClassLoader().getResource("vitruvius-invalid.md");
        vitruviusContent = FileUtils.readFileToString(new File(resource.getFile()));
        JsonObject eventPayload = new JsonObject();
        eventPayload.putString("address.content", "githuburl");
        eventPayload.putString("serviceName.content", "serviceName");
        RepositoryInformation repositoryInformation = new RepositoryInformationBuilder().markdown(vitruviusContent).build();

        when(gitHubRepositoryInformationExtractor.get(any(RepositoryUri.class))).thenReturn(repositoryInformation);
        Message<JsonObject> event = new JsonObjectMessage(false, "", eventPayload);

        underTest.handle(event);
        verifyNoMoreInteractions(repositoryESOperations);

    }

}
