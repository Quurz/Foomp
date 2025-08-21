package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Monad‑like binding contract for rank‑1 higher‑kinded types (HKTs). It enables sequencing
 *         of operations that produce values of the same HKT shape by applying a transformation to
 *         the carried value and returning a new HKT instance.
 *     </p>
 *     <p>
 *         The {@code WT} witness encodes the type constructor; {@code A} is the carried value type.
 *         Implementations are expected to be null‑safe: the transformation function must not be
 *         {@code null} and must not return {@code null}.
 *     </p>
 * </div>
 *
 * @param <WT> the witness type of the implementing HKT (see {@link org.quurz.foomp.higher.WitnessType})
 * @param <A>  the carried value type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Bindable<WT extends WitnessType, A> {

    /**
     * <div>
     *     <p>
     *         Applies the given transformation to the carried value and returns a new HKT instance
     *         of the same constructor shape. This operation supports fluent chaining of computations
     *         that depend on the previous result (monadic bind).
     *     </p>
     *     <p>
     *         Contract: the transformation must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation the function to transform the carried value into a new {@code Higher1} of the same witness
     * @param <B>            the new carried value type
     * @return a {@code Higher1} with the transformed value; never {@code null}
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher1<? extends WT, B> bind(final @NonNull Function<? super A, ? extends Higher1<? extends WT, B>> transformation);

}
