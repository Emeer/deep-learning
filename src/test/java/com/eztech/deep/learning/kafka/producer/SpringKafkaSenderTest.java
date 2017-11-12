package com.eztech.deep.learning.kafka.producer;



import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.eztech.deep.learning.kafka.AllSpringKafkaTests;
import com.eztech.deep.learning.kafka.Detection;
import com.eztech.deep.learning.kafka.DetectionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jia on 24/05/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaSenderTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(SpringKafkaSenderTest.class);


    private KafkaMessageListenerContainer<String, Detection> container;

    private BlockingQueue<ConsumerRecord<String, Detection>> records;

    @Autowired
    private Sender sender;


    @Before
    public void setUp() throws Exception {
        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("sender_group", "false", AllSpringKafkaTests.embeddedKafka);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<String, Detection> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProperties, new StringDeserializer(),
                        new JsonDeserializer<>(Detection.class));

        // set the topic that needs to be consumed
        ContainerProperties containerProperties =
                new ContainerProperties(AllSpringKafkaTests.SENDER_TOPIC);

        // create a Kafka MessageListenerContainer
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        // create a thread safe queue to store the received message
        records = new LinkedBlockingQueue<>();

        // setup a Kafka message listener
        container.setupMessageListener((MessageListener<String, Detection>) record -> {
            LOGGER.debug("test-listener received message='{}'", record.toString());
            records.add(record);
        });

        // start the container and underlying message listener
        container.start();
        // wait until the container has the required number of assigned partitions
        ContainerTestUtils.waitForAssignment(container,
                AllSpringKafkaTests.embeddedKafka.getPartitionsPerTopic());
    }


    @After
    public void tearDown() {
        // stop the container
        container.stop();
    }


    @Test
    public void testSend() throws Exception {
        // send the message

        sender.send(AllSpringKafkaTests.SENDER_TOPIC, getDetection());

        // check that the message was received
        assertEquals(records.poll(10, TimeUnit.SECONDS).value().getText(), getDetection().getText());
    }


    private Detection getDetection() {
        Detection detection = new Detection();
        detection.setType(DetectionType.TEXT);
        detection.setText("Hello Spring Kafka Sender!");
        return detection;
    }

}
