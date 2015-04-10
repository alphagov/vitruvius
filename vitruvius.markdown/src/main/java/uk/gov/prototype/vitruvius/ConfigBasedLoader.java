package uk.gov.prototype.vitruvius;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.json.impl.Json;
import uk.gov.prototype.vitruvius.elasticsearch.RepositoryESOperations;
import uk.gov.prototype.vitruvius.parser.RepositoryInformationExtractorFactory;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryUri;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class ConfigBasedLoader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigBasedLoader.class);

    private RepositoryESOperations repositoryESOperations;
    private RepositoryInformationCache informationCache;
    private RepositoryInformationExtractorFactory factory;

    public ConfigBasedLoader(RepositoryESOperations repositoryESOperations,
                             RepositoryInformationExtractorFactory factory,
                             RepositoryInformationCache informationCache) {
        this.repositoryESOperations = repositoryESOperations;
        this.informationCache = informationCache;
        this.factory = factory;
    }

    public VitruviusConfig loadConfig(File file) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
        String jsonString = new String(encoded, "utf-8");
        return Json.decodeValue(jsonString, VitruviusConfig.class);
    }

    public void preloadFromConfig(File configFile) throws IOException {
        VitruviusConfig vitruviusConfig = loadConfig(configFile);
        for (RepositoryUri repositoryUri : vitruviusConfig.getRepositoryUris()) {
            RepositoryInformation repositoryInformation = repositoryESOperations.getById(UUID.nameUUIDFromBytes(repositoryUri.getUri().getBytes()).toString());
            if (repositoryInformation == null) {
                try {
                    logger.info(repositoryUri + " is not already registered, registering now");
                    repositoryInformation = factory.getExtractor(repositoryUri).get(repositoryUri);
                    if (repositoryInformation != null) {
                        informationCache.populateCache(repositoryInformation);
                        repositoryESOperations.register(repositoryInformation);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
