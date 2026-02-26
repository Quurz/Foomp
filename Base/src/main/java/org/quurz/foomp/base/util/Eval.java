package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *   <p>
 *     A monadic interface for deferred evaluation of values, similar in spirit to Cats’ {@code Eval}.
 *     It models three evaluation modes:
 *   </p>
 *   <ul>
 *     <li>{@code Now}: eager, value is available immediately.</li>
 *     <li>{@code Later}: lazy and memoized; computed on first access and cached thereafter.</li>
 *     <li>{@code Always}: lazy and non‑memoized; computed on every access.</li>
 *   </ul>
 *   <p>
 *     Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     Laziness is implemented via suppliers; unwinding and accessors may evaluate them.
 *   </p>
 * </div>
 *
 * @param <A> the value type carried by this evaluation
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public sealed interface Eval<A>
        extends Unwindable<Eval<A>>,
                Transmogrifyable<Eval<A>>,
                Monadic<Eval.µ, A>,
                Value<A>,
                Higher1<Eval.µ, A>
        permits Eval.Now,
                Eval.Later,
                Eval.Always {

    /**
     * <div>
     *   <p>
     *     Witness type for {@code Eval} in the higher‑kinded encoding.
     *   </p>
     * </div>
     */
    final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *   <p>
     *     Narrows a {@link Higher1} value to an {@code Eval}.
     *   </p>
     * </div>
     *
     * @param wide the higher‑kinded value; must not be {@code null}
     * @param <A>  the value type
     * @return an {@code Eval} instance
     * @throws NullPointerException if {@code wide} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <A> Eval<A> narrow(@NonNull final Higher1<? extends µ, A> wide) {
        return (Eval<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    /**
     * <div>
     *   <p>
     *     Unwraps a nested {@code Eval} by one level.
     *   </p>
     * </div>
     *
     * @param wrapped a nested {@code Eval}; must not be {@code null}
     * @param <A>     the value type
     * @return a flattened {@code Eval}
     * @throws NullPointerException if {@code wrapped} is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Eval<A> unwrap(@NonNull final Higher1<? extends µ, ? extends Higher1<? extends µ, A>> wrapped) {
        Objects.requireNonNull(wrapped, nullValue("toJoin"));
        final var narrowed
            = narrow(wrapped);
        return narrow(narrowed.get());
    }

    /**
     * <div>
     *   <p>
     *     Eager constructor: wraps a value that is already available.
     *   </p>
     * </div>
     *
     * @param value the eager value; must not be {@code null}
     * @param <A>   the value type
     * @return an eager {@code Eval}
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Eval<A> evalNow(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Now<>(value);
    }

    /**
     * <div>
     *   <p>
     *     Convenience: memoized lazy constructor wrapping a constant. The value is already computed
     *     and will be returned on first access (and cached thereafter).
     *   </p>
     * </div>
     *
     * @param value the constant to expose lazily; must not be {@code null}
     * @param <A>   the value type
     * @return a memoized lazy {@code Eval}
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Eval<A> evalLater(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Later<>(() -> value);
    }

    /**
     * <div>
     *   <p>
     *     Convenience: non‑memoized lazy constructor wrapping a constant. The value is already computed
     *     and will be returned on each access (recomputed semantics do not apply to constants).
     *   </p>
     * </div>
     *
     * @param value the constant to expose lazily; must not be {@code null}
     * @param <A>   the value type
     * @return a non‑memoized lazy {@code Eval}
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Eval<A> evalAlways(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Always<>(() -> value);
    }

    /**
     * <div>
     *   <p>
     *     Indicates that a value is present. By design, {@code Eval} always carries a value.
     *   </p>
     * </div>
     *
     * @return always {@code true}
     *
     * @since 1.0.0
     */
    @Override
    default boolean isPresent() {
        return true;
    }

    /**
     * <div>
     *   <p>
     *     Functor map: transforms the stored value and preserves the evaluation mode.
     *     Now maps eagerly; Later and Always remain lazy (memoized vs. non‑memoized).
     *   </p>
     * </div>
     *
     * @param transformation the mapping function; must not be {@code null} and must not return {@code null}
     * @param <B>            the result type
     * @return an {@code Eval} in the corresponding mode (Now/Later/Always)
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <B> Eval<B> map(@NonNull final Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation);
        return switch (this) {
            case Now<A> now -> evalNow(Objects.requireNonNull(transformation.apply(now.get()), nullResult()));
            case Later<A> later -> new Later<>(() -> Objects.requireNonNull(transformation.apply(later.get()), nullResult()));
            case Always<A> always -> new Always<>(() -> Objects.requireNonNull(transformation.apply(always.get()), nullResult()));
        };
    }

    /**
     * <div>
     *   <p>
     *     Applicative applyTo: applies a function carried by another {@code Eval} to this value.
     *     The function container is evaluated lazily where possible (Later/Always), preserving mode semantics.
     *   </p>
     * </div>
     *
     * @param transformation an {@code Eval} containing a function; must not be {@code null}
     * @param <B>            the result type
     * @return an {@code Eval} with the applied function, preserving evaluation mode
     * @throws NullPointerException if {@code transformation} is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <B> Eval<B> applyTo(@NonNull final Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        final Eval<? extends Function<? super A, ? extends B>> funEval = narrow(transformation);
        return switch (this) {
            case Now<A> now -> evalNow(Objects.requireNonNull(funEval.get().apply(now.get()), nullResult()));
            case Later<A> later -> new Later<>(() -> {
                final var f = Objects.requireNonNull(funEval.get(), nullSupplied());
                return Objects.requireNonNull(f.apply(later.get()), nullResult());
            });
            case Always<A> always -> new Always<>(() -> {
                final var f = Objects.requireNonNull(funEval.get(), nullSupplied());
                return Objects.requireNonNull(f.apply(always.get()), nullResult());
            });
        };
    }

    /**
     * <div>
     *   <p>
     *     Monadic flatMap: sequences computation by mapping to another {@code Eval} and flattening.
     *     Now flatMaps (monadic bind) eagerly; Later and Always remain lazy (memoized vs. non‑memoized).
     *   </p>
     * </div>
     *
     * @param transformation the monadic transformation; must not be {@code null}
     * @param <B>            the result type
     * @return the bound {@code Eval}
     * @throws NullPointerException if {@code transformation} is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @Override
    @NonNull
    default <B> Eval<B> flatMap(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return switch (this) {
            case Eval.Now<A> now -> unwrap(this.map(transformation));
            case Eval.Later<A> later -> new Later<>(() -> narrow(transformation.apply(later.get())).get());
            case Eval.Always<A> always -> new Always<>(() -> narrow(transformation.apply(always.get())).get());
        };
    }

    /**
     * <div>
     *   <p>
     *     Unwinds this {@code Eval} to a semantically equivalent representation while preserving
     *     its concrete evaluation mode (subtype).
     *   </p>
     *   <ul>
     *     <li>{@code Now}: returns a new {@code Now} with the current value.</li>
     *     <li>{@code Later}: evaluates immediately and returns a {@code Later} that yields the
     *         materialized value (constant supplier; remains memoized and stable).</li>
     *     <li>{@code Always}: preserves the original supplier to keep non‑memoized semantics.</li>
     *   </ul>
     * </div>
     *
     * @return a semantically equivalent {@code Eval} with preserved subtype
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Eval<A> unwind() {
       return switch (this) {
           case Now<A> now -> evalNow(now.get());
           case Later<A> later -> evalLater(later.get());
           case Always<A> always -> new Always<>(always.supplier);
       };
    }

    /**
     * <div>
     *   <p>
     *     Applies a transmogrifier function to this {@code Eval}.
     *   </p>
     * </div>
     *
     * @param transmogrifier the transformation function; must not be {@code null} and must not return {@code null}
     * @param <T>            the result type
     * @return the transformation result
     * @throws NullPointerException if {@code transmogrifier} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <T> T transmogrify(final @NonNull Function<? super Eval<A>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transformer"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResult());
    }

    /**
     * <div>
     *   <p>
     *     Eager value holder ({@code Now}).
     *   </p>
     * </div>
     *
     * @param <A> the value type
     *
     * @since 1.0.0
     */
    final class Now<A>
            implements Eval<A> {

        private final A value;

        private Now(final A value) {
            this.value
                = value;
        }

        @Override
        @NonNull
        public A get() {
            return this.value;
        }

    }

    /**
     * <div>
     *   <p>
     *     Lazy + memoized value holder ({@code Later}). Computed once and cached.
     *   </p>
     * </div>
     *
     * @param <A> the value type
     *
     * @since 1.0.0
     */
    final class Later<A>
            implements Eval<A> {

        private final Supplier<A> supplier;
        private A memo;

        private Later(final Supplier<A> supplier) {
            this.supplier
                = supplier;
        }

        @Override
        @NonNull
        public A get() {
            if (this.memo == null) {
                this.memo
                    = Objects.requireNonNull(this.supplier.get(), nullSupplied());
            }
            return this.memo;
        }

    }

    /**
     * <div>
     *   <p>
     *     Lazy + non‑memoized value holder ({@code Always}). Computed on every access.
     *   </p>
     * </div>
     *
     * @param <A> the value type
     *
     * @since 1.0.0
     */
    final class Always<A>
            implements Eval<A> {

        private final Supplier<A> supplier;

        private Always(final Supplier<A> supplier) {
            this.supplier
                = supplier;
        }

        @Override
        @NonNull
        public A get() {
            return Objects.requireNonNull(this.supplier.get(), nullSupplied());
        }

    }

}
