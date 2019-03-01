package com.grapheign.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.schema.DataFetchingEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataFetchingEnvMethodArgsExtractorImplTest {

    DataFetchingEnvMethodArgsExtractorImpl extractor;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        extractor = new DataFetchingEnvMethodArgsExtractorImpl(mapper);
    }

    @Test
    public void shouldReturnEmptyArrayForNoArgMethods(@Mock DataFetchingEnvironment environment) throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("noArgsMethod");
        assertEquals(0, extractor.extractArguments(method, environment).length);
    }

    @Test
    public void shouldConvertGraphqlParmsToSingleMethodParms(@Mock DataFetchingEnvironment environment) throws NoSuchMethodException {
        Map<String, Object> graphqlParms = new HashMap<String, Object>() {{
            put("reallySimpleParm", "expectedValue");
        }};

        when(environment.getArguments()).thenReturn(graphqlParms);

        Method method = TestClass.class.getMethod("singlePrimitiveParm", String.class);
        Object[] actual = extractor.extractArguments(method, environment);
        Object[] expected = {"expectedValue"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldConvertGraphqlParmsToMultipleMethodParms(@Mock DataFetchingEnvironment environment) throws NoSuchMethodException {
        Map<String, Object> graphqlParms = new HashMap<String, Object>() {{
            put("stringParm1", "value1");
            put("stringParm2", "value2");
        }};

        when(environment.getArguments()).thenReturn(graphqlParms);

        Method method = TestClass.class.getMethod("fewSimplePArmsOfTheSameType", String.class, String.class);
        Object[] actual = extractor.extractArguments(method, environment);
        Object[] expected = {"value1", "value2"};
        assertArrayEquals(expected, actual);
    }
}

class TestClass {
    public Object noArgsMethod() {
        return new Object();
    }

    public Object singlePrimitiveParm(String reallySimpleParm) {
        return new Object();
    }

    public Object fewSimplePArmsOfTheSameType(String stringParm1, String stringParm2 ) {
        return new Object();
    }

}