package uk.gov.prototype.vitruvius.listener;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Json;
import org.vertx.testtools.TestVerticle;
import org.vertx.testtools.VertxAssert;
import uk.gov.prototype.vitruvius.parser.validator.ValidationMessage;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class ContentValidateEventHandlerTest extends TestVerticle {

    private ContentValidateEventHandler underTest;

    @Test
    public void canValidateInvalidContent() throws Exception {

        underTest = new ContentValidateEventHandler();

        EventBus eb = vertx.eventBus();
        eb.registerHandler(underTest.handlerAddress(), underTest);

        JsonObject eventPayload = new JsonObject();

        String fileName = this.getClass().getClassLoader().getResource("vitruvius-invalid.md").getFile();
        String invalidMarkdown = FileUtils.readFileToString(new File(fileName));

        eventPayload.putString("markdown.content", invalidMarkdown);

        eb.send(underTest.handlerAddress(), eventPayload, new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> reply) {
                String data = reply.body();
                List<ValidationMessage> messages = Json.decodeValue(data, List.class);
                VertxAssert.assertThat(messages.size(), is(3));
                VertxAssert.testComplete();
            }
        });
    }
}
