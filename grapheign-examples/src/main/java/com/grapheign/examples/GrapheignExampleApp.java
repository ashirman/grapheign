package com.grapheign.examples;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class GrapheignExampleApp {
    public static void main(String[] args) {
        SpringApplication.run(GrapheignExampleApp.class, args);
    }

/*    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        return mapper;
    }*/
}
