package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;

/**
 * <div>
 *     <p>
 *         Interface for a generic associative array (key–value mapping).
 *     </p>
 *     <p>
 *         Implementations should define their mutability and ordering semantics (e.g., insertion order,
 *         sorted order) in their respective documentation. Unless stated otherwise, keys and values
 *         are expected to be non-null.
 *     </p>
 * </div>
 *
 * @param <K> the key type of the associative array
 * @param <V> the value type of the associative array
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Assoc<K, V> {

    /**
     * <div>
     *     <p>
     *         Inserts (or replaces) a key–value pair into this associative array.
     *     </p>
     *     <p>
     *         Implementations should document whether an existing entry with the same key is replaced
     *         and whether the returned instance is the same object (mutable) or a new instance (immutable).
     *     </p>
     * </div>
     *
     * @param key   the key to insert; must not be {@code null}
     * @param value the value to insert; must not be {@code null}
     * @return this associative array after inserting the key–value pair (for fluent usage)
     *
     * @since 1.0.0
     */
    @NonNull
    Assoc<K, V> put(final @NonNull K key,
                    final @NonNull V value);

    /**
     * <div>
     *     <p>
     *         Checks whether this associative array contains the given key.
     *     </p>
     * </div>
     *
     * @param key the key to check; must not be {@code null}
     * @return {@code true} if this associative array contains the key; {@code false} otherwise
     *
     * @since 1.0.0
     */
    boolean contains(final @NonNull K key);

    /**
     * <div>
     *     <p>
     *         Looks up the value associated with the given key.
     *     </p>
     *     <p>
     *         The result is returned as a {@link Value}, allowing callers to apply a uniform
     *         access pattern. Implementations should specify whether absence results in an
     *         exception or a particular {@code Value} state.
     *     </p>
     * </div>
     *
     * @param key the key to look up; must not be {@code null}
     * @return a {@link Value} wrapper for the associated value; never {@code null}
     * @throws NoSuchElementException if the key is not present in this associative array
     *
     * @since 1.0.0
     */
    @NonNull
    Value<V> get(final @NonNull K key)
            throws NoSuchElementException;

}
