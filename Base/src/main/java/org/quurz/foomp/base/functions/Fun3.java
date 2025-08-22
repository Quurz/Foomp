package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;
import org.quurz.foomp.base.types.Deferrable3;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A functional interface for ternary functions. Provides utilities for composition,
 *         currying, partial application, and deferral with strict non-null contracts.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <X1> the type of the first argument
 * @param <X2> the type of the second argument
 * @param <X3> the type of the third argument
 * @param <Y>  the result type
 *
 * @since 1.0.0
 *
 *
 */
@FunctionalInterface
public interface Fun3<X1, X2, X3, Y>
        extends Deferrable3<X1, X2, X3, Y> {

    /**
     * <div>
     *     <p>
     *         Applies this function to the given arguments.
     *     </p>
     *     <p>
     *         Contract: inputs must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param x1 the first argument; must not be {@code null}
     * @param x2 the second argument; must not be {@code null}
     * @param x3 the third argument; must not be {@code null}
     * @return the result; never {@code null}
     */
    @Pure
    @NonNull Y apply(@NonNull final X1 x1,
                     @NonNull final X2 x2,
                     @NonNull final X3 x3);

    /**
     * <div>
     *     <p>
     *         Composes this function with a post-processing step: applies {@code this} and then {@code next}.
     *     </p>
     *     <p>
     *         Contract: {@code next} must not be {@code null}; intermediate and final results must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param <Z>  the result type of {@code next}
     * @param next the function to apply after this one; must not be {@code null}
     * @return a composed {@code Fun3} that applies {@code this} and then {@code next}
     * @throws NullPointerException if {@code next} is {@code null} or any intermediate result is {@code null}
     */
    default <Z> @NonNull Fun3<X1, X2, X3, Z> andThen(@NonNull final Function<? super Y, ? extends Z> next) {
        Objects.requireNonNull(next, "Argument 'next' must not be null");
        return (x1, x2, x3) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            return Objects.requireNonNull(next.apply(this.apply(x1, x2, x3)), nullResult());
        };
    }

    @Override
    @NonNull
    default Callable<Y> defer(final @NonNull Supplier<X1> a1,
                              final @NonNull Supplier<X2> a2,
                              final @NonNull Supplier<X3> a3) {
        Objects.requireNonNull(a1, nullValue("a1"));
        Objects.requireNonNull(a2, nullValue("a2"));
        Objects.requireNonNull(a3, nullValue("a3"));
        return () -> {
            final var x1
                = Objects.requireNonNull(a1.get(), nullSuppliedFrom("a1"));
            final var x2
                = Objects.requireNonNull(a2.get(), nullSuppliedFrom("a2"));
            final var x3
                = Objects.requireNonNull(a3.get(), nullSuppliedFrom("a3"));
            return Objects.requireNonNull(this.apply(x1, x2, x3), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Partially applies this function by supplying the first argument via a {@link Supplier}.
     *         Returns a binary function over the remaining arguments.
     *     </p>
     *     <p>
     *         Contract: the supplier and its value must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param supplier supplier for {@code x1}; must not be {@code null}
     * @return a binary function over {@code x2} and {@code x3}
     * @throws NullPointerException if the supplier or its value is {@code null}, or the result is {@code null}
     */
    default @NonNull Fun2<X2, X3, Y> partial1(@NonNull final Supplier<X1> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (x2, x3) -> {
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            return Objects.requireNonNull(this.apply(supplier.get(), x2, x3), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Partially applies this function by supplying the second argument via a {@link Supplier}.
     *         Returns a binary function over the remaining arguments.
     *     </p>
     *     <p>
     *         Contract: the supplier and its value must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param supplier supplier for {@code x2}; must not be {@code null}
     * @return a binary function over {@code x1} and {@code x3}
     * @throws NullPointerException if the supplier or its value is {@code null}, or the result is {@code null}
     */
    default @NonNull Fun2<X1, X3, Y> partial2(@NonNull final Supplier<X2> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (x1, x3) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x3, nullValue("x3"));
            return Objects.requireNonNull(this.apply(x1, supplier.get(), x3), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Partially applies this function by supplying the third argument via a {@link Supplier}.
     *         Returns a binary function over the remaining arguments.
     *     </p>
     *     <p>
     *         Contract: the supplier and its value must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param supplier supplier for {@code x3}; must not be {@code null}
     * @return a binary function over {@code x1} and {@code x2}
     * @throws NullPointerException if the supplier or its value is {@code null}
     */
    default @NonNull Fun2<X1, X2, Y> partial3(@NonNull final Supplier<X3> supplier) {
        Objects.requireNonNull(supplier, "Argument 'supplier' must not be null");
        return (x1, x2) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            return this.apply(x1, x2, supplier.get());
        };
    }

    /**
     * <div>
     *     <p>
     *         Curries this ternary function into a chain of unary functions.
     *     </p>
     *     <p>
     *         Contract: inputs and all intermediate results must not be {@code null}.
     *     </p>
     * </div>
     *
     * @return the curried representation of this function
     */
    default @NonNull Fun<X1, Fun<X2, Fun<X3, Y>>> curry() {
        return x1 -> x2 -> x3 -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            return Objects.requireNonNull(this.apply(x1, x2, x3), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Uncurries a curried ternary function into a {@code Fun3}.
     *     </p>
     *     <p>
     *         Contract: {@code curried} must not be {@code null}; none of the intermediate results may be {@code null}.
     *     </p>
     * </div>
     *
     * @param curried the curried function to uncurry; must not be {@code null}
     * @param <X1>    the type of the first argument
     * @param <X2>    the type of the second argument
     * @param <X3>    the type of the third argument
     * @param <Y>     the result type
     * @return a {@code Fun3} representing the uncurried function
     * @throws NullPointerException if {@code curried} is {@code null} or any intermediate result is {@code null}
     */
    static <X1, X2, X3, Y> Fun3<X1, X2, X3, Y> uncurry(@NonNull final Function<X1, Function<X2, Function<X3, Y>>> curried) {
        Objects.requireNonNull(curried, nullValue("curried"));
        return (x1, x2, x3) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            return Objects.requireNonNull(curried.apply(x1).apply(x2).apply(x3), nullResult());
        };
    }

}
