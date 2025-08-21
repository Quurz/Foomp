package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.util.Tuple2;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.BiFunction;

/**
 * <div>
 *     <p>
 *         Entry point for building a combination pipeline (builder style).
 *         Implementations create a {@link Combiner} to incrementally add sources and
 *         define how results are combined in insertion order.
 *     </p>
 *     <p>
 *         Implementations should document error/exception handling, null‑safety and
 *         whether the resulting combiner is mutable or persistent.
 *     </p>
 * </div>
 *
 * @param <WT> the witness type of the HKT
 * @param <A>  the value type carried by this combinable
 *
 * @see Combiner
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Combinable<WT extends WitnessType, A> {

    /**
     * <div>
     *     <p>
     *         Starts a new combination and returns a {@link Combiner} to add further values step by step.
     *     </p>
     * </div>
     *
     * @return a new {@code Combiner} that can be used to combine additional values
     *
     * @since 1.0.0
     */
    @NonNull Combiner<WT, A> combine();

    /**
     * <div>
     *     <p>
     *         Convenience: zips this value with another combinable into a {@code Tuple2}.
     *     </p>
     *     <p>
     *         Default implementation is a placeholder and throws {@link UnsupportedOperationException}.
     *         Implementations should provide an efficient zip if applicable.
     *     </p>
     * </div>
     *
     * @param other the other combinable value
     * @param <B>   the other value type
     * @return a new {@code Combinable} carrying {@code Tuple2<A,B>}
     *
     * @since 1.0.0
     */
    default <B> @NonNull Combinable<WT, Tuple2<A, B>> zip(final @NonNull Combinable<WT, B> other) {
        throw new UnsupportedOperationException("zip not implemented");
    }

    /**
     * <div>
     *     <p>
     *         Convenience: zips this value with another combinable using a zipper function.
     *     </p>
     *     <p>
     *         Default implementation is a placeholder and throws {@link UnsupportedOperationException}.
     *         Implementations should provide an efficient zipWith if applicable.
     *     </p>
     * </div>
     *
     * @param other    the other combinable value
     * @param zipper   the function combining both values; must not return {@code null}
     * @param <B>      the other value type
     * @param <R>      the resulting combined type
     * @return a new {@code Combinable} carrying the combined value
     *
     * @since 1.0.0
     */
    default <B, R> @NonNull Combinable<WT, R> zipWith(final @NonNull Combinable<WT, B> other,
                                                      final @NonNull BiFunction<? super A, ? super B, ? extends R> zipper) {
        throw new UnsupportedOperationException("zipWith not implemented");
    }

}
