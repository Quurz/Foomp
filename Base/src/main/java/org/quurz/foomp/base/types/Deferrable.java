package org.quurz.foomp.base.types;

import java.util.concurrent.Callable;

/**
 * <div>
 *   <p>
 *     Contract for deferring a computation by supplying its input later.
 *     A deferrable maps an input supplier to an executable handle (e.g. {@link java.util.concurrent.Callable}).
 *   </p>
 *   <p>
 *     Implementations must be null‑safe: the supplier must not be {@code null} and
 *     must not supply {@code null} inputs. The returned handle must execute the same
 *     computation as the immediate variant and must never return {@code null}.
 *   </p>
 * </div>
 *
 * @param <X> the input type
 * @param <Y> the result type
 *
 * @since 1.0.0
 */
public interface Deferrable<X, Y> {

    /**
     * <div>
     *   <p>
     *     Returns an executable that, when called, obtains the input from the given supplier
     *     and performs the computation, returning the result.
     *   </p>
     *   <p>
     *     Contract: {@code supplier} must not be {@code null}, and must not supply {@code null} inputs.
     *     The returned handle must not return {@code null}.
     *   </p>
     * </div>
     *
     * @param supplier the input supplier; must not be {@code null}
     * @return an executable handle that computes the result
     *
     * @since 1.0.0
     */
    Callable<Y> defer(java.util.function.Supplier<X> supplier);

}
