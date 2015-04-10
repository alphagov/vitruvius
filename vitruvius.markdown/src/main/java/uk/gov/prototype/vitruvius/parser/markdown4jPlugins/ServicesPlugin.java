package uk.gov.prototype.vitruvius.parser.markdown4jPlugins;

import org.markdown4j.Plugin;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformationBuilder;

import java.util.List;
import java.util.Map;

public class ServicesPlugin extends Plugin{
    private RepositoryInformationBuilder builder;

    public ServicesPlugin(RepositoryInformationBuilder builder) {
        super("Service");
        this.builder = builder;
    }

    @Override
    public void emit(StringBuilder defaultBuffer, List<String> lines, Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        TextToJsonParser.createLinkedProjectJson(stringBuilder, lines);
        builder.parseAndAddService(stringBuilder.toString());
    }

}
