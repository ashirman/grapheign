package com.grapheign.examples;

import com.grapheign.core.GraphqlTypeFetcherSupplier;
import graphql.schema.DataFetcher;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public class FeignGraphqlTypeFetcherSupplier implements GraphqlTypeFetcherSupplier {
    private final ApplicationContext context;

    public FeignGraphqlTypeFetcherSupplier(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public Map<String, Collection<Entry<String, DataFetcher>>> get() {
       return null;
    }
}
