package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <div>
 *     <p>
 *         A sequence of values of type {@code A}. A sequence may be empty, contain one or more
 *         elements, and can be extended by adding elements or concatenating other sequences.
 *     </p>
 *     <p>
 *         Unless stated otherwise by a concrete implementation, elements are treated in encounter
 *         order, operations are expected to be null‑safe (no {@code null} elements), and methods
 *         return non‑null results. Implementations should document strictness (eager vs. lazy),
 *         mutability, and complexity characteristics.
 *     </p>
 * </div>
 *
 * @param <A> the element type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public interface Seq<A>
        extends Container<A>,
                Value<A>,
                Streamable<A>,
                Iterable<A>,
                Higher1<Seq.µ, A> {

    /**
     * <div>
     *     <p>
     *         Witness type used to encode {@link Seq} as a higher‑kinded type.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    class µ implements WitnessType { protected µ() {} }

    /**
     * <div>
     *     <p>
     *         Returns whether this sequence contains at least one element.
     *     </p>
     * </div>
     *
     * @return {@code true} if this sequence has at least one element; {@code false} otherwise
     *
     * @since 1.0.0
     */
    boolean isNotEmpty();

    /**
     * <div>
     *     <p>
     *         Returns whether this sequence is empty.
     *     </p>
     * </div>
     *
     * @return {@code true} if this sequence is empty; {@code false} otherwise
     *
     * @since 1.0.0
     */
    default boolean isEmpty() {
        return !this.isNotEmpty();
    }

    /**
     * <div>
     *     <p>
     *         Returns the first element (head) of this sequence. Comparable to Lisp’s {@code car}.
     *     </p>
     * </div>
     *
     * @return the head element
     * @throws NoSuchElementException if this sequence is empty
     *
     * @since 1.0.0
     */
    @NonNull A head()
        throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Returns the remainder (tail) of this sequence without the first element.
     *     </p>
     * </div>
     *
     * @return the tail sequence (never {@code null}); may be empty
     *
     * @since 1.0.0
     */
    @NonNull Seq<A> tail();

    /**
     * <div>
     *     <p>
     *         Prepends an element to this sequence and returns the new sequence.
     *     </p>
     * </div>
     *
     * @param element the element to prepend; must not be {@code null} (unless an implementation explicitly allows it)
     * @return a new sequence with {@code element} as its head (never {@code null})
     *
     * @since 1.0.0
     */
    @NonNull Seq<A> cons(final @NonNull A element);

    /**
     * <div>
     *     <p>
     *         Prepends all elements of the given sequence to this sequence, preserving the given
     *         sequence’s encounter order.
     *     </p>
     * </div>
     *
     * @param other the sequence whose elements are to be prepended; must not be {@code null}
     * @return a new sequence with {@code other}’s elements followed by this sequence (never {@code null})
     *
     * @since 1.0.0
     */
    @NonNull Seq<A> consAll(final @NonNull Higher1<? extends Seq.µ, A> other);

    /**
     * <div>
     *     <p>
     *         Deconstructs this sequence into its head and tail.
     *     </p>
     * </div>
     *
     * @return a pair consisting of the first element and the remainder of the sequence
     * @throws NoSuchElementException if this sequence is empty
     *
     * @since 1.0.0
     */
    @NonNull Value2<A, ? extends Seq<A>> decons()
        throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Splits this sequence into two parts according to the given predicate.
     *     </p>
     * </div>
     *
     * @param predicate the predicate used to partition the sequence; must not be {@code null}
     * @return a pair of sequences: elements matching the predicate, and elements not matching it
     *
     * @since 1.0.0
     */
    @NonNull Value2<? extends Seq<A>, ? extends Seq<A>> split(final @NonNull Predicate<? super A> predicate);

    /**
     * <div>
     *     <p>
     *         Splits this sequence into two parts according to an implementation‑specific rule.
     *     </p>
     * </div>
     *
     * @return a pair of two sub‑sequences
     * @throws IllegalStateException if the sequence cannot be split according to the rule
     *
     * @since 1.0.0
     */
    @NonNull Value2<? extends Seq<A>, ? extends Seq<A>> split()
            throws IllegalStateException;

    /**
     * <div>
     *     <p>
     *         Filters this sequence by the given predicate, returning a sequence with only
     *         the elements that satisfy the predicate.
     *     </p>
     * </div>
     *
     * @param pred the filter predicate; must not be {@code null}
     * @return a new sequence containing only matching elements (never {@code null})
     *
     * @since 1.0.0
     */
    @NonNull Seq<A> filter(final @NonNull Predicate<? super A> pred);

    /**
     * <div>
     *     <p>
     *         Materializes this sequence into a concrete {@link java.util.Collection} provided by the supplier.
     *     </p>
     *     <p>
     *         Contract: {@code init} must not be {@code null} and must construct a fresh, mutable collection.
     *         Implementations must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param init a supplier for the target collection instance; must not be {@code null}
     * @param <C>  the concrete collection type
     * @return a collection containing the elements of this sequence (never {@code null})
     *
     * @since 1.0.0
     */
    <C extends Collection<? super A>> @NonNull C toCollection(final @NonNull Supplier<C> init);

    /**
     * <div>
     *     <p>
     *         Convenience: materializes this sequence into a mutable {@link java.util.ArrayList}.
     *     </p>
     * </div>
     *
     * @return a new {@code ArrayList} with the elements of this sequence
     *
     * @since 1.0.0
     */
    default @NonNull List<A> toList() {
        return this.toCollection(java.util.ArrayList::new);
    }

    /**
     * <div>
     *     <p>
     *         Convenience: materializes this sequence into a mutable {@link java.util.LinkedHashSet}
     *         (preserving encounter order).
     *     </p>
     * </div>
     *
     * @return a new {@code LinkedHashSet} with the elements of this sequence
     *
     * @since 1.0.0
     */
    default @NonNull Set<A> toSet() {
        return this.toCollection(java.util.LinkedHashSet::new);
    }
}
