package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Rank‑1 functor‑like interface: maps a transformation over the carried value and
 *         returns a value of the same constructor shape.
 *     </p>
 *     <p>
 *         Contract: the transformation must not be {@code null} and should not return {@code null};
 *         implementations should return a non‑null result.
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
@FunctionalInterface
public interface Mappable<WT extends WitnessType, A> {

    /**
     * <div>
     *     <p>
     *         Applies the given transformation to the carried value and returns a value of the same
     *         constructor shape with the transformed type.
     *     </p>
     * </div>
     *
     * @param transformation the function to apply to the carried value; must not be {@code null}
     * @param <B>            the new carried type after applying the transformation
     * @return a {@code Higher1} of the same constructor shape carrying the transformed value; never {@code null}
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher1<? extends WT, B> map(final @NonNull Function<? super A, ? extends B> transformation);

}
