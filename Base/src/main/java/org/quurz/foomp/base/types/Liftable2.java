package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Rank‑2 applicative‑like interface: lifts functions inside the context and applies them
 *         to the two carried type parameters, producing a value of the same constructor shape.
 *     </p>
 *     <p>
 *         Conceptually, this “applies” {@code (A1 -> B1)} to {@code A1} and {@code (A2 -> B2)} to {@code A2}
 *         within the same {@code Higher2&lt;WT, *, *&gt;} context.
 *     </p>
 *     <p>
 *         Contract: the provided higher‑kinded value must not be {@code null}, must not contain {@code null}
 *         functions, and the result must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <WT> the witness type of the implementing higher‑kinded type
 * @param <A1> the first carried type
 * @param <A2> the second carried type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Liftable2<WT extends WitnessType, A1, A2> {

    /**
     * <div>
     *     <p>
     *         Lifts and applies the given functions to the two carried values in this context.
     *     </p>
     * </div>
     *
     * @param transformation a {@code Higher2} carrying the functions {@code A1 -> B1} and {@code A2 -> B2}; must not be {@code null}
     * @param <B1>           the new first type after applying the function to {@code A1}
     * @param <B2>           the new second type after applying the function to {@code A2}
     * @return a {@code Higher2<WT, B1, B2>} value; never {@code null}
     *
     * @since 1.0.0
     */
    <B1, B2> @NonNull Higher2<WT, B1, B2> lift(
            @NonNull final Higher2<WT,
                    ? extends Function<? super A1, ? extends B1>,
                    ? extends Function<? super A2, ? extends B2>> transformation
    );

}
