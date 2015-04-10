package uk.gov.prototype.vitruvius.listener;


import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Json;
import org.vertx.testtools.TestVerticle;
import org.vertx.testtools.VertxAssert;
import uk.gov.prototype.vitruvius.elasticsearch.RepositoryESOperations;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformationBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class RepositoryInformationEventListenerTest extends TestVerticle {


    private RepositoryInformationEventListener underTest;

    private EventBus eb;

    @Mock
    private RepositoryESOperations repositoryESOperations;

    @Test
    public void canReturnRepositoryInformation() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTest = new RepositoryInformationEventListener(repositoryESOperations);

        eb = vertx.eventBus();
        eb.registerHandler(underTest.handlerAddress(), underTest);

        JsonObject eventPayload = new JsonObject();
        eventPayload.putString("type", "service");

        when(repositoryESOperations.getListOfRepositoryInformation(any(RepositoryESOperations.RepositoryInfoContext.class))).thenReturn(createList());

        eb.send(underTest.handlerAddress(), eventPayload, new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> reply) {
                String data = reply.body();
                List<RepositoryInformation> list = Json.decodeValue(data, List.class);
                VertxAssert.assertThat(list.size(), is(2));
                VertxAssert.testComplete();
            }
        });


    }

    private List<RepositoryInformation> createList() {
        List<RepositoryInformation> list = new ArrayList<>();
        list.add(new RepositoryInformationBuilder().build());
        list.add(new RepositoryInformationBuilder().build());
        return list;
    }
}
