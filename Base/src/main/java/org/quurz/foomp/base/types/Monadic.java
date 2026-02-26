package org.quurz.foomp.base.types;

import org.quurz.foomp.higher.WitnessType;

/**
 * <div>
 *     <p>
 *         Rank‑1 monad‑like interface that combines the contracts of {@link Mappable},
 *         {@link Appliable} and {@link Bindable}. Implementations support mapping, applicative
 *         application (applying a function inside the context), and monadic flatMap (monadic bind) over the
 *         carried value while preserving the constructor shape.
 *     </p>
 *     <p>
 *         Contract: transformation functions must not be {@code null} and should not return
 *         {@code null}; implementations should return non‑null results and keep the higher‑kinded
 *         constructor (witness) consistent.
 *     </p>
 * </div>
 *
 * @param <WT> the witness type of the implementing higher‑kinded type
 * @param <A>  the carried value type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Monadic<WT extends WitnessType, A>
        extends Mappable<WT, A>,
        Appliable<WT, A>,
        Bindable<WT, A> {}
