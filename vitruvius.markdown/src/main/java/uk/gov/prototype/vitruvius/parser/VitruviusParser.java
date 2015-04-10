package uk.gov.prototype.vitruvius.parser;

import org.markdown4j.Markdown4jProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformationBuilder;
import uk.gov.prototype.vitruvius.parser.markdown4jPlugins.*;

import java.io.File;
import java.io.IOException;

public class VitruviusParser {

    private static final Logger logger = LoggerFactory.getLogger(VitruviusParser.class);

    public static RepositoryInformationBuilder parseMarkdownFileToRepoStub(File file) throws IOException {
        RepositoryInformationBuilder repositoryInformationBuilder = new RepositoryInformationBuilder();
        Markdown4jProcessor processor = getMarkdownProcessor(repositoryInformationBuilder);
        processor.process(file);
        logger.info("finished parsing from file");
        return repositoryInformationBuilder;
    }

    public static Markdown4jProcessor getMarkdownProcessor(RepositoryInformationBuilder repositoryInformationBuilder) {
        Markdown4jProcessor processor = new Markdown4jProcessor();
        MetaDataPlugin metaDataPlugin = new MetaDataPlugin(repositoryInformationBuilder);
        ServicesPlugin servicesPlugin = new ServicesPlugin(repositoryInformationBuilder);
        ComponentPlugin componentPlugin = new ComponentPlugin(repositoryInformationBuilder);
        DataPlugin dataPlugin = new DataPlugin(repositoryInformationBuilder);

        processor.registerPlugins(metaDataPlugin, servicesPlugin, componentPlugin, dataPlugin);
        return processor;
    }


    public static RepositoryInformationBuilder generateRepositoryInformationFrom(String markdownContent) throws IOException {
        RepositoryInformationBuilder repositoryInformationBuilder = RepositoryInformationBuilder.newInstance();
        Markdown4jProcessor processor = getMarkdownProcessor(repositoryInformationBuilder);
        processor.process(markdownContent);
        logger.info("finished parsing from content");
        return repositoryInformationBuilder;
    }

    private static Markdown4jProcessor getMarkdown4jProcessorForHtml() {
        Markdown4jProcessor processor = new Markdown4jProcessor();
        processor.addHtmlAttribute("class", "img-responsive", "img");
        RepositoryInformationBuilder repositoryInformationBuilder = new RepositoryInformationBuilder();
        MetaDataPlugin metaDataPlugin = new MetaDataPlugin(repositoryInformationBuilder);
        ServicesHtmlPlugin servicesPlugin = new ServicesHtmlPlugin();
        ComponentHtmlPlugin componentPlugin= new ComponentHtmlPlugin();
        DataHtmlPlugin dataPlugin= new DataHtmlPlugin();
        UmlPlugin umlPlugin = new UmlPlugin();
        processor.registerPlugins(metaDataPlugin, servicesPlugin, componentPlugin, umlPlugin, dataPlugin);
        return processor;
    }

    public static String getHtmlOfMarkdown(String markdownAsString) throws IOException {
        Markdown4jProcessor processor = getMarkdown4jProcessorForHtml();
        return processor.process(markdownAsString);
    }


}
