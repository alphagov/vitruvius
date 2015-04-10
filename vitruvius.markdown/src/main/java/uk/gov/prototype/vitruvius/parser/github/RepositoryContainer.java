package uk.gov.prototype.vitruvius.parser.github;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.vertx.java.core.shareddata.Shareable;

public class RepositoryContainer implements Shareable {
    public Repository repository;
    public GitHubClient gitHubClient;

    public RepositoryContainer(Repository repository, GitHubClient gitHubClient) {
        this.repository = repository;
        this.gitHubClient = gitHubClient;
    }
}