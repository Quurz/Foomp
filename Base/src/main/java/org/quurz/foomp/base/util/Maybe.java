package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.quurz.foomp.base.types.*;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.*;

import static org.quurz.foomp.base.localisation.BaseMessages.*;

/**
 * <div>
 *   <p>
 *     A lightweight optional container: either there is a typed value, or there isn't.
 *     Laziness is preserved for the contained value via {@link Supplier}-based {@code Some} where applicable.
 *   </p>
 *   <p>
 *     <strong>Evaluation semantics:</strong>
 *     <ul>
 *       <li>
 *         The presence of a value ({@code Some} vs {@code None}) is decided eagerly at construction
 *         time or when using monadic operations such as {@link #flatMap(Function)}.
 *       </li>
 *       <li>
 *         The payload of {@code Some} is represented lazily and is only evaluated when methods like
 *         {@link #get()}, {@link #map(Function)}, {@link #flatMap(Function)} (on the inner value),
 *         {@link #unwind()}, {@link Object#equals(Object)}, {@link Object#hashCode()}, or {@link Object#toString()} are invoked.
 *       </li>
 *     </ul>
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
     *     Wraps a {@code Maybe} instance as a {@link Higher1}.
     *   </p>
     * </div>
     *
     * @param narrow the concrete {@code Maybe} instance; must not be {@code null}
     * @param <A>    the contained value type
     * @return a higher‑kinded value
     *
     * @since 1.0.0
     */
    static <A> Higher1<µ, A> wide(final @NonNull Maybe<A> narrow) {
        Objects.requireNonNull(narrow, nullValue("narrow"));
        return narrow;
    }

    /**
     * <div>
     *   <p>
     *     Flattens a nested {@code Maybe} by one level (monadic join).
     *   </p>
     * </div>
     *
     * @param wrapped the nested {@code Maybe}; must not be {@code null}
     * @param <A>     the contained value type
     * @return a flattened {@code Maybe}
     * @throws NullPointerException if {@code wrapped} is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Maybe<A> flatten(final @NonNull Higher1<? extends µ, ? extends Higher1<? extends µ, A>> wrapped) {
        Objects.requireNonNull(wrapped, nullValue("wrapped"));
        return narrow(wrapped).flatMap(Function.identity());
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
     *   <p>
     *     <strong>Laziness:</strong>
     *     The contained payload is evaluated eagerly when this method is invoked and
     *     {@code this} is {@code Some}. The consumer itself is responsible for any
     *     additional laziness or side effects.
     *   </p>
     * </div>
     *
     * @param consumer consumes the value; must not be {@code null}
     * @return this {@code Maybe}
     *
     * @since 1.0.0
     */
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
 *     Executes the given action if a value is present.
 *     This is a convenience overload of {@link #ifSome(Consumer)} for cases where
 *     the action does not need access to the contained value.
 *   </p>
 *   <p>
 *     <strong>Laziness:</strong>
 *     The decision whether to run the action is made eagerly based on whether this
 *     {@code Maybe} is {@code Some}. The payload is not evaluated unless other
 *     operations (e.g. {@link #get()}) are invoked.
 *   </p>
 * </div>
 *
 * @param runnable the action to run when this is {@code Some}; must not be {@code null}
 * @return this {@code Maybe}
 *
 * @since 1.0.0
 */
    default Maybe<A> ifSome(final @NonNull Runnable runnable) {
        Objects.requireNonNull(runnable, nullValue("runnable"));
        if (this instanceof Maybe.Some<A>) {
            runnable.run();
        }
        return this;
    }

/**
 * <div>
 *   <p>
 *     Returns this {@code Maybe} if a value is present; otherwise supplies a fallback value.
 *     If this is {@code None}, the supplied value is wrapped in {@code Some} and returned.
 *     A {@link NullPointerException} is thrown if the supplier is {@code null} or supplies
 *     {@code null}.
 *   </p>
 *   <p>
 *     <strong>Laziness:</strong>
 *     The supplier is only invoked eagerly when this {@code Maybe} is {@code None}.
 *     The fallback value itself is stored lazily inside {@code Some} according to the
 *     usual payload semantics.
 *   </p>
 * </div>
 *
 * @param supplier supplies a fallback value when this is {@code None}; must not be {@code null}
 * @return {@code this} if {@code Some}, otherwise a new {@code Some} with the supplied value
 *
 * @since 1.0.0
 */
    default Maybe<A> ifNone(final @NonNull Supplier<A> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        if (this instanceof Maybe.None<A>) {
            return some(Objects.requireNonNull(supplier.get(), nullSuppliedFrom("supplier")));
        }
        return this;
    }

/**
 * <div>
 *   <p>
 *     Executes the given action if this {@code Maybe} is empty.
 *     This is useful for triggering side effects (logging, metrics, fallbacks) when no value
 *     is present, without changing the {@code Maybe} itself.
 *   </p>
 *   <p>
 *     <strong>Laziness:</strong>
 *     No payload is evaluated, as {@code None} carries no value. The runnable is invoked
 *     eagerly if and only if this is {@code None}.
 *   </p>
 * </div>
 *
 * @param runnable the action to run when this is {@code None}; must not be {@code null}
 * @return this {@code Maybe}
 *
 * @since 1.0.0
 */
    default Maybe<A> ifNone(final @NonNull Runnable runnable) {
        Objects.requireNonNull(runnable, nullValue("runnable"));
        if (this instanceof Maybe.None<A>) {
            runnable.run();
        }
        return this;
    }

    /**
     * <div>
     *   <p>
     *     If a value is present, passes it to the consumer; otherwise runs {@code orElse}.
     *   </p>
     *   <p>
     *     <strong>Laziness:</strong>
     *     When this is {@code Some}, the payload is evaluated eagerly to pass it to the consumer.
     *     When this is {@code None}, the {@code orElse} runnable is invoked eagerly instead.
     *   </p>
     * </div>
     *
     * @param consumer consumes the value; must not be {@code null}
     * @param orElse   runnable to execute if empty; must not be {@code null}
     * @return this {@code Maybe}
     *
     * @since 1.0.0
     */
    @NonNull
    default Maybe<A> ifSomeOrElse(final @NonNull Consumer<A> consumer,
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
     *     Returns a new {@code Maybe} that ignores the current value and yields {@code Some(newValue)}.
     *     This is a convenience "wither" to replace the payload with a new non-null value.
     *   </p>
     * </div>
     *
     * @param newValue the replacement value; must not be {@code null}
     * @param <B>      the new value type
     * @return {@code Some(newValue)}
     *
     * @since 1.0.0
     */
    default <B> Maybe<B> with(final @NonNull B newValue) {
        Objects.requireNonNull(newValue, nullValue("newValue"));
        return some(newValue);
    }

    /**
     * <div>
     *   <p>
     *     Returns a new {@code Maybe} by replacing the current value with {@code newValue},
     *     accepting {@code null}. If {@code newValue} is {@code null}, the result is {@code None};
     *     otherwise {@code Some(newValue)}.
     *   </p>
     * </div>
     *
     * @param newValue the possibly {@code null} replacement value
     * @param <B>      the new value type
     * @return {@code None} if {@code newValue} is {@code null}; otherwise {@code Some(newValue)}
     *
     * @since 1.0.0
     */
    default <B> Maybe<B> withNullable(final B newValue) {
        return newValue == null
                ? none()
                : some(newValue);
    }

    /**
     * <div>
     *   <p>
     *     Functor map: transforms the contained value while preserving laziness of the payload.
     *     {@code Some} maps to a new {@code Some} whose supplier first evaluates the original payload
     *     and then applies {@code transformation}; {@code None} remains {@code None}.
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
            case Some<A> some
                -> (Maybe<B>) new Some<>(
                    () -> Objects.requireNonNull(transformation.apply(some.get()), nullResultFrom("transformation"))
                );
            case None<A> none
                -> (Maybe<B>) none;
        };
    }

    /**
     * <div>
     *   <p>
     *     Applicative applyTo: applies a function contained in another {@code Maybe} to this value.
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
    default <B> Maybe<B> applyTo(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation);
        final var narrowed
            = narrow(transformation);
        return switch (narrowed) {
            case None<? extends Function<? super A, ? extends B>> _$ -> none();
            case Some<? extends Function<? super A, ? extends B>> some -> this.map(some.get());
        };
    }

    /**
     * <div>
     *   <p>
     *     Monadic flatMap: maps the contained value to another {@code Maybe} and flattens the result.
     *   </p>
     *   <p>
     *     <strong>Laziness:</strong>
     *     <ul>
     *       <li>
     *         The presence of a value in the result ({@code Some} vs {@code None}) is determined eagerly
     *         when {@code flatMap} is invoked.
     *       </li>
     *       <li>
     *         For {@code Some}, the inner payload of the resulting {@code Maybe} may still be lazy,
     *         depending on the implementation of {@code transformation}.
     *       </li>
     *     </ul>
     *   </p>
     * </div>
     *
     * @param transformation mapping to another {@code Maybe}; must not be {@code null}
     * @param <B>            the new value type
     * @return the bound {@code Maybe}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @UnwindingOperation
    @Override
    @NonNull
    default <B> Maybe<B> flatMap(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return switch (this) {
            case None<A> _$ -> none();
            case Some<A> some -> Objects.requireNonNull(narrow(transformation.apply(some.get())), nullResultFrom("transformation"));
        };
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
     *   <p>
     *     After unwinding, the resulting {@code Maybe} no longer defers evaluation of its payload.
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
     *     If a value is present and the predicate evaluates to {@code true}, this {@code Maybe} is
     *     returned unchanged; otherwise {@code None} is returned.
     *   </p>
     *   <p>
     *     <strong>Laziness:</strong>
     *     <ul>
     *       <li>
     *         The decision whether the result is {@code Some} or {@code None} is made eagerly when
     *         {@code filter} is invoked, based on evaluating the predicate on the current payload.
     *       </li>
     *       <li>
     *         If the result is {@code Some}, the payload may still be lazy according to the original
     *         {@code Some} implementation.
     *       </li>
     *     </ul>
     *   </p>
     * </div>
     *
     * @param predicate the predicate to test the value; must not be {@code null}
     * @return a filtered {@code Maybe}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @NonNull
    default Maybe<A> filter(final @NonNull Predicate<? super A> predicate) {
        Objects.requireNonNull(predicate, nullValue("predicate"));
        return this.flatMap(a -> predicate.test(a)
                ? some(a)
                : none());
    }

    /**
     * <div>
     *   <p>
     *     Zips this {@code Maybe} with another using the given {@code zipper}.
     *     If either side is {@code None}, the result is {@code None}.
     *   </p>
     *   <p>
     *     <strong>Laziness:</strong>
     *     <ul>
     *       <li>
     *         The decision whether the result is {@code Some} or {@code None} is made eagerly
     *         when {@code zip} is invoked, based on the presence of values in both operands.
     *       </li>
     *       <li>
     *         The {@code zipper} is <strong>not</strong> invoked until the resulting {@code Some}
     *         is materialised (e.g., via {@link #get()} or {@link #unwind()}).
     *       </li>
     *     </ul>
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
    @SuppressWarnings("unused")
    @NonNull
    default <B, C> Maybe<C> zip(final @NonNull Maybe<B> other,
                                final @NonNull BiFunction<A, B, C> zipper) {
        Objects.requireNonNull(other, nullValue("other"));
        Objects.requireNonNull(zipper, nullValue("zipper"));

        return switch (this) {
            case None<A> _$
                -> none();
            case Some<A> some
                -> switch (other) {
                    case None<B> _$
                        -> none();
                    case Some<B> otherSome
                        -> new Some<>(() -> Objects.requireNonNull(zipper.apply(some.get(), otherSome.get()), nullResultFrom("zipper")));
                };
        };
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

    /**
     * <div>
     *   <p>
     *     Wraps a {@link Supplier} into a {@link Some} variant.
     *   </p>
     * </div>
     *
     * @param spool the supplier providing the value; must not be {@code null}
     * @param <A>   the contained value type
     * @return a new {@code Some} instance
     *
     * @since 1.0.0
     */
    static <A> Some<A> fromSpool(final @NonNull Supplier<A> spool) {
            return new Some<>(spool);
        }

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
