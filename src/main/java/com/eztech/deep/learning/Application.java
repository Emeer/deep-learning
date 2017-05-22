package com.eztech.deep.learning;

import com.eztech.deep.learning.config.KafkaConsumerConfig;
import com.eztech.deep.learning.config.KafkaProducerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Spring Big data Application
 *
 * @author Jia ZHOU
 */
@SpringBootApplication
@Import({KafkaConsumerConfig.class, KafkaProducerConfig.class})
public class Application {

    /** @param args  */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}





