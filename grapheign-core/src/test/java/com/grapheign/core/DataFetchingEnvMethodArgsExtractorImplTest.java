package com.grapheign.core;

import graphql.schema.DataFetchingEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataFetchingEnvMethodArgsExtractorImplTest {

    DataFetchingEnvMethodArgsExtractorImpl extractor;

    @BeforeEach
    void setUp() {
        extractor = new DataFetchingEnvMethodArgsExtractorImpl();
    }

    @Test
    public void shouldReturnEmptyArrayForNoArgMethods(@Mock DataFetchingEnvironment environment) throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("noArgsMethod");
        assertEquals(0, extractor.extractArguments(method, environment).length);
    }
}

class TestClass {
    public Object noArgsMethod() {
        return new Object();
    }
}