package uk.gov.prototype.vitruvius.listener;

import com.jayway.jsonpath.JsonPath;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Json;
import uk.gov.prototype.vitruvius.Committer;
import uk.gov.prototype.vitruvius.RepositoryInformationCache;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.validator.ValidationMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;

public class GitHubCommitEventHandler implements HandlerWithAddress<Message<JsonObject>> {

    private static final Logger logger = LoggerFactory.getLogger(GitHubCommitEventHandler.class);

    private Vertx vertx;
    private RepositoryInformationCache informationCache;

    public GitHubCommitEventHandler(Vertx vertx, RepositoryInformationCache informationCache) {
        this.vertx = vertx;
        this.informationCache = informationCache;
    }

    @Override
    public void handle(Message<JsonObject> event) {
        String jsonEvent = event.body().encodePrettily();

        List<Object> refs = JsonPath.read(jsonEvent, "$.commits[?]", filter(where("modified").is("vitruvius.md")));
        logger.info("any updates..." + refs);
        if (refs != null && refs.size() != 0) {
            String key = getKey(jsonEvent);
            logger.info("key " + key);
            if (key != null) {
                List<String> committers = JsonPath.read(jsonEvent, "$.commits[*].committer.name");
                String committer = Arrays.toString(committers.toArray(new String[committers.size()])).replaceAll("\\[|\\]", "");
                String updatedOn = getUpdatedOnDate(jsonEvent);

                Committer committedBy = new Committer(key, committer, updatedOn);
                final JsonObject jsonObject = new JsonObject();

                RepositoryInformation repositoryInformation = informationCache.get(key);

                jsonObject.putString("uri", key);
                jsonObject.putString("address.content", committedBy.getRepoUri());
                jsonObject.putString("serviceName.content", repositoryInformation.getServiceName());
                jsonObject.putString("updatedBy", committedBy.getUpdatedBy());
                jsonObject.putString("updatedOn", committedBy.getUpdatedOn());

                vertx.eventBus().send("vitruvius.register.service", jsonObject, new Handler<Message<JsonObject>>() {
                    @Override
                    public void handle(Message<JsonObject> event) {
                        String messagesAsString = event.body().getString("messages");
                        List messages = Json.decodeValue(messagesAsString, List.class);
                        List<ValidationMessage> transformed = new ArrayList<>();
                        for (Object message : messages) {
                            transformed.add((ValidationMessage)Json.decodeValue(Json.encodePrettily(message), ValidationMessage.class));
                        }
                        if (!ValidationMessage.hasErrors(transformed)) {
                            vertx.eventBus().publish("vitruvius.data.reloaded", jsonObject.encode());
                        }
                    }
                });

            }
        }
    }

    private String getUpdatedOnDate(String jsonEvent) {
        try {
            List<String> updatedOnList = JsonPath.read(jsonEvent, "$.commits[*].timestamp");
            return new DateTime(updatedOnList.get(0)).toDate().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new Date().toString();
        }
    }

    private String getKey(String event) {
        String name = JsonPath.read(event, "$.repository.full_name");
        logger.info("name URL '{}'", name);
        if (name == null) {
            return null;
        }
        return "https://api.github.com/repos/" + name;
    }

    @Override
    public String handlerAddress() {
        return "vitruvius.github.integration";
    }
}
