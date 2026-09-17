package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;

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
 *     <li>Only successful results are cached. If an invocation throws an exception, the exception is propagated
 *         immediately and is NOT cached, allowing subsequent invocations with the same argument to retry.</li>
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
            private final Map<X, Y> memo
                = new ConcurrentHashMap<>();

            @Override
            public Y apply(final @NonNull X x)
                    throws Exception {
                Objects.requireNonNull(x, nullValue("x"));
                try {
                    return this.memo.computeIfAbsent(x, _x -> {
                        try {
                            return Objects.requireNonNull(applicable.apply(_x), nullResult());
                        } catch (final RuntimeException e) {
                            throw e;
                        } catch (final Exception e) {
                            throw new WrappedException(e);
                        }
                    });
                } catch (final WrappedException e) {
                    throw e.getCause();
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
     *     Clears all cached results so that subsequent calls recompute values instead of returning cached ones.
     *   </p>
     * </div>
     *
     * @return this {@code MemoisingApplicable} (for chaining)
     *
     * @since 1.0.0
     */
    @NonNull MemoisingApplicable<X, Y> clear();

    final class WrappedException extends RuntimeException {
        private final Exception cause;

        WrappedException(final Exception cause) {
            super(cause);
            this.cause = cause;
        }

        @Override
        public Exception getCause() {
            return this.cause;
        }
    }

}
