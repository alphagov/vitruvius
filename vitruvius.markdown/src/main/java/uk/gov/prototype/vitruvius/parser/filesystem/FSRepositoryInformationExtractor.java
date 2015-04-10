package uk.gov.prototype.vitruvius.parser.filesystem;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Vertx;
import uk.gov.prototype.vitruvius.RepositoryInformationCache;
import uk.gov.prototype.vitruvius.elasticsearch.RepositoryESOperations;
import uk.gov.prototype.vitruvius.parser.DateParser;
import uk.gov.prototype.vitruvius.parser.RepositoryInformationExtractor;
import uk.gov.prototype.vitruvius.parser.VitruviusParser;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformationBuilder;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryUri;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class FSRepositoryInformationExtractor implements RepositoryInformationExtractor {

    private static final Logger logger = LoggerFactory.getLogger(FSRepositoryInformationExtractor.class);
    private final Vertx vertx;
    private final RepositoryInformationCache informationCache;
    private final RepositoryESOperations repositoryESOperations;

    private ConcurrentHashMap<String, Thread> fileWatchers = new ConcurrentHashMap<>();


    public FSRepositoryInformationExtractor(Vertx vertx, RepositoryInformationCache informationCache, RepositoryESOperations repositoryESOperations) {

        this.vertx = vertx;
        this.informationCache = informationCache;
        this.repositoryESOperations = repositoryESOperations;
    }

    public RepositoryInformation get(RepositoryUri repositoryUri) throws Exception {
        Path path = FileSystems.getDefault().getPath(repositoryUri.getUri() + "/vitruvius.md");
        RepositoryInformation repositoryInformation = null;
        File file = path.toFile();
        if (file.exists()) {
            registerFileWatcher(repositoryUri.getUri());
            RepositoryInformationBuilder repositoryInformationBuilder = VitruviusParser.parseMarkdownFileToRepoStub(file.getCanonicalFile());
            repositoryInformationBuilder.repoUri(repositoryUri.getUri());
            repositoryInformationBuilder.serviceName(repositoryUri.getName());
            repositoryInformationBuilder.link(repositoryUri.getUri());
            repositoryInformationBuilder.setLastUpdated(DateParser.parse(new Date(file.lastModified())));
            repositoryInformationBuilder.markdown(FileUtils.readFileToString(file));
            repositoryInformation = repositoryInformationBuilder.build();

        }
        return repositoryInformation;
    }

    private void registerFileWatcher(String repoUri) {
        if (fileWatchers.get(repoUri) == null) {
            logger.info("Creating filewatcher for '{}'", repoUri);
            Thread thread = new Thread(new FileChangeWatcher(repoUri, this));
            fileWatchers.put(repoUri, thread);
            thread.start();
        }

    }

    public Vertx getVertx() {
        return vertx;
    }

    public RepositoryESOperations getRepositoryESOperations() {
        return repositoryESOperations;
    }

    public RepositoryInformationCache getInformationCache() {
        return informationCache;
    }

}
