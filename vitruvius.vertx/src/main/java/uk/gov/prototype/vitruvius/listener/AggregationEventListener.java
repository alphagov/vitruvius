package uk.gov.prototype.vitruvius.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import uk.gov.prototype.vitruvius.elasticsearch.RepositoryESOperations;

public class AggregationEventListener implements HandlerWithAddress<Message<JsonObject>> {

    private RepositoryESOperations repositoryESOperations;
    private static final Logger logger = LoggerFactory.getLogger(AggregationEventListener.class);

    public AggregationEventListener(RepositoryESOperations repositoryESOperations) {
        this.repositoryESOperations = repositoryESOperations;
    }

    @Override
    public void handle(Message<JsonObject> event) {
        logger.info("received message: " + event.body().getField("query"));
        event.reply(repositoryESOperations.aggregations(event.body().<String>getField("query")));
    }

    @Override
    public String handlerAddress() {
        return "vitruvius.aggregations";
    }
}
