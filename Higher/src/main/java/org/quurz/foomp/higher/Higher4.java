package org.quurz.foomp.higher;

/**
 * <div>
 *     <p>
 *         Base interface for rank‑4 higher‑kinded types (HKTs).
 *     </p>
 *     <p>
 *         This interface represents higher‑kinded types that have exactly four type parameters.
 *         It is typically used to model abstractions that are parameterized by four types,
 *         such as certain fourth‑order functors or similar constructs. The {@code WT} parameter
 *         acts as the witness type (see {@link WitnessType}) to encode the HKT at the type level.
 *     </p>
 * </div>
 *
 * @param <WT> The witness type of the HKT (see {@link WitnessType})
 * @param <A>  The first type parameter carried by this HKT
 * @param <B>  The second type parameter carried by this HKT
 * @param <C>  The third type parameter carried by this HKT
 * @param <D>  The fourth type parameter carried by this HKT
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("unused")
public interface Higher4<WT extends WitnessType, A, B, C, D>
        extends Hkt<WT> {

    /**
     * <div>
     *     <p>
     *         Returns the rank (arity) of the higher‑kinded type.
     *     </p>
     *     <p>
     *         Since this HKT has exactly four type parameters, this method always returns 4.
     *     </p>
     * </div>
     *
     * @return 4, because this HKT has exactly four type parameters
     *
     * @since 1.0.0
     */
    @Override
    default int arity() {
        return 4;
    }

}
