package uk.gov.prototype.vitruvius.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Json;
import uk.gov.prototype.vitruvius.RepositoryInformationCache;
import uk.gov.prototype.vitruvius.elasticsearch.RepositoryESOperations;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryUri;
import uk.gov.prototype.vitruvius.parser.github.GitHubRepositoryInformationExtractor;
import uk.gov.prototype.vitruvius.parser.validator.ContentValidator;
import uk.gov.prototype.vitruvius.parser.validator.ValidationMessage;

import java.util.ArrayList;
import java.util.List;

public class RegisterRepositoryEventListener implements HandlerWithAddress<Message<JsonObject>> {
    private static final Logger logger = LoggerFactory.getLogger(RegisterRepositoryEventListener.class);
    private RepositoryESOperations repositoryESOperations;
    private RepositoryInformationCache cache;
    private GitHubRepositoryInformationExtractor gitHubRepositoryInformationExtractor;
    private ContentValidator validator = new ContentValidator();

    public RegisterRepositoryEventListener(
            RepositoryESOperations repositoryESOperations,
            RepositoryInformationCache cache,
            GitHubRepositoryInformationExtractor gitHubRepositoryInformationExtractor) {
        this.repositoryESOperations = repositoryESOperations;
        this.cache = cache;
        this.gitHubRepositoryInformationExtractor = gitHubRepositoryInformationExtractor;
    }

    @Override
    public void handle(Message<JsonObject> event) {
        JsonObject body = event.body();
        List<ValidationMessage> messages = registerService(body);
        String repositoryInformation = body.getString("repositoryInformation");

        JsonObject jsonObject = new JsonObject();
        String messagesAsString = Json.encodePrettily(messages);
        jsonObject.putString("messages", messagesAsString);
        jsonObject.putString("repositoryInformation", repositoryInformation);
        event.reply(jsonObject.encode());
    }

    private List<ValidationMessage> registerService(JsonObject body) {

        String gitHubAddress = body.getString("address.content");
        String gitHubName = body.getString("serviceName.content");
        logger.info("trying to register '{}' from '{}'", gitHubName, gitHubAddress);

        List<ValidationMessage> errorMessages = new ArrayList<>();
        try {
            RepositoryInformation repositoryInformation;
            try {
                repositoryInformation = gitHubRepositoryInformationExtractor.get(new RepositoryUri(gitHubName, gitHubAddress));
                body.putString("repositoryInformation", Json.encode(repositoryInformation));
            } catch (Exception e) {
                logger.error("Error occurred while trying to get repository information", e);
                errorMessages.add(new ValidationMessage(e.getMessage(), ValidationMessage.ValidationType.ERROR));
                return errorMessages;
            }
            errorMessages.addAll(validator.validate(repositoryInformation));
            if (!ValidationMessage.hasErrors(errorMessages)) {
                repositoryESOperations.register(repositoryInformation);
                cache.populateCache(repositoryInformation);
            }

        } catch (Exception e) {
            errorMessages.add(ValidationMessage.createErrorMessage("Could not parse vitruvius information from repository located at " + gitHubAddress + " due to " + e.getMessage()));
            logger.error("errors have been thrown", e);
        }
        return errorMessages;
    }

    @Override
    public String handlerAddress() {
        return "vitruvius.register.service";
    }
}
