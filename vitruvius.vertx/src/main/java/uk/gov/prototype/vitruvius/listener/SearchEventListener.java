package uk.gov.prototype.vitruvius.listener;

import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import uk.gov.prototype.vitruvius.elasticsearch.RepositoryESOperations;

public class SearchEventListener implements HandlerWithAddress<Message<JsonObject>> {

    private RepositoryESOperations repositoryESOperations;

    public SearchEventListener(RepositoryESOperations repositoryESOperations) {
        this.repositoryESOperations = repositoryESOperations;
    }

    @Override
    public void handle(Message<JsonObject> event) {
        String query = event.body().getString("query");
        event.reply(repositoryESOperations.search(query));

    }

    @Override
    public String handlerAddress() {
        return "vitruvius.search";
    }
}
