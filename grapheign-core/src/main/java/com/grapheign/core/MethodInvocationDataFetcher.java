package com.grapheign.core;

import java.lang.reflect.Method;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

class MethodInvocationDataFetcher implements DataFetcher {
    Method method;
    Object object;

    public MethodInvocationDataFetcher(Method method, Object object) {
        this.method = method;
        this.object = object;
    }

    public Object get(DataFetchingEnvironment environment) throws Exception {
        //TODO extract required params from env

        return method.invoke(object);
    }
}