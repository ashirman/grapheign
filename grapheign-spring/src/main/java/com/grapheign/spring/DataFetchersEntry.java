package com.grapheign.spring;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;

/*
 * need to have custom container where equals/hashcode compares candidate using keys only (value of the entry is ignored!)
 *  the reason for this is that by design the only one datafetcher per field is allowed. so, users may wish to override
 *  some method of this class and provide their own datafetcher implementation (different from MethodInvocationDataFetcher).
 * it does not allow to control consistency and keep 1 datafetcher-per-1-field mapping.
 */
final class DataFetchersEntry<K, V> extends SimpleEntry<K, V> {
    public DataFetchersEntry(K key, V value) {
        super(key, value);
    }

    public DataFetchersEntry(Entry<? extends K, ? extends V> entry) {
        super(entry);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Map.Entry))
            return false;
        Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
        return getKey() == null ? e.getKey() == null : getKey().equals(e.getKey());
    }

    @Override
    public int hashCode() {
        return (getKey() == null ? 0 : getKey().hashCode());
    }
}