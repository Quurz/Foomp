package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.types.*;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.*;
import static org.quurz.foomp.base.util.Maybe.none;
import static org.quurz.foomp.base.util.Maybe.some;

/**
 * <div>
 *   <p>
 *     A disjoint union (sum type) that holds either a value of type {@code L} (left)
 *     or a value of type {@code R} (right).
 *   </p>
 *   <p>
 *     Commonly used to model alternative outcomes (e.g., error or success) without throwing
 *     exceptions. This implementation is right‑biased: mapping and flatMap operations act
 *     on the right value, while leaving the left value unchanged.
 *   </p>
 *   <p>
 *     <strong>Evaluation semantics:</strong>
 *     <ul>
 *       <li>
 *         The active side (left vs right) is determined eagerly at construction time or when
 *         using monadic operations such as {@link #flatMap(Function)}.
 *       </li>
 *       <li>
 *         The payload stored in {@code Left} and {@code Right} is represented lazily via
 *         {@link Supplier} and is evaluated when accessors such as {@link #getLeft()},
 *         {@link #getRight()}, {@link #map(Function)}, {@link #mapLeft(Function)},
 *         {@link #unwind()}, {@link Object#equals(Object)}, {@link Object#hashCode()}, or
 *         {@link Object#toString()} are invoked.
 *       </li>
 *     </ul>
 *   </p>
 *   <p>
 *     Contract: unless stated otherwise, inputs must not be {@code null} and results must not be
 *     {@code null}. Lazy evaluation is used internally via suppliers; unwinding methods evaluate
 *     stored suppliers.
 *   </p>
 * </div>
 *
 * @param <L> the left value type
 * @param <R> the right value type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public sealed interface Either<L, R>
        extends Mappable<Either.µ, R>,
        Appliable2<Either.µ, L, R>,
                Bindable<Either.µ, R>,
                Swappable<Either<R, L>, L, R>,
                Unwindable<Either<L, R>>,
                Transmogrifyable<Either<L, R>>,
                XorValue<L, R>,
                Higher2<Either.µ, L, R>,
                Higher1<Either.µ, R>
        permits Either.Left,
                Either.Right {


    /**
     * <div>
     *   <p>
     *     Witness type for {@code Either} used in higher‑kinded encodings.
     *   </p>
     * </div>
     *
     * @since 1.0.0
     */
    final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *   <p>
     *     Narrows a {@link Higher2} instance to an {@code Either}.
     *   </p>
     * </div>
     *
     * @param wide the higher‑kinded value; must not be {@code null}
     * @param <L>  the left value type
     * @param <R>  the right value type
     * @return the narrowed {@code Either}
     * @throws NullPointerException if {@code wide} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <L, R> Either<L, R> narrow(final @NonNull Higher2<? extends µ, L, R> wide) {
        return (Either<L, R>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    /**
     * <div>
     *   <p>
     *     Flattens a nested {@code Either} by one level.
     *   </p>
     *   <p>
     *     <strong>Semantics:</strong>
     *     <ul>
     *       <li>
     *         If the outer {@code Either} is a {@code Left}, its content (an inner higher-kinded
     *         {@code Either}) is narrowed and returned.
     *       </li>
     *       <li>
     *         If the outer {@code Either} is a {@code Right}, its content (an inner higher-kinded
     *         {@code Either}) is narrowed and returned.
     *       </li>
     *     </ul>
     *   </p>
     * </div>
     *
     * @param wrapped the nested {@code Either}; must not be {@code null}
     * @param <L>     the left value type
     * @param <R>     the right value type
     * @return the flattened {@code Either}
     * @throws NullPointerException if {@code wrapped} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <L, R> Either<L, R> flatten(final @NonNull Higher2<µ, ? extends Higher2<? extends µ, L, R>, ? extends Higher2<? extends µ, L, R>> wrapped) {
        Objects.requireNonNull(wrapped, nullValue("wrapped"));

        final var outer
            = narrow(wrapped);

        if (outer instanceof Left<?, ?> left) {
            final var innerLeft =
                    Objects.requireNonNull(left.getLeft(), nullSupplied());
            // innerLeft ist Higher2<? extends µ,L,R>; wir nehmen an, dass es Higher2<µ,L,R> ist.
            return narrow((Higher2<µ, L, R>) innerLeft);
        } else if (outer instanceof Right<?, ?> right) {
            final var innerRight =
                    Objects.requireNonNull(right.getRight(), nullSupplied());
            return narrow((Higher2<µ, L, R>) innerRight);
        } else {
            throw new IllegalStateException("Unexpected Either variant: " + outer);
        }
    }

    /**
     * <div>
     *   <p>
     *     Constructs a left value.
     *   </p>
     * </div>
     *
     * @param value the left value; must not be {@code null}
     * @param <L>   the left value type
     * @param <R>   the right value type
     * @return an {@code Either.Left}
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    static <L, R> Either<L, R> left(final @NonNull L value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Left<>(() -> value);
    }

    /**
     * <div>
     *   <p>
     *     Constructs a right value.
     *   </p>
     * </div>
     *
     * @param value the right value; must not be {@code null}
     * @param <L>   the left value type
     * @param <R>   the right value type
     * @return an {@code Either.Right}
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    static <L, R> Either<L, R> right(final @NonNull R value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Right<>(() -> value);
    }

    /**
     * <div>
     *   <p>
     *     Returns whether this {@code Either} holds a left value.
     *   </p>
     * </div>
     *
     * @return {@code true} if this is a left; {@code false} otherwise
     *
     * @since 1.0.0
     */
    @Override
    default boolean isLeft() {
        return !this.isRight();
    }

    /**
     * <div>
     *   <p>
     *     Returns the left value or throws if this is a right.
     *   </p>
     * </div>
     *
     * @return the left value
     * @throws NoSuchElementException if this is a right
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @SuppressWarnings("unused")
    @NonNull
    default L getLeft()
            throws NoSuchElementException {
        return switch (this) {
            case Left<L, R> left -> Objects.requireNonNull(left.spool.get(), nullSupplied());
            case Right<L, R> _$ -> throw new NoSuchElementException(noValuePresent());
        };
    }

    /**
     * <div>
     *   <p>
     *     Returns the left value or an alternative supplied value when this is a right.
     *   </p>
     * </div>
     *
     * @param supplier supplies the fallback; must not be {@code null}
     * @return the left value or the supplied alternative
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @SuppressWarnings("unused")
    @NonNull
    default L getLeftOrElse(@NonNull final Supplier<L> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return switch (this) {
            case Left<L, R> left -> left.getLeft();
            case Right<L, R> _$ -> Objects.requireNonNull(supplier.get(), nullSupplied());
        };
    }

    /**
     * <div>
     *   <p>
     *     Safely returns the left value as {@code Maybe}: {@code Some(left)} or {@code None}.
     *   </p>
     * </div>
     *
     * @return {@code Some(left)} if left; otherwise {@code None}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @UnwindingOperation
    @NonNull
    default Maybe<L> getLeftSafe() {
        return switch (this) {
            case Left<L, R> left -> some(left.getLeft());
            case Right<L, R> _$ -> none();
        };
    }

    /**
     * <div>
     *   <p>
     *     Returns the left value or throws a supplied exception when this is a right.
     *   </p>
     * </div>
     *
     * @param exceptionSupplier supplies the exception to throw; must not be {@code null}
     * @param <E>               the exception type
     * @return the left value
     * @throws E if this is a right
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @UnwindingOperation
    @NonNull
    default <E extends Exception> L getLeftOrThrow(@NonNull final Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));
        return switch (this) {
            case Left<L, R> left -> left.getLeft();
            case Right<L, R> _$ -> throw Objects.requireNonNull(exceptionSupplier.get(), nullSupplied());
        };
    }

    /**
     * <div>
     *   <p>
     *     Indicates whether a right value is present (right‑biased presence).
     *   </p>
     * </div>
     *
     * @return {@code true} if this is a right; {@code false} otherwise
     *
     * @since 1.0.0
     */
    @Override
    default boolean isPresent() {
        return this.isRight();
    }

    /**
     * <div>
     *   <p>
     *     Returns whether this {@code Either} holds a right value.
     *   </p>
     * </div>
     *
     * @return {@code true} if this is a right; {@code false} otherwise
     *
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("unused")
    default boolean isRight() {
        return switch (this) {
            case Left<L, R> $_ -> false;
            case Right<L, R> $_ -> true;
        };
    }

    /**
     * <div>
     *   <p>
     *     Right‑biased {@code get}: returns the right value or throws if this is a left.
     *   </p>
     * </div>
     *
     * @return the right value
     * @throws NoSuchElementException if this is a left
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default R get()
            throws NoSuchElementException {
        return this.getRight();
    }

    /**
     * <div>
     *   <p>
     *     Returns the right value or throws if this is a left.
     *   </p>
     * </div>
     *
     * @return the right value
     * @throws NoSuchElementException if this is a left
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @SuppressWarnings("unused")
    @NonNull
    default R getRight()
            throws NoSuchElementException {
        return switch (this) {
            case Left<L, R> $_ -> throw new NoSuchElementException(noValuePresent());
            case Right<L, R> right -> Objects.requireNonNull(right.spool.get(), nullSupplied());
        };
    }

    /**
     * <div>
     *   <p>
     *     Returns the right value or a supplied alternative when this is a left.
     *   </p>
     * </div>
     *
     * @param supplier supplies the fallback; must not be {@code null}
     * @return the right value or the supplied alternative
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @SuppressWarnings("unused")
    @NonNull
    default R getRightOrElse(@NonNull final Supplier<R> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return switch (this) {
            case Left<L, R> $_ -> Objects.requireNonNull(supplier.get(), nullSupplied());
            case Right<L, R> right -> this.getRight();
        };
    }

    /**
     * <div>
     *   <p>
     *     Safely returns the right value as {@code Maybe}: {@code Some(right)} or {@code None}.
     *   </p>
     * </div>
     *
     * @return {@code Some(right)} if right; otherwise {@code None}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @SuppressWarnings("unused")
    @NonNull
    default Maybe<R> getRightSafe() {
        return switch (this) {
            case Left<L, R> $_ -> none();
            case Right<L, R> right -> some(right.getRight());
        };
    }

    /**
     * <div>
     *   <p>
     *     Returns the right value or throws a supplied exception when this is a left.
     *   </p>
     * </div>
     *
     * @param exceptionSupplier supplies the exception to throw; must not be {@code null}
     * @param <E>               the exception type
     * @return the right value
     * @throws E if this is a left
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @SuppressWarnings("unused")
    @NonNull
    default <E extends Exception> R getRightOrThrow(@NonNull final Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));
        return switch (this) {
            case Left<L, R> $_ -> throw Objects.requireNonNull(exceptionSupplier.get(), nullSupplied());
            case Right<L, R> right -> right.getRight();
        };
    }

    /**
     * <div>
     *   <p>
     *     Runs the given action if a left value is present (peek).
     *   </p>
     * </div>
     *
     * @param consumer consumes the left value; must not be {@code null}
     * @return this {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @UnwindingOperation
    @NonNull
    default Either<L, R> ifLeft(@NonNull final Consumer<L> consumer) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        return this.ifEither(consumer, _$ -> {});
    }

    /**
     * <div>
     *   <p>
     *     Runs the given action if a right value is present (peek).
     *   </p>
     * </div>
     *
     * @param consumer consumes the right value; must not be {@code null}
     * @return this {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @UnwindingOperation
    @NonNull
    default Either<L, R> ifRight(@NonNull final Consumer<R> consumer) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        return this.ifEither(_$ -> {}, consumer);
    }

    /**
     * <div>
     *   <p>
     *     Runs the corresponding action depending on whether this is a left or a right (peek).
     *   </p>
     * </div>
     *
     * @param leftConsumer  consumes the left value; must not be {@code null}
     * @param rightConsumer consumes the right value; must not be {@code null}
     * @return this {@code Either}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @NonNull
    default Either<L, R> ifEither(@NonNull final Consumer<L> leftConsumer,
                                  @NonNull final Consumer<R> rightConsumer) {
        Objects.requireNonNull(leftConsumer, nullValue("leftConsumer"));
        Objects.requireNonNull(rightConsumer, nullValue("rightConsumer"));
        if (this instanceof Left<L, R> left) {
            leftConsumer.accept(left.getLeft());
        } else if (this instanceof Right<L, R> right) {
            rightConsumer.accept(right.getRight());
        }
        return this;
    }

    /**
     * <div>
     *   <p>
     *     Functor map (right‑biased): maps the right value.
     *   </p>
     * </div>
     *
     * @param transformation mapping function; must not be {@code null}
     * @param <S>            the new right value type
     * @return the mapped {@code Either}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <S> Either<L, S> map(final @NonNull Function<? super R, ? extends S> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.mapRight(transformation);
    }

    /**
     * <div>
     *   <p>
     *     Maps the right value.
     *   </p>
     * </div>
     *
     * @param fMap mapping for the right value; must not be {@code null}
     * @param <S>  the new right value type
     * @return the mapped {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    default <S> Either<L, S> mapRight(@NonNull final Function<? super R, ? extends S> fMap) {
        Objects.requireNonNull(fMap, nullValue("fMap"));
        return switch (this) {
            case Left<L, R> left -> (Either<L, S>) left;
            case Right<L, R> right -> new Right<>(() -> {
                final var r
                    = Objects.requireNonNull(right.spool.get(), nullSupplied());
                return Objects.requireNonNull(fMap.apply(r), nullResult());
            });
        };
    }

    /**
     * <div>
     *   <p>
     *     Maps the left value.
     *   </p>
     * </div>
     *
     * @param fMap mapping for the left value; must not be {@code null}
     * @param <M>  the new left value type
     * @return the mapped {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    default <M> Either<M, R> mapLeft(@NonNull final Function<? super L, ? extends M> fMap) {
        Objects.requireNonNull(fMap, nullValue("fMap"));
        return switch (this) {
            case Left<L, R> left -> new Left<>(() -> {
                final var l
                    = Objects.requireNonNull(left.spool.get(), nullSupplied());
                return Objects.requireNonNull(fMap.apply(l), nullResult());
            });
            case Right<L, R> right -> (Either<M, R>) right;
        };
    }

    /**
     * <div>
     *   <p>
     *     Maps both sides (bimap): left using {@code fMapLeft} and right using {@code fMapRight}.
     *   </p>
     * </div>
     *
     * @param fMapLeft  mapping for the left value; must not be {@code null}
     * @param fMapRight mapping for the right value; must not be {@code null}
     * @param <M>       the new left value type
     * @param <S>       the new right value type
     * @return the mapped {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    default <M, S> Either<M, S> mapEither(final @NonNull Fun<? super L, ? extends M> fMapLeft,
                                          final @NonNull Fun<? super R, ? extends S> fMapRight) {
        Objects.requireNonNull(fMapLeft, nullValue("fMapLeft"));
        Objects.requireNonNull(fMapRight, nullValue("fMapRight"));
        return (Either<M, S>) this.mapLeft(fMapLeft).mapRight(fMapRight);
    }

    /**
     * <div>
     *   <p>
     *     Applicative applyTo: applies functions stored in {@code transformation} to this {@code Either},
     *     respecting sides (left applies to left, right applies to right).
     *   </p>
     * </div>
     *
     * @param transformation {@code Either} of functions for left/right; must not be {@code null}
     * @param <M>            the new left type
     * @param <S>            the new right type
     * @return the lifted {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    default <M, S> Either<M, S> applyTo(final @NonNull Higher2<µ, ? extends Function<? super L, ? extends M>, ? extends Function<? super R, ? extends S>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        final Either<? extends Function<? super L, ? extends M>, ? extends Function<? super R, ? extends S>> narrowed
            = narrow(transformation);
        return switch (this) {
            case Left<L, R> left -> narrowed.isLeft()
                                        ? (Either<M, S>) left.mapLeft(narrowed.getLeft())
                                        : (Either<M, S>) left;
            case Right<L, R> right -> narrowed.isRight()
                                        ? (Either<M, S>) right.mapRight(narrowed.getRight())
                                        : (Either<M, S>) right;
        };
    }

    /**
     * <div>
     *   <p>
     *     Monadic flatMap (right‑biased): applies the given transformation (monadic bind) to the right value.
     *     Left values pass through unchanged.
     *   </p>
     *   <p>
     *     <strong>Laziness:</strong>
     *     <ul>
     *       <li>
     *         When this is a {@code Left}, {@code flatMap} returns the same left value without
     *         evaluating any payload or invoking {@code transformation}.
     *       </li>
     *       <li>
     *         When this is a {@code Right}, {@code flatMap} eagerly evaluates the current right
     *         payload and applies {@code transformation} in order to obtain the resulting
     *         {@code Either}. In other words, the choice of left/right in the result is made
     *         at {@code flatMap}-time, not deferred.
     *       </li>
     *       <li>
     *         Any laziness of the resulting payload depends on the implementation of
     *         {@code transformation}.
     *       </li>
     *     </ul>
     *   </p>
     * </div>
     *
     * @param transformation right‑side flatMap function; must not be {@code null}
     * @param <S>            the resulting right type
     * @return the bound {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    default <S> Either<L, S> flatMap(
            final @NonNull Function<? super R, ? extends Higher1<? extends µ, S>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return switch (this) {
            case Left<L, R> left -> (Either<L, S>) left;
            case Right<L, R> right -> {
                // Right-payload auswerten
                final var r
                    = Objects.requireNonNull(right.spool.get(), nullSupplied());
                // transformation anwenden und das Higher1<Either.µ, S> zu einem konkreten Either<L,S> narrown
                final var innerHK
                    = Objects.requireNonNull(transformation.apply(r), nullResult());
                yield narrow((Higher2<µ, L, S>) innerHK);
            }
        };
    }

    /**
     * <div>
     *   <p>
     *     Swaps sides: left becomes right and right becomes left.
     *   </p>
     * </div>
     *
     * @return the swapped {@code Either}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Either<R, L> swap() {
        return switch (this) {
            case Left<L, R> left -> new Right<>(left.spool);
            case Right<L, R> right -> new Left<>(right.spool);
        };
    }

    /**
     * <div>
     *   <p>
     *     Transmogrifies this {@code Either} with a provided function.
     *   </p>
     * </div>
     *
     * @param transmogrifier the function to apply; must not be {@code null}
     * @param <T>            the result type
     * @return the transformed value
     * @throws NullPointerException if {@code transmogrifier} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    default <T> @NonNull T transmogrify(final @NonNull Function<? super Either<L, R>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResult());
    }

    /**
     * <div>
     *   <p>
     *     Unwinds (materializes) the current value on the active side and returns a strict {@code Either}.
     *     {@code Left} evaluates its supplier and returns {@code Left(value)}; {@code Right} evaluates
     *     its supplier and returns {@code Right(value)}.
     *   </p>
     *   <p>
     *     After unwinding, the resulting {@code Either} no longer defers evaluation of its payload:
     *     subsequent calls to accessors or structural operations will work on already-materialized
     *     values for both sides.
     *   </p>
     * </div>
     *
     * @return a strict {@code Either} with the same side/value
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @NonNull
    default Either<L, R> unwind() {
        return switch (this) {
            case Left<L, R> left -> left(left.getLeft());
            case Right<L, R> right -> right(right.getRight());
        };
    }

    /**
     * <div>
     *   <p>
     *     Represents the left variant of {@code Either}.
     *   </p>
     *   <p>
     *     Note: equality and hashCode unwind the stored supplier.
     *   </p>
     * </div>
     *
     * @param <L> the left value type
     * @param <R> the right value type
     *
     * @since 1.0.0
     */
    final class Left<L, R>
            implements Either<L, R> {

        private final Supplier<L> spool;

        Left(final Supplier<L> spool) {
            this.spool
                = spool;
        }

        /**
         * <div>
         *   <p>
         *     Compares this {@code Left} for equality with another object.
         *     Two {@code Left} instances are equal if their left values are equal.
         *   </p>
         * </div>
         *
         * @param o the object to compare with
         * @return {@code true} if the other object is a {@code Left} with an equal value; otherwise {@code false}
         *
         * @since 1.0.0
         */
        @Override
        @UnwindingOperation
        public boolean equals(final Object o) {
            if (!(o instanceof Left<?, ?> left)) return false;

            return spool.get().equals(left.spool.get());
        }

        /**
         * <div>
         *   <p>
         *     Computes the hash code based on the left value.
         *   </p>
         * </div>
         *
         * @return the hash code for this {@code Left}
         * @since 1.0.0
         */
        @Override
        @UnwindingOperation
        public int hashCode() {
            return spool.get().hashCode();
        }

        /**
         * <div>
         *   <p>
         *     Returns a string representation of the left value.
         *   </p>
         * </div>
         *
         * @return the string representation of the left value
         *
         * @since 1.0.0
         */
        @Override
        @UnwindingOperation
        public String toString() {
            return new StringJoiner(", ", Left.class.getSimpleName() + "{", "}")
                .add(String.valueOf(Objects.requireNonNull(this.spool.get(), nullResult())))
                .toString();
        }

    }

    /**
     * <div>
     *   <p>
     *     Represents the right variant of {@code Either}.
     *   </p>
     *   <p>
     *     Note: equality and hashCode unwind the stored supplier.
     *   </p>
     * </div>
     *
     * @param <L> the left value type
     * @param <R> the right value type
     *
     * @since 1.0.0
     */
    final class Right<L, R>
            implements Either<L, R> {

       private final Supplier<R> spool;

        Right(final Supplier<R> spool) {
            this.spool
                = spool;
        }

        /**
         * <div>
         *   <p>
         *     Compares this {@code Right} for equality with another object.
         *     Two {@code Right} instances are equal if their right values are equal.
         *   </p>
         * </div>
         *
         * @param o the object to compare with
         * @return {@code true} if the other object is a {@code Right} with an equal value; otherwise {@code false}
         *
         * @since 1.0.0
         */
        @UnwindingOperation
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Right<?, ?> right)) return false;

            return spool.get().equals(right.spool.get());
        }

        /**
         * <div>
         *   <p>
         *     Computes the hash code based on the right value.
         *   </p>
         * </div>
         *
         * @return the hash code for this {@code Right}
         *
         * @since 1.0.0
         */
        @UnwindingOperation
        @Override
        public int hashCode() {
            return spool.get().hashCode();
        }

        /**
         * <div>
         *   <p>
         *     Returns a string representation of the right value.
         *   </p>
         * </div>
         *
         * @return the string representation of the right value
         *
         * @since 1.0.0
         */
        @Override
        @UnwindingOperation
        public String toString() {
            return new StringJoiner(", ", Right.class.getSimpleName() + "{", "}")
                .add(String.valueOf(Objects.requireNonNull(this.spool.get(), nullResult())))
                .toString();
        }

    }

    /**
     * <div>
     *   <p>
     *     Returns the arity of the right‑projection (this {@code Either} implements {@link Higher1} for {@code R}).
     *   </p>
     * </div>
     *
     * @return {@code 1} (right‑projection arity)
     *
     * @since 1.0.0
     */
    @Override
    default int arity() {
        return 1;
    }

}
