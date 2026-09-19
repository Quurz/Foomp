package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Foldable;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.Seq;
import org.quurz.foomp.base.types.Streamable;
import org.quurz.foomp.higher.Higher1;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.quurz.foomp.base.localisation.BaseMessages.cantCast;
import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullElementInAt;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Maybe.none;
import static org.quurz.foomp.base.util.Maybe.some;
import static org.quurz.foomp.base.util.Tuple2.tuple2;
import static org.quurz.foomp.base.util.Util.requireNonNullElements;

/**
 * <div>
 *     <p>
 *         An immutable, eager, random-access sequence data structure extending {@link AbstractList}
 *         and implementing functional interfaces such as {@link Seq}, {@link Monadic}, {@link Foldable},
 *         and {@link Streamable}.
 *     </p>
 *     <p>
 *         {@code SeqList} combines the standard Java {@link List} contract (as an unmodifiable list) with
 *         functional sequence operations (such as {@link #cons(Object)}, {@link #tail()}, {@link #decons()},
 *         {@link #partition(Predicate)}, {@link #span(Predicate)}, {@link #map(Function)}, {@link #applyTo(Higher1)},
 *         and {@link #flatMap(Function)}).
 *     </p>
 *     <p>
 *         All elements are stored eagerly in an internal list providing $O(1)$ random-access index lookups
 *         via {@link #get(int)}. Mutating operations inherited from {@link AbstractList} throw
 *         {@link UnsupportedOperationException}.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <A> the type of elements contained in the list
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 * @author Junie
 */
