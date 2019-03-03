package com.grapheign.examples;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grapheign.core.DataFetchingEnvMethodArgsExtractor;
import com.grapheign.core.DataFetchingEnvMethodArgsExtractorImpl;
import com.grapheign.core.GraphqlTypeFetcherSupplier;
import com.grapheign.core.MethodInvocationDataFetcher;
import com.grapheign.core.annotations.GrapheignField;
import com.grapheign.core.annotations.GrapheignMutation;
import com.grapheign.core.annotations.GrapheignQuery;
import graphql.schema.DataFetcher;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

public class FeignGraphqlTypeFetcherSupplier implements GraphqlTypeFetcherSupplier {
    private final ApplicationContext context;
    private final DataFetchingEnvMethodArgsExtractor argsExtractor;

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
                .filter(this::hasGrapheignTypeAnnotation)
                .flatMap(this::fieldsDataFetcher)
                .collect(toMap(Entry::getKey, Entry::getValue, mergeFetchers()));
    }

    private Stream<Entry<String, Collection<Entry<String, DataFetcher>>>> fieldsDataFetcher(Object grapheignBean) {
        return stream(AopUtils.getTargetClass(grapheignBean).getInterfaces())
                .filter(this::hasGrapheignTypeAnnotation)
                .map(this::toGrapheignMethodSet)
                .map(e -> toDataFetcher(e, grapheignBean));
    }

    /*
     * multiple feign client interfaces can be annotated by {@code @GrapheignQuery} and {@code @GrapheignMutation}.
     * this method merges 2 collections of datafetchers from different feign interfaces into single collection.
     * */
    private BinaryOperator<Collection<Entry<String, DataFetcher>>> mergeFetchers() {
        return (c1, c2) -> new HashSet<Entry<String, DataFetcher>>() {{
            addAll(c1);
            addAll(c2);
        }};
    }

    private Entry<String, Collection<Entry<String, Method>>> toGrapheignMethodSet(Class<?> c) {
        Set<Entry<String, Method>> fieldToMethodMapping = stream(c.getMethods())
                .filter(this::grapheignMethods)
                .map(m -> new DataFetchersEntry<>(getGrapheignFieldName(m), m))
                .collect(toSet());

        return new DataFetchersEntry<>(getGrapheignTypeName(c), fieldToMethodMapping);
    }

    private Entry<String, Collection<Entry<String, DataFetcher>>> toDataFetcher(Entry<String, Collection<Entry<String, Method>>> methodFetchers, Object grapheignBean) {
        Set<Entry<String, DataFetcher>> collect = methodFetchers.getValue()
                .stream()
                .map(e -> new DataFetchersEntry<String, DataFetcher>(e.getKey(),
                        new MethodInvocationDataFetcher(e.getValue(), grapheignBean, argsExtractor)))
                .collect(toSet());
        return new DataFetchersEntry<>(methodFetchers.getKey(), collect);
    }

    private String getGrapheignFieldName(Method m) {
        //if method is annotated just by @GrapheignField without specifying field name so let's use method name as a
        //grapheign field name
        String name = m.getDeclaredAnnotation(GrapheignField.class).name();
        if ("".equals(name)) {
            return m.getName();
        }

        return name;
    }

    private String getGrapheignTypeName(Class<?> c) {
        GrapheignQuery query = c.getAnnotation(GrapheignQuery.class);
        if (query != null) {
            return query.type();
        }

        GrapheignMutation mutation = c.getAnnotation(GrapheignMutation.class);
        if (mutation != null) {
            return mutation.type();
        }

        return "";
    }

    private boolean grapheignMethods(Method method) {
        return method.getDeclaredAnnotation(GrapheignField.class) != null;
    }

    private boolean hasGrapheignTypeAnnotation(Object v) {
        Class<?>[] proxiedUserInterfaces = AopProxyUtils.proxiedUserInterfaces(v);
        //TODO add GrapheignType annotations procecssing. so far Query and Mutation only are supported
        return stream(proxiedUserInterfaces)
                .anyMatch(this::hasGrapheignTypeAnnotation);
    }

    private boolean hasGrapheignTypeAnnotation(Class<?> c) {
        return (findAnnotation(c, GrapheignQuery.class) != null) || (findAnnotation(c, GrapheignMutation.class) != null);
    }

    /*
     * need to have custom container where equals/hashcode compares candidate using keys only (value of the entry is ignored!)
     *  the reason for this is that by design the only one datafetcher per field is allowed. so, users may wish to override
     *  some method of this class and provide their own datafetcher implementation (different from MethodInvocationDataFetcher).
     * it does not allow to control consistency and keep 1 datafetcher-per-1-field mapping.
     */
    final static class DataFetchersEntry<K, V> extends SimpleEntry<K, V> {
        public DataFetchersEntry(K key, V value) {
            super(key, value);
        }

        public DataFetchersEntry(Entry<? extends K, ? extends V> entry) {
            super(entry);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            return getKey() == null ? e.getKey() == null : getKey().equals(e.getKey());
        }

        @Override
        public int hashCode() {
            return (getKey() == null ? 0 : getKey().hashCode());
        }
    }
}
