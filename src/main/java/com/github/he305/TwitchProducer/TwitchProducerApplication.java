package com.github.he305.TwitchProducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class TwitchProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwitchProducerApplication.class, args);
    }
}
