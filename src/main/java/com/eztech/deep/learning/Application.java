package com.eztech.deep.learning;

import com.eztech.deep.learning.config.HadoopConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Spring Big data Application
 *
 * @author Jia ZHOU
 */
@SpringBootApplication
@Import({HadoopConfig.class})
public class Application {

    /** @param args */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}





