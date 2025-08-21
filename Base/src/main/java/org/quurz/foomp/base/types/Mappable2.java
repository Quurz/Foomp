package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Functor‑like interface for independently or jointly mapping over two type parameters.
 *     </p>
 *     <p>
 *         Contract: mapping functions must not be {@code null} and should not return {@code null};
 *         implementations should return non‑null results.
 *     </p>
 * </div>
 *
 * @param <WT> the witness type representing the mapping context
 * @param <A1> the first mappable type parameter
 * @param <A2> the second mappable type parameter
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Mappable2<WT extends WitnessType, A1, A2> {

    /**
     * <div>
     *     <p>
     *         Maps the first type parameter using the given function.
     *     </p>
     * </div>
     *
     * @param transformation the function applied to {@code A1}; must not be {@code null}
     * @param <B1>           the resulting first type
     * @return a new {@code Mappable2} instance with the transformed first type; never {@code null}
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    default <B1> @NonNull Mappable2<WT, B1, A2> map(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(transformation, Fun.identity());
    }

    /**
     * <div>
     *     <p>
     *         Alias for {@link #map(Function)}.
     *     </p>
     * </div>
     *
     * @param transformation the function applied to {@code A1}; must not be {@code null}
     * @param <B1>           the resulting first type
     * @return a new {@code Mappable2} instance with the transformed first type; never {@code null}
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    default <B1> @NonNull Mappable2<WT, B1, A2> map1(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.map(transformation);
    }

    /**
     * <div>
     *     <p>
     *         Maps the second type parameter using the given function.
     *     </p>
     * </div>
     *
     * @param transformation the function applied to {@code A2}; must not be {@code null}
     * @param <B2>           the resulting second type
     * @return a new {@code Mappable2} instance with the transformed second type; never {@code null}
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    default <B2> @NonNull Mappable2<WT, A1, B2> map2(final @NonNull Function<? super A2, ? extends B2> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(Fun.identity(), transformation);
    }

    /**
     * <div>
     *     <p>
     *         Maps both type parameters simultaneously using the provided functions.
     *     </p>
     * </div>
     *
     * @param transformation1 the function applied to {@code A1}; must not be {@code null}
     * @param transformation2 the function applied to {@code A2}; must not be {@code null}
     * @param <B1>            the resulting first type
     * @param <B2>            the resulting second type
     * @return a new {@code Mappable2} instance with both transformed types; never {@code null}
     * @throws NullPointerException if {@code transformation1} or {@code transformation2} is {@code null}
     */
    <B1, B2> @NonNull Mappable2<WT, B1, B2> mapAll(final @NonNull Function<? super A1, ? extends B1> transformation1,
                                                   final @NonNull Function<? super A2, ? extends B2> transformation2);

}
