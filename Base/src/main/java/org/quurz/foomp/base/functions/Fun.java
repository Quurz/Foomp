package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.functions.MemoisingFun.memoisingFun;
import static org.quurz.foomp.base.localisation.BaseMessages.*;

/**
 * <div>
 *     <p>
 *         A functional interface that extends {@link Function} with stronger contracts and
 *         convenient combinators such as composition, memoization, and null-safety wrappers.
 *     </p>
 *     <p>
 *         Compared to the JDK's {@code Function}, this interface adopts a stricter non-null contract:
 *         unless explicitly stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *         Helper methods like {@link #fun(Function)} and {@link #nullSafe()} make these guarantees explicit.
 *     </p>
 *     <p>
 *         Side effects: Functions created or used via {@code Fun} are not required to be pure or
 *         referentially transparent. They may have side effects and may produce different results over time.
 *         If you need memoization with stable semantics, use {@link MemoisingFun} only with pure functions.
 *     </p>
 * </div>
 *
 * @param <X> the input type
 * @param <Y> the output type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Fun<X, Y>
        extends Function<X, Y>,
                Applicable<X, Y> {

    /**
     * <div>
     *     <p>
     *         Creates a new {@code Fun} from the given {@link Function}, enforcing non-null input and output.
     *     </p>
     *     <p>
     *         Contract: {@code function} must not be {@code null}; applying the returned {@code Fun} will
     *         throw a {@link NullPointerException} if the input is {@code null} or if the wrapped function returns {@code null}.
     *     </p>
     * </div>
     *
     * @param <X>       the input type
     * @param <Y>       the output type
     * @param function  the function to wrap; must not be {@code null}
     * @return a {@code Fun} that enforces the non-null contract
     *
     * @since 1.0.0
     */
    static <X, Y> Fun<X, Y> fun(@NonNull final Function<X, Y> function) {
        Objects.requireNonNull(function, nullValue("function"));
        return x -> {
            Objects.requireNonNull(x, nullValue("x"));
            return Objects.requireNonNull(function.apply(x), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Applies this function to the provided input.
     *     </p>
     *     <p>
     *         Contract: {@code x} must not be {@code null}; the result must not be {@code null}.
     *         Implementations can rely on wrappers like {@link #fun(Function)} or {@link #nullSafe()}
     *         to enforce this at runtime.
     *     </p>
     * </div>
     *
     * @param x the input; must not be {@code null}
     * @return the result; never {@code null}
     * @throws NullPointerException if {@code x} is {@code null} or the implementation returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @Pure
    @NonNull
    Y apply(@NonNull final X x);

    /**
     * <div>
     *     <p>
     *         Returns a function representing the composition {@code this ∘ first}.
     *         The resulting function first applies {@code first}, then applies {@code this}.
     *     </p>
     *     <p>
     *         Contract: {@code first} must not be {@code null}; both {@code first} and {@code this} must not
     *         return {@code null}.
     *     </p>
     * </div>
     *
     * @param <W>   the input type of {@code first}
     * @param first the function to apply before this one; must not be {@code null}
     * @return the composed function
     * @throws NullPointerException if {@code first} is {@code null} or any intermediate result is {@code null}
     *
     * @since 1.0.0
     */
    default <W> @NonNull Fun<W, Y> compose(@NonNull final Fun<? super W, ? extends X> first) {
        Objects.requireNonNull(first, nullValue("first"));
        return w -> {
            final var x
                = Objects.requireNonNull(first.apply(w), nullResult());
            return Objects.requireNonNull(this.apply(x), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Returns a function representing the composition {@code next ∘ this}.
     *         The resulting function first applies {@code this}, then applies {@code next}.
     *     </p>
     *     <p>
     *         Contract: {@code next} must not be {@code null}; both {@code this} and {@code next} must not
     *         return {@code null}.
     *     </p>
     * </div>
     *
     * @param <Z>  the output type of the resulting function
     * @param next the function to apply after this one; must not be {@code null}
     * @return the composed function
     * @throws NullPointerException if {@code next} is {@code null} or any intermediate result is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("SuspiciousNameCombination")
    default <Z> @NonNull Fun<X, Z> andThen(@NonNull final Fun<? super Y, ? extends Z> next) {
        Objects.requireNonNull(next, nullValue("next"));
        return x -> {
            final var y
                = Objects.requireNonNull(this.apply(x), nullResult());
            return Objects.requireNonNull(next.apply(y), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Returns a memoized view of this function that caches results by input.
     *     </p>
     *     <p>
     *         Contract:
     *     </p>
     *     <ul>
     *         <li>The returned function must never return {@code null}.</li>
     *         <li>Cache semantics assume referential transparency for equal inputs. Using this on
     *             non‑pure/side‑effecting functions can yield surprising results and is not recommended.</li>
     *     </ul>
     * </div>
     *
     * @return a memoizing wrapper of this function
     *
     * @since 1.0.0
     */
    default MemoisingFun<X, Y> memoise() {
        return memoisingFun(this);
    }

    /**
     * <div>
     *     <p>
     *         Returns a null-checking wrapper around this function that rejects {@code null} inputs
     *         and results with {@link NullPointerException}.
     *     </p>
     * </div>
     *
     * @return a null-safe wrapper of this function
     *
     * @since 1.0.0
     */
    default Fun<X, Y> nullSafe() {
        return x -> {
            Objects.requireNonNull(x, nullValue("x"));
            return Objects.requireNonNull(this.apply(x), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Exposes this function as an {@link Applicable}.
     *     </p>
     * </div>
     *
     * @return this function as {@code Applicable}
     *
     * @since 1.0.0
     */
    default Applicable<X, Y> applicable() {
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Returns a strict identity function, rejecting {@code null} inputs.
     *     </p>
     *     <p>
     *         This is stricter than {@link Function#identity()} which allows {@code null}. Here, a
     *         {@link NullPointerException} is thrown if the input is {@code null}.
     *     </p>
     * </div>
     *
     * @param <X> the input/output type
     * @return an identity function for type {@code X}
     *
     * @since 1.0.0
     */
    static <X> Fun<X, X> identity() {
        return x -> {
            Objects.requireNonNull(x, nullValue("x"));
            return x;
        };
    }

        /**
         * <div>
         *   <p>
         *     Applies this function asynchronously to a value supplied lazily.
         *   </p>
         *   <p>
         *     Execution is delegated to the current {@link ForkJoinPool} if the calling thread
         *     is a {@link ForkJoinWorkerThread}; otherwise, {@link ForkJoinPool#commonPool()} is used.
         *   </p>
         *   <p>
         *     This is a <strong>convenience method</strong> for ad-hoc asynchronous application
         *     of functions. For more control over execution (e.g., custom thread pools, testing with
         *     synchronous executors), use {@link #async(Supplier, ExecutorService)}.
         *   </p>
         *   <p>
         *     <strong>Use cases:</strong>
         *     <ul>
         *       <li>Parallelizing expensive function applications in pipelines.</li>
         *       <li>Quick async wrapping without explicit executor management.</li>
         *       <li>Integration with lazy evaluation frameworks (e.g., {@link Eval}).</li>
         *     </ul>
         *   </p>
         * </div>
         *
         * @param supplier supplies the input value; must not be {@code null} and must not return {@code null}
         * @return a {@code Future} representing the asynchronous computation
         * @throws NullPointerException if {@code supplier} is {@code null} or returns {@code null}
         *
         * @see #async(Supplier, ExecutorService)
         *
         * @since 1.0.0
         */
        @SuppressWarnings("resource")
        default Future<Y> async(final @NonNull Supplier<X> supplier) {
            Objects.requireNonNull(supplier, nullValue("supplier"));

            final ForkJoinPool pool;
            if (Thread.currentThread() instanceof ForkJoinWorkerThread forkJoinWorkerThread) {
                pool = forkJoinWorkerThread.getPool();
            } else {
                pool = ForkJoinPool.commonPool();
            }

            return pool.submit(() -> {
                final var input = Objects.requireNonNull(supplier.get(), nullSupplied());
                return this.apply(input);
            });
        }

        /**
         * <div>
         *   <p>
         *     Applies this function asynchronously to a value supplied lazily, using the given
         *     {@link ExecutorService} for execution.
         *   </p>
         *   <p>
         *     This overload provides <strong>explicit control</strong> over the execution context,
         *     making it suitable for:
         *     <ul>
         *       <li>Custom thread pools with specific sizing or behavior.</li>
         *       <li>Testing with synchronous executors (e.g., {@code MoreExecutors.newDirectExecutorService()})
         *           to ensure deterministic, non‑concurrent execution.</li>
         *       <li>Scenarios where {@link ForkJoinPool} semantics are undesirable.</li>
         *     </ul>
         *   </p>
         *   <p>
         *     Contract: The caller is responsible for managing the lifecycle of the provided
         *     {@code ExecutorService} (shutdown, error handling, etc.).
         *   </p>
         * </div>
         *
         * @param supplier        supplies the input value; must not be {@code null} and must not return {@code null}
         * @param executorService the executor to run the computation; must not be {@code null}
         * @return a {@code Future} representing the asynchronous computation
         * @throws NullPointerException if {@code supplier} or {@code executorService} is {@code null},
         *                              or if {@code supplier} returns {@code null}
         *
         * @see #async(Supplier)
         *
         * @since 1.0.0
         */
        default Future<Y> async(final @NonNull Supplier<X> supplier,
                                final @NonNull ExecutorService executorService) {
            Objects.requireNonNull(supplier, nullValue("supplier"));
            Objects.requireNonNull(executorService, nullValue("executorService"));

            return executorService.submit(() -> {
                final var input = Objects.requireNonNull(supplier.get(), nullSupplied());
                return this.apply(input);
            });
        }

}
