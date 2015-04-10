package uk.gov.prototype.vitruvius.parser;

import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryUri;

public interface RepositoryInformationExtractor {

    public RepositoryInformation get(RepositoryUri repositoryUri) throws Exception;
}
