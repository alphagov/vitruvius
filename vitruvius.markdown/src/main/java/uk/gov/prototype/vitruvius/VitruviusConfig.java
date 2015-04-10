package uk.gov.prototype.vitruvius;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryUri;

import java.util.List;

public class VitruviusConfig {

    public static final String port_s = "port";
    public static final String oauth_token_s = "oauth_token";
    public static final String repos_s = "repos";

    @JsonProperty
    private final int port;
    @JsonProperty
    private final String oauth_token;
    @JsonProperty
    private final List<RepositoryUri> repositoryUris;

    public VitruviusConfig(@JsonProperty(port_s) int port,
                           @JsonProperty(oauth_token_s) String oauth_token,
                           @JsonProperty(repos_s) List<RepositoryUri> repositoryUris) {
        this.port = port;
        this.oauth_token = oauth_token;
        this.repositoryUris = repositoryUris;
    }

    public int getPort() {
        return port;
    }

    public String getOauth_token() {
        return oauth_token;
    }

    public List<RepositoryUri> getRepositoryUris() {
        return repositoryUris;
    }
}
