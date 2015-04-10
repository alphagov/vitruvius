package uk.gov.prototype.vitruvius.listener;

import org.vertx.java.core.Handler;


/**
 * Extends vertx handler to include endpoint address.
 * @param <E>
 */
public interface HandlerWithAddress<E> extends Handler<E> {

    public String handlerAddress();
}
