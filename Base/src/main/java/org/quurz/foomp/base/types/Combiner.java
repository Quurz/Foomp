package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

/**
 * <div>
 *     <p>
 *         Builder interface that enables stepwise combination of values.
 *         Implementations define how to append additional sources and how to
 *         materialize the combined structure into a final result.
 *     </p>
 *     <ul>
 *         <li>Ordering: combinations follow the insertion order of {@code with(...)} steps.</li>
 *         <li>Null-safety: sources and zipper functions must not be {@code null}; the zipper must not return {@code null}.</li>
 *         <li>Error handling: implementations should document whether they are fail-fast or aggregate errors.</li>
 *     </ul>
 * </div>
 *
 * @param <WT> the witness type of the higher-kinded type (HKT)
 * @param <A>  the current accumulated/combined value type stored in this builder
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
     *         Appends another source and combines it with the current value using the given zipper.
     *     </p>
     * </div>
     *
     * @param other    another value of type {@code Higher1<WT, B>} to combine
     * @param combiner a function combining the current and the other value into a new result value
     * @param <B>      the other value type
     * @param <R>      the resulting combined type
     * @return a new {@code Combiner} carrying the combined shape
     *
     * @since 1.0.0
     */
    <B, R> Combiner<WT, R> with(final @NonNull Higher1<WT, B> other,
                                final @NonNull BiFunction<? extends A, ? extends B, ? super R> combiner);

    /**
     * <div>
     *     <p>
     *         Materializes the combined result (sequential semantics).
     *     </p>
     * </div>
     *
     * @return the final combined result as {@code Higher1<WT, A>}
     *
     * @since 1.0.0
     */
    @NonNull
    Higher1<WT, A> finish();

    /**
     * <div>
     *     <p>
     *         Materializes the combined result with sequential semantics.
     *         Default implementation delegates to {@link #finish()}.
     *     </p>
     * </div>
     *
     * @return the final combined result as {@code Higher1<WT, A>}
     *
     * @since 1.0.0
     */
    default @NonNull Higher1<WT, A> finishSequential() {
        return finish();
    }

    /**
     * <div>
     *     <p>
     *         Materializes the combined result using parallel execution (common pool).
     *         Default implementation throws {@link UnsupportedOperationException} as a placeholder.
     *     </p>
     * </div>
     *
     * @return the final combined result as {@code Higher1<WT, A>}
     *
     * @since 1.0.0
     */
    default @NonNull Higher1<WT, A> finishParallel() {
        throw new UnsupportedOperationException("finishParallel() not implemented");
    }

    /**
     * <div>
     *     <p>
     *         Materializes the combined result using the given {@link Executor} and a time budget.
     *         Default implementation throws {@link UnsupportedOperationException} as a placeholder.
     *     </p>
     *     <p>
     *         Implementations should document timeout and cancellation policy (fail‑fast, soft‑cancel, etc.).
     *     </p>
     * </div>
     *
     * @param executor the executor used to schedule/execute the sources
     * @param timeout  the maximum duration for materialization
     * @return the final combined result as {@code Higher1<WT, A>}
     *
     * @since 1.0.0
     */
    default @NonNull Higher1<WT, A> finishParallel(final @NonNull Executor executor,
                                                   final @NonNull Duration timeout) {
        throw new UnsupportedOperationException("finishParallel(executor, timeout) not implemented");
    }

}
