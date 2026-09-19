package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.types.Foldable;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.Seq;
import org.quurz.foomp.base.types.Streamable;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.base.types.Value2;
import org.quurz.foomp.higher.Higher1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.quurz.foomp.base.localisation.BaseMessages.cantCast;
import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullElementInAt;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Maybe.none;
import static org.quurz.foomp.base.util.Maybe.some;
import static org.quurz.foomp.base.util.Tuple2.tuple2;
import static org.quurz.foomp.base.util.Util.requireNonNullElements;

/**
 * <div>
 *     <p>
 *         An immutable, persistent sequence data structure modeled as a segmented doubly linked structure
 *         with lazy transformation fusion.
 *     </p>
 *     <p>
 *         {@code Sequence} organizes elements into segments carrying a composable {@code spool} transformation
 *         function. Transformations such as {@link #map(Function)} and {@link #applyTo(Higher1)} compose functions
 *         lazily without modifying or copying inner data nodes (<i>structural sharing</i>).
 *     </p>
 *     <p>
 *         {@code Sequence} implements common functional abstractions such as {@link Foldable}, {@link Monadic},
 *         {@link Seq}, and {@link Higher1}.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <A> the type of elements contained in the sequence
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 * @author Junie
 */
@SuppressWarnings("NonAsciiCharacters")
public class Sequence<A>
        implements Iterable<A>,
                   Streamable<A>,
                   Foldable<A>,
                   Monadic<Seq.µ, A>,
                   Seq<A>,
                   Higher1<Seq.µ, A> {

    /**
     * <div>
     *     <p>
     *         Witness type for {@code Sequence} used in the higher‑kinded encoding.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ extends Seq.µ { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Narrows a {@link Higher1} witness representation to a concrete {@code Sequence}.
     *     </p>
     * </div>
     *
     * @param wide the higher‑kinded sequence value; must not be {@code null}
     * @param <A>  the element type
     * @return the narrowed {@code Sequence} instance
     * @throws NullPointerException     if {@code wide} is {@code null}
     * @throws IllegalArgumentException if {@code wide} is not an instance of {@code Sequence}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> Sequence<A> narrow(final @NonNull Higher1<? extends Seq.µ, A> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        if (wide instanceof Sequence<?> sequence) {
            return (Sequence<A>) sequence;
        } else {
            throw new IllegalArgumentException(cantCast("wide", Sequence.class));
        }
    }

    /**
     * <div>
     *     <p>
     *         Returns an empty {@code Sequence}.
     *     </p>
     * </div>
     *
     * @param <A> the element type
     * @return an empty immutable {@code Sequence}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> Sequence<A> sequence() {
        return (Sequence<A>) EMPTY_SEQUENCE;
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code Sequence} containing the specified elements in order.
     *     </p>
     * </div>
     *
     * @param elements the elements to populate the sequence with; must not be {@code null} and must not contain {@code null}
     * @param <A>      the element type
     * @return a new immutable {@code Sequence} containing the elements, or an empty sequence if no elements are provided
     * @throws NullPointerException if {@code elements} is {@code null} or contains any {@code null} elements
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <A> Sequence<A> sequenceOf(final @NonNull A... elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        final Sequence<A> newSequence;
        if (elements.length > 0) {
            final Segment<A> segment
                = new Segment<>();
            requireNonNullElements(
                elements,
                segment::appendNoCopy,
                index -> new NullPointerException(nullElementInAt("elements", index))
            );
            newSequence
                = new Sequence<>(segment, segment);
        } else {
            newSequence
                = (Sequence<A>) EMPTY_SEQUENCE;
        }
        return newSequence;
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code Sequence} from the elements of the given collection.
     *     </p>
     * </div>
     *
     * @param collection the collection whose elements to include; must not be {@code null} and must not contain {@code null}
     * @param <A>        the element type
     * @return a new immutable {@code Sequence} containing the collection's elements, or an empty sequence if the collection is empty
     * @throws NullPointerException if {@code collection} is {@code null} or contains any {@code null} elements
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> Sequence<A> sequenceFrom(final @NonNull Collection<A> collection) {
        Objects.requireNonNull(collection, nullValue("collection"));
        final Sequence<A> newSequence;
        if (!collection.isEmpty()) {
            final Segment<A> segment
                = new Segment<>();
            requireNonNullElements(
                collection,
                segment::appendNoCopy,
                index -> new NullPointerException(nullElementInAt("collection", index))
            );

            newSequence
                = new Sequence<>(segment, segment);
        } else {
            newSequence
                = (Sequence<A>) EMPTY_SEQUENCE;
        }
        return newSequence;
    }

    /**
     * <div>
     *     <p>
     *         Returns a {@link Collector} that accumulates input elements into a new {@code Sequence}.
     *     </p>
     * </div>
     *
     * @param <A> the element type
     * @return a collector accumulating elements into a {@code Sequence}
     *
     * @since 1.0.0
     */
    public static <A> Collector<A, List<A>, Sequence<A>> collectToSequence() {
        return Collector.of(
            ArrayList::new,
            List::add,
            (accu1, accu2) -> {
                accu1.addAll(accu2);
                return accu1;
            },
            Sequence::sequenceFrom
        );
    }

    /**
     * <div>
     *     <p>
     *         Represents a contiguous segment of elements within a {@link Sequence}, paired with
     *         a lazy transformation function (spool).
     *     </p>
     * </div>
     *
     * @param <A> the element type contained in this segment
     */
    private static final class Segment<A> {

        /**
         * Reference to the next segment in the sequence, or {@code null} if this is the last segment.
         */
        private Segment<A> nextSegment;

        /**
         * Reference to the previous segment in the sequence, or {@code null} if this is the first segment.
         */
        private Segment<A> previousSegment;

        /**
         * The first element of this segment, or {@code null} if empty.
         */
        private Element<A> firstElement;

        /**
         * The last element of this segment, or {@code null} if empty.
         */
        private Element<A> lastElement;

        /**
         * The lazy transformation function applied to elements within this segment.
         */
        private final Fun<Object, Object> spool;

        /**
         * Constructs an empty segment with an identity transformation function.
         */
        private Segment() {
            this.spool
                = Fun.identity();
        }

        /**
         * Constructs a segment with the given boundary elements, transformation function, and segment links.
         *
         * @param firstElement    the first element of the segment
         * @param lastElement     the last element of the segment
         * @param spool           the transformation function
         * @param nextSegment     the next segment link
         * @param previousSegment the previous segment link
         */
        @SuppressWarnings("unchecked")
        private Segment(final Element<?> firstElement,
                        final Element<?> lastElement,
                        final Fun<Object, Object> spool,
                        final Segment<A> nextSegment,
                        final Segment<A> previousSegment) {
            this.firstElement
                = (Element<A>) firstElement;
            this.lastElement
                = (Element<A>) lastElement;
            this.spool
                = spool;
            this.nextSegment
                = nextSegment;
            this.previousSegment
                = previousSegment;
        }

        /**
         * Appends an element to this segment without copying existing elements.
         *
         * @param element the element to append; must not be {@code null}
         */
        private void appendNoCopy(final @NonNull A element) {
            if (this.firstElement == null) {
                this.firstElement
                    = new Element<>(element);
                this.lastElement
                    = this.firstElement;
            } else {
                this.lastElement
                    = this.lastElement.appendNoCopy(element);
            }
        }

        /**
         * Creates a copy of this segment with the specified previous segment link and no next segment link.
         *
         * @param previousSegment the previous segment link in the new chain
         * @return the copied segment
         */
        private Segment<A> copyWithPrevious(final Segment<A> previousSegment) {
            return new Segment<>(
                this.firstElement,
                this.lastElement,
                this.spool,
                null,
                previousSegment
            );
        }

        /**
         * Creates a copy of this segment with a new transformation spool function, the specified previous segment link,
         * and no next segment link.
         *
         * @param newSpool        the new transformation spool function
         * @param previousSegment the previous segment link in the new chain
         * @param <B>             the resulting element type
         * @return the copied and transformed segment
         */
        @SuppressWarnings("unchecked")
        private <B> Segment<B> copyWithSpoolAndPrevious(final Fun<Object, Object> newSpool,
                                                        final Segment<B> previousSegment) {
            return new Segment<>(
                this.firstElement,
                this.lastElement,
                newSpool,
                null,
                previousSegment
            );
        }

    }

    /**
     * <div>
     *     <p>
     *         A doubly-linked element node holding a value in a {@link Segment}.
     *     </p>
     * </div>
     *
     * @param <A> the element value type
     */
    private static final class Element<A> {

        /**
         * The stored element value.
         */
        private final A value;

        /**
         * Reference to the previous element in the chain, or {@code null} if this is the head.
         */
        private Element<A> previousElement;

        /**
         * Reference to the next element in the chain, or {@code null} if this is the tail.
         */
        private Element<A> nextElement;

        /**
         * Constructs an element node with the specified value and neighboring element links.
         *
         * @param value           the element value
         * @param previousElement the previous element in the chain
         * @param nextElement     the next element in the chain
         */
        private Element(final A value,
                        final Element<A> previousElement,
                        final Element<A> nextElement) {
            this.value
                = value;
            this.previousElement
                = previousElement;
            this.nextElement
                = nextElement;
        }

        /**
         * Constructs a standalone element node with the given non-null value.
         *
         * @param value the element value; must not be {@code null}
         */
        private Element(final @NonNull A value) {
            Objects.requireNonNull(value, nullValue("value"));
            this.value
                = value;
        }

        /**
         * Appends a new element after this node without copying and establishes bidirectional links.
         *
         * @param element the element value to append
         * @return the newly created and appended element node
         */
        private Element<A> appendNoCopy(final A element) {
            final var newNext
                = new Element<>(element);
            this.nextElement
                = newNext;
            newNext.previousElement
                = this;
            return newNext;
        }

    }

    /**
     * The singleton instance representing an empty {@link Sequence}.
     */
    private static final Sequence<?> EMPTY_SEQUENCE
        = new Sequence<>(null, null);

    /**
     * Reference to the first segment in the sequence, or {@code null} if empty.
     */
    private final Segment<A> firstSegment;

    /**
     * Reference to the last segment in the sequence, or {@code null} if empty.
     */
    private final Segment<A> lastSegment;

    /**
     * Constructs a {@code Sequence} with the specified boundary segments.
     *
     * @param firstSegment the first segment
     * @param lastSegment  the last segment
     */
    private Sequence(final Segment<A> firstSegment,
                     final Segment<A> lastSegment) {
        this.firstSegment
            = firstSegment;
        this.lastSegment
            = lastSegment;
    }

    /**
     * <div>
     *     <p>
     *         Checks whether this sequence contains at least one element.
     *     </p>
     * </div>
     *
     * @return {@code true} if the sequence is not empty; {@code false} otherwise
     *
     * @since 1.0.0
     */
    @Override
    public boolean isNotEmpty() {
        return this.firstSegment != null;
    }

    /**
     * <div>
     *     <p>
     *         Retrieves the first element of this sequence, applying any pending lazy transformations.
     *     </p>
     * </div>
     *
     * @return the first element of this sequence
     * @throws NoSuchElementException if this sequence is empty
     * @throws NullPointerException  if a transformation evaluates to {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    @UnwindingOperation
    public @NonNull A head()
            throws NoSuchElementException {
        if (   this.firstSegment != null
            && this.firstSegment.firstElement != null) {
            return Objects.requireNonNull((A) this.firstSegment.spool.apply(firstSegment.firstElement.value), nullResult());
        } else {
            throw new NoSuchElementException(noValuePresent());
        }
    }

    /**
     * <div>
     *     <p>
     *         Safely retrieves the first element of this sequence wrapped in a {@link Maybe}.
     *     </p>
     * </div>
     *
     * @return {@link Maybe#some(Object)} containing the head element, or {@link Maybe#none()} if this sequence is empty
     * @throws NullPointerException if a transformation evaluates to {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    @UnwindingOperation
    public @NonNull Maybe<A> headSafe() {
        final Maybe<A> maybeHead;
        if (   this.firstSegment != null
            && this.firstSegment.firstElement != null) {
            maybeHead
                = some(Objects.requireNonNull((A) this.firstSegment.spool.apply(firstSegment.firstElement.value), nullResult()));
        } else {
            maybeHead
                = none();
        }
        return maybeHead;
    }

    /**
     * <div>
     *     <p>
     *         Returns the remainder of this sequence after removing the first element.
     *     </p>
     * </div>
     *
     * @return a new {@code Sequence} containing all elements except the first
     * @throws NoSuchElementException if this sequence is empty
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public @NonNull Sequence<A> tail()
            throws NoSuchElementException {
        final Sequence<A> tail;
        if (this.isNotEmpty()) {
            if (   this.firstSegment.firstElement != null
                && this.firstSegment.firstElement.nextElement != null) {
                final Segment<A> newFirstSegment
                    = new Segment<>(
                        this.firstSegment.firstElement.nextElement,
                        this.firstSegment.lastElement,
                        this.firstSegment.spool,
                        null,
                        null
                    );
                Segment<A> currentOriginalSegment
                    = this.firstSegment.nextSegment;
                Segment<A> previousCopiedSegment
                    = newFirstSegment;
                while (currentOriginalSegment != null) {
                    final Segment<A> currentCopiedSegment
                        = currentOriginalSegment.copyWithPrevious(previousCopiedSegment);
                    previousCopiedSegment.nextSegment
                        = currentCopiedSegment;
                    previousCopiedSegment
                        = currentCopiedSegment;
                    currentOriginalSegment
                        = currentOriginalSegment.nextSegment;
                }
                tail = new Sequence<>(newFirstSegment, previousCopiedSegment);
            } else if (this.firstSegment.nextSegment != null) {
                Segment<A> currentOriginalSegment
                    = this.firstSegment.nextSegment;
                Segment<A> firstCopiedSegment
                    = null;
                Segment<A> previousCopiedSegment
                    = null;
                while (currentOriginalSegment != null) {
                    final Segment<A> currentCopiedSegment
                        = currentOriginalSegment.copyWithPrevious(previousCopiedSegment);
                    if (previousCopiedSegment != null) {
                        previousCopiedSegment.nextSegment
                            = currentCopiedSegment;
                    }
                    if (firstCopiedSegment == null) {
                        firstCopiedSegment
                            = currentCopiedSegment;
                    }
                    previousCopiedSegment
                        = currentCopiedSegment;
                    currentOriginalSegment
                        = currentOriginalSegment.nextSegment;
                }
                tail = new Sequence<>(firstCopiedSegment, previousCopiedSegment);
            } else {
                tail = (Sequence<A>) EMPTY_SEQUENCE;
            }
        } else {
            throw new NoSuchElementException(noValuePresent());
        }
        return tail;
    }

    /**
     * <div>
     *     <p>
     *         Prepends an element to the beginning of this sequence.
     *     </p>
     * </div>
     *
     * @param element the element to prepend; must not be {@code null}
     * @return a new {@code Sequence} with the element added at the front
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Sequence<A> cons(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        final Sequence<A> consedSequence;
        if (this.isNotEmpty()) {
            final Element<A> newElement
                = new Element<>(element, null, null);
            final Segment<A> newFirstSegment
                = new Segment<>(
                    newElement,
                    newElement,
                    Fun.identity(),
                    null,
                    null
                );
            Segment<A> currentOriginalSegment
                = this.firstSegment;
            Segment<A> previousCopiedSegment
                = newFirstSegment;
            while (currentOriginalSegment != null) {
                final Segment<A> currentCopiedSegment
                    = currentOriginalSegment.copyWithPrevious(previousCopiedSegment);
                previousCopiedSegment.nextSegment
                    = currentCopiedSegment;
                previousCopiedSegment
                    = currentCopiedSegment;
                currentOriginalSegment
                    = currentOriginalSegment.nextSegment;
            }
            consedSequence
                = new Sequence<>(newFirstSegment, previousCopiedSegment);
        } else {
            final Element<A> newElement
                = new Element<>(element, null, null);
            final Segment<A> newSegment
                = new Segment<>(
                    newElement,
                    newElement,
                    Fun.identity(),
                    null,
                    null
                );
            consedSequence
                = new Sequence<>(newSegment, newSegment);
        }
        return consedSequence;
    }

    /**
     * <div>
     *     <p>
     *         Alias for {@link #cons(Object)}. Prepends an element to the beginning of this sequence.
     *     </p>
     * </div>
     *
     * @param element the element to prepend; must not be {@code null}
     * @return a new {@code Sequence} with the element added at the front
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    public @NonNull Sequence<A> prepend(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return this.cons(element);
    }

    /**
     * <div>
     *     <p>
     *         Appends an element to the end of this sequence without mutating the original sequence.
     *     </p>
     * </div>
     *
     * @param element the element to append; must not be {@code null}
     * @return a new {@code Sequence} with the element added at the end
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    public @NonNull Sequence<A> append(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        final Sequence<A> appended;
        if (this.isNotEmpty()) {
            Segment<A> currentOriginalSegment
                = this.firstSegment;
            Segment<A> firstCopiedSegment
                = null;
            Segment<A> previousCopiedSegment
                = null;
            while (currentOriginalSegment != null) {
                final Segment<A> currentCopiedSegment
                    = currentOriginalSegment.copyWithPrevious(previousCopiedSegment);
                if (previousCopiedSegment != null) {
                    previousCopiedSegment.nextSegment
                        = currentCopiedSegment;
                }
                if (firstCopiedSegment == null) {
                    firstCopiedSegment
                        = currentCopiedSegment;
                }
                previousCopiedSegment
                    = currentCopiedSegment;
                currentOriginalSegment
                    = currentOriginalSegment.nextSegment;
            }
            final Element<A> newElement
                = new Element<>(element, null, null);
            final Segment<A> newLastSegment
                = new Segment<>(
                    newElement,
                    newElement,
                    Fun.identity(),
                    null,
                    previousCopiedSegment
                );
            if (previousCopiedSegment != null) {
                previousCopiedSegment.nextSegment
                    = newLastSegment;
            }
            appended
                = new Sequence<>(firstCopiedSegment, newLastSegment);
        } else {
            final Element<A> newElement
                = new Element<>(element, null, null);
            final Segment<A> newSegment
                = new Segment<>(
                    newElement,
                    newElement,
                    Fun.identity(),
                    null,
                    null
                );
            appended
                = new Sequence<>(newSegment, newSegment);
        }
        return appended;
    }

    /**
     * <div>
     *     <p>
     *         Deconstructs this sequence into a pair consisting of its head element and its tail sequence.
     *     </p>
     * </div>
     *
     * @return a {@link Tuple2} containing the head element and the tail sequence
     * @throws NoSuchElementException if this sequence is empty
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull Tuple2<A, Sequence<A>> decons()
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
     *         Filters elements of this sequence using the specified predicate.
     *     </p>
     * </div>
     *
     * @param predicate the condition to test elements against; must not be {@code null}
     * @return a new {@code Sequence} containing only elements that satisfy the predicate
     * @throws NullPointerException if {@code predicate} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull Sequence<A> filter(final @NonNull Predicate<? super A> predicate) {
        Objects.requireNonNull(predicate, nullValue("predicate"));
        final var temp
            = new ArrayList<A>();
        this.iterateOverAllElementsFromLeft(value -> {
            if (predicate.test(value)) {
                temp.add(value);
            }
        });
        return sequenceFrom(temp);
    }

    /**
     * <div>
     *     <p>
     *         Partitions this sequence into a tuple of sequences according to the specified predicate.
     *         The first sequence contains all elements that satisfy the predicate, and the second
     *         sequence contains all elements that do not.
     *     </p>
     * </div>
     *
     * @param pred the predicate used to partition the sequence; must not be {@code null}
     * @return a {@link Tuple2} containing the sequence of matching elements and the sequence of non-matching elements (never {@code null})
     * @throws NullPointerException if {@code pred} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull Tuple2<Sequence<A>, Sequence<A>> partition(final @NonNull Predicate<? super A> pred) {
        Objects.requireNonNull(pred, nullValue("pred"));

        final var matches
            = new ArrayList<A>();
        final var nonMatches
            = new ArrayList<A>();
        this.iterateOverAllElementsFromLeft(value -> {
            if (pred.test(value)) {
                matches.add(value);
            } else {
                nonMatches.add(value);
            }
        });

        return tuple2(sequenceFrom(matches), sequenceFrom(nonMatches));
    }

    /**
     * <div>
     *     <p>
     *         Splits this sequence into a tuple of sequences according to the specified predicate.
     *         The first sequence contains the longest prefix of elements satisfying the predicate,
     *         and the second sequence contains the remainder of the sequence starting from the first element
     *         that does not satisfy the predicate.
     *     </p>
     * </div>
     *
     * @param pred the predicate used to test elements; must not be {@code null}
     * @return a {@link Tuple2} containing the prefix sequence and remainder sequence (never {@code null})
     * @throws NullPointerException if {@code pred} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull Tuple2<Sequence<A>, Sequence<A>> span(final @NonNull Predicate<? super A> pred) {
        Objects.requireNonNull(pred, nullValue("pred"));

        final var before
            = new ArrayList<A>();
        final var after
            = new ArrayList<A>();
        final var stillBefore
            = new AtomicBoolean(true);
        this.iterateOverAllElementsFromLeft(value -> {
            if (stillBefore.get() && pred.test(value)) {
                before.add(value);
            } else {
                stillBefore.set(false);
                after.add(value);
            }
        });

        return tuple2(sequenceFrom(before), sequenceFrom(after));
    }

    /**
     * <div>
     *     <p>
     *         Collects all elements of this sequence into a newly supplied mutable {@link Collection}.
     *     </p>
     * </div>
     *
     * @param init a supplier providing the target collection instance; must not be {@code null} and must not supply {@code null}
     * @param <C>  the collection type
     * @return the populated collection
     * @throws NullPointerException if {@code init} is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull <C extends Collection<? super A>> C toCollection(final @NonNull Supplier<C> init) {
        Objects.requireNonNull(init, nullValue("init"));
        final var collection
            = Objects.requireNonNull(init.get(), nullSuppliedFrom("init"));
        this.iterateOverAllElementsFromLeft(collection::add);
        return collection;
    }

    /**
     * <div>
     *     <p>
     *         Transforms elements of this sequence lazily by composing the transformation function onto segment spools.
     *     </p>
     * </div>
     *
     * @param transformation the transformation function; must not be {@code null}
     * @param <B>            the target element type
     * @return a new {@code Sequence} representing the lazily transformed elements
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <B> Sequence<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        final Sequence<B> mappedSequence;
        if (this.isNotEmpty()) {
            Segment<A> currentOriginalSegment
                = this.firstSegment;
            Segment<B> firstMappedSegment
                = null;
            Segment<B> previousMappedSegment
                = null;
            while (currentOriginalSegment != null) {
                final Segment<B> currentMappedSegment
                    = currentOriginalSegment.copyWithSpoolAndPrevious(
                        currentOriginalSegment.spool.andThen(Fun.fun((Function<Object, Object>) transformation)),
                        previousMappedSegment
                    );
                if (previousMappedSegment != null) {
                    previousMappedSegment.nextSegment
                        = currentMappedSegment;
                }
                if (firstMappedSegment == null) {
                    firstMappedSegment
                        = currentMappedSegment;
                }
                previousMappedSegment
                    = currentMappedSegment;
                currentOriginalSegment
                    = currentOriginalSegment.nextSegment;
            }
            mappedSequence
                = new Sequence<>(firstMappedSegment, previousMappedSegment);
        } else {
            mappedSequence
                = (Sequence<B>) EMPTY_SEQUENCE;
        }
        return mappedSequence;
    }

    /**
     * <div>
     *     <p>
     *         Applies a sequence of transformation functions to this sequence (Applicative Functor / Cartesian Product).
     *     </p>
     * </div>
     *
     * @param transformation the higher‑kinded sequence of transformation functions; must not be {@code null}
     * @param <B>            the target element type
     * @return a new {@code Sequence} containing the results of applying every function to every element
     * @throws NullPointerException     if {@code transformation} is {@code null}
     * @throws IllegalArgumentException if {@code transformation} is not an instance of {@code Seq}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <B> Sequence<B> applyTo(final @NonNull Higher1<? extends Seq.µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        final Seq<? extends Function<? super A, ? extends B>> transformingSeq
            = Seq.narrow(transformation);
        final Sequence<B> newSequence;
        if (this.isNotEmpty() && transformingSeq.isNotEmpty()) {
            final Sequence<? extends Function<? super A, ? extends B>> transformingSequence
                = transformingSeq instanceof Sequence<? extends Function<? super A, ? extends B>> s
                    ? s
                    : sequenceFrom(transformingSeq.toCollection(ArrayList::new));

            Segment<B> firstResultSegment
                = null;
            Segment<B> previousResultSegment
                = null;
            Segment<? extends Function<? super A, ? extends B>> currentFnSegment
                = transformingSequence.firstSegment;
            while (currentFnSegment != null) {
                Element<? extends Function<? super A, ? extends B>> currentFnElement
                    = currentFnSegment.firstElement;
                while (currentFnElement != null) {
                    final var fn
                        = (Function<? super A, ? extends B>) Objects.requireNonNull(
                            currentFnSegment.spool.apply(currentFnElement.value),
                            nullResult()
                        );
                    Segment<A> currentDataSegment
                        = this.firstSegment;
                    while (currentDataSegment != null) {
                        final Segment<B> currentMappedSegment
                            = currentDataSegment.copyWithSpoolAndPrevious(
                                currentDataSegment.spool.andThen(Fun.fun((Function<Object, Object>) fn)),
                                previousResultSegment
                            );
                        if (previousResultSegment != null) {
                            previousResultSegment.nextSegment
                                = currentMappedSegment;
                        }
                        if (firstResultSegment == null) {
                            firstResultSegment
                                = currentMappedSegment;
                        }
                        previousResultSegment
                            = currentMappedSegment;
                        currentDataSegment
                            = currentDataSegment.nextSegment;
                    }
                    currentFnElement
                        = currentFnElement.nextElement;
                }
                currentFnSegment
                    = currentFnSegment.nextSegment;
            }
            newSequence
                = new Sequence<>(firstResultSegment, previousResultSegment);
        } else {
            newSequence
                = (Sequence<B>) EMPTY_SEQUENCE;
        }
        return newSequence;
    }

    /**
     * <div>
     *     <p>
     *         Applies a function that returns a sequence for each element and flattens the resulting sequences into a single sequence (Monad).
     *     </p>
     * </div>
     *
     * @param transformation the transformation function returning higher‑kinded sequences; must not be {@code null}
     * @param <B>            the target element type
     * @return a new {@code Sequence} formed by concatenating all resulting sequences
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <B> Sequence<B> flatMap(final @NonNull Function<? super A, ? extends Higher1<? extends Seq.µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));

        if (this.isEmpty()) {
            return (Sequence<B>) EMPTY_SEQUENCE;
        }

        Segment<B> firstResultSegment = null;
        Segment<B> previousResultSegment = null;

        var currentSourceSegment = this.firstSegment;
        while (currentSourceSegment != null) {
            var currentSourceElement = currentSourceSegment.firstElement;
            while (currentSourceElement != null) {
                final var element = (A) Objects.requireNonNull(
                    currentSourceSegment.spool.apply(currentSourceElement.value),
                    nullResult()
                );

                final var higher = Objects.requireNonNull(transformation.apply(element), nullResult());
                final var innerSeq = Seq.narrow(higher);

                if (innerSeq.isNotEmpty()) {
                    final Sequence<B> innerSequence
                        = innerSeq instanceof Sequence<B> s
                            ? s
                            : sequenceFrom(innerSeq.toCollection(ArrayList::new));

                    var currentInnerSegment = innerSequence.firstSegment;
                    while (currentInnerSegment != null) {
                        final Segment<B> copiedSegment
                            = currentInnerSegment.copyWithPrevious(previousResultSegment);

                        if (previousResultSegment != null) {
                            previousResultSegment.nextSegment = copiedSegment;
                        }
                        if (firstResultSegment == null) {
                            firstResultSegment = copiedSegment;
                        }

                        previousResultSegment = copiedSegment;
                        currentInnerSegment = currentInnerSegment.nextSegment;
                    }
                }

                currentSourceElement = currentSourceElement.nextElement;
            }
            currentSourceSegment = currentSourceSegment.nextSegment;
        }

        return firstResultSegment != null
            ? new Sequence<>(firstResultSegment, previousResultSegment)
            : (Sequence<B>) EMPTY_SEQUENCE;
    }

    /**
     * <div>
     *     <p>
     *         Folds elements from left to right (left-associative) using an accumulator and a combining function.
     *     </p>
     * </div>
     *
     * @param init     the initial accumulator value; must not be {@code null}
     * @param function the accumulator function; must not be {@code null}
     * @param <B>      the result type
     * @return the final accumulated value
     * @throws NullPointerException if {@code init} or {@code function} is {@code null}, or if {@code function} returns {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    @UnwindingOperation
    public @NonNull <B> B foldLeft(final @NonNull B init,
                                   final @NonNull BiFunction<? super B, ? super A, ? extends B> function) {
        Objects.requireNonNull(init, nullValue("init"));
        Objects.requireNonNull(function, nullValue("function"));
        B currentResult
            = init;
        var currentSegment
            = this.firstSegment;
        while (currentSegment != null) {
            var currentElement
                = currentSegment.firstElement;
            while (currentElement != null) {
                final var value
                    = (A) Objects.requireNonNull(
                        currentSegment.spool.apply(currentElement.value),
                        nullResult()
                    );
                currentResult
                    = Objects.requireNonNull(
                        function.apply(currentResult, value),
                        nullResult()
                    );
                currentElement
                    = currentElement.nextElement;
            }
            currentSegment
                = currentSegment.nextSegment;
        }
        return currentResult;
    }

    /**
     * <div>
     *     <p>
     *         Folds elements from right to left (right-associative) using an accumulator and a combining function.
     *     </p>
     * </div>
     *
     * @param init     the initial accumulator value; must not be {@code null}
     * @param function the accumulator function; must not be {@code null}
     * @param <B>      the result type
     * @return the final accumulated value
     * @throws NullPointerException if {@code init} or {@code function} is {@code null}, or if {@code function} returns {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    @UnwindingOperation
    public @NonNull <B> B foldRight(final @NonNull B init,
                                    final @NonNull BiFunction<? super A, ? super B, ? extends B> function) {
        Objects.requireNonNull(init, nullValue("init"));
        Objects.requireNonNull(function, nullValue("function"));
        B currentResult
            = init;
        Segment<A> currentSegment
            = this.lastSegment;
        while (currentSegment != null) {
            Element<A> currentElement
                = currentSegment.lastElement;
            while (currentElement != null) {
                final var value
                    = (A) Objects.requireNonNull(
                        currentSegment.spool.apply(currentElement.value),
                        nullResult()
                    );
                currentResult
                    = Objects.requireNonNull(
                        function.apply(value, currentResult),
                        nullResult()
                    );
                if (currentElement == currentSegment.firstElement) {
                    break;
                }
                currentElement
                    = currentElement.previousElement;
            }
            currentSegment
                = currentSegment.previousSegment;
        }
        return currentResult;
    }

    /**
     * <div>
     *     <p>
     *         Returns an iterator over the elements of this sequence in proper order.
     *     </p>
     * </div>
     *
     * @return an iterator over the elements in this sequence
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull Iterator<A> iterator() {
        return this.toCollection(ArrayList::new).iterator();
    }

    /**
     * <div>
     *     <p>
     *         Returns a sequential {@link Stream} with this sequence as its source.
     *     </p>
     * </div>
     *
     * @return a sequential stream over the elements in this sequence
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull Stream<A> stream() {
        return this.toCollection(ArrayList::new).stream();
    }

    /**
     * <div>
     *     <p>
     *         Merges this sequence with the specified other sequence by concatenating their elements.
     *     </p>
     *     <p>
     *         The resulting sequence contains all elements of this sequence followed by all elements
     *         of the other sequence. Lazy transformations (spooling) are preserved when merging
     *         with another {@code Sequence}.
     *     </p>
     * </div>
     *
     * @param other the other sequence to merge with; must not be {@code null}
     * @return a new sequence containing the concatenated elements of both sequences
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Sequence<A> merge(final @NonNull Seq<A> other) {
        Objects.requireNonNull(other, nullValue("other"));

        final Sequence<A> merged;

        if (this.isEmpty()) {
            merged
                = other instanceof Sequence<A> s
                    ? s
                    : sequenceFrom(other.toCollection(ArrayList::new));
        } else if (other.isEmpty()) {
            merged
                = this;
        } else if (other instanceof Sequence<A> sequence) {
            merged
                = this.mergeSequence(sequence);
        } else {
            final var temp = this.toCollection(ArrayList<A>::new);
            other.toCollection(() -> temp);
            merged
                = sequenceFrom(temp);
        }

        return merged;
    }

    /**
     * <div>
     *     <p>
     *         Fast-path implementation for merging two {@link Sequence} instances by linking copies
     *         of their segment chains.
     *     </p>
     * </div>
     *
     * @param other the other sequence to merge; must not be {@code null}
     * @return a new {@code Sequence} with combined segment chains
     */
    private @NonNull Sequence<A> mergeSequence(final @NonNull Sequence<A> other) {
        Segment<A> firstCopiedSegment = null;
        Segment<A> previousCopiedSegment = null;

        var current = this.firstSegment;
        while (current != null) {
            final Segment<A> copiedSegment
                = current.copyWithPrevious(previousCopiedSegment);
            if (previousCopiedSegment != null) {
                previousCopiedSegment.nextSegment = copiedSegment;
            }
            if (firstCopiedSegment == null) {
                firstCopiedSegment = copiedSegment;
            }
            previousCopiedSegment = copiedSegment;
            current = current.nextSegment;
        }

        current = other.firstSegment;
        while (current != null) {
            final Segment<A> copiedSegment
                = current.copyWithPrevious(previousCopiedSegment);
            if (previousCopiedSegment != null) {
                previousCopiedSegment.nextSegment = copiedSegment;
            }
            if (firstCopiedSegment == null) {
                firstCopiedSegment = copiedSegment;
            }
            previousCopiedSegment = copiedSegment;
            current = current.nextSegment;
        }

        return new Sequence<>(firstCopiedSegment, previousCopiedSegment);
    }

    /**
     * <div>
     *     <p>
     *         Appends all elements of the specified other sequence to the end of this sequence.
     *     </p>
     *     <p>
     *         This operation is semantically equivalent to {@link #merge(Seq)}.
     *     </p>
     * </div>
     *
     * @param other the sequence whose elements should be appended; must not be {@code null}
     * @return a new sequence containing all elements of this sequence followed by all elements of the other sequence
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Sequence<A> appendAll(final @NonNull Seq<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return this.merge(other);
    }

    /**
     * <div>
     *     <p>
     *         Appends all elements of the specified collection to the end of this sequence.
     *     </p>
     * </div>
     *
     * @param elements the collection whose elements should be appended; must not be {@code null} and must not contain {@code null}
     * @return a new sequence containing all elements of this sequence followed by all elements of the collection
     * @throws NullPointerException if {@code elements} is {@code null} or contains {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public @NonNull Sequence<A> appendAll(final @NonNull Collection<? extends A> elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        final Sequence<A> appended;
        if (elements.isEmpty()) {
            appended
                = this;
        } else if (this.isEmpty()) {
            appended
                = sequenceFrom((Collection<A>) elements);
        } else {
            appended
                = this.mergeSequence(sequenceFrom((Collection<A>) elements));
        }
        return appended;
    }

    /**
     * <div>
     *     <p>
     *         Prepends all elements of the specified other sequence to the front of this sequence,
     *         preserving their encounter order.
     *     </p>
     * </div>
     *
     * @param other the sequence whose elements should be prepended; must not be {@code null}
     * @return a new sequence containing all elements of the other sequence followed by all elements of this sequence
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Sequence<A> prependAll(final @NonNull Seq<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        final Sequence<A> prepended;
        if (other.isEmpty()) {
            prepended
                = this;
        } else if (this.isEmpty()) {
            prepended
                = other instanceof Sequence<A> s
                    ? s
                    : sequenceFrom(other.toCollection(ArrayList::new));
        } else if (other instanceof Sequence<A> sequence) {
            prepended
                = sequence.mergeSequence(this);
        } else {
            final var temp = other.toCollection(ArrayList<A>::new);
            this.toCollection(() -> temp);
            prepended
                = sequenceFrom(temp);
        }
        return prepended;
    }

    /**
     * <div>
     *     <p>
     *         Prepends all elements of the specified collection to the front of this sequence,
     *         preserving their encounter order.
     *     </p>
     * </div>
     *
     * @param elements the collection whose elements should be prepended; must not be {@code null} and must not contain {@code null}
     * @return a new sequence containing all elements of the collection followed by all elements of this sequence
     * @throws NullPointerException if {@code elements} is {@code null} or contains {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public @NonNull Sequence<A> prependAll(final @NonNull Collection<? extends A> elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        final Sequence<A> prepended;
        if (elements.isEmpty()) {
            prepended
                = this;
        } else if (this.isEmpty()) {
            prepended
                = sequenceFrom((Collection<A>) elements);
        } else {
            prepended
                = sequenceFrom((Collection<A>) elements).mergeSequence(this);
        }
        return prepended;
    }

    /**
     * <div>
     *     <p>
     *         Iterates over all elements of this sequence from left to right, passing each element to the consumer.
     *     </p>
     * </div>
     *
     * @param consumer the action to perform on each element; must not be {@code null}
     * @throws NullPointerException if {@code consumer} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @UnwindingOperation
    public void iterateOverAllElementsFromLeft(final @NonNull Consumer<? super A> consumer) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        Segment<A> currentSegment
            = this.firstSegment;
        while (currentSegment != null) {
            Element<A> currentElement
                = currentSegment.firstElement;
            while (currentElement != null) {
                final var value
                    = (A) Objects.requireNonNull(
                        currentSegment.spool.apply(currentElement.value),
                        nullResult()
                    );
                consumer.accept(value);
                currentElement
                    = currentElement.nextElement;
            }
            currentSegment
                = currentSegment.nextSegment;
        }
    }

    /**
     * <div>
     *     <p>
     *         Iterates over all elements of this sequence from right to left, passing each element to the consumer.
     *     </p>
     * </div>
     *
     * @param consumer the action to perform on each element; must not be {@code null}
     * @throws NullPointerException if {@code consumer} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @UnwindingOperation
    public void iterateOverAllElementsFromRight(final @NonNull Consumer<? super A> consumer) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        Segment<A> currentSegment
            = this.lastSegment;
        while (currentSegment != null) {
            Element<A> currentElement
                = currentSegment.lastElement;
            while (currentElement != null) {
                final var value
                    = (A) Objects.requireNonNull(
                        currentSegment.spool.apply(currentElement.value),
                        nullResult()
                    );
                consumer.accept(value);
                if (currentElement == currentSegment.firstElement) {
                    break;
                }
                currentElement
                    = currentElement.previousElement;
            }
            currentSegment
                = currentSegment.previousSegment;
        }
    }

}