@SuppressWarnings("NonAsciiCharacters")
public class SeqList<A>
        extends AbstractList<A>
        implements Iterable<A>,
                   Streamable<A>,
                   Foldable<A>,
                   Monadic<Seq.µ, A>,
                   Seq<A>,
                   Higher1<SeqList.µ, A> {

    /**
     * <div>
     *     <p>
     *         Witness type for {@code SeqList} used in the higher‑kinded encoding.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ extends Seq.µ { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Narrows a {@link Higher1} witness representation to a concrete {@code SeqList}.
     *     </p>
     * </div>
     *
     * @param wide the higher‑kinded sequence list value; must not be {@code null}
     * @param <A>  the element type
     * @return the narrowed {@code SeqList} instance (never {@code null})
     * @throws NullPointerException     if {@code wide} is {@code null}
     * @throws IllegalArgumentException if {@code wide} is not an instance of {@code SeqList}
     *
     * @since 1.0.0
     */
    public static <A> SeqList<A> narrow(final @NonNull Higher1<µ, A> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));

        if (wide instanceof SeqList<?>) {
            return (SeqList<A>) wide;
        } else {
            throw new IllegalArgumentException(cantCast("wider", SeqList.class));
        }
    }

    private static final SeqList<?> EMPTY_SEQLIST
        = new SeqList<>();

    /**
     * <div>
     *     <p>
     *         Returns an empty {@code SeqList}.
     *     </p>
     * </div>
     *
     * @param <A> the element type
     * @return an empty {@code SeqList} instance (never {@code null})
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> SeqList<A> seqList() {
        return (SeqList<A>) EMPTY_SEQLIST;
    }

    /**
     * <div>
     *     <p>
     *         Creates a {@code SeqList} containing the specified elements in encounter order.
     *     </p>
     * </div>
     *
     * @param elements the elements to populate the list with; must not be {@code null} and must not contain {@code null}
     * @param <A>      the element type
     * @return a {@code SeqList} containing the given elements (never {@code null})
     * @throws NullPointerException if {@code elements} is {@code null} or contains any {@code null} element
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> SeqList<A> seqList(final @NonNull A... elements) {
        Objects.requireNonNull(elements, nullValue("elements"));

        final ArrayList<A> temp
            = new ArrayList<>();
        final SeqList<A> newSeqList;

        if (elements.length > 0) {
            requireNonNullElements(
                elements,
                temp::add,
                index -> new NullPointerException(nullElementInAt("elements", index))
            );
            newSeqList
                = new SeqList<>(temp);
        } else {
            newSeqList
                = (SeqList<A>) EMPTY_SEQLIST;
        }

        return newSeqList;
    }

    /**
     * <div>
     *     <p>
     *         Creates a {@code SeqList} containing the elements of the specified collection in encounter order.
     *     </p>
     * </div>
     *
     * @param collection the collection whose elements are to be placed into the list; must not be {@code null} and must not contain {@code null}
     * @param <A>        the element type
     * @return a {@code SeqList} containing the elements of the collection (never {@code null})
     * @throws NullPointerException if {@code collection} is {@code null} or contains any {@code null} element
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> SeqList<A> seqListFrom(final Collection<? extends A> collection) {
        Objects.requireNonNull(collection, nullValue("collection"));

        final ArrayList<A> temp
            = new ArrayList<>();
        final SeqList<A> newSeqList;

        if (!collection.isEmpty()) {
            requireNonNullElements(
                collection,
                temp::add,
                index -> new NullPointerException(nullElementInAt("collection", index))
            );
            newSeqList
                = new SeqList<>(temp);
        } else {
            newSeqList
                = (SeqList<A>) EMPTY_SEQLIST;
        }

        return newSeqList;
    }

    /**
     * <div>
     *     <p>
     *         Returns a {@link Collector} that accumulates stream elements into a new {@code SeqList}.
     *     </p>
     * </div>
     *
     * @param <A> the element type
     * @return a {@link Collector} that collects elements into a {@code SeqList}
     *
     * @since 1.0.0
     */
    public static <A> Collector<A, List<A>, SeqList<A>> collectToSeqList() {
        return Collector.of(
            ArrayList::new,
            List::add,
            (accu1, accu2) -> {
                accu1.addAll(accu2);
                return accu1;
            },
            SeqList::seqListFrom
        );
    }

    private final List<A> backingList;

    private SeqList() {
        this.backingList
            = new ArrayList<>();
    }

    private SeqList(final ArrayList<A> backingList) {
        this.backingList
            = backingList;
    }

    /**
     * <div>
     *     <p>
     *         Returns whether this list contains at least one element.
     *     </p>
     * </div>
     *
     * @return {@code true} if this list has at least one element; {@code false} otherwise
     *
     * @since 1.0.0
     */
    @Override
    public boolean isNotEmpty() {
        return !this.backingList.isEmpty();
    }

    /**
     * <div>
     *     <p>
     *         Returns the first element (head) of this list.
     *     </p>
     * </div>
     *
     * @return the first element
     * @throws NoSuchElementException if this list is empty
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull A head()
            throws NoSuchElementException {
        if (this.isNotEmpty()) {
            return this.backingList.getFirst();
        } else {
            throw new NoSuchElementException(noValuePresent());
        }
    }

    /**
     * <div>
     *     <p>
     *         Safely returns the first element of this list wrapped in a {@link Maybe}.
     *     </p>
     * </div>
     *
     * @return {@link Maybe#some(Object)} containing the head element if present, or {@link Maybe#none()} if empty
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Maybe<A> headSafe() {
        final Maybe<A> maybeHead;

        if (this.isNotEmpty()) {
            maybeHead
                = some(this.backingList.getFirst());
        } else {
            maybeHead
                = none();
        }

        return maybeHead;
    }

    /**
     * <div>
     *     <p>
     *         Returns a {@code SeqList} containing all elements except the first.
     *     </p>
     * </div>
     *
     * @return a {@code SeqList} of all elements except the first
     * @throws NoSuchElementException if this list is empty
     *
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public @NonNull SeqList<A> tail()
            throws NoSuchElementException {
        final SeqList<A> tail;

        if (this.isNotEmpty()) {
            if (this.size() > 1) {
                tail = seqListFrom(this.backingList.subList(1, this.size()));
            } else {
                tail = (SeqList<A>) EMPTY_SEQLIST;
            }
        } else {
            throw new NoSuchElementException(noValuePresent());
        }

        return tail;
    }

    /**
     * <div>
     *     <p>
     *         Prepends the specified element to the beginning of this list, returning a new {@code SeqList}.
     *     </p>
     * </div>
     *
     * @param element the element to prepend; must not be {@code null}
     * @return a new {@code SeqList} with the element placed at index 0
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull SeqList<A> cons(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));

        final var newList = new ArrayList<A>(this.backingList.size() + 1);
        newList.add(element);
        newList.addAll(this.backingList);

        return new SeqList<>(newList);
    }

    /**
     * <div>
     *     <p>
     *         Deconstructs this list into its head element and remaining tail list as a {@link Tuple2}.
     *     </p>
     * </div>
     *
     * @return a {@link Tuple2} where {@code get1()} is the head and {@code get2()} is the tail
     * @throws NoSuchElementException if this list is empty
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Tuple2<A, SeqList<A>> decons()
            throws NoSuchElementException {
        if (this.isNotEmpty()) {
            return tuple2(this.head(), this.tail());
        } else {
            throw new NoSuchElementException(noValuePresent());
        }
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code SeqList} containing only elements that satisfy the given predicate.
     *     </p>
     * </div>
     *
     * @param pred the predicate to test elements against; must not be {@code null}
     * @return a new {@code SeqList} of elements matching the predicate
     * @throws NullPointerException if {@code pred} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public @NonNull SeqList<A> filter(final @NonNull Predicate<? super A> pred) {
        Objects.requireNonNull(pred, nullValue("pred"));

        final SeqList<A> filteredSeqList;

        if (this.isNotEmpty()) {
            filteredSeqList
                = this.backingList.stream()
                    .filter(pred)
                    .collect(collectToSeqList());
        } else {
            filteredSeqList
                = (SeqList<A>) EMPTY_SEQLIST;
        }

        return filteredSeqList;
    }

    /**
     * <div>
     *     <p>
     *         Partitions all elements of this list into a pair of lists according to the specified predicate.
     *     </p>
     *     <p>
     *         The first component of the returned {@link Tuple2} contains all elements matching the predicate,
     *         and the second component contains all non-matching elements.
     *     </p>
     * </div>
     *
     * @param pred the predicate used to partition elements; must not be {@code null}
     * @return a {@link Tuple2} containing matching elements in {@code get1()} and non-matching elements in {@code get2()}
     * @throws NullPointerException if {@code pred} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Tuple2<SeqList<A>, ? extends Seq<A>> partition(final @NonNull Predicate<? super A> pred) {
        Objects.requireNonNull(pred, nullValue("pred"));

        final var matches
            = new ArrayList<A>();
        final var nonMatches
            = new ArrayList<A>();

        for (final var element : this.backingList) {
            if (pred.test(element)) {
                matches.add(element);
            } else {
                nonMatches.add(element);
            }
        }

        return tuple2(seqListFrom(matches), seqListFrom(nonMatches));
    }

    /**
     * <div>
     *     <p>
     *         Splits this list at the first element where the predicate evaluates to {@code false}.
     *     </p>
     *     <p>
     *         The first component of the returned {@link Tuple2} contains the longest prefix of elements satisfying
     *         the predicate, and the second component contains the remainder of the list.
     *     </p>
     * </div>
     *
     * @param pred the predicate to test elements; must not be {@code null}
     * @return a {@link Tuple2} containing the prefix satisfying the predicate and the remainder
     * @throws NullPointerException if {@code pred} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Tuple2<SeqList<A>, SeqList<A>> span(final @NonNull Predicate<? super A> pred) {
        Objects.requireNonNull(pred, nullValue("pred"));

        final var before
            = new ArrayList<A>();
        final var after
            = new ArrayList<A>();
        final var stillBefore
            = new AtomicBoolean(true);

        for (final var element : this.backingList) {
            if (stillBefore.get() && pred.test(element)) {
                before.add(element);
            } else {
                stillBefore.set(false);
                after.add(element);
            }
        }

        return tuple2(seqListFrom(before), seqListFrom(after));
    }

    /**
     * <div>
     *     <p>
     *         Appends all elements from this list into the target collection provided by {@code init}.
     *     </p>
     * </div>
     *
     * @param init a supplier providing the target collection; must not be {@code null} and must return a non-null collection
     * @param <C>  the target collection type
     * @return the target collection containing all elements
     * @throws NullPointerException if {@code init} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <C extends Collection<? super A>> C toCollection(final @NonNull Supplier<C> init) {
        Objects.requireNonNull(init, nullValue("init"));

        final var target
            = Objects.requireNonNull(init.get(), nullSuppliedFrom("init"));

        target.addAll(this.backingList);

        return target;
    }

    /**
     * <div>
     *     <p>
     *         Merges this list with another {@link Seq}, placing all elements of {@code other} after all elements
     *         of this list.
     *     </p>
     * </div>
     *
     * @param other the sequence to append to this list; must not be {@code null}
     * @return a new {@code SeqList} containing all elements of this list followed by all elements of {@code other}
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull SeqList<A> merge(final @NonNull Seq<A> other) {
        Objects.requireNonNull(other, nullValue("other"));

        final var merged
            = new ArrayList<A>();

        merged.addAll(this.backingList);
        merged.addAll(other.toCollection(() -> new ArrayList<>()));

        return seqListFrom(merged);
    }

    /**
     * <div>
     *     <p>
     *         Applies the given mapping function eagerly to every element in this list, returning a new {@code SeqList}.
     *     </p>
     * </div>
     *
     * @param transformation the mapping function to apply; must not be {@code null} and must return non-null values
     * @param <B>            the target element type
     * @return a new {@code SeqList} containing the mapped elements
     * @throws NullPointerException if {@code transformation} is {@code null} or produces a {@code null} result
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> SeqList<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));

        final var mapped
            = new ArrayList<B>();

        for (final var element : this.backingList) {
            mapped.add(
                Objects.requireNonNull(transformation.apply(element), nullResultFrom("transformation"))
            );
        }

        return seqListFrom(mapped);
    }

    /**
     * <div>
     *     <p>
     *         Applies a sequence of functions to all elements in this list (Applicative Cartesian product),
     *         returning a new {@code SeqList}.
     *     </p>
     * </div>
     *
     * @param transformation a sequence of functions wrapped in a higher-kinded type; must not be {@code null}
     * @param <B>            the target element type
     * @return a new {@code SeqList} containing all combination results
     * @throws NullPointerException if {@code transformation} is {@code null} or any function produces a {@code null} result
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> SeqList<B> applyTo(final @NonNull Higher1<? extends Seq.µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));

        final var fnSeq
            = Seq.narrow(transformation);
        final var lifted
            = new ArrayList<B>();

        final var fnList
            = fnSeq.toCollection(ArrayList::new);
        for (final var fn : fnList) {
            for (final var element : this.backingList) {
                lifted.add(
                    Objects.requireNonNull(fn.apply(element), nullResult())
                );
            }
        }

        return seqListFrom(lifted);
    }

    /**
     * <div>
     *     <p>
     *         Monadically maps each element of this list to a sequence and flattens the resulting sequences into
     *         a single new {@code SeqList}.
     *     </p>
     * </div>
     *
     * @param transformation the monadic binding function; must not be {@code null} and must return non-null sequences
     * @param <B>            the target element type
     * @return a new {@code SeqList} resulting from flattening all generated sequences
     * @throws NullPointerException if {@code transformation} is {@code null} or produces a {@code null} result
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> SeqList<B> flatMap(final @NonNull Function<? super A, ? extends Higher1<? extends Seq.µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));

        final var bound
            = this.backingList.stream()
                .map(element -> Objects.requireNonNull(transformation.apply(element), nullResult()))
                .map(Seq::narrow)
                .map(sequence -> sequence.toCollection(ArrayList::new))
                .flatMap(Collection::stream)
                .toList();

        return seqListFrom(bound);
    }

    /**
     * <div>
     *     <p>
     *         Performs a left-associative fold over the elements of this list using the specified accumulator function
     *         and initial value.
     *     </p>
     * </div>
     *
     * @param init     the initial accumulator value; must not be {@code null}
     * @param function the accumulator function; must not be {@code null} and must return non-null values
     * @param <B>      the accumulator result type
     * @return the folded result value
     * @throws NullPointerException if {@code init} or {@code function} is {@code null}, or if {@code function} produces {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> B foldLeft(final @NonNull B init,
                                   final @NonNull BiFunction<? super B, ? super A, ? extends B> function) {
        Objects.requireNonNull(init, nullValue("init"));
        Objects.requireNonNull(function, nullValue("function"));

        B currentResult = init;
        for (final var element : this.backingList) {
            currentResult = Objects.requireNonNull(
                function.apply(currentResult, element),
                nullResultFrom("function")
            );
        }

        return currentResult;
    }

    /**
     * <div>
     *     <p>
     *         Performs a right-associative fold over the elements of this list using the specified accumulator function
     *         and initial value.
     *     </p>
     * </div>
     *
     * @param init     the initial accumulator value; must not be {@code null}
     * @param function the accumulator function; must not be {@code null} and must return non-null values
     * @param <B>      the accumulator result type
     * @return the folded result value
     * @throws NullPointerException if {@code init} or {@code function} is {@code null}, or if {@code function} produces {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> B foldRight(final @NonNull B init,
                                    final @NonNull BiFunction<? super A, ? super B, ? extends B> function) {
        Objects.requireNonNull(init, nullValue("init"));
        Objects.requireNonNull(function, nullValue("function"));

        B currentResult = init;
        for (int i = this.backingList.size() - 1; i >= 0; i--) {
            final var element = this.backingList.get(i);
            currentResult = Objects.requireNonNull(
                function.apply(element, currentResult),
                nullResultFrom("function")
            );
        }

        return currentResult;
    }

    /**
     * <div>
     *     <p>
     *         Returns a sequential {@link Stream} over the elements in this list.
     *     </p>
     * </div>
     *
     * @return a sequential {@link Stream} over the elements of this list
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Stream<A> stream() {
        return this.backingList.stream();
    }

    /**
     * <div>
     *     <p>
     *         Returns an iterator over the elements in this list in proper sequence.
     *     </p>
     * </div>
     *
     * @return an {@link Iterator} over the elements in this list
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Iterator<A> iterator() {
        return Collections.unmodifiableList(this.backingList).iterator();
    }

    /**
     * <div>
     *     <p>
     *         Returns the element at the specified position in this list.
     *     </p>
     * </div>
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index >= size()})
     *
     * @since 1.0.0
     */
    @Override
    public A get(final int index) {
        Objects.checkIndex(index, this.backingList.size());
        return this.backingList.get(index);
    }

    /**
     * <div>
     *     <p>
     *         Returns the number of elements in this list.
     *     </p>
     * </div>
     *
     * @return the number of elements in this list
     *
     * @since 1.0.0
     */
    @Override
    public int size() {
        return this.backingList.size();
    }

}
