package uk.gov.prototype.vitruvius.parser.markdown4jPlugins;


import net.sourceforge.plantuml.code.TranscoderUtil;
import org.markdown4j.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


public class UmlPlugin extends Plugin {

    private static final Logger logger = LoggerFactory.getLogger(UmlPlugin.class);


    public UmlPlugin() {
        super("Uml");
    }

    @Override
    public void emit(StringBuilder out, List<String> lines, Map<String, String> params) {
        String content = "@startuml\n";
        boolean hasUmlTag = false;
        for (String line : lines) {
            if (!line.equals("@startuml") || line.equals("@enduml")) {
                content = content + "\n" + line;
            } else {
                hasUmlTag = true;
            }
        }

        content = content + "\n";
        if (!hasUmlTag) {
            content = content + "@enduml\n";
        }

        String link;
        try {
            link = generateDiagram(content);
            out.append("<img class=\"img-responsive\" src=\"");
            out.append(link);
            out.append("\"/>");
        } catch (Exception e) {
            logger.error("Exception occurred while trying to generate diagram", e);
            link = "<div class='label label-warning'>Unable to generate UML diagram</div><div><pre>" + content + "</code></pre>";
            out.append(link);

        }
    }


    private String generateDiagram(String text) throws Exception {
        String plantHost = "http://www.plantuml.com/plantuml";
        try {
            checkCanConnectToSite(plantHost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String link = plantHost + "/img/" + TranscoderUtil.getDefaultTranscoder().encode(text);
        logger.debug("Link sent '{}' ", link);
        return link;
    }

    private void checkCanConnectToSite(String plantUml) throws IOException {
        URL url = new URL(plantUml);
        URLConnection conn = url.openConnection();
        conn.connect();
    }


}
