package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * <div>
 *     <p>
 *         Functional interface describing a computation with two inputs that can be deferred.
 *         Instead of passing the inputs directly, they are provided as {@link Supplier}s so that
 *         evaluation can be performed on demand.
 *     </p>
 *     <p>
 *         This is useful when computing inputs is expensive or when execution order and timing
 *         should be controlled explicitly.
 *     </p>
 * </div>
 *
 * @param <A1> the type of the first input
 * @param <A2> the type of the second input
 * @param <B>  the result type of the computation
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Deferrable2<A1, A2, B> {

    /**
     * <div>
     *     <p>
     *         Returns an executable handle that, when called, obtains the two inputs from the given
     *         suppliers and performs the computation to produce the result.
     *     </p>
     *     <p>
     *         Contract: the suppliers must not be {@code null} and must not supply {@code null} values.
     *         The returned handle must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param a1 a {@link Supplier} providing the first input; must not be {@code null}
     * @param a2 a {@link Supplier} providing the second input; must not be {@code null}
     * @return a {@link java.util.concurrent.Callable} performing the deferred computation
     *
     * @throws NullPointerException if any supplier is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    Callable<B> defer(final @NonNull Supplier<A1> a1,
                      final @NonNull Supplier<A2> a2);

}
