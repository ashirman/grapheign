package com.grapheign.spring;

import com.grapheign.core.annotations.GrapheignField;
import com.grapheign.core.annotations.GrapheignMutation;
import com.grapheign.core.annotations.GrapheignQuery;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrapheignAnnotationUtilsTest {

    @GrapheignQuery
    interface DefaultQuery {
        @GrapheignField
        String foo();

        @GrapheignField(name = "superbar")
        String bar();

        //no annotations!
        String baz();
    }

    @GrapheignQuery(type = "CustomQueryTypeName")
    interface CustomQueryClass {
    }

    @GrapheignMutation
    interface MutationClass {
    }

    @GrapheignMutation(type = "CustomMutationTypeName")
    interface CustomMutationClass {
    }

    interface AnnotationFreeClass {
    }

    @Test
    void getGrapheignTypeName() {
        assertEquals("", GrapheignAnnotationUtils.getGrapheignTypeName(AnnotationFreeClass.class));
        assertEquals("CustomMutationTypeName", GrapheignAnnotationUtils.getGrapheignTypeName(CustomMutationClass.class));
        assertEquals("Mutation", GrapheignAnnotationUtils.getGrapheignTypeName(MutationClass.class));
        assertEquals("CustomQueryTypeName", GrapheignAnnotationUtils.getGrapheignTypeName(CustomQueryClass.class));
        assertEquals("Query", GrapheignAnnotationUtils.getGrapheignTypeName(DefaultQuery.class));
    }

    @Test
    void getGrapheignFieldName() throws NoSuchMethodException {
        assertEquals("foo", GrapheignAnnotationUtils.getGrapheignFieldName(DefaultQuery.class.getMethod("foo")));
        assertEquals("superbar", GrapheignAnnotationUtils.getGrapheignFieldName(DefaultQuery.class.getMethod("bar")));
    }

    @Test
    void grapheignMethods() throws NoSuchMethodException {
        assertTrue(GrapheignAnnotationUtils.isGrapheignMethods(DefaultQuery.class.getMethod("foo")));
        assertTrue(GrapheignAnnotationUtils.isGrapheignMethods(DefaultQuery.class.getMethod("bar")));
        assertFalse(GrapheignAnnotationUtils.isGrapheignMethods(DefaultQuery.class.getMethod("baz")));
}

    @Test
    void hasGrapheignTypeAnnotation() {
        assertTrue(GrapheignAnnotationUtils.hasGrapheignTypeAnnotation(DefaultQuery.class));
        assertTrue(GrapheignAnnotationUtils.hasGrapheignTypeAnnotation(MutationClass.class));
        assertFalse(GrapheignAnnotationUtils.hasGrapheignTypeAnnotation(AnnotationFreeClass.class));
    }
}