package com.grapheign.core;

import graphql.schema.DataFetchingEnvironment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MethodInvocationDataFetcherTest {

    @Test
    public void shouldInvokeGivenMethodApplyingProvidedArgsExtractor(@Mock DataFetchingEnvMethodArgsExtractor argsExtractor,
                                                                     @Mock DataFetchingEnvironment environment) throws Exception {
        Method method = TestClass.class.getMethod("returnsItsArgument", String.class);

        when(argsExtractor.extractArguments(method, environment)).thenReturn(new Object[]{"methodArguments"});

        Object actual = new MethodInvocationDataFetcher(method, new TestClass(), argsExtractor).get(environment);

        assertEquals(actual, "methodArguments");
        verify(argsExtractor).extractArguments(method, environment);
    }
}