package com.c99.innovation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class InnovationApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnovationApplication.class, args);
    }
}
