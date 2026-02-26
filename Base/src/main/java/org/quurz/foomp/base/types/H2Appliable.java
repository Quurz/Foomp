package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Rank-2 applicative functor interface that applies a function inside the context
 *         to the first type parameter while preserving the second parameter {@code R}.
 *     </p>
 *     <p>
 *         This interface is an analog to Haskell's {@code Applicative} functor class for types
 *         of shape {@code Higher2<WT, A, R>} where {@code R} (e.g., a result/context type)
 *         remains fixed during the operation.
 *     </p>
 *     <p>
 *         Contract: the provided higher‑kinded function must not be {@code null} and must not contain
 *         a {@code null} function; the result must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <WT> the witness type representing the higher‑kinded constructor
 * @param <A>  the input type consumed by the function
 * @param <R>  the preserved (second) type parameter, e.g. a result/context type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface H2Appliable<WT extends WitnessType, A, R> {

    /**
     * <div>
     *     <p>
     *         Applies a function {@code A -> B} inside the context to the first type parameter,
     *         preserving {@code R}.
     *     </p>
     * </div>
     *
     * @param transformation a higher‑kinded value carrying the function {@code A -> B}; must not be {@code null}
     * @param <B>            the new first type parameter after applying the function
     * @return a value of shape {@code Higher2&lt;WT, B, R&gt;}
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher2<? extends WT, B, R> applyTo(final @NonNull Higher2<? extends WT, Function<A, B>, R> transformation);

}
