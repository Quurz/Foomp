package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Applicable;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

/**
 * <div>
 *     <p>
 *         Monad‑like interface whose transformations are allowed to throw checked exceptions.
 *         This “unsafe” variant is useful when computations inside the monad may fail and you
 *         want to express those failures at the type level via checked exceptions rather than
 *         by encoding them as values.
 *     </p>
 *     <p>
 *         It complements the typical functor/applicative/monad contracts by providing
 *         exception‑throwing counterparts. Implementations should document their evaluation
 *         model (lazy/eager) and whether repeated invocations are idempotent.
 *     </p>
 * </div>
 *
 * @param <WT> the witness type of the monad (e.g., {@code Attempt}, {@code Option}, {@code Either}, …)
 * @param <A>  the contained value type
 *
 * @see Monadic
 * @see org.quurz.foomp.base.util.Attempt
 *
 * @since 1.0.0
 */
public interface UnsafeMonadic<WT extends WitnessType, A> {

    /**
     * <div>
     *     <p>
     *         Maps the contained value using a transformation that may throw a checked exception.
     *         This is the exception‑throwing analogue of the usual {@code map} operation.
     *     </p>
     * </div>
     *
     * @param transformation the transformation; must not be {@code null}
     * @param <B>            the resulting value type
     * @return a new monadic value with the transformed result (never {@code null})
     * @throws Exception if the transformation fails
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher1<? extends WT, B> mapUnsafe(final @NonNull Applicable<? super A, ? extends B> transformation)
        throws Exception;

    /**
     * <div>
     *     <p>
     *         Applies a function contained in the monad to the current value; the function itself
     *         may throw a checked exception. This is the exception‑throwing analogue of the
     *         applicative {@code ap}/applyTo operation.
     *     </p>
     * </div>
     *
     * @param transformation a monadic value carrying a function to apply; must not be {@code null}
     * @param <B>            the resulting value type after applying the function
     * @return a new monadic value with the application result (never {@code null})
     * @throws Exception if applying the function fails
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher1<? extends WT, B> applyToUnsafe(final @NonNull Higher1<? extends WT, ? extends Applicable<? super A, ? extends B>> transformation)
        throws Exception;

    /**
     * <div>
     *     <p>
     *         Sequences computations by flatMap (monadic bind) with a transformation that returns another monadic value
     *         and may throw a checked exception. This is the exception‑throwing analogue of {@code flatMap}/{@code flatMap}.
     *     </p>
     * </div>
     *
     * @param transformation a function producing a new monadic value; must not be {@code null}
     * @param <B>            the resulting value type after flatMap
     * @return the resulting monadic value (never {@code null})
     * @throws Exception if the transformation fails
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher1<? extends WT, B> flatMapUnsafe(final @NonNull Applicable<? super A, ? extends Higher1<? extends WT, B>> transformation)
        throws Exception;

}
