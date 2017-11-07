package com.eztech.deep.learning;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Big data Application
 *
 * @author Jia ZHOU
 */
@SpringBootApplication
@Slf4j
public class Application implements CommandLineRunner {


    /** @param args  */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

    }
}





