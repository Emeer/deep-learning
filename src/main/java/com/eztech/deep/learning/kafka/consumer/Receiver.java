package com.eztech.deep.learning.kafka.consumer;

import java.util.concurrent.CountDownLatch;

import com.eztech.deep.learning.kafka.Detection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Created by jia on 24/05/2017.
 */
public class Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);


    public CountDownLatch getLatch() {
        return latch;
    }


    @KafkaListener(topics = "${topic.receiver}")
    public void receive(Detection message) {
        LOGGER.info("received message='{}'", message);
        latch.countDown();
    }
}
