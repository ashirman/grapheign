package com.grapheign.spring;

import com.grapheign.core.annotations.GrapheignField;
import com.grapheign.core.annotations.GrapheignMutation;
import com.grapheign.core.annotations.GrapheignQuery;
import org.springframework.aop.framework.AopProxyUtils;

import java.lang.reflect.Method;

import static java.util.Arrays.stream;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

public class GrapheignAnnotationUtils {

    public static String getGrapheignTypeName(Class<?> c) {
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

    public static String getGrapheignFieldName(Method m) {
        //if method is annotated just by @GrapheignField without specifying field name so let's use method name as a
        //grapheign field name
        String name = m.getDeclaredAnnotation(GrapheignField.class).name();
        if ("".equals(name)) {
            return m.getName();
        }

        return name;
    }

    public static boolean grapheignMethods(Method method) {
        return method.getDeclaredAnnotation(GrapheignField.class) != null;
    }

    public static boolean hasGrapheignTypeAnnotation(Class<?> c) {
        return (findAnnotation(c, GrapheignQuery.class) != null) || (findAnnotation(c, GrapheignMutation.class) != null);
    }

    public static boolean hasGrapheignTypeAnnotation(Object v) {
        //TODO add GrapheignType annotations procecssing. so far Query and Mutation only are supported
        return stream(AopProxyUtils.proxiedUserInterfaces(v))
                .anyMatch(GrapheignAnnotationUtils::hasGrapheignTypeAnnotation);
    }


}
