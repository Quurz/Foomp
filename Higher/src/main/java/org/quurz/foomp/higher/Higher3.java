package org.quurz.foomp.higher;

/**
 * <div>
 *     <p>
 *         Base interface for rank‑3 higher‑kinded types (HKTs).
 *     </p>
 *     <p>
 *         This interface represents higher‑kinded types that have exactly three type parameters.
 *         It is typically used to model abstractions that are parameterized by three types,
 *         such as certain third‑order functors or similar constructs. The {@code WT} parameter
 *         acts as the witness type (see {@link WitnessType}) to encode the HKT at the type level.
 *     </p>
 * </div>
 *
 * @param <WT> The witness type of the HKT (see {@link WitnessType})
 * @param <A>  The first type parameter carried by this HKT
 * @param <B>  The second type parameter carried by this HKT
 * @param <C>  The third type parameter carried by this HKT
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("unused")
public interface Higher3<WT extends WitnessType, A, B, C>
        extends Hkt<WT> {

    /**
     * <div>
     *     <p>
     *         Returns the rank (arity) of the higher‑kinded type.
     *     </p>
     *     <p>
     *         Since this HKT has exactly three type parameters, this method always returns 3.
     *     </p>
     * </div>
     *
     * @return 3, because this HKT has exactly three type parameters
     *
     * @since 1.0.0
     */
    @Override
    default int arity() {
        return 3;
    }

}
