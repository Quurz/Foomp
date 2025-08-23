    package org.quurz.foomp.base.functions;

    import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A functional interface for memoized functions. Caches results by input to avoid
 *         repeated computation on identical arguments.
 *     </p>
 *     <p>
 *         Contract:
 *     </p>
 *     <ul>
 *         <li>Inputs must not be {@code null} and results must not be {@code null}.</li>
 *         <li>The wrapped computation SHOULD be referentially transparent (pure): for equal inputs it must
 *             always produce equal results and must not rely on or cause observable side effects. Using
 *             non‑pure/side‑effecting functions with memoization can yield stale or misleading cached values
 *             and is discouraged.</li>
 *         <li>The cache is unbounded and keyed by the input's equality semantics ({@link Object#equals(Object)}
 *             and {@link Object#hashCode()}).</li>
 *     </ul>
 *     <p>
 *         Concurrency: the implementation returned by {@link #memoisingFun(Function)} is thread‑safe and
 *         uses a {@link java.util.concurrent.ConcurrentHashMap}. Cache inserts are atomic via
 *         {@link java.util.concurrent.ConcurrentMap#computeIfAbsent(Object, java.util.function.Function)}.
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
    public interface MemoisingFun<X, Y>
            extends Fun<X, Y> {

    /**
     * <div>
     *     <p>
     *         Erzeugt eine neue {@code MemoisingFun}-Instanz aus einer gegebenen {@link Function}, die
     *         Creates a memoizing wrapper over the given {@link Function}. The returned function
     *         caches results for previously seen inputs and returns cached values on subsequent calls
     *         with equal inputs.
     *     </p>
     *     <p>
     *         Contract:
     *     </p>
     *     <ul>
     *         <li>{@code function} must not be {@code null}; inputs must not be {@code null}; the wrapped
     *             function must not return {@code null}.</li>
     *         <li>The wrapped function SHOULD be pure/referentially transparent. Applying memoization to
     *             non‑pure functions can cause surprising behaviour (e.g., missing side effects or outdated
     *             values) and is therefore not recommended.</li>
     *         <li>The cache is unbounded and uses the input's {@code equals}/{@code hashCode} for keying.</li>
     *         <li>Thread‑Safety: the returned implementation is thread‑safe (based on {@link ConcurrentHashMap}).</li>
     *     </ul>
     * </div>
     *
     * @param <X>       the input type
     * @param <Y>       the output type
     * @param function  the function to memoize; must not be {@code null}
         * @return a memoizing function wrapper
         * @throws NullPointerException falls {@code function} oder deren Resultat {@code null} ist
         *
         * @since 1.0.0
         */
        static <X, Y> MemoisingFun<X, Y> memoisingFun(final @NonNull Function<X, Y> function) {
            Objects.requireNonNull(function, nullValue("function"));

            return new MemoisingFun<>() {
                private final Map<X, Y> memo
                    = new ConcurrentHashMap<>();

                @Override
                public @NonNull Y apply(final @NonNull X x) {
                    Objects.requireNonNull(x, nullValue("x"));
                    return this.memo.computeIfAbsent(x, _x -> Objects.requireNonNull(function.apply(_x), nullResult()));
                }

                @Override
                public MemoisingFun<X, Y> clear() {
                    this.memo.clear();
                    return this;
                }
            };
        }

        /**
         * <div>
         *     <p>
         *         Clears all cached results so that subsequent calls recompute values instead of returning
         *         cached ones.
         *     </p>
         * </div>
         *
         * @return this {@code MemoisingFun} instance (for method chaining)
         *
         * @since 1.0.0
         */
        MemoisingFun<X, Y> clear();

    }
