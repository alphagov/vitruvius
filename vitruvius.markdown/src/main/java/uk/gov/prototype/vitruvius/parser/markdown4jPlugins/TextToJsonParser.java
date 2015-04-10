package uk.gov.prototype.vitruvius.parser.markdown4jPlugins;


import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.List;

public class TextToJsonParser {

    static void createMetaDataJson(StringBuilder buffer, List<String> lines) {
        buffer.append("{");
        Iterator<String> lineIterator = lines.iterator();
        while (lineIterator.hasNext()) {
            String lineString = lineIterator.next();
            String[] split = lineString.split(":");
            buffer.append("\"");
            buffer.append(split[0].trim());
            buffer.append("\"");
            buffer.append(':');
            if(split[0].trim().equals("tags")){
                buffer.append("[");
                String[] tags = split[1].split(",");
                boolean hasPrevious = false;
                for(String tag : tags){
                    if(hasPrevious){
                        buffer.append(",");
                    }else {
                        hasPrevious = true;
                    }
                    buffer.append("\"");
                    buffer.append(tag.trim());
                    buffer.append("\"");
                }
                buffer.append("]");
            }else {
                buffer.append("\"");
                buffer.append(split[1].trim());
                buffer.append("\"");
            }
            if (lineIterator.hasNext()) {
                buffer.append(',');
            } else {
                buffer.append("}");
            }
        }
    }

    static void createLinkedProjectJson(StringBuilder buffer, List<String> lines) {
        buffer.append("{");

        Iterator<String> lineIterator = lines.iterator();
        while (lineIterator.hasNext()) {
            String lineString = lineIterator.next();

            if (lineString.startsWith("link") && hasHttpLink(lineString)) {
                String linkLabel = StringUtils.substringBefore(lineString, ":").trim();
                String value = StringUtils.substringAfter(lineString, ":").trim();
                buffer.append("\"");
                buffer.append(linkLabel);
                buffer.append("\"");
                buffer.append(':');
                buffer.append("\"");
                buffer.append(value);
                buffer.append("\"");

            } else {
                String[] split = lineString.split(":");
                buffer.append("\"");
                buffer.append(split[0].trim());
                buffer.append("\"");
                buffer.append(':');
                buffer.append("\"");
                buffer.append(split[1].trim());
                buffer.append("\"");
            }
            if (lineIterator.hasNext())
                buffer.append(',');
            else
                buffer.append("}");
        }
    }

    private static boolean hasHttpLink(String lineString) {
        return lineString.contains("http") || lineString.contains("https");
    }
}
