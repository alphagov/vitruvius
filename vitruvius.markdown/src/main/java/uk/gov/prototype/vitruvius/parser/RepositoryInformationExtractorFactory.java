package uk.gov.prototype.vitruvius.parser;


import uk.gov.prototype.vitruvius.parser.domain.RepositoryUri;
import uk.gov.prototype.vitruvius.parser.filesystem.FSRepositoryInformationExtractor;
import uk.gov.prototype.vitruvius.parser.github.GitHubRepositoryInformationExtractor;

public class RepositoryInformationExtractorFactory {

    private GitHubRepositoryInformationExtractor gitHubRepositoryInformationExtractor;
    private FSRepositoryInformationExtractor fsRepositoryInformationExtractor;

    public RepositoryInformationExtractorFactory(GitHubRepositoryInformationExtractor gitHubRepositoryInformationExtractor,
                                                 FSRepositoryInformationExtractor fsRepositoryInformationExtractor) {
        this.fsRepositoryInformationExtractor = fsRepositoryInformationExtractor;
        this.gitHubRepositoryInformationExtractor = gitHubRepositoryInformationExtractor;
    }

    public RepositoryInformationExtractor getExtractor(RepositoryUri repositoryUri) {
        return repositoryUri.isGitHub() ? gitHubRepositoryInformationExtractor : fsRepositoryInformationExtractor;
    }

}
