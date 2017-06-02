package com.eztech.deep.learning;

import com.eztech.deep.learning.kafka.Detection;
import com.eztech.deep.learning.kafka.DetectionType;
import com.eztech.deep.learning.kafka.producer.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Big data Application
 *
 * @author Jia ZHOU
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private Sender sender;

    @Value("${topic.receiver}")
    private String topic;


    /** @param args  */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Detection detection = new Detection();
        detection.setText("test");
        detection.setType(DetectionType.TEXT);
        sender.send(topic, detection);
    }
}





