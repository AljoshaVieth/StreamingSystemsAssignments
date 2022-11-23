package streamingsystems.communication;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamingsystems.CommandsModel.EventStore;
import streamingsystems.CommandsModel.Meta.Event;
import streamingsystems.MovingItemListGenerator;

import java.time.Duration;
import java.util.*;

/**
 * This class should provide a method to extract all events from kafka
 */
public class KafkaExtractor {
    private static final KafkaExtractor singletonInstance = new KafkaExtractor();

    final static String GROUP_ID = "EventStoreClientConsumerGroup";
    private final Logger logger;
    Properties kafkaConsumerProperties;


    private KafkaExtractor() {
        logger = LoggerFactory.getLogger(KafkaExtractor.class);
        kafkaConsumerProperties = generateProperties();
    }

    public static KafkaExtractor getSingletonInstance() {
        return singletonInstance;
    }

    private Properties generateProperties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, EventStore.KAFKA_URL);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        return properties;
    }

    public LinkedList<Event> getEvents(String topic) {
        KafkaConsumer<String, byte[]> kafkaConsumer = new KafkaConsumer<>(kafkaConsumerProperties);
        kafkaConsumer.subscribe(List.of(topic));
        LinkedList<Event> eventList = new LinkedList<>();
        do {
            logger.info("Polling for messages...");
            ConsumerRecords<String, byte[]> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(2500));
            for (ConsumerRecord<String, byte[]> record : consumerRecords) {
                logger.info("BYTES EVENT VALUE: " + Arrays.toString(record.value()));
                Event deserializedData = SerializationUtils.deserialize(record.value());
                eventList.add(deserializedData);
            }
        } while (eventList.isEmpty());

        return eventList;
    }
}
