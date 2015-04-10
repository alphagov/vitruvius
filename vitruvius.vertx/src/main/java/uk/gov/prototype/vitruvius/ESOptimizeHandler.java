package uk.gov.prototype.vitruvius;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import uk.gov.prototype.vitruvius.elasticsearch.RepositoryESOperations;

public class ESOptimizeHandler implements Handler<Long> {
    private static final Logger logger = LoggerFactory.getLogger(ESOptimizeHandler.class);

    private RepositoryESOperations repositoryESOperations;

    public ESOptimizeHandler(RepositoryESOperations repositoryESOperations) {
        this.repositoryESOperations = repositoryESOperations;
    }

    @Override
    public void handle(Long event) {
        logger.info("ES optimize being called");
        repositoryESOperations.optimise();
    }
}
