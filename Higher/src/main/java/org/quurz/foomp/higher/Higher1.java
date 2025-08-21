package org.quurz.foomp.higher;

/**
 * <div>
 *     <p>
 *         Base interface for rank‑1 higher‑kinded types (HKTs).
 *     </p>
 *     <p>
 *         This interface represents higher‑kinded types that have exactly one type parameter.
 *         It is typically used to model abstractions that are parameterized by a single type,
 *         such as first‑order functors or monads. The {@code WT} parameter acts as the
 *         witness type (see {@link WitnessType}) to encode the HKT at the type level.
 *     </p>
 * </div>
 *
 * @param <WT> The witness type of the HKT (see {@link WitnessType})
 * @param <A>  The type parameter carried by this HKT
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("unused")
public interface Higher1<WT extends WitnessType, A>
        extends Hkt<WT> {

    /**
     * <div>
     *     <p>
     *         Returns the rank of the higher‑kinded type.
     *     </p>
     *     <p>
     *         Since this HKT has exactly one type parameter, this method always returns 1.
     *     </p>
     * </div>
     *
     * @return 1, because this HKT has exactly one type parameter
     *
     * @since 1.0.0
     */
    @Override
    default int arity() {
        return 1;
    }

}
