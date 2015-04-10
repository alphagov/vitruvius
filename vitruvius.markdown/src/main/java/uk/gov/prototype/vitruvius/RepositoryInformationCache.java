package uk.gov.prototype.vitruvius;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.shareddata.ConcurrentSharedMap;
import org.vertx.java.core.shareddata.SharedData;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;

public class RepositoryInformationCache {

    private ConcurrentSharedMap<String, RepositoryInformation> cache;

    private static final Logger logger = LoggerFactory.getLogger(RepositoryInformationCache.class);

    public RepositoryInformationCache(Vertx vertx) {
        SharedData sharedData = vertx.sharedData();
        this.cache = sharedData.getMap("repoStubsMap");
    }


    public void populateCache(RepositoryInformation... repositoryInformations) {
        try {
            for (RepositoryInformation stub : repositoryInformations) {
                cache.put(stub.getRepoUri(), stub);
                logger.debug("Repository information '{}' added to cache", repositoryInformations);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public RepositoryInformation get(String repoUrl) {
        return cache.get(repoUrl);
    }
}
