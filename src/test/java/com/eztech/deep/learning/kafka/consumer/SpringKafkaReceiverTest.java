package com.eztech.deep.learning.kafka.consumer;


import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerConfig;

import com.eztech.deep.learning.kafka.AllSpringKafkaTests;
import com.eztech.deep.learning.kafka.Detection;
import com.eztech.deep.learning.kafka.DetectionType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jia on 24/05/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaReceiverTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringKafkaReceiverTest.class);

    @Autowired
    private Receiver receiver;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private KafkaTemplate<String, Detection> template;


    @Before
    public void setUp() throws Exception {
        // set up the Kafka producer properties
        Map<String, Object> senderProperties =
                KafkaTestUtils.senderProps(AllSpringKafkaTests.embeddedKafka.getBrokersAsString());
        senderProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // create a Kafka producer factory
        ProducerFactory<String, Detection> producerFactory =
                new DefaultKafkaProducerFactory<>(senderProperties);

        // create a Kafka template
        template = new KafkaTemplate<>(producerFactory);
        // set the default topic to send to
        template.setDefaultTopic(AllSpringKafkaTests.RECEIVER_TOPIC);

        // wait until the partitions are assigned
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
                .getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer,
                    AllSpringKafkaTests.embeddedKafka.getPartitionsPerTopic());
        }
    }


    @Test
    public void testReceive() throws Exception {
        // send the message
        template.sendDefault(getDetection());
        LOGGER.debug("test-sender sent message='{}'", getDetection());

        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        // check that the message was received
        assertEquals(receiver.getLatch().getCount(), 0);
    }


    private Detection getDetection() {
        Detection detection = new Detection();
        detection.setType(DetectionType.TEXT);
        detection.setText("Hello Spring Kafka Receiver!");
        return detection;
    }
}
