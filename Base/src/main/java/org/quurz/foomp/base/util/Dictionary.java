package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Dict;
import org.quurz.foomp.base.types.Value;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public class Dictionary<K, V>
        implements Dict<K, V> {

    public static <K, V> Dictionary<K, V> dictionary() {
        return null;    // TODO
    }

    @SafeVarargs
    public static <K, V> Dictionary<K, V> dictionaryOf(final @NonNull Tuple2<K, V>... entries) {
        Objects.requireNonNull(entries, nullValue("entries"));
        return null;    // TODO
    }

    public static <K, V> Dictionary<K, V> dictionaryFrom(final @NonNull Map<K, V> source) {
        Objects.requireNonNull(source, nullValue("source"));
        return null;    // TODO
    }

    @Override
    public @NonNull Dict<K, V> put(final @NonNull K key, @NonNull V value) {
        return null;    // TODO
    }

    @Override
    public boolean contains(final @NonNull K key) {
        return false;    // TODO
    }

    @Override
    public @NonNull V get(final @NonNull K key)
            throws NoSuchElementException {
        return null;    // TODO
    }

    @Override
    public @NonNull Value<V> getSafe(final @NonNull K key) {
        return null;    // TODO
    }

    @Override
    public @NonNull Dict<K, V> remove(@NonNull K key)
            throws NoSuchElementException {
        return null;    // TODO
    }

}
