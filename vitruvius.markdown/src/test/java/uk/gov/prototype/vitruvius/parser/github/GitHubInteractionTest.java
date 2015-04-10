package uk.gov.prototype.vitruvius.parser.github;

import org.apache.commons.io.FileUtils;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.GitHubResponse;
import org.elasticsearch.common.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryUri;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GitHubInteractionTest {

    private static final String OWNER = "owner";
    private static final String NAME = "repoName";
    private static final String HTTP_URL = "https://github.com/" + OWNER + "/" + NAME;
    private static final String REPO_URI = "https://api.github.com/repos/" + OWNER + "/" + NAME;
    private static final String DESCRIPTION = "This is a test repo";
    private GitHubRepositoryInformationExtractor gitHubRepositoryInformationExtractor;
    @Mock
    private GitHubRepositoryInformationExtractor.RepositoryContainerService repositoryContainerService;

    @Before
    public void setUp() {
        gitHubRepositoryInformationExtractor = new GitHubRepositoryInformationExtractor(repositoryContainerService);
    }

    @Test
    public void testCanGetRepositoryInformationFromGitHub() throws Exception {
        RepositoryUri repositoryUri = new RepositoryUri(NAME, HTTP_URL);

        GitHubClient gitHubClient = mock(GitHubClient.class);

        Repository repository
                = mock(Repository.class);

        when(repository.getHtmlUrl()).thenReturn(HTTP_URL);
        when(repository.generateId()).thenReturn(UUID.randomUUID().toString());
        when(repository.getDescription()).thenReturn(DESCRIPTION);
        Date value = new Date();
        when(repository.getUpdatedAt()).thenReturn(value);

        GitHubResponse response = mock(GitHubResponse.class);

        RepositoryContents repositoryContents = new RepositoryContents();
        File file = new File(this.getClass().getClassLoader().getResource("test-vitruvius.md").getFile());
        String content = FileUtils.readFileToString(file);
        repositoryContents.setContent(Base64.encodeBytes(content.getBytes()));


        when(response.getBody()).thenReturn(repositoryContents);
        when(gitHubClient.get(any(GitHubRequest.class))).thenReturn(response);
        RepositoryContainer repositoryContainer = new RepositoryContainer(repository, gitHubClient);

        when(repositoryContainerService.getRepositoryContainer(repositoryUri)).thenReturn(repositoryContainer);

        RepositoryInformation repoInformation = gitHubRepositoryInformationExtractor.get(repositoryUri);

        verify(repositoryContainerService).createWebHooks(repositoryContainer);
        assertThat(repoInformation).isNotNull();
        assertThat(repoInformation.getRepoUri()).isEqualTo(REPO_URI);
        assertThat(repoInformation.getLink()).isEqualTo(HTTP_URL);
        assertThat(repoInformation.getMeta()).isNotNull();
        assertThat(repoInformation.getMeta().getDescription()).isEqualTo(DESCRIPTION);
        assertThat(repoInformation.getMarkdown()).isEqualTo(content);
    }


}