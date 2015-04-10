package uk.gov.prototype.vitruvius.listener;


import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.eventbus.impl.JsonObjectMessage;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Json;
import org.vertx.testtools.TestVerticle;
import org.vertx.testtools.VertxAssert;
import uk.gov.prototype.vitruvius.RepositoryInformationCache;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.validator.ValidationMessage;
import uk.gov.prototype.vitruvius.test.helper.StandardTestObjects;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;


public class GitHubCommitEventHandlerTest extends TestVerticle {

    private GitHubCommitEventHandler underTest;

    @Test
    public void canHandleEvent() throws Exception {

        RepositoryInformationCache informationCache = new RepositoryInformationCache(vertx);
        RepositoryInformation standardService = StandardTestObjects.getStandardService("aminmc/sample");
        informationCache.populateCache(standardService);

        DataRefreshListener dataRefreshListener = new DataRefreshListener();
        vertx.eventBus().registerHandler(dataRefreshListener.handlerAddress(), dataRefreshListener);

        ServiceRegister serviceRegister = new ServiceRegister();
        vertx.eventBus().registerHandler(serviceRegister.handlerAddress(), serviceRegister);


        underTest = new GitHubCommitEventHandler(vertx, informationCache);
        URL resource = this.getClass().getClassLoader().getResource("commit.json");
        String jsonContent = FileUtils.readFileToString(new File(resource.getFile()));


        JsonObject eventPayload = new JsonObject(jsonContent);
        Message<JsonObject> event = new JsonObjectMessage(false, "", eventPayload);

        underTest.handle(event);


    }

    private static class ServiceRegister implements HandlerWithAddress<Message<JsonObject>> {

        @Override
        public String handlerAddress() {
            return "vitruvius.register.service";
        }

        @Override
        public void handle(Message<JsonObject> event) {
            List<ValidationMessage> messages = new ArrayList<>();
            messages.add(ValidationMessage.createWarning("test"));
            event.reply(new JsonObject().putString("messages", Json.encode(messages)));
        }
    }

    private static class DataRefreshListener implements HandlerWithAddress<Message<String>> {

        @Override
        public String handlerAddress() {
            return "vitruvius.data.reloaded";
        }

        @Override
        public void handle(Message<String> event) {
            String updatedBy = "Amin Mohammed-Coleman";
            String updatedOn = "Fri Nov 14 13:35:12 GMT 2014";
            JsonObject body = new JsonObject(event.body());
            VertxAssert.assertThat(body.getString("updatedBy"), is(updatedBy));
            VertxAssert.assertThat(body.getString("updatedOn"), is(updatedOn));
            VertxAssert.testComplete();
        }
    }


}
