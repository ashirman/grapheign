package com.grapheign.core;

import java.lang.reflect.Method;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class MethodInvocationDataFetcher implements DataFetcher {
    private final DataFetchingEnvMethodArgsExtractor argsExtractor;
    private final Method method;
    private final Object object;

    public MethodInvocationDataFetcher(Method method, Object object, DataFetchingEnvMethodArgsExtractor argsExtractor) {
        this.method = method;
        this.object = object;
        this.argsExtractor = argsExtractor;
    }

    public Object get(DataFetchingEnvironment environment) throws Exception {
        Object[] arguments = argsExtractor.extractArguments(method, environment);
        return method.invoke(object, arguments);
    }
}