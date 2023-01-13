package streamingsystems.CommandsModel;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamingsystems.CommandsModel.Meta.Event;
import streamingsystems.RabbitMQConnectionManager;

import java.io.IOException;

import static streamingsystems.RabbitMQConnectionManager.QUEUE_NAME;

/**
 * The event store that stores all events.
 */
public class EventStore {
    private static final EventStore singletonInstance = new EventStore();
    private final Logger logger;

    private EventStore() {
        logger = LoggerFactory.getLogger(EventStore.class);
        logger.info("Instantiated EventStore singleton...");
    }

    /**
     * @return The singleton instance of the event store.
     */
    public static EventStore getInstance() {
        return singletonInstance;
    }

    /**
     * Adds an event to the event store.
     * @param event The event to be stored.
     */
    public void addEvent(Event event) {
        try {
            byte[] data = SerializationUtils.serialize(event);
            RabbitMQConnectionManager.getInstance().getEventStoreChannel().basicPublish("", QUEUE_NAME, null, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
