package com.grapheign.spring;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataFetchersEntryTest {

    @Test
    public void shouldUseKeyOnlyForEqual() {
        DataFetchersEntry<String, Object> entry1 = new DataFetchersEntry<>("key", new Object());
        DataFetchersEntry<String, Object> entry2 = new DataFetchersEntry<>("key", new Object());

        assertEquals(entry1, entry2);
        assertEquals(entry2, entry1);
        assertNotEquals(entry1.getValue(), entry2.getValue());
        assertNotEquals(entry2.getValue(), entry1.getValue());
    }

    @Test
    public void shouldUseKeyOnlyForHascode() {
        DataFetchersEntry<String, Object> entry1 = new DataFetchersEntry<>("key", new Object());
        DataFetchersEntry<String, Object> entry2 = new DataFetchersEntry<>("key", new Object());

        assertEquals(entry1.hashCode(), entry2.hashCode());
    }
}