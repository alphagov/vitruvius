package uk.gov.prototype.vitruvius.parser;

import org.junit.Assert;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author swilliams
 *
 * @param <T>
 */
public class QueueReplyHandler<T> implements Handler<Message<T>> {

    private final LinkedBlockingQueue<T> queue;

    private final long timeout;

    private final TimeUnit timeUnit;

    public QueueReplyHandler(LinkedBlockingQueue<T> queue) {
        this(queue, 5000L);
    }

    public QueueReplyHandler(LinkedBlockingQueue<T> queue, long timeout) {
        this(queue, timeout, TimeUnit.MILLISECONDS);
    }

    public QueueReplyHandler(LinkedBlockingQueue<T> queue, long timeout, TimeUnit timeUnit) {
        this.queue = queue;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    @Override
    public void handle(Message<T> event) {

        try {
            if (event != null && event.body() != null) {
                queue.offer(event.body(), timeout, timeUnit);
            }

        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
    }
}