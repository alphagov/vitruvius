package uk.gov.prototype.vitruvius;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.prototype.vitruvius.elasticsearch.EsSearchException;
import uk.gov.prototype.vitruvius.elasticsearch.RepositoryESOperations;
import uk.gov.prototype.vitruvius.parser.RepositoryInformationExtractorFactory;
import uk.gov.prototype.vitruvius.parser.VitruviusParser;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryUri;

import java.util.UUID;

public class HtmlContentGenerator {

    private static final Logger logger = LoggerFactory.getLogger(HtmlContentGenerator.class);
    private RepositoryInformationCache informationCache;
    private RepositoryESOperations repositoryESOperations;
    private RepositoryInformationExtractorFactory factory;

    public HtmlContentGenerator(RepositoryInformationCache informationCache,
                                RepositoryESOperations repositoryESOperations,
                                RepositoryInformationExtractorFactory factory) {
        this.informationCache = informationCache;
        this.repositoryESOperations = repositoryESOperations;
        this.factory = factory;
    }

    public String generateContent(String repoUrl) throws Exception {
        RepositoryInformation repository = informationCache.get(repoUrl);
        if (repository != null) {
            logger.info("Loaded repository information from cache...");
            return VitruviusParser.getHtmlOfMarkdown(repository.getMarkdown());
        }
        repository = repositoryESOperations.getById(UUID.nameUUIDFromBytes(repoUrl.getBytes()).toString());
        if (repository == null) {
            RepositoryUri repositoryUri = new RepositoryUri(StringUtils.substringAfterLast(repoUrl, "/"), repoUrl);
            repository = factory.getExtractor(repositoryUri).get(repositoryUri);
        }
        if (repository != null) {
            repositoryESOperations.register(repository);
            informationCache.populateCache(repository);
            return VitruviusParser.getHtmlOfMarkdown(repository.getMarkdown());
        }
        throw new EsSearchException("Could not locate repositoryInformation for repository " + repoUrl);
    }

}
