package com.miludin.fetchtify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FetchtifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FetchtifyApplication.class, args);
    }
}
