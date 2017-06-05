package com.eztech.deep.learning.kafka.consumer;

import java.util.concurrent.CountDownLatch;

import com.eztech.deep.learning.kafka.Detection;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Created by jia on 24/05/2017.
 */
@Slf4j
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);


    @KafkaListener(topics = "${topic.receiver}")
    public void receive(Detection data) {
        log.info("received message='{}'", data);
        latch.countDown();
    }


    public CountDownLatch getLatch() {
        return latch;
    }

}
