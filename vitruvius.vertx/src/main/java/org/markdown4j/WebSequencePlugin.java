package org.markdown4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Copied to handle the fact that if you're not connected to the web then it explodes!
 */
public class WebSequencePlugin extends Plugin {

    public WebSequencePlugin() {
        super("sequence");
    }

    @Override
    public void emit(final StringBuilder out, final List<String> lines, final Map<String, String> params) {
        String style = params.get("style");
        if (style == null) {
            style = "default";
        }
        String content = null;
        for (String line : lines) {
            if (content == null) {
                content = line;
            } else {
                content = content + "\n" + line;
            }
        }

        content = content + "\n";
        String link;
        try {
            /*outbound messaging calls are turned off for the moment due to the data being held
            link = getSequenceDiagram(content, style);
            out.append("<img class=\"img-responsive\" src=\"");*/
            link = "<div class='diagram'>" + content + "</div>";
            out.append(link);
            //out.append("\"/>");
        } catch (Exception e) {
            e.printStackTrace();
            link = "<div class='diagram'>" + content + "</div><div class='label label-warning'>Generated offline</div>";
            out.append(link);

        }


    }

    private String getSequenceDiagram(String text, String style) throws IOException {
        //Build parameter string
        String data = "style=" + style + "&message=" + URLEncoder.encode(text, "UTF-8") + "&apiVersion=1";

        // Send the request
        URL url = new URL("http://www.websequencediagrams.com");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

        //write parameters
        writer.write(data);
        writer.flush();

        // Get the response
        StringBuffer answer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            answer.append(line);
        }
        writer.close();
        reader.close();

        String json = answer.toString();

        int start = json.indexOf("?png=");
        int end = json.indexOf("\"", start);

        if (start != -1 && end != -1) {
            String surl = "http://www.websequencediagrams.com/" + json.substring(start, end);
            return surl;
        }
        return null;

    }
}
