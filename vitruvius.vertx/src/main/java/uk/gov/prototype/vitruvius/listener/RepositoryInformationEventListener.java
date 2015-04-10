package uk.gov.prototype.vitruvius.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Json;
import uk.gov.prototype.vitruvius.elasticsearch.RepositoryESOperations;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;

import java.util.List;

public class RepositoryInformationEventListener implements HandlerWithAddress<Message<JsonObject>> {

    public static final String LIST_LISTENER_ADDRESS = "vitruvius.repo.list.address";
    private static final Logger logger = LoggerFactory.getLogger(RepositoryInformationEventListener.class);
    private static final String TYPE_PARAM_NAME = "type";

    private RepositoryESOperations repositoryESOperations;

    public RepositoryInformationEventListener(RepositoryESOperations repositoryESOperations) {
        this.repositoryESOperations = repositoryESOperations;
    }

    @Override
    public void handle(Message<JsonObject> message) {
        JsonObject body = message.body();
        String typeParam = message.body().getString(TYPE_PARAM_NAME);
        String sort = message.body().getString("sort");
        logger.info("message received from bus '{}' ", body);
        try {
            RepositoryESOperations.RepositoryInfoContext context = new RepositoryESOperations.RepositoryInfoContext();
            context.setSortParam(sort);
            context.setTypeParam(typeParam);
            List<RepositoryInformation> repositoryInformation = repositoryESOperations.getListOfRepositoryInformation(context);
            message.reply(Json.encode(repositoryInformation));
        } catch (Exception e) {
            logger.error("failed to load list of type '{}'", typeParam, e);
            message.fail(1, e.getMessage());
        }
    }

    @Override
    public String handlerAddress() {
        return LIST_LISTENER_ADDRESS;
    }
}
