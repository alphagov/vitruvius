package uk.gov.prototype.vitruvius;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;
import uk.gov.prototype.vitruvius.elasticsearch.*;
import uk.gov.prototype.vitruvius.listener.*;
import uk.gov.prototype.vitruvius.parser.RepositoryInformationExtractorFactory;
import uk.gov.prototype.vitruvius.parser.filesystem.FSRepositoryInformationExtractor;
import uk.gov.prototype.vitruvius.parser.github.GitHubRepositoryInformationExtractor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class VitruviusVerticle extends Verticle {


    private static final Logger logger = LoggerFactory.getLogger(VitruviusVerticle.class);

    private RepositoryESOperations repositoryESOperations;
    private RepositoryInformationCache informationCache;

    @Override
    public void start() {
        String outhToken = container.env().get("GITHUB_TOKEN");
        if (StringUtils.isNotEmpty(outhToken)) {
            vertx.sharedData().getMap("config").put("github.token", outhToken);
        }

        informationCache = new RepositoryInformationCache(vertx);

        try {
            logger.debug("repository type : " + RepositoryESOperations.class.getName());
            repositoryESOperations = createRepositoryOperations(container.config());
            repositoryESOperations.initialise();
        } catch (IOException e) {
            throw new RuntimeException("failed to create repository connection");
        }

        GitHubRepositoryInformationExtractor gitHubRepositoryInformationExtractor = new GitHubRepositoryInformationExtractor(new GitHubRepositoryInformationExtractor.RepositoryContainerService(vertx.sharedData()));
        FSRepositoryInformationExtractor fsRepositoryInformationExtractor = new FSRepositoryInformationExtractor(vertx, informationCache, repositoryESOperations);

        RepositoryInformationExtractorFactory factory = new RepositoryInformationExtractorFactory(gitHubRepositoryInformationExtractor, fsRepositoryInformationExtractor);

        register(new RepositoryInformationEventListener(repositoryESOperations));
        register(new ViewDetailEventListener(new HtmlContentGenerator(informationCache, repositoryESOperations, factory)));
        register(new ContentValidateEventHandler());
        register(new RegisterRepositoryEventListener(repositoryESOperations, informationCache, gitHubRepositoryInformationExtractor));
        register(new SearchEventListener(repositoryESOperations));
        register(new AggregationEventListener(repositoryESOperations));
        register(new GitHubCommitEventHandler(vertx, informationCache));

        try {
            new ConfigBasedLoader(repositoryESOperations, factory, informationCache)
                    .preloadFromConfig(new File(container.config().getString("root") + container.config().getString("configLocation")));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("failed to load list from config at : " + container.config().getString("root") + container.config().getString("configLocation"));
            logger.error(e.getMessage());
        }


        vertx.setPeriodic(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS), new ESOptimizeHandler(repositoryESOperations));

    }

    private RepositoryESOperations createRepositoryOperations(JsonObject config) throws IOException {
        boolean embedded = Boolean.valueOf(config.getString("embedded.mode", "true"));
        if (embedded) {
            return new EmbeddedRepositoryESOperations(new EmbeddedElasticSearch(config));
        }
        return new HttpRepositoryESOperations(new HttpESClient(config));
    }


    private void register(HandlerWithAddress handlerWithAddress) {
        vertx.eventBus().registerHandler(handlerWithAddress.handlerAddress(), handlerWithAddress);
    }

    @Override
    public void stop() {
        super.stop();
        repositoryESOperations.shutdown();
    }
}
