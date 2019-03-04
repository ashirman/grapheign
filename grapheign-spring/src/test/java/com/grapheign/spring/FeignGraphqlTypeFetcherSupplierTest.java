package com.grapheign.spring;

import com.grapheign.core.GraphqlTypeFetcherSupplier;
import graphql.schema.DataFetcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GrapheignTypeFetcherConfig.class})
class FeignGraphqlTypeFetcherSupplierTest {

    @Autowired
    GraphqlTypeFetcherSupplier feignTypeSupplier;

    @Test
    public void shouldCollectDataFetchersFromMultipleInterfaces() {
        Map<String, Collection<Entry<String, DataFetcher>>> actual = feignTypeSupplier.get();

        assertThat(actual).containsOnlyKeys("Mutation", "Query");

        //actual DataFetcher interaction is tested as part of integration tests. here we care about field keys only
        assertThat(actual.get("Query").stream().map(Entry::getKey)).containsOnly("hero", "human", "droid", "character");
        assertThat(actual.get("Mutation").stream().map(Entry::getKey)).containsOnly("mutationMethod", "anotherMutation");
    }
}