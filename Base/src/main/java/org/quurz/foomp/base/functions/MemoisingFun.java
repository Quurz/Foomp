    package org.quurz.foomp.base.functions;

    import org.checkerframework.checker.nullness.qual.NonNull;

    import java.util.HashMap;
    import java.util.Map;
    import java.util.Objects;
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
     *         Contract: inputs must not be {@code null} and results must not be {@code null}.
     *         The cache is unbounded and keyed by the input's equality semantics ({@link Object#equals(Object)}
     *         and {@link Object#hashCode()}).
     *     </p>
     *     <p>
     *         Concurrency: the default implementation returned by {@link #memoisingFun(Function)} is not
     *         thread-safe. If concurrent access is required, wrap externally or adapt the implementation
     *         to use a concurrent map.
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
         *         Contract: {@code function} must not be {@code null}; inputs must not be {@code null};
         *         the wrapped function must not return {@code null}. The cache is unbounded and uses the
         *         input's {@code equals}/{@code hashCode} for keying. The returned implementation is not
         *         thread-safe.
         *     </p>
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
                    = new HashMap<>();

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
