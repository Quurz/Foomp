package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.BiFunction;

/**
 * <div>
 *     <p>
 *         Fluent builder interface for incrementally combining multiple higher-kinded type instances.
 *     </p>
 * </div>
 *
 * @param <WT> the witness type of the HKT
 * @param <A>  the current accumulated value type
 *
 * @see Combinable
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Combiner<WT extends WitnessType, A> {

    /**
     * <div>
     *     <p>
     *         Combines the current accumulated value with the value from {@code other} using the given {@code combiner} function.
     *     </p>
     * </div>
     *
     * @param other    the other value container to combine; must not be {@code null}
     * @param combiner the combining function; must not be {@code null} and must not return {@code null}
     * @param <B>      the value type of the other container
     * @param <R>      the result type of the combination
     * @return a new {@code Combiner} holding the combined result
     * @throws NullPointerException if {@code other} or {@code combiner} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    <B, R> Combiner<WT, R> with(final @NonNull Higher1<? extends WT, B> other,
                                final @NonNull BiFunction<? super A, ? super B, ? extends R> combiner);

    /**
     * <div>
     *     <p>
     *         Finishes the combination pipeline and returns the final combined result as a {@link Higher1}.
     *     </p>
     * </div>
     *
     * @return the combined result as a {@link Higher1}
     *
     * @since 1.0.0
     */
    @NonNull
    Higher1<WT, A> finish();

}
