package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Dict;
import org.quurz.foomp.base.types.Tree;
import org.quurz.foomp.higher.WitnessType;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.RedBlackTree.redBlackTree;

public class Dictionary<K, V>
        implements Dict<K, V> {

    public static final class µ implements WitnessType { private µ() {} }

    private static final Dictionary<?, ?> EMPTY_DICTIONARY
        = new Dictionary<>();

    @SuppressWarnings("unchecked")
    public static <K, V> Dictionary<K, V> dictionary() {
        return (Dictionary<K, V>) EMPTY_DICTIONARY;
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

    private static final class Entry<K, V> {

        private final K key;
        private final Supplier<V> spool;
        private final Entry<K, V> next;

        private Entry(final K key) {
            this.key
                = key;
            this.spool
                = null;
            this.next
                = null;
        }

        private Entry(final K key,
                      final Supplier<V> spool) {
            this.key
                = key;
            this.spool
                = spool;
            this.next
                = null;
        }

        private Entry(final K key,
                      final Supplier<V> spool,
                      final Entry<K, V> next) {
            this.key
                = key;
            this.spool
                = spool;
            this.next
                = next;
        }

        private Entry<K, V> put(final K key, final Supplier<V> spool) {
            Entry<K, V> newEntry;
            if (Objects.equals(this.key, key)) {
                newEntry
                    = new Entry<>(key, spool, this.next);
            } else if (this.next == null) {
                newEntry
                    = new Entry<>(key, spool, this);
            } else {
                newEntry
                    = new Entry<>(this.key, this.spool, this.next.put(key, spool));
            }
            return newEntry;
        }

    }

    private final RedBlackTree<Entry<K, V>> tree;

    private Dictionary() {
        this.tree
            = redBlackTree(
                Tree.InsertionStrategy.Replace,
                Comparator.comparingInt(entry -> Objects.hashCode(entry.key))
            );
    }

    private Dictionary(final RedBlackTree<Entry<K, V>> tree) {
        this.tree
            = tree;
    }

    @Override
    public @NonNull Dict<K, V> put(final @NonNull K key,
                                   final @NonNull V value) {
        Objects.requireNonNull(key, nullValue("key"));
        Objects.requireNonNull(value, nullValue("value"));

        final Entry<K, V> searchPattern
            = new Entry<>(key);
        final Maybe<Entry<K, V>> existingEntry
            = this.tree.searchSafe(searchPattern);

        final Entry<K, V> updatedEntry;
        if (existingEntry.isSome()) {
            updatedEntry
                = existingEntry.get().put(key, () -> value);
        } else {
            updatedEntry
                = new Entry<>(key, () -> value);
        }

        final Dict<K, V> updatedDict
            = new Dictionary<>(this.tree.insert(updatedEntry));

        return updatedDict;
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
    public @NonNull Maybe<V> getSafe(final @NonNull K key) {
        return null;    // TODO
    }

    @Override
    public @NonNull Dict<K, V> remove(@NonNull K key)
            throws NoSuchElementException {
        return null;    // TODO
    }

}
