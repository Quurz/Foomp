package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Provider;
import org.quurz.foomp.base.types.Seq;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.*;
import static org.quurz.foomp.base.util.Maybe.none;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

/**
 * <div>
 *     <p>
 *         A functional, immutable sequence of elements of type {@code A}. This interface
 *         represents a singly linked list structure with lazy evaluation of element values
 *         via {@link Provider}.
 *     </p>
 *     <p>
 *         <strong>Characteristics:</strong>
 *     </p>
 *     <ul>
 *         <li><strong>Immutability:</strong> All operations return a new sequence without modifying the original.</li>
 *         <li><strong>Stack-Safety:</strong> Recursive operations (like {@link #filter(Predicate)} or {@link #reverse()})
 *             are implemented using {@link Trampoline} to prevent {@link StackOverflowError} on long sequences.</li>
 *         <li><strong>Encounter-Order:</strong> Methods preserve the order of elements as they were provided during construction.</li>
 *         <li><strong>Laziness:</strong> While the structure (links between elements) is eager in the current
 *             implementation, the values themselves are supplied lazily.</li>
 *     </ul>
 * </div>
 *
 * @param <A> the element type
 *
 * @see Seq
 * @see Trampoline
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public sealed interface Sequence<A>
    extends Seq<A>,
            Higher1<Sequence.µ, A>
    permits Sequence.Element,
            Sequence.Empty {

    /**
     * <div>
     *     <p>
     *         Witness type for {@code Sequence} used in the higher‑kinded encoding.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    final class µ extends Seq.µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Narrows a {@link Higher1} value to a concrete {@code Sequence}.
     *     </p>
     * </div>
     *
     * @param wide the higher‑kinded value; must not be {@code null}
     * @param <A>  the element type
     * @return a {@code Sequence} instance
     * @throws NullPointerException     if {@code wide} is {@code null}
     * @throws IllegalArgumentException if {@code wide} is not an instance of {@code Sequence}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <A> Sequence<A> narrow(final @NonNull Higher1<? extends µ, A> wide) {
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
     *         Returns an empty sequence.
     *     </p>
     * </div>
     *
     * @param <A> the phantom element type
     * @return an empty {@code Sequence}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <A> Sequence<A> sequence() {
        return (Sequence<A>) Empty.INSTANCE;
    }

    /**
     * <div>
     *     <p>
     *         Creates a sequence from the given elements.
     *     </p>
     *     <p>
     *         Contract: The elements are stored in the order they appear in the array.
     *         Input array and all elements must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param elements the elements to include; must not be {@code null} and must not contain {@code null}
     * @param <A>      the element type
     * @return a sequence containing the given elements
     * @throws NullPointerException if {@code elements} is {@code null}
     * @throws IllegalArgumentException if any element in the array is {@code null}
     *
     * @since 1.0.0
     */
    @SafeVarargs
    static <A> Sequence<A> sequenceOf(final @NonNull A... elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        Sequence<A> sequence
            = sequence();
        for (int i = (elements.length - 1); i >= 0; --i) {
             final var element
                 = elements[i];
            if (element != null) {
                sequence
                    = sequence.cons(element);
            } else {
                throw new IllegalArgumentException(nullElementInAt("elements", i));
            }
        }
        return sequence;
    }

    /**
     * <div>
     *     <p>
     *         Creates a sequence from the given collection.
     *     </p>
     * </div>
     *
     * @param elements the collection of elements; must not be {@code null}
     * @param <A>      the element type
     * @return a sequence containing the elements of the collection
     * @throws NullPointerException if {@code elements} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <A> Sequence<A> sequenceFrom(final Collection<? extends A> elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        return sequenceOf((A[]) elements.toArray());
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    default boolean isNotEmpty() {
        return (this instanceof Sequence.Element<A>);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    default @NonNull A head() throws NoSuchElementException {
        return switch (this) {
            case Element<A> element -> element.spool.get();
            case Empty<A> _ -> throw new NoSuchElementException(noValuePresent());
        };
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    default @NonNull Maybe<A> headSafe() {
        return switch (this) {
            case Element<A> element -> Maybe.Some.fromSpool(element.spool);
            case Empty<A> _ -> none();
        };
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    default @NonNull Sequence<A> tail()
            throws NoSuchElementException {
        return switch (this) {
            case Element<A> element -> element.tail;
            case Empty<A> _ -> throw new NoSuchElementException(noValuePresent());    // TODO: Bessere Meldung
        };
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    default @NonNull Sequence<A> cons(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return switch (this) {
            case Element<A> _ -> new Element<>(() -> element, this);
            case Empty<A> _ -> new Element<>(() -> element, (Sequence<A>) Empty.INSTANCE);
        };
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    default @NonNull Tuple2<A, Sequence<A>> decons()
            throws NoSuchElementException {
        return switch (this) {
            case Element<A> element -> tuple2(element.head(), element.tail);
            case Empty<A> _ -> throw new NoSuchElementException(noValuePresent());
        };
    }

    /**
     * <div>
     *     <p>
     *         Reverses the order of elements in this sequence.
     *     </p>
     *     <p>
     *         Stack-Safety: This method is implemented using tail-recursion via {@link Trampoline}.
     *     </p>
     * </div>
     *
     * @return a new sequence with elements in reversed order
     *
     * @since 1.0.0
     */
    default @NonNull Sequence<A> reverse() {
        return reverseRecursive(this, sequence()).get();
    }

    /**
     * Internal tail-recursive implementation for {@link #reverse()}.
     */
    private static <A> Trampoline<Sequence<A>> reverseRecursive(final Sequence<A> current,
                                                                final Sequence<A> accu) {
        return switch (current) {
            case Element<A> e -> Trampoline.more(() -> reverseRecursive(e.tail(), accu.cons(e.head())));
            case Empty<A> _ -> Trampoline.done(accu);
        };
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *     Stack-Safety: This method is implemented using tail-recursion via {@link Trampoline}.
     *     Note that the current implementation reverses the sequence twice to maintain encounter order.
     * </p>
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    default @NonNull Sequence<A> filter(final @NonNull Predicate<? super A> predicate) {
        Objects.requireNonNull(predicate, nullValue("predicate"));
        return filterRecursive(predicate, this, sequence()).get().reverse();
    }

    /**
     * Internal tail-recursive implementation for {@link #filter(Predicate)}.
     */
    private static <A> Trampoline<Sequence<A>> filterRecursive(final Predicate<? super A> predicate,
                                                               final Sequence<A> current,
                                                               final Sequence<A> accu) {
        return switch (current) {
            case Element<A> element
                -> {
                    final var value
                        = element.spool.get();
                    yield predicate.test(value)
                        ? Trampoline.more(() -> filterRecursive(predicate, element.tail, accu.cons(value)))
                        : Trampoline.more(() -> filterRecursive(predicate, element.tail, accu));
                }
            case Empty<A> _
                -> Trampoline.done(accu);
        };
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    default <C extends Collection<? super A>> @NonNull C toCollection(final @NonNull Supplier<C> init) {
        return null;    // TODO
    }

    /**
     * <div>
     *     <p>
     *         Returns the number of elements in this sequence.
     *     </p>
     * </div>
     *
     * @return the sequence size
     *
     * @since 1.0.0
     */
    default int getSize() {
        return 0;    // TODO
    }

    /**
     * <div>
     *     <p>
     *         Represents a non-empty part of the sequence.
     *     </p>
     * </div>
     *
     * @param <A> the element type
     *
     * @since 1.0.0
     */
    final class Element<A>
            implements Sequence<A> {

        private final Provider<A> spool;
        private final Sequence<A> tail;

        private Element(final Provider<A> spool,
                        final Sequence<A> tail) {
            this.spool
                = spool;
            this.tail
                = tail;
        }

    }

    /**
     * <div>
     *     <p>
     *         Represents an empty sequence.
     *     </p>
     * </div>
     *
     * @param <A> phantom element type
     *
     * @since 1.0.0
     */
    final class Empty<A>
            implements Sequence<A> {

        private static final Sequence<?> INSTANCE
            = new Empty<>();

        private Empty() {}

    }

}
