package com.eztech.deep.learning;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

/**
 * Created by jiazhou on 2017/5/22.
 */
public class Listener {

    public final CountDownLatch countDownLatch1 = new CountDownLatch(1);

    @KafkaListener(id = "foo", topics = "topic1", group = "group1")
    public void listen(ConsumerRecord<?, ?> record) {
        System.out.println(record);
        countDownLatch1.countDown();
    }

}
