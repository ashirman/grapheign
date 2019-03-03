package com.grapheign.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GrapheignExampleApp {
    public static void main(String[] args) {
        SpringApplication.run(GrapheignExampleApp.class, args);
    }
}
