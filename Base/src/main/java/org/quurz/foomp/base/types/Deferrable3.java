package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * <div>
 *     <p>
 *         Functional interface describing a computation with three inputs that can be deferred.
 *         Inputs are provided as {@link java.util.function.Supplier}s and are obtained on demand.
 *     </p>
 * </div>
 *
 * @param <A1> the type of the first input
 * @param <A2> the type of the second input
 * @param <A3> the type of the third input
 * @param <B>  the result type of the computation
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Deferrable3<A1, A2, A3, B> {

    /**
     * <div>
     *     <p>
     *         Returns an executable handle that, when called, obtains the three inputs from the
     *         given suppliers and performs the computation to produce the result.
     *     </p>
     *     <p>
     *         Contract: all suppliers must not be {@code null} and must not supply {@code null} values.
     *         The returned handle must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param a1 a {@link Supplier} providing the first input; must not be {@code null}
     * @param a2 a {@link Supplier} providing the second input; must not be {@code null}
     * @param a3 a {@link Supplier} providing the third input; must not be {@code null}
     * @return a {@link java.util.concurrent.Callable} performing the deferred computation
     *
     * @throws NullPointerException if any supplier is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    Callable<B> defer(final @NonNull Supplier<A1> a1,
                      final @NonNull Supplier<A2> a2,
                      final @NonNull Supplier<A3> a3);

}
