package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A functional interface for memoized predicates. Caches results by input to avoid
 *         repeated evaluation on identical arguments.
 *     </p>
 *     <p>
 *         Contract:
 *     </p>
 *     <ul>
 *         <li>Inputs must not be {@code null}.</li>
 *         <li>The wrapped predicate SHOULD be referentially transparent (pure): for equal inputs it must
 *             always produce equal results and must not rely on or cause observable side effects.</li>
 *         <li>The cache is unbounded and keyed by the input's equality semantics ({@link Object#equals(Object)}
 *             and {@link Object#hashCode()}).</li>
 *     </ul>
 * </div>
 *
 * @param <A> the input type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface MemoisingPred<A>
        extends Pred<A> {

    /**
     * <div>
     *     <p>
     *         Creates a memoizing wrapper over the given {@link Pred}. The returned predicate
     *         caches results for previously seen inputs and returns cached values on subsequent calls
     *         with equal inputs.
     *     </p>
     *     <p>
     *         Contract:
     *     </p>
     *     <ul>
     *         <li>{@code pred} must not be {@code null}; inputs must not be {@code null}.</li>
     *         <li>The wrapped predicate SHOULD be pure/referentially transparent.</li>
     *         <li>The cache is unbounded and uses the input's {@code equals}/{@code hashCode} for keying.</li>
     *     </ul>
     * </div>
     *
     * @param <A>  the input type
     * @param pred the predicate to memoize; must not be {@code null}
     * @return a memoizing predicate wrapper
     * @throws NullPointerException if {@code pred} is {@code null}
     *
     * @since 1.0.0
     */
    static <A> MemoisingPred<A> memoisingPred(final @NonNull Pred<A> pred) {
        Objects.requireNonNull(pred, nullValue("pred"));

        return new MemoisingPred<A>() {
            private final Map<A, Boolean> memo
                = new ConcurrentHashMap<>();

            @Override
            public boolean test(@NonNull A a) {
                Objects.requireNonNull(a, nullValue("a"));
                return this.memo.computeIfAbsent(a, pred::test);
            }

            @Override
            public MemoisingPred<A> clear() {
                this.memo.clear();
                return this;
            }

        };
    }

    /**
     * <div>
     *     <p>
     *         Clears all cached results so that subsequent calls re-evaluate the predicate instead of returning
     *         cached ones.
     *     </p>
     * </div>
     *
     * @return this {@code MemoisingPred} instance (for method chaining)
     *
     * @since 1.0.0
     */
    MemoisingPred<A> clear();

}
