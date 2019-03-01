package com.grapheign.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.schema.DataFetchingEnvironment;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Map;

public class DataFetchingEnvMethodArgsExtractorImpl implements DataFetchingEnvMethodArgsExtractor {
    private static final Object[] EMPTY_ARGS = new Object[0];
    private final ObjectMapper mapper;

    public DataFetchingEnvMethodArgsExtractorImpl(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Object[] extractArguments(Method method, DataFetchingEnvironment environment) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            return EMPTY_ARGS;
        }

        //so far the simplest case only: parameters in graphql should match method parameter names, order and types.
        //TODO add conversion/default values, graphql-java agrs mapping support
        ArrayList<Object> args = new ArrayList<>();
        Map<String, Object> arguments = environment.getArguments();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class parmType = parameterTypes[i];
            String parmName = getParmName(parameters[i]);
            Object converted = mapper.convertValue(arguments.get(parmName), parmType);
            args.add(i, converted);
        }

        return args.toArray();
    }

    //to get parameter.getName() the javac option '-parameters' or '-g' is required. we can't force users to
    // use these options. So, let's do our best to find parm names:
    // - get parm name from  'GrapheignType' annotation on parm
    // - if debug info is present and there is no GrapheignType annotation on parm need to use parm name from debug info
    // - if none of them present let's rely just on order of parms.
    //TODO implement all possible ways to get parm name. for now let's rely on debug info only.
    private String getParmName(Parameter parameter) {
        return parameter.getName();
    }


}
