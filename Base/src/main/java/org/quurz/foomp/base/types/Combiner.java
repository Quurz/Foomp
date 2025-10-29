package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
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
     *         Materializes the combined result with asynchronous/parallel execution support.
     *     </p>
     *     <p>
     *         This variant allows implementations to execute combinations concurrently using
     *         the provided {@link ExecutorService}. The {@code timeout} parameter defines
     *         the maximum duration to wait for completion.
     *     </p>
     *     <p>
     *         Implementations should document their specific timeout behavior (e.g., whether
     *         they throw an exception or return partial results on timeout).
     *     </p>
     * </div>
     *
     * @param executorService the executor service to use for concurrent execution; must not be {@code null}
     * @param timeout         the maximum time to wait for completion; must not be {@code null}
     * @return the final combined result as {@code Higher1<WT, A>}
     * @throws NullPointerException if {@code executorService} or {@code timeout} is {@code null}
     *
     * @since 1.0.0
     */
    Higher1<WT, A> finish(final @NonNull ExecutorService executorService,
                          final @NonNull Duration timeout);

}
