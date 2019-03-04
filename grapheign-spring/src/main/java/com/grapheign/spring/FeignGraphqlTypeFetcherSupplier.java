package com.grapheign.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grapheign.core.DataFetchingEnvMethodArgsExtractor;
import com.grapheign.core.DataFetchingEnvMethodArgsExtractorImpl;
import com.grapheign.core.GraphqlTypeFetcherSupplier;
import com.grapheign.core.MethodInvocationDataFetcher;
import graphql.schema.DataFetcher;
import org.springframework.aop.support.AopUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static com.grapheign.spring.GrapheignAnnotationUtils.getGrapheignFieldName;
import static com.grapheign.spring.GrapheignAnnotationUtils.getGrapheignTypeName;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class FeignGraphqlTypeFetcherSupplier implements GraphqlTypeFetcherSupplier {
    private final ApplicationContext context;
    private final DataFetchingEnvMethodArgsExtractor argsExtractor;

    private final BinaryOperator<Collection<Entry<String, DataFetcher>>> mergeDataFetchers =
            (c1, c2) -> new HashSet<Entry<String, DataFetcher>>() {{
                addAll(c1);
                addAll(c2);
            }};

    public FeignGraphqlTypeFetcherSupplier(ApplicationContext context) {
        this.context = context;
        this.argsExtractor = new DataFetchingEnvMethodArgsExtractorImpl(new ObjectMapper());
    }

    public FeignGraphqlTypeFetcherSupplier(ApplicationContext context, DataFetchingEnvMethodArgsExtractor dataFetchingEnvMethodArgsExtractor) {
        this.context = context;
        this.argsExtractor = dataFetchingEnvMethodArgsExtractor;
    }

    @Override
    public Map<String, Collection<Entry<String, DataFetcher>>> get() {
        Map<String, Object> feignClients = context.getBeansWithAnnotation(FeignClient.class);

        return feignClients
                .values()
                .stream()
                .filter(GrapheignAnnotationUtils::hasGrapheignTypeAnnotation)
                .flatMap(this::fieldsDataFetcher)
                .collect(toMap(Entry::getKey, Entry::getValue, mergeDataFetchers));
    }

    private Stream<Entry<String, Collection<Entry<String, DataFetcher>>>> fieldsDataFetcher(Object grapheignBean) {
        return stream(AopUtils.getTargetClass(grapheignBean).getInterfaces())
                .filter(GrapheignAnnotationUtils::hasGrapheignTypeAnnotation)
                .map(c -> toDataFetcher(c, grapheignBean));
    }

    private Entry<String, Collection<Entry<String, DataFetcher>>> toDataFetcher(Class<?> c, Object grapheignBean) {

        Collection<Entry<String, DataFetcher>> dataFetchers = stream(c.getMethods())
                .filter(GrapheignAnnotationUtils::grapheignMethods)
                .map(m -> new DataFetchersEntry<String, DataFetcher>(getGrapheignFieldName(m),
                        new MethodInvocationDataFetcher(m, grapheignBean, argsExtractor)))
                .collect(toSet());

        return new DataFetchersEntry<>(getGrapheignTypeName(c), dataFetchers);
    }
}