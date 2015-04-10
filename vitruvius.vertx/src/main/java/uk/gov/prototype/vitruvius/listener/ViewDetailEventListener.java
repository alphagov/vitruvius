package uk.gov.prototype.vitruvius.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import uk.gov.prototype.vitruvius.HtmlContentGenerator;

public class ViewDetailEventListener implements HandlerWithAddress<Message<JsonObject>> {
    private static final Logger logger = LoggerFactory.getLogger(ViewDetailEventListener.class);
    private static final String SINGLE_LISTENER_ADDRESS = "vitruvius.view.address";
    private static final String LINK_PARAM_NAME = "link";
    private HtmlContentGenerator parser;

    public ViewDetailEventListener(HtmlContentGenerator parser) {
        this.parser = parser;
    }

    @Override
    public void handle(Message<JsonObject> message) {
        logger.info("Message received '{}'", message.body());
        String linkParam = message.body().getString(LINK_PARAM_NAME);
        String vitJson = null;
        try {
            vitJson = parser.generateContent(linkParam);
        } catch (Exception e) {
            logger.error("failed to load page : " + linkParam + "/vitruvius.md", e);
            message.fail(1, e.getMessage());
        }
        message.reply(vitJson);
    }


    @Override
    public String handlerAddress() {
        return SINGLE_LISTENER_ADDRESS;
    }
}
