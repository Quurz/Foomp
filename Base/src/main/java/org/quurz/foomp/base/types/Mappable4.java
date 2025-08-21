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
 *         Functor‑like interface enabling independent mapping over up to four values of a structure.
 *         Each {@code mapN} method applies a function to the respective value and returns a new instance
 *         with that value transformed.
 *     </p>
 *     <p>
 *         Contract: mapping functions must not be {@code null} and should not return {@code null};
 *         implementations should return non‑null results.
 *     </p>
 * </div>
 *
 * @param <WT> the witness type (context) of the structure
 * @param <A1> type of the first value
 * @param <A2> type of the second value
 * @param <A3> type of the third value
 * @param <A4> type of the fourth value
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Mappable4<WT extends WitnessType, A1, A2, A3, A4> {

    /**
     * Applies a function to the first value and returns a new instance with the transformed first value.
     *
     * @param transformation the function for the first value; must not be {@code null}
     * @param <B1>           the new first value type
     * @return a new {@code Mappable4} with the transformed first value; never {@code null}
     *
     * @since 1.0.0
     */
    default <B1> @NonNull Mappable4<WT, B1, A2, A3, A4> map1(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(transformation, Fun.identity(), Fun.identity(), Fun.identity());
    }

    /**
     * Applies a function to the second value and returns a new instance with the transformed second value.
     *
     * @param transformation the function for the second value; must not be {@code null}
     * @param <B2>           the new second value type
     * @return a new {@code Mappable4} with the transformed second value; never {@code null}
     *
     * @since 1.0.0
     */
    default <B2> @NonNull Mappable4<WT, A1, B2, A3, A4> map2(final @NonNull Function<? super A2, ? extends B2> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(Fun.identity(), transformation, Fun.identity(), Fun.identity());
    }

    /**
     * Applies a function to the third value and returns a new instance with the transformed third value.
     *
     * @param transformation the function for the third value; must not be {@code null}
     * @param <B3>           the new third value type
     * @return a new {@code Mappable4} with the transformed third value; never {@code null}
     *
     * @since 1.0.0
     */
    default <B3> @NonNull Mappable4<WT, A1, A2, B3, A4> map3(final @NonNull Function<? super A3, ? extends B3> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(Fun.identity(), Fun.identity(), transformation, Fun.identity());
    }

    /**
     * Applies a function to the fourth value and returns a new instance with the transformed fourth value.
     *
     * @param transformation the function for the fourth value; must not be {@code null}
     * @param <B4>           the new fourth value type
     * @return a new {@code Mappable4} with the transformed fourth value; never {@code null}
     *
     * @since 1.0.0
     */
    default <B4> @NonNull Mappable4<WT, A1, A2, A3, B4> map4(final @NonNull Function<? super A4, ? extends B4> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(Fun.identity(), Fun.identity(), Fun.identity(), transformation);
    }

    /**
     * Applies four mapping functions to the four values of this structure and returns a new instance
     * with all four values transformed.
     *
     * @param transformation1 function for the first value; must not be {@code null}
     * @param transformation2 function for the second value; must not be {@code null}
     * @param transformation3 function for the third value; must not be {@code null}
     * @param transformation4 function for the fourth value; must not be {@code null}
     * @param <B1>            new first value type
     * @param <B2>            new second value type
     * @param <B3>            new third value type
     * @param <B4>            new fourth value type
     * @return a new {@code Mappable4} with all four values transformed; never {@code null}
     *
     * @since 1.0.0
     */
    <B1, B2, B3, B4> @NonNull Mappable4<WT, B1, B2, B3, B4> mapAll(final @NonNull Function<? super A1, ? extends B1> transformation1,
                                                                   final @NonNull Function<? super A2, ? extends B2> transformation2,
                                                                   final @NonNull Function<? super A3, ? extends B3> transformation3,
                                                                   final @NonNull Function<? super A4, ? extends B4> transformation4);

}
