package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.WitnessType;

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

}
