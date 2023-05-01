package com.p2.naturesnap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class NaturesnapApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaturesnapApplication.class, args);
    }

}
