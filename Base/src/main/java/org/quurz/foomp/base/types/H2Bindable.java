package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Rank‑2 monad‑like interface that sequences computations by binding over the first
 *         type parameter while preserving the second parameter {@code R}.
 *     </p>
 *     <p>
 *         The {@code bind} operation applies a transformation that returns a new value of the same
 *         constructor shape {@code Higher2&lt;WT, B, R&gt;} and keeps {@code R} unchanged.
 *     </p>
 *     <p>
 *         Contract: the transformation must not be {@code null} and must not return {@code null}.
 *     </p>
 * </div>
 *
 * @param <WT> the witness type representing the higher‑kinded constructor
 * @param <A>  the current (first) type parameter
 * @param <R>  the preserved (second) type parameter, e.g. a result/context type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface H2Bindable<WT extends WitnessType, A, R> {

    /**
     * <div>
     *     <p>
     *         Monadic bind over the first type parameter: applies the given transformation and
     *         returns a value of the same constructor shape with {@code R} preserved.
     *     </p>
     * </div>
     *
     * @param transformation a function {@code A -> Higher2<WT, B, R>}; must not be {@code null}
     * @param <B>            the new first type parameter
     * @return a {@code Higher2<WT, B, R>} value; never {@code null}
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher2<? extends WT, B, R> bind(final @NonNull Function<A, ? extends Higher2<? extends WT, B, R>> transformation);

}
