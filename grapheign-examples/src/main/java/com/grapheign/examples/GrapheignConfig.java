package com.grapheign.examples;

import com.grapheign.core.GraphqlTypeFetcherSupplier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrapheignConfig {
    @Bean
    public GraphqlTypeFetcherSupplier feignTypeSupplier(ApplicationContext context) {
        return new FeignGraphqlTypeFetcherSupplier(context);
    }
}
