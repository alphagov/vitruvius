package uk.gov.prototype.vitruvius.parser.markdown4jPlugins;

import org.markdown4j.Plugin;
import org.vertx.java.core.json.impl.Json;
import uk.gov.prototype.vitruvius.parser.domain.CollaboratingProject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public abstract class CollaboratingProjectHtmlPlugin extends Plugin {

    public CollaboratingProjectHtmlPlugin(String idPlugin) {
        super(idPlugin);
    }

    public void emit(StringBuilder defaultBuffer, List<String> lines, Map<String, String> params) {
        StringBuilder jsonBuilder = new StringBuilder();
        TextToJsonParser.createLinkedProjectJson(jsonBuilder, lines);

        CollaboratingProject collaboratingProject = Json.decodeValue(jsonBuilder.toString(), CollaboratingProject.class);

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<ul class=\"list-group\">\n");
        try {
            htmlBuilder.append("<li class=\"list-group-item\"><a href=\"/#/details?link=" + URLEncoder.encode(collaboratingProject.getLink(), "UTF-8") +"\">" + collaboratingProject.getName() + "</a></li>\n");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (collaboratingProject.getDescription() != null) {
            htmlBuilder.append("<li class=\"list-group-item\">" + collaboratingProject.getDescription() + "\n");

        }

        htmlBuilder.append("</ul>\n");

        defaultBuffer.append(htmlBuilder.toString());
    }
}
