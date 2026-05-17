package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A functional interface for binary functions that augments {@link BiFunction} with
 *         stronger non-null contracts and utilities for currying, partial application, flipping,
 *         and composition.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, arguments must not be {@code null} and results must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <X1> the type of the first argument
 * @param <X2> the type of the second argument
 * @param <Y>  the result type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Fun2<X1, X2, Y>
        extends BiFunction<X1, X2, Y> {

    /**
     * <div>
     *     <p>
     *         Wraps the provided {@link BiFunction} into a {@code Fun2}, enforcing a non-null result.
     *     </p>
     * </div>
     *
     * @param function the function to wrap; must not be {@code null}
     * @param <X1>     the type of the first argument
     * @param <X2>     the type of the second argument
     * @param <Y>      the result type
     * @return a {@code Fun2} wrapper
     * @throws NullPointerException if {@code function} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    static <X1, X2, Y> Fun2<X1, X2, Y> fun2(final @NonNull BiFunction<X1, X2, Y> function) {
        Objects.requireNonNull(function, nullValue("function"));
        return (x1, x2) -> Objects.requireNonNull(function.apply(x1, x2), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Applies this binary function to the given arguments.
     *     </p>
     *     <p>
     *         Contract: inputs must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param x1 the first argument; must not be {@code null}
     * @param x2 the second argument; must not be {@code null}
     * @return the result; never {@code null}
     * @throws NullPointerException if any input is {@code null} or the implementation returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @Pure
    @NonNull Y apply(final @NonNull X1 x1,
                     final @NonNull X2 x2);

    /**
     * <div>
     *     <p>
     *         Returns a function that first applies {@code this} to its arguments, and then applies
     *         {@code next} to the result.
     *     </p>
     *     <p>
     *         This represents the composition {@code next ∘ this}.
     *     </p>
     *     <p>
     *         Contract: {@code next} must not be {@code null}; both {@code this} and {@code next} must not
     *         return {@code null}.
     *     </p>
     * </div>
     *
     * @param next the function to apply afterwards; must not be {@code null}
     * @param <Z>  the result type of {@code next}
     * @return the composed function
     * @throws NullPointerException if {@code next} is {@code null} or any intermediate result is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    default <Z> @NonNull Fun2<X1, X2, Z> andThen(@NonNull final Function<? super Y, ? extends Z> next) {
        Objects.requireNonNull(next, nullValue("next"));
        return (x1, x2) -> {
            final var y
                = Objects.requireNonNull(this.apply(x1, x2), nullResult());
            return Objects.requireNonNull(next.apply(y), nullResult());
        };
    }


    /**
     * <div>
     *     <p>
     *         Partially applies this function by supplying the first argument via a {@link java.util.function.Supplier}.
     *     </p>
     *     <p>
     *         Returns a unary function {@code x2 -> apply(supplier.get(), x2)}.
     *         The supplier is invoked <em>every time</em> the returned function is applied.
     *     </p>
     *     <p>
     *         Contract: the supplier and its value must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param supplier supplier for the first argument; must not be {@code null}
     * @return a unary function over the second argument
     * @throws NullPointerException if the supplier or its value is {@code null}, or the result is {@code null}
     *
     * @since 1.0.0
     */
    default @NonNull Fun<X2, Y> partial1(final @NonNull Supplier<X1> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return x2 -> {
            Objects.requireNonNull(x2, nullValue("x2"));
            final var x1
                = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(this.apply(x1, x2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Partially applies this function by supplying the second argument via a {@link java.util.function.Supplier}.
     *     </p>
     *     <p>
     *         Returns a unary function {@code x1 -> apply(x1, supplier.get())}.
     *         The supplier is invoked <em>every time</em> the returned function is applied.
     *     </p>
     *     <p>
     *         Contract: the supplier and its value must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param supplier supplier for the second argument; must not be {@code null}
     * @return a unary function over the first argument
     * @throws NullPointerException if the supplier or its value is {@code null}, or the result is {@code null}
     *
     * @since 1.0.0
     */
    default @NonNull Fun<X1, Y> partial2(final @NonNull Supplier<X2> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return x1 -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            final var x2
                = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(this.apply(x1, x2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Returns a function that flips the order of arguments before applying {@code this}.
     *     </p>
     *     <p>
     *         {@code (x2, x1) -> apply(x1, x2)}
     *     </p>
     * </div>
     *
     * @return a function with flipped argument order
     *
     * @since 1.0.0
     */
    default @NonNull Fun2<X2, X1, Y> flip() {
        return (x2, x1) -> {
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x1, nullValue("x1"));
            return Objects.requireNonNull(this.apply(x1, x2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Curries this binary function into a unary function returning another unary function.
     *     </p>
     *     <p>
     *         {@code x1 -> x2 -> apply(x1, x2)}
     *     </p>
     *     <p>
     *         Contract: neither intermediate nor final results may be {@code null}.
     *     </p>
     * </div>
     *
     * @return a curried form of this function
     *
     * @since 1.0.0
     */
    default @NonNull Fun<X1, Fun<X2, Y>> curry() {
        return x1 -> x2 -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            return Objects.requireNonNull(this.apply(x1, x2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
         * Uncurries a curried function into a binary function.
         *     </p>
         *     <p>
         *         {@code (x1, x2) -> curried.apply(x1).apply(x2)}
         *     </p>
         *     <p>
         *         Contract: {@code curried} must not be {@code null}; none of the intermediate results may be {@code null}.
         *     </p>
         * </div>
         *
         * @param curried the curried function; must not be {@code null}
     * @param <X1>    the type of the first argument
     * @param <X2>    the type of the second argument
     * @param <Y>     the result type
     * @return the uncurried {@code Fun2}
     * @throws NullPointerException if {@code curried} is {@code null} or any intermediate result is {@code null}
     *
     * @since 1.0.0
     */
    static <X1, X2, Y> Fun2<X1, X2, Y> uncurry(@NonNull final Fun<X1, Fun<X2, Y>> curried) {
        Objects.requireNonNull(curried, nullValue("curried"));
        return (x1, x2) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            return Objects.requireNonNull(curried.apply(x1).apply(x2), nullResult());
        };
    }

}
