package uk.gov.prototype.vitruvius.parser.github;

import org.apache.commons.lang.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class GitHubUriParser {

    private String uri;
    private String PUBLIC_GITHUB_API_URL = "api.github.com";
    private boolean isUsingPublicAPI = false;
    private String SEPARATOR = "/";
    private String REPOS = "repos";
    private boolean isGitHub = false;

    public GitHubUriParser(String uri) {
        this.uri = uri;
        if (uri.contains("api.github.com")) {
            isUsingPublicAPI = true;
        }
        if(uri.contains("github.com")){
            isGitHub = true;
        }
    }

    public String clientHost() {
        return isUsingPublicAPI? StringUtils.substringBefore(uri, SEPARATOR + REPOS).replace("https://", ""): PUBLIC_GITHUB_API_URL;
    }

    public String repoName() {
        return isUsingPublicAPI? StringUtils.substringAfter(uri, SEPARATOR + REPOS + SEPARATOR).split(SEPARATOR)[1]: uri.substring(uri.lastIndexOf('/') + 1);
    }

    public String owner() {

        return isUsingPublicAPI? StringUtils.substringAfter(uri, SEPARATOR + REPOS + SEPARATOR).split(SEPARATOR)[0] : getOwnerName();
    }

    private String getOwnerName() {
        try {
            URL url = new URL(uri);
            return url.getPath().trim().split(SEPARATOR)[1];

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String repoUri() {
        return isGitHub ? "https://" + PUBLIC_GITHUB_API_URL + SEPARATOR +REPOS + SEPARATOR + owner() + SEPARATOR + repoName() : uri;
    }
}
