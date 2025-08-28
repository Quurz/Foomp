package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Util.requiresNonNullResult2;

/**
 * <div>
 *   <p>
 *     A lightweight optional container: either there is a typed value, or there isn't.
 *     Laziness is preserved via suppliers where applicable.
 *   </p>
 *   <p>
 *     Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *   </p>
 * </div>
 *
 * @param <A> the contained value type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public sealed interface Maybe<A>
        extends Monadic<Maybe.µ, A>,
                Copyable<Maybe<A>>,
                Unwindable<Maybe<A>>,
                Transmogrifyable<Maybe<A>>,
                Value<A>,
                Higher1<Maybe.µ, A>
        permits Maybe.Some,
                Maybe.None {

    /**
     * <div>
     *   <p>
     *     Witness type for {@code Maybe} used in the higher‑kinded encoding.
     *   </p>
     * </div>
     *
     * @since 1.0.0
     */
    final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *   <p>
     *     Narrows a {@link Higher1} value to a concrete {@code Maybe}.
     *   </p>
     * </div>
     *
     * @param wide the higher‑kinded value; must not be {@code null}
     * @param <A>  the contained value type
     * @return a {@code Maybe} instance
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <A> Maybe<A> narrow(final @NonNull Higher1<? extends µ, A> wide) {
        return (Maybe<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

/**
 * <div>
 *   <p>
 *     Creates a {@code Maybe.Some} with the given value.
 *   </p>
 * </div>
 *
 * @param value the value to store; must not be {@code null}
 * @param <A>   the contained value type
 * @return a non‑empty {@code Maybe}
 * @throws NullPointerException if {@code value} is {@code null}
 *
 * @since 1.0.0
 */
static <A> Maybe<A> some(final @NonNull A value) {
    Objects.requireNonNull(value, nullValue("value"));
    return new Some<>(() -> value);
}

/**
 * <div>
 *   <p>
 *     Creates a {@code Maybe} from a possibly {@code null} value.
 *     Returns {@code Some(value)} if non‑null, otherwise {@code None}.
 *   </p>
 * </div>
 *
 * @param value the possibly {@code null} value
 * @param <A>   the contained value type
 * @return a {@code Maybe} reflecting the presence or absence of {@code value}
 *
 * @since 1.0.0
 */
    static <A> Maybe<A> maybeOfNullable(@Nullable final A value) {
        return value != null
            ? some(value)
            : none();
    }

/**
 * <div>
 *   <p>
 *     Returns an empty {@code Maybe}.
 *   </p>
 * </div>
 *
 * @param <A> the (phantom) value type
 * @return a {@code None}
 *
 * @since 1.0.0
 */
    @SuppressWarnings("unchecked")
    static <A> Maybe<A> none() {
        return (Maybe<A>) None.NONE;
    }

/**
 * <div>
 *   <p>
 *     Creates a {@code Maybe} from {@link Optional}.
 *     An empty {@code Optional} maps to {@code None}, otherwise {@code Some(value)}.
 *   </p>
 * </div>
 *
 * @param optional the optional to convert; must not be {@code null}
 * @param <A>      the contained value type
 * @return a corresponding {@code Maybe}
 *
 * @since 1.0.0
 */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <A> Maybe<A> maybeFrom(@NonNull Optional<A> optional) {
        Objects.requireNonNull(optional, nullValue("optional"));
        return optional.map(Maybe::some).orElseGet(Maybe::none);
    }

/**
 * <div>
 *   <p>
 *     Indicates whether a value is present.
 *   </p>
 * </div>
 *
 * @return {@code true} if this is {@code Some}; {@code false} otherwise
 *
 * @since 1.0.0
 */
    @Override
    default boolean isPresent() {
        return this.isSome();
    }

/**
 * <div>
 *   <p>
 *     Returns whether this is {@code Some}.
 *   </p>
 * </div>
 *
 * @return {@code true} if non‑empty; {@code false} otherwise
 *
 * @since 1.0.0
 */
    @SuppressWarnings("unused")
    default boolean isSome() {
        return (this instanceof Maybe.Some<A>);
    }

/**
 * <div>
 *   <p>
 *     Returns whether this is {@code None}.
 *   </p>
 * </div>
 *
 * @return {@code true} if empty; {@code false} otherwise
 *
 * @since 1.0.0
 */
    default boolean isNone() {
        return !this.isSome();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @SuppressWarnings({"SwitchLabeledRuleCanBeCodeBlock", "unused"})
    @Override
    @UnwindingOperation
    @NonNull
    default A get()
            throws NoSuchElementException {
        return switch (this) {
            case Some<A> some -> Objects.requireNonNull(some.spool.get(), nullSupplied());
            case None<A> _$ -> throw new NoSuchElementException(noValuePresent());
        };
    }

    /**
     * <div>
     *   <p>
     *     Returns the contained value if present; otherwise returns the value supplied by {@code supplier}.
     *   </p>
     *   <p>
     *     A {@link NullPointerException} is thrown if {@code supplier} is {@code null} or returns {@code null}.
     *   </p>
     * </div>
     *
     * @param supplier supplies a fallback value; must not be {@code null}
     * @return the present value or the supplier's value
     * @since 1.0.0
     */
    @SuppressWarnings({"SwitchLabeledRuleCanBeCodeBlock", "unused"})
    @UnwindingOperation
    @NonNull
    default A getOrElse(@NonNull final Supplier<A> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return switch (this) {
            case Some<A> some -> some.get();
            case None<A> _$ -> Objects.requireNonNull(supplier.get(), nullSuppliedFrom("supplier"));
        };
    }

    /**
     * <div>
     *   <p>
     *     Returns the contained value if present; otherwise throws the exception supplied by {@code exceptionSupplier}.
     *   </p>
     *   <p>
     *     NPE considerations:
     *     - {@code exceptionSupplier} must not be {@code null}
     *     - The exception returned by {@code exceptionSupplier} must not be {@code null}
     *     - For {@code Some}, the contained value must not be {@code null}
     *   </p>
     * </div>
     *
     * @param exceptionSupplier supplies the exception to throw; must not be {@code null}
     * @param <E>               the exception type
     * @return the present value
     * @throws E                    if this is {@code None}
     * @throws NullPointerException if {@code exceptionSupplier} is {@code null}, returns {@code null}, or if the contained value is {@code null}
     * @since 1.0.0
     */
    @SuppressWarnings({"SwitchLabeledRuleCanBeCodeBlock", "unused"})
    @UnwindingOperation
    @NonNull
    default <E extends Throwable> A getOrThrow(final @NonNull Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));
        return switch (this) {
            case Some<A> some -> some.get();
            case None<A> _$ -> throw Objects.requireNonNull(exceptionSupplier.get(), nullSupplied());
        };
    }

    /**
     * <div>
     *   <p>
     *     If a value is present, passes it to the given consumer (peek).
     *   </p>
     * </div>
     *
     * @param consumer consumes the value; must not be {@code null}
     * @return this {@code Maybe}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @NonNull
    default Maybe<A> ifSome(final @NonNull Consumer<A> consumer) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        if (this instanceof Maybe.Some<A> some) {
            consumer.accept(some.get());
        }
        return this;
    }

    /**
     * <div>
     *   <p>
     *     If a value is present, passes it to the consumer; otherwise runs {@code orElse}.
     *   </p>
     * </div>
     *
     * @param consumer consumes the value; must not be {@code null}
     * @param orElse   runnable to execute if empty; must not be {@code null}
     * @return this {@code Maybe}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @NonNull
    default Maybe<A> ifPresentOrElse(final @NonNull Consumer<A> consumer,
                                     final @NonNull Runnable orElse) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        Objects.requireNonNull(orElse, nullValue("orElse"));
        if (this.isSome()) {
            consumer.accept(this.get());
        } else {
            orElse.run();
        }
        return this;
    }

    /**
     * <div>
     *   <p>
     *     Functor map: transforms the contained value while preserving laziness.
     *     {@code Some} maps to {@code Some(f(value))}; {@code None} remains {@code None}.
     *   </p>
     * </div>
     *
     * @param transformation mapping function; must not be {@code null} and must not return {@code null}
     * @param <B>            the new value type
     * @return a mapped {@code Maybe}
     *
     * @since 1.0.0
     */
    @SuppressWarnings({"unchecked"})
    @Override
    @NonNull
    default <B> Maybe<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return switch (this) {
            case Some<A> some -> (Maybe<B>) new Some<>(() -> {
                final var v
                    = Objects.requireNonNull(some.spool.get(), nullSupplied());
                return Objects.requireNonNull(transformation.apply(v), nullResult());
            });
            case None<A> none -> (Maybe<B>) none;
        };
    }

    /**
     * <div>
     *   <p>
     *     Applicative lift: applies a function contained in another {@code Maybe} to this value.
     *     If the function is absent, returns {@code None}.
     *   </p>
     * </div>
     *
     * @param transformation {@code Maybe} holding a function; must not be {@code null}
     * @param <B>            the new value type
     * @return a {@code Maybe} with the applied function or {@code None}
     *
     * @since 1.0.0
     */
    @SuppressWarnings({"unused"})
    @Override
    @NonNull
    default <B> Maybe<B> lift(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation);
        final var narrowed
            = narrow(transformation);
        return narrowed.isSome()
            ? this.map(narrowed.get())
            : none();
    }

    /**
     * <div>
     *   <p>
     *     Monadic bind: maps to another {@code Maybe} and flattens the result.
     *   </p>
     * </div>
     *
     * @param transformation mapping to another {@code Maybe}; must not be {@code null}
     * @param <B>            the new value type
     * @return the bound {@code Maybe}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <B> Maybe<B> bind(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("bindM"));
        return this.map(a -> narrow(transformation.apply(a)).get());
    }

    /**
     * <div>
     *   <p>
     *     Converts this {@code Maybe} to {@link Optional}.
     *     {@code None → Optional.empty()}, {@code Some(a) → Optional.of(a)}.
     *   </p>
     * </div>
     *
     * @return the corresponding {@code Optional}
     *
     * @since 1.0.0
     */
    default Optional<A> toOptional() {
        return this.map(Optional::of).getOrElse(Optional::empty);
    }

    /**
     * <div>
     *   <p>
     *     Returns a shallow copy of this {@code Maybe} (preserves laziness).
     *   </p>
     * </div>
     *
     * @return a copy of this {@code Maybe}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Maybe<A> copy() {
        return switch (this) {
            case Some<A> some -> new Some<>(some.spool);
            case None<A> none -> none;
        };
    }

    /**
     * <div>
     *   <p>
     *     Transmogrifies this {@code Maybe} using the given function.
     *   </p>
     * </div>
     *
     * @param transmogrifier the transforming function; must not be {@code null} and must not return {@code null}
     * @param <T>            the result type
     * @return the transformed value
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <T> T transmogrify(final @NonNull Function<? super Maybe<A>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResult());
    }

    /**
     * <div>
     *   <p>
     *     Unwinds (materializes) this {@code Maybe} into a strict representation.
     *     {@code Some} evaluates its supplier and returns {@code Some(value)}; {@code None} remains {@code None}.
     *   </p>
     * </div>
     *
     * @return a strict {@code Maybe} with the same presence/value
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @NonNull
    default Maybe<A> unwind() {
        return switch (this) {
            case Some<A> some -> some(some.get());
            case None<A> none -> none;
        };
    }

    /**
     * <div>
     *   <p>
     *     Filters the contained value using the given predicate.
     *     If absent or if the predicate returns {@code false}, returns {@code None}.
     *   </p>
     * </div>
     *
     * @param predicate the predicate to test the value; must not be {@code null}
     * @return a filtered {@code Maybe}
     *
     * @since 1.0.0
     */
    @NonNull
    default Maybe<A> filter(final @NonNull Predicate<? super A> predicate) {
        Objects.requireNonNull(predicate, nullValue("predicate"));
        return this.bind(a -> predicate.test(a)
                ? some(a)
                : none());
    }

    /**
     * <div>
     *   <p>
     *     Zips this {@code Maybe} with another using the given {@code zipper}.
     *     If either side is {@code None}, the result is {@code None}.
     *   </p>
     * </div>
     *
     * @param other   the other {@code Maybe}; must not be {@code null}
     * @param zipper  the combining function; must not be {@code null} and must not return {@code null}
     * @param <B>     the other value type
     * @param <C>     the result type
     * @return a {@code Maybe} of the combined value
     *
     * @since 1.0.0
     */
    @NonNull
    default <B, C> Maybe<C> zip(final @NonNull Maybe<B> other,
                                final @NonNull BiFunction<A, B, C> zipper) {
        Objects.requireNonNull(other, nullValue("other"));
        Objects.requireNonNull(zipper, nullValue("zipper"));
        return this.bind(a -> other.map(b -> requiresNonNullResult2(zipper, "zipper").apply(a, b)));
    }

    /**
     * <div>
     *   <p>
     *     The non‑empty variant {@code Some}.
     *   </p>
     * </div>
     *
     * @param <A> the contained value type
     *
     * @since 1.0.0
     */
    final class Some<A>
            implements Maybe<A> {

        private final Supplier<A> spool;

        private Some(final Supplier<A> spool) {
            this.spool
                = spool;
        }

        @Override
        @UnwindingOperation
        public boolean equals(final Object o) {
            if (!(o instanceof Some<?> some)) return false;

            return Objects.equals(this.spool.get(), some.spool.get());
        }

        @Override
        @UnwindingOperation
        public int hashCode() {
            return Objects.hashCode(this.spool.get());
        }

        @Override
        @UnwindingOperation
        public String toString() {
            return new StringJoiner(", ", Some.class.getSimpleName() + "[", "]")
                    .add("value=" + this.spool.get())
                    .toString();
        }
    }

    /**
     * <div>
     *   <p>
     *     The empty variant {@code None}.
     *   </p>
     * </div>
     *
     * @param <A> phantom type parameter
     *
     * @since 1.0.0
     */
    final class None<A>
            implements Maybe<A> {

        private static final Maybe<Void> NONE
            = new None<>();

        private None() {}

        @Override
        public boolean equals(final Object o) {
            return o instanceof None;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", None.class.getSimpleName() + "[", "]")
                    .toString();
        }

    }

}
