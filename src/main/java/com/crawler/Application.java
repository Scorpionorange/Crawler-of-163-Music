package com.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application {

    public static void main(String[] args) throws IOException, URISyntaxException {
        SpringApplication.run(Application.class);
    }

}
