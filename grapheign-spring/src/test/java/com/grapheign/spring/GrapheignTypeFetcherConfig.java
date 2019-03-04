package com.grapheign.spring;

import com.grapheign.core.GraphqlTypeFetcherSupplier;
import com.grapheign.core.annotations.GrapheignField;
import com.grapheign.core.annotations.GrapheignMutation;
import com.grapheign.core.annotations.GrapheignQuery;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Configuration
@EnableFeignClients(clients = {
        //explicitly define list of clients for tests so don't pick up anything from the classpath
        GrapheignTypeFetcherConfig.StarWarFeignClientOne.class,
        GrapheignTypeFetcherConfig.StarWarFeignClientTwo.class,
        GrapheignTypeFetcherConfig.StarWarFeignMutationClient.class,
        GrapheignTypeFetcherConfig.StarWarFeignAnotherMutationClient.class,
        GrapheignTypeFetcherConfig.MutationClientWithoutGrapheignAnnotation.class,
        GrapheignTypeFetcherConfig.QueryClientWithoutGrapheignAnnotation.class
})
@EnableAutoConfiguration
public class GrapheignTypeFetcherConfig {
    @FeignClient(name = "startwarclientOne", url = "http://localhost:8080")
    @GrapheignQuery
    interface StarWarFeignClientOne {
        @GrapheignField(name = "hero")
        @RequestMapping(value = "/hero", method = GET)
        String hero(@RequestParam("episode") Long id);

        @GrapheignField(name = "human")
        @RequestMapping(value = "/human/{id}", method = GET)
        String human(@PathVariable("id") String id);
    }

    @FeignClient(name = "startwarclientTwo", url = "http://localhost:8080")
    @GrapheignQuery(type = "Query")
    interface StarWarFeignClientTwo {
        @GrapheignField //no field name defined. should use method name
        @RequestMapping(value = "/droid/{id}", method = GET)
        String droid(@PathVariable("id") String id);

        @GrapheignField(name = "character")
        @RequestMapping(value = "/character/{id}", method = GET)
        String character(@PathVariable("id") String id);

        //no grapheign annotation at all. method should be ignored
        @RequestMapping(value = "/human/{id}", method = GET)
        String superHuman(@PathVariable("id") String id);
    }

    @FeignClient(name = "anotherMutationClient", url = "http://localhost:8080")
    interface QueryClientWithoutGrapheignAnnotation {
        //this mistake made explicitly: there is @GrapheignField on method without @GrapheignMutation on class.
        //we just ignore such interfaces
        @GrapheignField(name = "anotherYetQuery")
        @RequestMapping(value = "/character/{id}", method = GET)
        String ignoredMethod(@PathVariable("id") String id);
    }

    @FeignClient(name = "MutationClient", url = "http://localhost:8080")
    @GrapheignMutation
    interface StarWarFeignMutationClient {
        @GrapheignField //no field name defined. should use method name
        @RequestMapping(value = "/mutation/{id}", method = POST)
        String mutationMethod(@PathVariable("id") String id);

        //no grapheign annotation at all. method should be ignored
        @RequestMapping(value = "/human/{id}", method = POST)
        String superHuman(@PathVariable("id") String id);
    }

    @FeignClient(name = "anotherMutationClient", url = "http://localhost:8080")
    @GrapheignMutation(type = "Mutation")
    interface StarWarFeignAnotherMutationClient {
        @GrapheignField(name = "anotherMutation")
        @RequestMapping(value = "/character/{id}", method = POST)
        String character(@PathVariable("id") String id);
    }

    @FeignClient(name = "anotherMutationClient", url = "http://localhost:8080")
    interface MutationClientWithoutGrapheignAnnotation {
        //this mistake made explicitly: there is @GrapheignField on method without @GrapheignMutation on class.
        //we just ignore such interfaces
        @GrapheignField(name = "anotherYetMutation")
        @RequestMapping(value = "/character/{id}", method = POST)
        String character(@PathVariable("id") String id);
    }

    @Bean
    public GraphqlTypeFetcherSupplier feignTypeSupplier(ApplicationContext context) {
        return new FeignGraphqlTypeFetcherSupplier(context);
    }
}