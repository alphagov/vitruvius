package uk.gov.prototype.vitruvius;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.prototype.vitruvius.elasticsearch.RepositoryESOperations;
import uk.gov.prototype.vitruvius.parser.RepositoryInformationExtractorFactory;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.filesystem.FSRepositoryInformationExtractor;
import uk.gov.prototype.vitruvius.parser.github.GitHubRepositoryInformationExtractor;
import uk.gov.prototype.vitruvius.test.helper.StandardTestObjects;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class
        ConfigBasedLoaderTest {

    private static final String TEST_CONFIG_LOCATION = "test-conf.json";

    private File configFile = new File(Thread.currentThread().getContextClassLoader().getResource(TEST_CONFIG_LOCATION).getPath());

    private ConfigBasedLoader underTest;
    @Mock
    private RepositoryESOperations repositoryESOperations;
    @Mock
    private GitHubRepositoryInformationExtractor gitHubRepositoryInformationExtractor;
    @Mock
    private FSRepositoryInformationExtractor fsRepositoryInformationExtractor;
    @Mock
    private RepositoryInformationCache cache;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTest = new ConfigBasedLoader(repositoryESOperations, new RepositoryInformationExtractorFactory(gitHubRepositoryInformationExtractor, fsRepositoryInformationExtractor), cache);
    }

    @Test
    public void testCanParseConfig() throws IOException {
        VitruviusConfig config = underTest.loadConfig(configFile);
        assertThat(config).isNotNull();
        assertThat(config.getPort()).isEqualTo(7000);
        assertThat(config.getOauth_token()).isEqualTo("NOT_A_REAL_OAUTH_TOKEN");
        assertThat(config.getRepositoryUris().size()).isEqualTo(1);
        assertThat(config.getRepositoryUris().get(0).getName()).isEqualTo("Income Service");
    }

    @Test
    public void testGenerateNewRepositoryInformationFromConfigInEsAlready() throws IOException {
        RepositoryInformation standardService = StandardTestObjects.getStandardService();
        when(repositoryESOperations.getById(UUID.nameUUIDFromBytes("incomeTaxService".getBytes()).toString())).thenReturn(standardService);
        underTest.preloadFromConfig(configFile);
    }

    @Test
    public void testGenerateNewRepositoryInformationFromConfigNotEsAlready() throws IOException {
//        when(repositoryESOperations.getById(UUID.nameUUIDFromBytes("incomeTaxService".getBytes()).toString())).thenReturn(null);
//        when(parsingTool.getRepositoryInformation(any(RepositoryUri.class), eq(true))).thenReturn(StandardTestObjects.getStandardService());
//        List<RepositoryInformation> repositoryInformationList = underTest.generateNewRepositoryInformationFromConfig(configFile);
//        assertThat(repositoryInformationList).isNotEmpty();
}

}
