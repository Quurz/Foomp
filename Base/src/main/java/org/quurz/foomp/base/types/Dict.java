package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * <div>
 *     <p>
 *         Interface for a generic dictionary (key–value mapping).
 *     </p>
 *     <p>
 *         Implementations should define their mutability and ordering semantics (e.g., insertion order,
 *         sorted order) in their respective documentation. Unless stated otherwise, keys and values
 *         are expected to be non-null.
 *     </p>
 * </div>
 *
 * @param <K> the key type of the dictionary
 * @param <V> the value type of the dictionary
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Dict<K, V> {

    /**
     * <div>
     *     <p>
     *         Inserts (or replaces) a key–value pair into this dictionary.
     *     </p>
     *     <p>
     *         Implementations should document whether an existing entry with the same key is replaced
     *         and whether the returned instance is the same object (mutable) or a new instance (immutable).
     *     </p>
     * </div>
     *
     * @param key   the key to insert; must not be {@code null}
     * @param value the value to insert; must not be {@code null}
     * @return this dictionary after inserting the key–value pair (for fluent usage)
     *
     * @since 1.0.0
     */
    @NonNull Dict<K, V> put(final @NonNull K key,
                            final @NonNull V value);

    /**
     * <div>
     *     <p>
     *         Checks whether this dictionary contains the given key.
     *     </p>
     * </div>
     *
     * @param key the key to check; must not be {@code null}
     * @return {@code true} if this dictionary contains the key; {@code false} otherwise
     *
     * @since 1.0.0
     */
    boolean contains(final @NonNull K key);

    /**
     * <div>
     *     <p>
     *         Retrieves the value associated with the given key.
     *     </p>
     * </div>
     *
     * @param key the key whose associated value is to be returned; must not be {@code null}
     * @return the value associated with the key
     * @throws NoSuchElementException if the key is not present in this dictionary
     *
     * @since 1.0.0
     */
    @NonNull V get(final @NonNull K key)
            throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Safely retrieves the value associated with the given key, wrapped in a {@link Value}.
     *     </p>
     * </div>
     *
     * @param key the key whose associated value is to be returned; must not be {@code null}
     * @return a {@link Value} containing the associated value if present, or an empty {@link Value} otherwise
     *
     * @since 1.0.0
     */
    @NonNull Value<V> getSafe(final @NonNull K key);

    /**
     * <div>
     *     <p>
     *         Removes the mapping for the given key from this dictionary if present.
     *     </p>
     *     <p>
     *         Implementations should document whether the returned instance is the same object (mutable)
     *         or a new instance (immutable).
     *     </p>
     * </div>
     *
     * @param key the key whose mapping is to be removed; must not be {@code null}
     * @return this dictionary after removing the mapping
     * @throws NoSuchElementException if the key is not present in this dictionary
     *
     * @since 1.0.0
     */
    @NonNull Dict<K, V> remove(final @NonNull K key)
            throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Collects all key–value pairs of this dictionary into a newly supplied mutable {@link Map}.
     *     </p>
     * </div>
     *
     * @param init a supplier providing the target map instance; must not be {@code null} and must not supply {@code null}
     * @param <M>  the map type
     * @return the populated map
     * @throws NullPointerException if {@code init} is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    @NonNull <M extends Map<K, V>> M toMap(final @NonNull Supplier<M> init);

}
