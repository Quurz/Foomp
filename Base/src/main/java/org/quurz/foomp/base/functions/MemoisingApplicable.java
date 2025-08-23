package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.XorValue;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *   <p>
 *     Memoizing variant of {@link Applicable}. Caches results by input {@code X} to avoid repeated
 *     computation on identical arguments.
 *   </p>
 *   <p>
 *     Contract:
 *   </p>
 *   <ul>
 *     <li>Inputs must not be {@code null}; results must not be {@code null}.</li>
 *     <li>The wrapped {@link Applicable} SHOULD be referentially transparent (pure): for equal inputs it
 *         must always produce equal results and must not rely on or cause observable side effects.
 *         Using memoization on non‑pure operations can yield stale or misleading cached values and
 *         missing side effects and is therefore discouraged.</li>
 *     <li>Both successful results and exceptions are cached. On repeated calls with the same input,
 *         a cached exception is thrown again (same instance/stacktrace as cached).</li>
 *     <li>Cache keys rely on {@code equals}/{@code hashCode} of {@code X}.</li>
 *     <li>Thread‑Safety: the implementation returned by
 *         {@link #memoisingApplicable(Applicable)} is thread‑safe and uses a
 *         {@link java.util.concurrent.ConcurrentHashMap}. Cache inserts are atomic via
 *         {@link java.util.concurrent.ConcurrentMap#computeIfAbsent(Object, java.util.function.Function)}.</li>
 *   </ul>
 * </div>
 *
 * @param <X> input type
 * @param <Y> result type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface MemoisingApplicable<X, Y>
        extends Applicable<X, Y> {

    static <X, Y> MemoisingApplicable<X, Y> memoisingApplicable(final @NonNull Applicable<X, Y> applicable) {
        Objects.requireNonNull(applicable, nullValue("applicable"));

        return new MemoisingApplicable<X, Y>() {
            private final Map<X, XorValue<Exception, Y>> memo
                = new ConcurrentHashMap<>();

            @Override
            public Y apply(final @NonNull X x)
                    throws Exception {
                Objects.requireNonNull(x, nullValue("x"));
                final var result
                    = this.memo.computeIfAbsent(x, _x -> Objects.requireNonNull(applicable.safe().apply(_x), nullResult()));
                if (result.isRight()) {
                    return result.getRight();
                } else {
                    throw result.getLeft();
                }
            }

            @Override
            public @NonNull MemoisingApplicable<X, Y> clear() {
                this.memo.clear();
                return this;
            }

        };
    }

    /**
     * <div>
     *   <p>
     *     Clears all cached results so that subsequent calls recompute values or rethrow fresh exceptions
     *     instead of returning cached ones.
     *   </p>
     * </div>
     *
     * @return this {@code MemoisingApplicable} (for chaining)
     *
     * @since 1.0.0
     */
    @NonNull MemoisingApplicable<X, Y> clear();

}
