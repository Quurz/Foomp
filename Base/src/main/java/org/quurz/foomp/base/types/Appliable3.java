package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher3;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Rank-3 applicative functor interface: applies functions inside the context
 *         to three carried type parameters, producing a value of the same constructor shape.
 *     </p>
 *     <p>
 *         This interface is the rank-3 analog to Haskell's {@code Applicative} functor class.
 *     </p>
 *     <p>
 *         Contract: the provided higher‑kinded value must not be {@code null}, must not contain {@code null}
 *         functions, and the result must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <WT>  the witness type representing the constructor
 * @param <A1>  the first carried type
 * @param <A2>  the second carried type
 * @param <A3>  the third carried type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Appliable3<WT extends WitnessType, A1, A2, A3> {

    /**
     * <div>
     *     <p>
     *         Applies the given functions to the three carried values in this context.
     *     </p>
     * </div>
     *
     * @param transformation a {@code Higher3} carrying the functions {@code A1 -> B1}, {@code A2 -> B2}, {@code A3 -> B3}; must not be {@code null}
     * @param <B1>           the new first type after applying the function to {@code A1}
     * @param <B2>           the new second type after applying the function to {@code A2}
     * @param <B3>           the new third type after applying the function to {@code A3}
     * @return a {@code Higher3<WT, B1, B2, B3>} value; never {@code null}
     * @throws NullPointerException if {@code transformation} is {@code null} or contains {@code null} functions
     *
     * @since 1.0.0
     */
    <B1, B2, B3> @NonNull Higher3<WT, B1, B2, B3> applyTo(
            @NonNull final Higher3<
                    WT,
                    ? extends Function<? super A1, ? extends B1>,
                    ? extends Function<? super A2, ? extends B2>,
                    ? extends Function<? super A3, ? extends B3>
            > transformation
    );

}
