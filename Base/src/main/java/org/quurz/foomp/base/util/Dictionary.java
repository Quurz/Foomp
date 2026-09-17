package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Dict;
import org.quurz.foomp.base.types.Mappable;
import org.quurz.foomp.base.types.Tree;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.cantCast;
import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.RedBlackTree.redBlackTree;

/**
 * <div>
 *     <p>
 *         An immutable, persistent, and lazy dictionary implementation backed by a balanced
 *         {@link RedBlackTree} and singly linked collision chains.
 *     </p>
 *     <p>
 *         Keys are organized in the underlying Red-Black tree by their {@link Objects#hashCode(Object)},
 *         providing {@code O(log n)} average-case performance for insertions, lookups, and deletions.
 *         Hash collisions are resolved via immutable collision chains within tree nodes.
 *     </p>
 *     <p>
 *         Values are managed lazily via {@link Supplier} thunks, ensuring that transformations
 *         applied via {@link #map(Function)} are deferred until explicitly requested via
 *         {@link #get(Object)} or {@link #getSafe(Object)}.
 *     </p>
 * </div>
 *
 * @param <K> the type of keys maintained by this dictionary
 * @param <V> the type of mapped values
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 * @author Junie
 */
@SuppressWarnings("NonAsciiCharacters")
public class Dictionary<K, V>
        implements Mappable<Dictionary.µ, V>,
                   Dict<K, V>,
                   Higher2<Dictionary.µ, K, V>,
                   Higher1<Dictionary.µ, V> {

    /**
     * <div>
     *     <p>
     *         Witness type for higher-kinded type representations of {@link Dictionary}.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Narrows a {@link Higher1} witness representation to a concrete {@code Dictionary}.
     *     </p>
     * </div>
     *
     * @param wide the higher‑kinded dictionary value; must not be {@code null}
     * @param <K>  the key type
     * @param <V>  the value type
     * @return the narrowed {@code Dictionary} instance
     * @throws NullPointerException     if {@code wide} is {@code null}
     * @throws IllegalArgumentException if {@code wide} is not an instance of {@code Dictionary}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Dictionary<K, V> narrow(final @NonNull Higher1<? extends Dictionary.µ, V> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        if (wide instanceof Dictionary<?, ?> dictionary) {
            return (Dictionary<K, V>) dictionary;
        } else {
            throw new IllegalArgumentException(cantCast("wide", Dictionary.class));
        }
    }

    /**
     * <div>
     *     <p>
     *         Narrows a {@link Higher2} witness representation to a concrete {@code Dictionary}.
     *     </p>
     * </div>
     *
     * @param wide the higher‑kinded dictionary value; must not be {@code null}
     * @param <K>  the key type
     * @param <V>  the value type
     * @return the narrowed {@code Dictionary} instance
     * @throws NullPointerException     if {@code wide} is {@code null}
     * @throws IllegalArgumentException if {@code wide} is not an instance of {@code Dictionary}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Dictionary<K, V> narrow(final @NonNull Higher2<? extends Dictionary.µ, K, V> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        if (wide instanceof Dictionary<?, ?> dictionary) {
            return (Dictionary<K, V>) dictionary;
        } else {
            throw new IllegalArgumentException(cantCast("wide", Dictionary.class));
        }
    }

    /**
     * The singleton instance representing an empty {@link Dictionary}.
     */
    private static final Dictionary<?, ?> EMPTY_DICTIONARY
        = new Dictionary<>();

    /**
     * <div>
     *     <p>
     *         Returns an empty {@link Dictionary}.
     *     </p>
     * </div>
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return an empty dictionary
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Dictionary<K, V> dictionary() {
        return (Dictionary<K, V>) EMPTY_DICTIONARY;
    }

    /**
     * <div>
     *     <p>
     *         Creates a {@link Dictionary} containing the given key-value entries.
     *     </p>
     * </div>
     *
     * @param entries the key-value pairs to populate; must not be {@code null} or contain {@code null} entries
     * @param <K>     the key type
     * @param <V>     the value type
     * @return a new dictionary containing the provided entries
     * @throws NullPointerException if {@code entries} or any entry is {@code null}
     *
     * @since 1.0.0
     */
    @SafeVarargs
    public static <K, V> Dictionary<K, V> dictionaryOf(final @NonNull Tuple2<K, V>... entries) {
        Objects.requireNonNull(entries, nullValue("entries"));
        Dictionary<K, V> result
            = dictionary();
        for (final Tuple2<K, V> entry : entries) {
            Objects.requireNonNull(entry, nullValue("entry"));
            result
                = (Dictionary<K, V>) result.put(entry.get1(), entry.get2());
        }
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Creates a {@link Dictionary} from an existing {@link Map}.
     *     </p>
     * </div>
     *
     * @param source the source map; must not be {@code null} or contain {@code null} entries
     * @param <K>    the key type
     * @param <V>    the value type
     * @return a new dictionary containing all mappings from the source map
     * @throws NullPointerException if {@code source} or any entry is {@code null}
     *
     * @since 1.0.0
     */
    public static <K, V> Dictionary<K, V> dictionaryFrom(final @NonNull Map<K, V> source) {
        Objects.requireNonNull(source, nullValue("source"));
        Dictionary<K, V> result
            = dictionary();
        for (final Map.Entry<K, V> entry : source.entrySet()) {
            Objects.requireNonNull(entry, nullValue("entry"));
            result
                = (Dictionary<K, V>) result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Recursively traverses the given {@link RedBlackTree} and transforms the values of all
     *         contained entries using the specified function.
     *     </p>
     * </div>
     *
     * @param tree           the tree node to transform
     * @param transformation the value transformation function
     * @param <K>            the key type
     * @param <V>            the original value type
     * @param <W>            the transformed value type
     * @return a new tree containing the mapped entries
     */
    private static <K, V, W> RedBlackTree<Entry<K, W>> mapTree(final RedBlackTree<Entry<K, V>> tree,
                                                               final Function<? super V, ? extends W> transformation) {
        if (!tree.isNode()) {
            return redBlackTree(
                Tree.InsertionStrategy.Replace,
                Comparator.comparingInt(entry -> Objects.hashCode(entry.key))
            );
        }
        final Entry<K, W> mappedEntry
            = tree.element().map(transformation);
        final RedBlackTree<Entry<K, W>> mappedLeft
            = mapTree(tree.left(), transformation);
        final RedBlackTree<Entry<K, W>> mappedRight
            = mapTree(tree.right(), transformation);

        return mappedLeft.insert(mappedEntry).merge(mappedRight);
    }


    /**
     * <div>
     *     <p>
     *         Internal node entry holding a key, a lazy value supplier, and a reference to the next
     *         entry in case of a hash collision.
     *     </p>
     * </div>
     *
     * @param key   the entry key
     * @param spool the lazy value supplier
     * @param next  the next entry in the collision chain, or {@code null} if none
     * @param <K>   the key type
     * @param <V>   the value type
     */
    private record Entry<K, V>(K key,
                               Supplier<V> spool,
                               Entry<K, V> next) {

        /**
         * Creates a search pattern entry with the specified key and no value or next entry.
         *
         * @param key the key to search for
         */
        private Entry(final K key) {
            this(key, null, null);
        }

        /**
         * Creates an entry with the specified key and lazy value supplier without collisions.
         *
         * @param key   the entry key
         * @param spool the lazy value supplier
         */
        private Entry(final K key,
                      final Supplier<V> spool) {
            this(key, spool, null);
        }

        /**
         * Inserts or updates a key-value mapping within this collision chain.
         *
         * @param key   the key to insert or update
         * @param spool the lazy value supplier
         * @return the updated entry representing the head of the collision chain
         */
        private Entry<K, V> put(final K key, final Supplier<V> spool) {
            final Entry<K, V> newEntry;
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

        /**
         * Checks whether this collision chain contains the specified key.
         *
         * @param key the key to search for
         * @return {@code true} if the key exists in this chain; {@code false} otherwise
         */
        private boolean contains(final K key) {
            Entry<K, V> current
                = this;
            boolean found
                = false;
            while (current != null && !found) {
                if (Objects.equals(current.key, key)) {
                    found
                        = true;
                } else {
                    current
                        = current.next;
                }
            }
            return found;
        }

        /**
         * Finds the lazy value supplier associated with the specified key in this collision chain.
         *
         * @param key the key to look up
         * @return the lazy value supplier, or {@code null} if not found
         */
        private Supplier<V> find(final K key) {
            Entry<K, V> current
                = this;
            Supplier<V> foundSpool
                = null;
            while (current != null && foundSpool == null) {
                if (Objects.equals(current.key, key)) {
                    foundSpool
                        = current.spool;
                } else {
                    current
                        = current.next;
                }
            }
            return foundSpool;
        }

        /**
         * Removes the entry matching the specified key from this collision chain.
         *
         * @param key the key to remove
         * @return the new head of the collision chain, or {@code null} if the chain is empty
         */
        private Entry<K, V> remove(final K key) {
            final Entry<K, V> result;
            if (Objects.equals(this.key, key)) {
                result
                    = this.next;
            } else if (this.next == null) {
                result
                    = this;
            } else {
                result
                    = new Entry<>(this.key, this.spool, this.next.remove(key));
            }
            return result;
        }

        /**
         * Lazily transforms the value of this entry and any subsequent entries in the collision chain.
         *
         * @param transformation the function to apply to each value
         * @param <W>            the transformed value type
         * @return a new entry with mapped value suppliers
         */
        private <W> Entry<K, W> map(final Function<? super V, ? extends W> transformation) {
            final Entry<K, W> mappedEntry;
            if (this.spool != null) {
                mappedEntry
                    = new Entry<>(
                        this.key,
                        () -> Objects.requireNonNull(transformation.apply(this.spool.get()), nullResultFrom("transformation")),
                        this.next != null ? this.next.map(transformation) : null
                    );
            } else {
                mappedEntry
                    = new Entry<>(
                        this.key,
                        null,
                        this.next != null ? this.next.map(transformation) : null
                    );
            }
            return mappedEntry;
        }

    }

    /**
     * The underlying balanced Red-Black tree storing entry collision chains indexed by key hash code.
     */
    private final RedBlackTree<Entry<K, V>> tree;

    /**
     * Constructs an empty {@code Dictionary} backed by an empty Red-Black tree comparing entries by hash code.
     */
    private Dictionary() {
        this.tree
            = redBlackTree(
                Tree.InsertionStrategy.Replace,
                Comparator.comparingInt(entry -> Objects.hashCode(entry.key))
            );
    }

    /**
     * Constructs a {@code Dictionary} wrapping the specified {@link RedBlackTree}.
     *
     * @param tree the underlying Red-Black tree
     */
    private Dictionary(final RedBlackTree<Entry<K, V>> tree) {
        this.tree
            = tree;
    }

    /**
     * <div>
     *     <p>
     *         Inserts a key-value mapping into this dictionary.
     *     </p>
     *     <p>
     *         If the key already exists, its value is updated. If a hash collision occurs,
     *         the new entry is prepended to the collision chain within the tree node.
     *     </p>
     * </div>
     *
     * @param key   the key to insert; must not be {@code null}
     * @param value the value to associate with the key; must not be {@code null}
     * @return a new dictionary containing the updated mapping
     * @throws NullPointerException if {@code key} or {@code value} is {@code null}
     *
     * @since 1.0.0
     */
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

        return new Dictionary<>(this.tree.insert(updatedEntry));
    }

    /**
     * <div>
     *     <p>
     *         Checks whether this dictionary contains a mapping for the specified key.
     *     </p>
     * </div>
     *
     * @param key the key to check; must not be {@code null}
     * @return {@code true} if this dictionary contains a mapping for the key; {@code false} otherwise
     * @throws NullPointerException if {@code key} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public boolean contains(final @NonNull K key) {
        Objects.requireNonNull(key, nullValue("key"));
        return this.tree.searchSafe(new Entry<>(key))
                .map(entry -> entry.contains(key))
                .getOrElse(() -> false);
    }

    /**
     * <div>
     *     <p>
     *         Retrieves the value associated with the specified key.
     *     </p>
     * </div>
     *
     * @param key the key whose associated value is to be returned; must not be {@code null}
     * @return the value associated with the specified key
     * @throws NoSuchElementException if no mapping for the key exists
     * @throws NullPointerException   if {@code key} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull V get(final @NonNull K key)
            throws NoSuchElementException {
        return this.getSafe(key)
                .getOrThrow(() -> new NoSuchElementException(noValuePresent()));
    }

    /**
     * <div>
     *     <p>
     *         Safely retrieves the value associated with the specified key wrapped in a {@link Maybe}.
     *     </p>
     * </div>
     *
     * @param key the key whose associated value is to be returned; must not be {@code null}
     * @return a {@link Maybe} containing the associated value, or {@code none()} if not present
     * @throws NullPointerException if {@code key} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull Maybe<V> getSafe(final @NonNull K key) {
        Objects.requireNonNull(key, nullValue("key"));
        final Entry<K, V> searchPattern
            = new Entry<>(key);
        return this.tree.searchSafe(searchPattern)
                .map(entry -> entry.find(key))
                .flatMap(spool -> spool != null
                    ? Maybe.some(Objects.requireNonNull(spool.get(), nullResult()))
                    : Maybe.none());
    }

    /**
     * <div>
     *     <p>
     *         Removes the mapping for the specified key from this dictionary.
     *     </p>
     * </div>
     *
     * @param key the key whose mapping is to be removed; must not be {@code null}
     * @return a new dictionary with the mapping removed
     * @throws NoSuchElementException if no mapping for the key exists
     * @throws NullPointerException   if {@code key} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Dict<K, V> remove(final @NonNull K key)
            throws NoSuchElementException {
        Objects.requireNonNull(key, nullValue("key"));

        final Entry<K, V> searchPattern
            = new Entry<>(key);
        final Maybe<Entry<K, V>> existingEntry
            = this.tree.searchSafe(searchPattern);

        if (existingEntry.isNone() || !existingEntry.get().contains(key)) {
            throw new NoSuchElementException(noValuePresent());
        }

        final Entry<K, V> updatedChain
            = existingEntry.get().remove(key);

        final RedBlackTree<Entry<K, V>> updatedTree;
        if (updatedChain == null) {
            updatedTree
                = this.tree.remove(searchPattern);
        } else {
            updatedTree
                = this.tree.insert(updatedChain);
        }

        return new Dictionary<>(updatedTree);
    }

    /**
     * <div>
     *     <p>
     *         Transforms all values in this dictionary using the specified function.
     *     </p>
     *     <p>
     *         This operation is evaluated lazily; transformations are deferred until values
     *         are requested.
     *     </p>
     * </div>
     *
     * @param transformation the function to apply to each value; must not be {@code null}
     * @param <W>            the type of values produced by the transformation
     * @return a new dictionary containing the transformed values
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <W> Dictionary<K, W> map(final @NonNull Function<? super V, ? extends W> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        final RedBlackTree<Entry<K, W>> mappedTree
            = mapTree(this.tree, transformation);
        return new Dictionary<>(mappedTree);
    }

    /**
     * <div>
     *     <p>
     *         Collects all key–value pairs of this dictionary into a newly supplied mutable {@link Map}.
     *     </p>
     *     <p>
     *         This operation unwinds lazy value transformations stored in this dictionary.
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
    @Override
    @UnwindingOperation
    public @NonNull <M extends Map<K, V>> M toMap(final @NonNull Supplier<M> init) {
        Objects.requireNonNull(init, nullValue("init"));
        final M target
            = Objects.requireNonNull(init.get(), nullSuppliedFrom("init"));

        for (final Entry<K, V> entry : this.tree.toCollection(ArrayList::new)) {
            Entry<K, V> current
                = entry;
            while (current != null) {
                final V val
                    = current.spool != null
                    ? Objects.requireNonNull(current.spool.get(), nullResult())
                    : null;
                target.put(current.key, val);
                current
                    = current.next;
            }
        }

        return target;
    }

    /**
     * <div>
     *     <p>
     *         Returns the arity of this type constructor.
     *     </p>
     * </div>
     *
     * @return 2 for {@link Higher2}
     *
     * @since 1.0.0
     */
    @Override
    public int arity() {
        return Higher2.super.arity();
    }

}
