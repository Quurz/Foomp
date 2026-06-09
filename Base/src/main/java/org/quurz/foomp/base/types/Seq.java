package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

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
public interface Seq<A> {

    class µ implements WitnessType { protected µ() {} }

    @SuppressWarnings("unchecked")
    static <A> Seq<A> narrow(final @NonNull Higher1<? extends µ, A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (Seq<A>) other;
    }

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

    @NonNull Value<A> headSafe();

    /**
     * <div>
     *     <p>
     *         Returns the remainder (tail) of this sequence without the first element.
     *     </p>
     * </div>
     *
     * @return the tail sequence (never {@code null}); may be empty
     * @throws NoSuchElementException if this sequence is empty
     *
     * @since 1.0.0
     */
    @NonNull Seq<A> tail()
        throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Prepends an element to this sequence and returns the new sequence.
     *     </p>
     * </div>
     *
     * @param element the element to prepend; must not be {@code null} (unless an implementation explicitly allows it)
     * @return a new sequence with {@code element} as its head (never {@code null})
     * @throws NullPointerException if {@code element} is {@code null} and the implementation does not support nulls
     *
     * @since 1.0.0
     */
    @NonNull Seq<A> cons(final @NonNull A element);

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
     *         Filters this sequence by the given predicate, returning a sequence with only
     *         the elements that satisfy the predicate.
     *     </p>
     * </div>
     *
     * @param pred the filter predicate; must not be {@code null}
     * @return a new sequence containing only matching elements (never {@code null})
     * @throws NullPointerException if {@code pred} is {@code null}
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
     * @throws NullPointerException if {@code init} is {@code null}
     *
     * @since 1.0.0
     */
    <C extends Collection<? super A>> @NonNull C toCollection(final @NonNull Supplier<C> init);

}
