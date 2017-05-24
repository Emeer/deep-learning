package com.eztech.deep.learning.kafka;

import com.eztech.deep.learning.kafka.consumer.SpringKafkaReceiverTest;
import com.eztech.deep.learning.kafka.producer.SpringKafkaSenderTest;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.test.rule.KafkaEmbedded;


@RunWith(Suite.class)
@Suite.SuiteClasses({ApplicationTest.class, SpringKafkaSenderTest.class,
        SpringKafkaReceiverTest.class})
public class AllSpringKafkaTests {

    public static final String SENDER_TOPIC = "sender.t";

    public static final String RECEIVER_TOPIC = "receiver.t";

    private static final Logger LOGGER = LoggerFactory.getLogger(AllSpringKafkaTests.class);

    @ClassRule
    public static KafkaEmbedded embeddedKafka =
            new KafkaEmbedded(1, true, SENDER_TOPIC, RECEIVER_TOPIC);


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String kafkaBootstrapServers = embeddedKafka.getBrokersAsString();

        LOGGER.debug("kafkaServers='{}'", kafkaBootstrapServers);
        // override the property in application.properties
        System.setProperty("kafka.bootstrap-servers", kafkaBootstrapServers);
    }

}
