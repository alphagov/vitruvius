package uk.gov.prototype.vitruvius.listener;


import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;
import org.vertx.testtools.VertxAssert;
import uk.gov.prototype.vitruvius.elasticsearch.RepositoryESOperations;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

public class AggregationEventListenerTest extends TestVerticle {

    private AggregationEventListener underTest;

    @Mock
    private RepositoryESOperations repositoryESOperations;


    @Test
    public void canGetAggregationData() throws Exception {
        MockitoAnnotations.initMocks(this);

        final String content = FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("aggregation.json").getFile()));

        underTest = new AggregationEventListener(repositoryESOperations);

        EventBus eb = vertx.eventBus();
        eb.registerHandler(underTest.handlerAddress(), underTest);

        JsonObject eventPayload = new JsonObject();
        eventPayload.putString("query", "service");

        when(repositoryESOperations.aggregations("service")).thenReturn(content);

        eb.send(underTest.handlerAddress(), eventPayload, new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> reply) {
                String data = reply.body();
                VertxAssert.assertThat(data, is(content));
                VertxAssert.testComplete();
            }
        });

    }

}
