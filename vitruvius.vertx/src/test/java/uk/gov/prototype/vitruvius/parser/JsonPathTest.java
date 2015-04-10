package uk.gov.prototype.vitruvius.parser;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JsonPathTest {

    String json = "{\"ref\": \"refs/heads/master\", \"after\": \"56df19a245c968cfbf939da5006be5be0a74ec75\", \"before\": \"afc7cfa8b0a8d08b9277fe10a8e80bd0ff1642fb\", \"created\": false, \"deleted\": false, \"forced\": false, \"compare\": \"https://github.com/aminmc/sample/compare/afc7cfa8b0a8...56df19a245c9\", \"commits\": [\n" +
            "    {\n" +
            "        \"id\": \"56df19a245c968cfbf939da5006be5be0a74ec75\",\n" +
            "        \"distinct\": true,\n" +
            "        \"message\": \"Update vitruvius.md\",\n" +
            "        \"timestamp\": \"2014-09-08T16:48:19+01:00\",\n" +
            "        \"url\": \"https://github.com/aminmc/sample/commit/56df19a245c968cfbf939da5006be5be0a74ec75\",\n" +
            "        \"author\": {\n" +
            "            \"name\": \"Amin Mohammed-Coleman\",\n" +
            "            \"email\": \"aminmc@gmail.com\",\n" +
            "            \"username\": \"aminmc\"\n" +
            "        },\n" +
            "        \"committer\": {\n" +
            "            \"name\": \"John Smith\",\n" +
            "            \"email\": \"aminmc@gmail.com\",\n" +
            "            \"username\": \"aminmc\"\n" +
            "        },\n" +
            "        \"added\": [],\n" +
            "        \"removed\": [],\n" +
            "        \"modified\": [\"vitruvius.md\"]\n" +
            "    }\n" +
            "], \"head_commit\": {\n" +
            "    \"id\": \"56df19a245c968cfbf939da5006be5be0a74ec75\",\n" +
            "    \"distinct\": true,\n" +
            "    \"message\": \"Update vitruvius.md\",\n" +
            "    \"timestamp\": \"2014-09-08T16:48:19+01:00\",\n" +
            "    \"url\": \"https://github.com/aminmc/sample/commit/56df19a245c968cfbf939da5006be5be0a74ec75\",\n" +
            "    \"author\": {\n" +
            "        \"name\": \"Amin Mohammed-Coleman\",\n" +
            "        \"email\": \"aminmc@gmail.com\",\n" +
            "        \"username\": \"aminmc\"\n" +
            "    },\n" +
            "    \"committer\": {\n" +
            "        \"name\": \"Amin Mohammed-Coleman\",\n" +
            "        \"email\": \"aminmc@gmail.com\",\n" +
            "        \"username\": \"aminmc\"\n" +
            "    },\n" +
            "    \"added\": [],\n" +
            "    \"removed\": [],\n" +
            "    \"modified\": [\"vitruvius.md\"]\n" +
            "}, \"repository\": {\n" +
            "    \"id\": 23193390,\n" +
            "    \"name\": \"sample\",\n" +
            "    \"full_name\": \"aminmc/sample\",\n" +
            "    \"owner\": {\n" +
            "        \"name\": \"aminmc\",\n" +
            "        \"email\": \"aminmc@gmail.com\"\n" +
            "    },\n" +
            "    \"private\": false,\n" +
            "    \"html_url\": \"https://github.com/aminmc/sample\",\n" +
            "    \"description\": \"\",\n" +
            "    \"fork\": false,\n" +
            "    \"url\": \"https://github.com/aminmc/sample\",\n" +
            "    \"forks_url\": \"https://api.github.com/repos/aminmc/sample/forks\",\n" +
            "    \"keys_url\": \"https://api.github.com/repos/aminmc/sample/keys{/key_id}\",\n" +
            "    \"collaborators_url\": \"https://api.github.com/repos/aminmc/sample/collaborators{/collaborator}\",\n" +
            "    \"teams_url\": \"https://api.github.com/repos/aminmc/sample/teams\",\n" +
            "    \"hooks_url\": \"https://api.github.com/repos/aminmc/sample/hooks\",\n" +
            "    \"issue_events_url\": \"https://api.github.com/repos/aminmc/sample/issues/events{/number}\",\n" +
            "    \"events_url\": \"https://api.github.com/repos/aminmc/sample/events\",\n" +
            "    \"assignees_url\": \"https://api.github.com/repos/aminmc/sample/assignees{/user}\",\n" +
            "    \"branches_url\": \"https://api.github.com/repos/aminmc/sample/branches{/branch}\",\n" +
            "    \"tags_url\": \"https://api.github.com/repos/aminmc/sample/tags\",\n" +
            "    \"blobs_url\": \"https://api.github.com/repos/aminmc/sample/git/blobs{/sha}\",\n" +
            "    \"git_tags_url\": \"https://api.github.com/repos/aminmc/sample/git/tags{/sha}\",\n" +
            "    \"git_refs_url\": \"https://api.github.com/repos/aminmc/sample/git/refs{/sha}\",\n" +
            "    \"trees_url\": \"https://api.github.com/repos/aminmc/sample/git/trees{/sha}\",\n" +
            "    \"statuses_url\": \"https://api.github.com/repos/aminmc/sample/statuses/{sha}\",\n" +
            "    \"languages_url\": \"https://api.github.com/repos/aminmc/sample/languages\",\n" +
            "    \"stargazers_url\": \"https://api.github.com/repos/aminmc/sample/stargazers\",\n" +
            "    \"contributors_url\": \"https://api.github.com/repos/aminmc/sample/contributors\",\n" +
            "    \"subscribers_url\": \"https://api.github.com/repos/aminmc/sample/subscribers\",\n" +
            "    \"subscription_url\": \"https://api.github.com/repos/aminmc/sample/subscription\",\n" +
            "    \"commits_url\": \"https://api.github.com/repos/aminmc/sample/commits{/sha}\",\n" +
            "    \"git_commits_url\": \"https://api.github.com/repos/aminmc/sample/git/commits{/sha}\",\n" +
            "    \"comments_url\": \"https://api.github.com/repos/aminmc/sample/comments{/number}\",\n" +
            "    \"issue_comment_url\": \"https://api.github.com/repos/aminmc/sample/issues/comments/{number}\",\n" +
            "    \"contents_url\": \"https://api.github.com/repos/aminmc/sample/contents/{+path}\",\n" +
            "    \"compare_url\": \"https://api.github.com/repos/aminmc/sample/compare/{base}...{head}\",\n" +
            "    \"merges_url\": \"https://api.github.com/repos/aminmc/sample/merges\",\n" +
            "    \"archive_url\": \"https://api.github.com/repos/aminmc/sample/{archive_format}{/ref}\",\n" +
            "    \"downloads_url\": \"https://api.github.com/repos/aminmc/sample/downloads\",\n" +
            "    \"issues_url\": \"https://api.github.com/repos/aminmc/sample/issues{/number}\",\n" +
            "    \"pulls_url\": \"https://api.github.com/repos/aminmc/sample/pulls{/number}\",\n" +
            "    \"milestones_url\": \"https://api.github.com/repos/aminmc/sample/milestones{/number}\",\n" +
            "    \"notifications_url\": \"https://api.github.com/repos/aminmc/sample/notifications{?since,all,participating}\",\n" +
            "    \"labels_url\": \"https://api.github.com/repos/aminmc/sample/labels{/name}\",\n" +
            "    \"releases_url\": \"https://api.github.com/repos/aminmc/sample/releases{/id}\",\n" +
            "    \"created_at\": 1408636435,\n" +
            "    \"updated_at\": \"2014-08-21T15:53:55Z\",\n" +
            "    \"pushed_at\": 1410191299,\n" +
            "    \"git_url\": \"git://github.com/aminmc/sample.git\",\n" +
            "    \"ssh_url\": \"git@github.com:aminmc/sample.git\",\n" +
            "    \"clone_url\": \"https://github.com/aminmc/sample.git\",\n" +
            "    \"svn_url\": \"https://github.com/aminmc/sample\",\n" +
            "    \"homepage\": null,\n" +
            "    \"size\": 136,\n" +
            "    \"stargazers_count\": 0,\n" +
            "    \"watchers_count\": 0,\n" +
            "    \"language\": null,\n" +
            "    \"has_issues\": true,\n" +
            "    \"has_downloads\": true,\n" +
            "    \"has_wiki\": true,\n" +
            "    \"forks_count\": 0,\n" +
            "    \"mirror_url\": null,\n" +
            "    \"open_issues_count\": 0,\n" +
            "    \"forks\": 0,\n" +
            "    \"open_issues\": 0,\n" +
            "    \"watchers\": 0,\n" +
            "    \"default_branch\": \"master\",\n" +
            "    \"stargazers\": 0,\n" +
            "    \"master_branch\": \"master\"\n" +
            "}, \"pusher\": {\n" +
            "    \"name\": \"aminmc\",\n" +
            "    \"email\": \"aminmc@gmail.com\"\n" +
            "}}";

    @Test
    public void canGetCommitterName() {
        List<Object> committers = JsonPath.read(json, "$.commits[*].committer.name");
        assertThat(committers.get(0).toString(), is("John Smith"));
    }

    @Test
    public void canGetHtmlUrl() {
        final String htmlUrl = JsonPath.read(json, "$.repository.full_name");
    }
}

