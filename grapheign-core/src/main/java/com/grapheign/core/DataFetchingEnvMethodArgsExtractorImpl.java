package com.grapheign.core;

import graphql.schema.DataFetchingEnvironment;

import java.lang.reflect.Method;

public class DataFetchingEnvMethodArgsExtractorImpl implements DataFetchingEnvMethodArgsExtractor {
    private static final Object[] EMPTY_ARGS = new Object[0];

    public Object[] extractArguments(Method method, DataFetchingEnvironment environment) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            return EMPTY_ARGS;
        }

        // FIXME
        return EMPTY_ARGS;
    }
}
