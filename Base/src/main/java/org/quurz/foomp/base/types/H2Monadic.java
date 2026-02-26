package org.quurz.foomp.base.types;

import org.quurz.foomp.higher.WitnessType;

/**
 * <div>
 *     <p>
 *         Rank‑2 monad‑like interface: combines the contracts of {@link H2Mappable},
 *         {@link H2Appliable} and {@link H2Bindable}. Operations map/flatMap over the first
 *         type parameter while preserving the second parameter {@code R} (e.g. result/context).
 *     </p>
 *     <p>
 *         Typical use cases are structures like {@code State<S, A>} or {@code Cont<R, A>},
 *         which behave monadically in {@code A} with a fixed {@code S}/{@code R}.
 *     </p>
 *     <p>
 *         Contract: transformations must not be {@code null} and must not return {@code null};
 *         implementations should return non‑null results and keep {@code R} unchanged.
 *     </p>
 * </div>
 *
 * @param <WT> the witness type representing the higher‑kinded constructor
 * @param <A>  the mapped/bound (first) type parameter
 * @param <R>  the preserved (second) type parameter, e.g. a result/context type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */

public interface H2Monadic<WT extends WitnessType, A, R>
        extends H2Mappable<WT, A, R>,
        H2Appliable<WT, A, R>,
        H2Bindable<WT, A, R> {}
