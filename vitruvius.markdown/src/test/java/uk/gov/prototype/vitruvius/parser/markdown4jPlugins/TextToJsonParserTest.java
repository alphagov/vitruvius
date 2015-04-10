package uk.gov.prototype.vitruvius.parser.markdown4jPlugins;

import org.elasticsearch.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class TextToJsonParserTest {

    @Test
    public void testAppendLinkedProjectRepresentationLocalLink() throws Exception {
        StringBuilder buffer = new StringBuilder();
        List<String> lines = ImmutableList.of("link: ../repository/PaymentService");
        TextToJsonParser.createLinkedProjectJson(buffer, lines);
        assertThat(buffer.toString()).isEqualTo("{\"link\":\"../repository/PaymentService\"}");
    }

    @Test
    public void testAppendLinkedProjectRepresentationSecureGitHubLink() throws Exception {
        StringBuilder buffer = new StringBuilder();
        List<String> lines = ImmutableList.of("link: https://www.github.com");
        TextToJsonParser.createLinkedProjectJson(buffer, lines);
        assertThat(buffer.toString()).isEqualTo("{\"link\":\"https://www.github.com\"}");
    }

    @Test
    public void testAppendLinkedProjectRepresentationUnSecureGitHubLink() throws Exception {
        StringBuilder buffer = new StringBuilder();
        List<String> lines = ImmutableList.of("link: http://www.github.com");
        TextToJsonParser.createLinkedProjectJson(buffer, lines);
        assertThat(buffer.toString()).isEqualTo("{\"link\":\"http://www.github.com\"}");
    }

    @Test
    public void testAppendLinkedProjectRepresentationUnSecureGitHubLink2() throws Exception {
        StringBuilder buffer = new StringBuilder();
        List<String> lines = ImmutableList.of("link: http://www.github.com/http");
        TextToJsonParser.createLinkedProjectJson(buffer, lines);
        assertThat(buffer.toString()).isEqualTo("{\"link\":\"http://www.github.com/http\"}");
    }
}