package uk.gov.prototype.vitruvius.parser.markdown4jPlugins;

import org.apache.commons.lang.StringUtils;
import org.markdown4j.Plugin;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformationBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MetaDataPlugin extends Plugin {

    private final RepositoryInformationBuilder builder;

    public MetaDataPlugin(RepositoryInformationBuilder builder) {
        super("MetaData");
        this.builder = builder;
    }

    @Override
    public void emit(StringBuilder defaultBuffer, List<String> lines, Map<String, String> params) {

        StringBuffer metaDataBuffer = new StringBuffer();
        metaDataBuffer.append("<h1>Meta Data</h1>");
        metaDataBuffer.append("<pre>");

        Collections.sort(lines);

        for (String data : lines) {
            if (data.contains("<") && data.contains(">")) {
                data = data.replace("<", "&lt;");
                data = data.replace(">", "&gt;");

            }
            metaDataBuffer.append(generateLabel(data)).append("\n");
        }
        metaDataBuffer.append("</pre>");
        defaultBuffer.append(metaDataBuffer.toString());

        StringBuilder stringBuilder = new StringBuilder();
        TextToJsonParser.createMetaDataJson(stringBuilder, lines);
        builder.parseAndAddMeta(stringBuilder.toString());
    }


    private String generateLabel(String data) {
        String label = StringUtils.substringBefore(data, ":");
        return StringUtils.replaceOnce(data, label, StringUtils.capitalize(label));

    }
}
