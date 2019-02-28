package com.grapheign.core;

import graphql.schema.DataFetchingEnvironment;

import java.lang.reflect.Method;

/**
 * builds array of argument to invoke given {@code method} using Graphql {@code environment}.
 *
 * implementations may use variety of ways to build required argument set. e.g:
 *
 *  - process custom annotation to get the default value when {@code environment} does not
 * have enough information
 *  - get value of some parameters from somewhere (e.g any kind SecurityContext)
 *  - process & transform & validate given Graphql parameters value
* */
public interface DataFetchingEnvMethodArgsExtractor {
    Object[] extractArguments(Method method, DataFetchingEnvironment environment);
}
