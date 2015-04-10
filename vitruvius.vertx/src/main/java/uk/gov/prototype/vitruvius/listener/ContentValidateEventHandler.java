package uk.gov.prototype.vitruvius.listener;

import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Json;
import uk.gov.prototype.vitruvius.parser.validator.ContentValidator;
import uk.gov.prototype.vitruvius.parser.validator.ValidationMessage;

import java.util.List;

public class ContentValidateEventHandler implements HandlerWithAddress<Message<JsonObject>> {


    private final ContentValidator contentValidator = new ContentValidator();

    @Override
    public void handle(Message<JsonObject> event) {
        String markDownContent = event.body().getString("markdown.content");
        List<ValidationMessage> messages = contentValidator.validate(markDownContent);
        event.reply(Json.encode(messages));
    }

    @Override
    public String handlerAddress() {
        return "vitruvius.data.validation";
    }
}
