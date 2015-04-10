package uk.gov.prototype.vitruvius;

import org.junit.Before;
import org.junit.Test;
import uk.gov.prototype.vitruvius.elasticsearch.EmbeddedRepositoryESOperations;
import uk.gov.prototype.vitruvius.elasticsearch.EsSearchException;
import uk.gov.prototype.vitruvius.parser.RepositoryInformationExtractorFactory;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryUri;
import uk.gov.prototype.vitruvius.parser.filesystem.FSRepositoryInformationExtractor;
import uk.gov.prototype.vitruvius.parser.github.GitHubRepositoryInformationExtractor;
import uk.gov.prototype.vitruvius.test.helper.StandardTestObjects;

import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class HtmlContentGeneratorTest {

    private HtmlContentGenerator underTest;

    private EmbeddedRepositoryESOperations embeddedElasticSearch = mock(EmbeddedRepositoryESOperations.class);
    private GitHubRepositoryInformationExtractor gitHubRepositoryInformationExtractor = mock(GitHubRepositoryInformationExtractor.class);
    private FSRepositoryInformationExtractor FSRepositoryInformationExtractor = mock(FSRepositoryInformationExtractor.class);

    private RepositoryInformationCache cache = mock(RepositoryInformationCache.class);


    @Before
    public void setup() {

        underTest = new HtmlContentGenerator(cache, embeddedElasticSearch, new RepositoryInformationExtractorFactory(gitHubRepositoryInformationExtractor, FSRepositoryInformationExtractor));

    }

    @Test
    public void testCanGrabMarkdownFromElasticSearch() throws Exception {
        String id = UUID.randomUUID().toString();
        when(cache.get(id)).thenReturn(null);
        when(embeddedElasticSearch.getById(UUID.nameUUIDFromBytes(id.getBytes()).toString())).thenReturn(StandardTestObjects.getStandardService());
        String html = underTest.generateContent(id);
        assertThat(html).isNotNull();
        assertThat(html).isEqualTo("<p>" + StandardTestObjects.TEST_MARKDOWN + "</p>\n");
    }

    @Test
    public void testCanGrabMarkdownFromGitHub() throws Exception {
        String id = "https://github.com/name";
        when(cache.get(id)).thenReturn(null);
        when(embeddedElasticSearch.getById(UUID.nameUUIDFromBytes(id.getBytes()).toString())).thenReturn(null);
        when(gitHubRepositoryInformationExtractor.get(any(RepositoryUri.class))).thenReturn(StandardTestObjects.getStandardService());
        String html = underTest.generateContent(id);
        assertThat(html).isNotNull();
        assertThat(html).isEqualTo("<p>" + StandardTestObjects.TEST_MARKDOWN + "</p>\n");
    }

    @Test
    public void testCanGrabMarkdownFromCache() throws Exception {
        String id = UUID.randomUUID().toString();
        when(cache.get(id)).thenReturn(StandardTestObjects.getStandardService());
        String html = underTest.generateContent(id);
        assertThat(html).isNotNull();
        assertThat(html).isEqualTo("<p>" + StandardTestObjects.TEST_MARKDOWN + "</p>\n");
    }

    @Test
    public void testCanGrabMarkdownFromFileSystem() throws Exception {
        //NOTE - if this test is failing on a local copy it is a file system issue - you need the working directory to be vitruvius.vertx the same as it is when running maven from the command line
        String id = this.getClass().getClassLoader().getResource("carTaxService").getFile();
        when(cache.get(id)).thenReturn(null);
        when(FSRepositoryInformationExtractor.get(any(RepositoryUri.class))).thenReturn(StandardTestObjects.getStandardService());
        String html = underTest.generateContent(id);
        assertThat(html).isNotNull();
        assertThat(html).isNotEmpty();
    }

    @Test(expected = EsSearchException.class)
    public void testCanFailToReturnContentForUnknownId() throws Exception {
        String id = UUID.randomUUID().toString();
        when(cache.get(id)).thenReturn(null);
        when(embeddedElasticSearch.getById(UUID.nameUUIDFromBytes(id.getBytes()).toString())).thenReturn(null);
        underTest.generateContent(id);
    }


}