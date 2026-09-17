package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.types.*;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.*;

/**
 * <div>
 *     <p>
 *         A lightweight container that holds a value lazily using a {@link Supplier}.
 *     </p>
 *     <p>
 *         {@code Box} implements common algebraic abstractions such as {@link Monadic}, {@link Value},
 *         {@link Transmogrifyable}, {@link Unwindable}, and {@link Copyable}.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <A> the type of the contained value
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public class Box<A>
        implements Copyable<Box<A>>,
                   Transmogrifyable<Box<A>>,
                   Unwindable<Box<A>>,
                   Value<A>,
                   Monadic<Box.µ, A>,
                   Higher1<Box.µ, A> {

    // TODO: Sollte Provider implementieren

    /**
     * <div>
     *     <p>
     *         Witness type for {@code Box} used in the higher‑kinded encoding.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    @SuppressWarnings("NonAsciiCharacters")
    public static final class µ implements WitnessType { private µ() { } }

    /**
     * <div>
     *     <p>
     *         Narrows a {@link Higher1} value to a concrete {@code Box}.
     *     </p>
     * </div>
     *
     * @param wide the higher‑kinded value; must not be {@code null}
     * @param <A>  the contained value type
     * @return a {@code Box} instance
     * @throws NullPointerException     if {@code wide} is {@code null}
     * @throws IllegalArgumentException if {@code wide} is not an instance of {@code Box}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> Box<A> narrow(final @NonNull Higher1<? extends µ, A> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        if (wide instanceof Box<?> box) {
            return (Box<A>) box;
        } else {
            throw new IllegalArgumentException(cantCast("wide", Box.class));
        }
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code Box} wrapping the specified value.
     *     </p>
     * </div>
     *
     * @param value the value to box; must not be {@code null}
     * @param <A>   the type of the value
     * @return a new {@code Box} containing the given value
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> Box<A> box(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Box<>(() -> value);
    }

    private final Supplier<A> spool;

    private Box(final @NonNull Supplier<A> spool) {
        this.spool
            = spool;
    }

    /**
     * <div>
     *     <p>
     *         Returns {@code true} because a {@code Box} always contains a value.
     *     </p>
     * </div>
     *
     * @return {@code true}
     *
     * @since 1.0.0
     */
    @Override
    public boolean isPresent() {
        return true;
    }

    /**
     * <div>
     *     <p>
     *         Evaluates and returns the contained value.
     *     </p>
     * </div>
     *
     * @return the contained value
     * @throws NoSuchElementException if no value is present
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public A get()
            throws NoSuchElementException {
        return this.spool.get();
    }

    /**
     * <div>
     *     <p>
     *         Transforms the contained value using the given transformation function while preserving laziness.
     *     </p>
     * </div>
     *
     * @param transformation the transformation function; must not be {@code null} and must not return {@code null}
     * @param <B>            the type of the resulting value
     * @return a new {@code Box} with the transformed value
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Box<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return new Box<>(
            () -> Objects.requireNonNull(transformation.apply(this.spool.get()), nullResultFrom("transformation"))
        );
    }

    /**
     * <div>
     *     <p>
     *         Applies a function contained within another {@code Box} (as a {@link Higher1}) to this value.
     *     </p>
     * </div>
     *
     * @param transformation a {@code Box} holding the function; must not be {@code null}
     * @param <B>            the return type of the function
     * @return a new {@code Box} containing the result of applying the function
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Box<B> applyTo(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        final var boxWithFunction
            = narrow(transformation);
        return new Box<>(
            () -> {
                final var function
                    = Objects.requireNonNull(boxWithFunction.get());
                return Objects.requireNonNull(function.apply(this.spool.get()), nullResult());
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Applies the given monadic function to the contained value and flattens the resulting {@code Box}.
     *     </p>
     * </div>
     *
     * @param transformation the monadic mapping function; must not be {@code null}
     * @param <B>            the target value type
     * @return the unwrapped result of the transformation
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Box<B> flatMap(@NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return narrow(this.map(transformation).get());
    }

    /**
     * <div>
     *     <p>
     *         Combines this {@code Box} with another {@code Box} using the given {@code combiner} function.
     *     </p>
     *     <p>
     *         <strong>Laziness:</strong> The {@code combiner} function is not invoked until the value
     *         of the resulting {@code Box} is evaluated.
     *     </p>
     * </div>
     *
     * @param other    the other {@code Box}; must not be {@code null}
     * @param combiner the combining function; must not be {@code null} and must not return {@code null}
     * @param <B>      the other value type
     * @param <C>      the result type
     * @return a new {@code Box} containing the combined value
     * @throws NullPointerException if {@code other} or {@code combiner} is {@code null}
     *
     * @since 1.0.0
     */
    public @NonNull <B, C> Box<C> zip(final @NonNull Box<B> other,
                                      final @NonNull BiFunction<? super A, ? super B, ? extends C> combiner) {
        Objects.requireNonNull(other, nullValue("other"));
        Objects.requireNonNull(combiner, nullValue("combiner"));

        return new Box<>(() -> Objects.requireNonNull(combiner.apply(this.spool.get(), other.spool.get()), nullResultFrom("combiner")));
    }

    /**
     * <div>
     *     <p>
     *         Creates a shallow copy of this {@code Box} sharing the same underlying supplier.
     *     </p>
     * </div>
     *
     * @return a new {@code Box} instance
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Box<A> copy() {
        return new Box<>(this.spool);
    }

    /**
     * <div>
     *     <p>
     *         Evaluates (unwinds) the underlying supplier and returns an eagerly boxed value.
     *     </p>
     * </div>
     *
     * @return an unwound {@code Box}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull Box<A> unwind() {
        return box(this.spool.get());
    }

    /**
     * <div>
     *     <p>
     *         Transforms this {@code Box} into an arbitrary type {@code T} using the supplied transmogrifier.
     *     </p>
     * </div>
     *
     * @param transmogrifier the transformation function; must not be {@code null}
     * @param <T>            the target type
     * @return the result of applying {@code transmogrifier} to this {@code Box}
     * @throws NullPointerException if {@code transmogrifier} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <T> T transmogrify(@NonNull Function<? super Box<A>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResultFrom("transmogrifier"));
    }

    /**
     * <div>
     *     <p>
     *         Compares this {@code Box} with another object for equality by unwinding both values.
     *     </p>
     * </div>
     *
     * @param o the object to compare with
     * @return {@code true} if the other object is a {@code Box} with an equal value, {@code false} otherwise
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public boolean equals(Object o) {
        if (!(o instanceof Box<?> box)) return false;
        return Objects.equals(spool.get(), box.spool.get());
    }

    /**
     * <div>
     *     <p>
     *         Returns the hash code of the evaluated value contained in this {@code Box}.
     *     </p>
     * </div>
     *
     * @return the hash code
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public int hashCode() {
        return Objects.hashCode(spool.get());
    }

    /**
     * <div>
     *     <p>
     *         Returns a string representation of this {@code Box} including the evaluated value.
     *     </p>
     * </div>
     *
     * @return a string representation
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public String toString() {
        final StringBuilder sb = new StringBuilder("Box{");
        sb.append("spool.get=").append(this.spool.get());
        sb.append('}');
        return sb.toString();
    }
    
}
