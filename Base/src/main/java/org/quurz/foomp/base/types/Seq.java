package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.cantCast;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A sequence of values of type {@code A}. A sequence may be empty, contain one or more
 *         elements, and can be extended by adding elements.
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
        extends Mergeable<Seq<A>> {

    /**
     * <div>
     *     <p>
     *         Witness type for {@code Seq} used in the higher‑kinded encoding.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    class µ implements WitnessType { protected µ() {} }

    /**
     * <div>
     *     <p>
     *         Narrows a {@link Higher1} back to a {@code Seq}.
     *     </p>
     * </div>
     *
     * @param wide the higher‑kinded value to narrow; must not be {@code null}
     * @param <A>   the element type
     * @return the narrowed {@code Seq} instance (never {@code null})
     * @throws NullPointerException if {@code wide} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <A> Seq<A> narrow(final @NonNull Higher1<? extends µ, A> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        if (wide instanceof Seq<?>) {
            return (Seq<A>) wide;
        } else {
            throw new IllegalArgumentException(cantCast("wide", Seq.class));
        }
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

    /**
     * <div>
     *     <p>
     *         Returns the first element of this sequence in a safe manner.
     *     </p>
     * </div>
     *
     * @return a {@link Value} containing the head element, or an empty {@link Value} if this sequence is empty
 *
 * @since 1.0.0
 */
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
     *         Appends all elements of the specified other sequence to the end of this sequence.
     *     </p>
     * </div>
     *
     * @param other the sequence whose elements should be appended; must not be {@code null}
     * @return a new sequence containing all elements of this sequence followed by all elements of the other sequence
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    default @NonNull Seq<A> appendAll(final @NonNull Seq<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return this.merge(other);
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
    default @NonNull Seq<A> prependAll(final @NonNull Seq<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return other.merge(this);
    }

    /**
     * <div>
     *     <p>
     *         Deconstructs this sequence into its head and tail.
     *     </p>
     * </div>
     *
     * @return a {@link Value2} consisting of the first element and the remainder of the sequence (never {@code null})
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
     *         Partitions this sequence into a pair of sequences according to the specified predicate.
     *         The first sequence contains all elements that satisfy the predicate, and the second
     *         sequence contains all elements that do not.
     *     </p>
     * </div>
     *
     * @param pred the predicate used to partition the sequence; must not be {@code null}
     * @return a {@link Value2} holding the sequence of matching elements as its first value and non-matching elements as its second value (never {@code null})
     * @throws NullPointerException if {@code pred} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull Value2<? extends Seq<A>, ? extends Seq<A>> partition(final @NonNull Predicate<? super A> pred);

    /**
     * <div>
     *     <p>
     *         Splits this sequence into a pair of sequences according to the specified predicate.
     *         The first sequence contains the longest prefix of elements satisfying the predicate,
     *         and the second sequence contains the remainder of the sequence starting from the first element
     *         that does not satisfy the predicate.
     *     </p>
     * </div>
     *
     * @param pred the predicate used to test elements; must not be {@code null}
     * @return a {@link Value2} holding the prefix of matching elements as its first value and the remainder of the sequence as its second value (never {@code null})
     * @throws NullPointerException if {@code pred} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull Value2<? extends Seq<A>, ? extends Seq<A>> span(final @NonNull Predicate<? super A> pred);

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
