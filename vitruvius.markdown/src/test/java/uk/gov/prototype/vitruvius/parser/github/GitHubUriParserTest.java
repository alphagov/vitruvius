package uk.gov.prototype.vitruvius.parser.github;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class GitHubUriParserTest{

    private static final String REPO_NAME = "repo";
    private static final String OWNER = "owner";
    private static final String GITHUB_API = "api.github.com";
    private static final String GITHUB_URL = "github.com";
    private static final String API_STRING = "https://"+GITHUB_API+"/repos/"+OWNER+"/"+REPO_NAME;
    private static final String NON_API_STRING = "https://"+GITHUB_URL+"/"+OWNER+"/"+REPO_NAME;

    @Test
    public void testClientHostUsingAPI() throws Exception {
        GitHubUriParser parse = new GitHubUriParser(API_STRING);
        assertThat(parse.clientHost()).isEqualTo(GITHUB_API);
    }

    @Test
    public void testRepoNameUsingAPI() throws Exception {
        GitHubUriParser parse = new GitHubUriParser(API_STRING);
        assertThat(parse.repoName()).isEqualTo(REPO_NAME);
    }

    @Test
    public void testOwnerUsingAPI() throws Exception {
        GitHubUriParser parse = new GitHubUriParser(API_STRING);
        assertThat(parse.owner()).isEqualTo(OWNER);
    }

    @Test
    public void testRepoUriUsingAPI() throws Exception {
        GitHubUriParser parse = new GitHubUriParser(API_STRING);
        assertThat(parse.repoUri()).isEqualTo(API_STRING);
    }

    @Test
    public void testClientHostNotUsingAPI() throws Exception {
        GitHubUriParser parse = new GitHubUriParser(NON_API_STRING);
        assertThat(parse.clientHost()).isEqualTo(GITHUB_API);
    }

    @Test
    public void testRepoNameNotUsingAPI() throws Exception {
        GitHubUriParser parse = new GitHubUriParser(NON_API_STRING);
        assertThat(parse.repoName()).isEqualTo(REPO_NAME);
    }

    @Test
    public void testOwnerNotUsingAPI() throws Exception {
        GitHubUriParser parse = new GitHubUriParser(NON_API_STRING);
        assertThat(parse.owner()).isEqualTo(OWNER);
    }

    @Test
    public void testRepoUriNotUsingAPI() throws Exception {
        GitHubUriParser parse = new GitHubUriParser(NON_API_STRING);
        assertThat(parse.repoUri()).isEqualTo(API_STRING);
    }
}