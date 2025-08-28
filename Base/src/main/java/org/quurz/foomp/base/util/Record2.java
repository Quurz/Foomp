package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Eager;
import org.quurz.foomp.base.types.Liftable2;
import org.quurz.foomp.base.types.Mappable2;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.Value2;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

/**
 * <div>
 *     <p>
 *         A record-based, immutable 2-tuple carrying two non-null values. This type offers
 *         value accessors, structural updates (with1/with2), mapping utilities (map/map1/map2/mapAll),
 *         applicative lifting (lift) and conversions (toTuple), as well as transmogrification and copying.
 *     </p>
 *     <p>
 *         Semantics:
 *         <ul>
 *             <li>Non-null contract: inputs must not be {@code null}, results must not be {@code null}.</li>
 *             <li>Eager evaluation: mapping and lifting operations evaluate immediately.</li>
 *             <li>Structural equality: equals/hashCode compare values component-wise.</li>
 *             <li>Stable string format: {@code Record2[value1=..., value2=...]}</li>
 *         </ul>
 *     </p>
 * </div>
 *
 * @param <A1> the type of the first value
 * @param <A2> the type of the second value
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public record Record2<A1, A2>(A1 value1,
                              A2 value2)
        implements Liftable2<Record2.µ, A1, A2>,
                   Mappable2<Record2.µ, A1, A2>,
                   Transmogrifyable<Record2<A1, A2>>,
                   Copyable<Record2<A1, A2>>,
                   Value2<A1, A2>,
                   Higher2<Record2.µ, A1, A2> {

    /**
     * <div>
     *     <p>
     *         The witness type for {@code Record2}.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    @SuppressWarnings("NonAsciiCharacters")
    public static final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Narrows a {@code Higher2} value to a concrete {@code Record2}.
     *     </p>
     *     <p>
     *         Contract: {@code wide} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param wide the higher-kinded value to narrow; must not be {@code null}
     * @param <A1> the first component type
     * @param <A2> the second component type
     * @return the narrowed {@code Record2}
     * @throws NullPointerException if {@code wide} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A1, A2> Record2<A1, A2> narrow(final @NonNull Higher2<Record2.µ, A1, A2> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        return (Record2<A1, A2>) wide;
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code Record2} with the given values (convenience factory).
     *     </p>
     *     <p>
     *         Contract: {@code value1} and {@code value2} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param value1 the first value; must not be {@code null}
     * @param value2 the second value; must not be {@code null}
     * @param <A1> the first component type
     * @param <A2> the second component type
     * @return a {@code Record2} instance containing the values
     * @throws NullPointerException if any value is {@code null}
     *
     * @since 1.0.0
     */
    public static <A1, A2> Record2<A1, A2> record2(final @NonNull A1 value1,
                                                   final @NonNull A2 value2) {
        Objects.requireNonNull(value1, nullValue("value1"));
        Objects.requireNonNull(value2, nullValue("value2"));
        return new Record2<>(value1, value2);
    }

    /**
     * <div>
     *     <p>
     *         Canonical constructor validating non-null components.
     *     </p>
     *     <p>
     *         Contract: {@code value1} and {@code value2} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param value1 the first value; must not be {@code null}
     * @param value2 the second value; must not be {@code null}
     * @throws NullPointerException if any value is {@code null}
     *
     * @since 1.0.0
     */
    public Record2(final @NonNull A1 value1,
                   final @NonNull A2 value2) {
        this.value1 = Objects.requireNonNull(value1, nullValue("value1"));
        this.value2 = Objects.requireNonNull(value2, nullValue("value2"));
    }

    /**
     * <div>
     *     <p>
     *         Indicates presence of values (always {@code true} for records).
     *         Alias for {@link #is1()}.
     *     </p>
     * </div>
     *
     * @return always {@code true}
     *
     * @since 1.0.0
     */
    @Override
    public boolean isPresent() {
        return this.is1();
    }

    /**
     * <div>
     *     <p>
     *         Indicates whether the first value is present (always {@code true}).
     *     </p>
     * </div>
     *
     * @return always {@code true}
     *
     * @since 1.0.0
     */
    @Override
    public boolean is1() {
        return true;
    }

    /**
     * <div>
     *     <p>
     *         Indicates whether the second value is present (always {@code true}).
     *     </p>
     * </div>
     *
     * @return always {@code true}
     *
     * @since 1.0.0
     */
    @Override
    public boolean is2() {
        return true;
    }

    /**
     * <div>
     *     <p>
     *         Returns the first value (alias for {@link #get1()}).
     *     </p>
     * </div>
     *
     * @return the first value
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull A1 get() {
        return this.get1();
    }

    /**
     * <div>
     *     <p>
     *         Returns the first value.
     *     </p>
     * </div>
     *
     * @return the first value
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull A1 get1() {
        return this.value1();
    }

    /**
     * <div>
     *     <p>
     *         Returns the second value.
     *     </p>
     * </div>
     *
     * @return the second value; never {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull A2 get2() {
        return this.value2();
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Record2} with a replaced first value.
     *     </p>
     *     <p>
     *         Contract: {@code value} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param value the new first value; must not be {@code null}
     * @param <B1> the new first component type
     * @return a new {@code Record2} with the updated first value
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public @NonNull <B1> Record2<B1, A2> with1(final @NonNull B1 value) {
        Objects.requireNonNull(value, nullValue("value"));
        return record2(value, this.value2);
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Record2} with a replaced second value.
     *     </p>
     *     <p>
     *         Contract: {@code value} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param value the new second value; must not be {@code null}
     * @param <B2> the new second component type
     * @return a new {@code Record2} with the updated second value
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public @NonNull <B2> Record2<A1, B2> with2(final @NonNull B2 value) {
        Objects.requireNonNull(value, nullValue("value"));
        return record2(this.value1, value);
    }

    /**
     * <div>
     *     <p>
     *         Maps the first value using the provided transformation. The second value remains unchanged.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation function to transform the first value
     * @param <B1> the transformed first component type
     * @return a new {@code Record2} with the transformed first value
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public @NonNull <B1> Record2<B1, A2> map(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.mapAll(transformation, Function.identity());
    }

    /**
     * <div>
     *     <p>
     *         Alias for {@link #map(Function)}.
     *     </p>
     * </div>
     *
     * @param transformation function to transform the first value; see {@link #map(Function)}
     * @param <B1> the transformed first component type
     * @return a new {@code Record2} with the transformed first value
     *
     * @since 1.0.0
     */
    @Eager
    public @NonNull <B1> Record2<B1, A2> map1(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.mapAll(transformation, Function.identity());
    }

    /**
     * <div>
     *     <p>
     *         Maps the second value using the provided transformation. The first value remains unchanged.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation function to transform the second value
     * @param <B2> the transformed second component type
     * @return a new {@code Record2} with the transformed second value
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public @NonNull <B2> Record2<A1, B2> map2(final @NonNull Function<? super A2, ? extends B2> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.mapAll(Function.identity(), transformation);
    }

    /**
     * <div>
     *     <p>
     *         Maps both values using the provided transformations.
     *     </p>
     *     <p>
     *         Contract: neither transformation may be {@code null}, and neither result may be {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation1 transformation for the first value; must not be {@code null} and must not return {@code null}
     * @param transformation2 transformation for the second value; must not be {@code null} and must not return {@code null}
     * @param <B1> the transformed first component type
     * @param <B2> the transformed second component type
     * @return a new {@code Record2} with transformed values
     * @throws NullPointerException if any transformation is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public @NonNull <B1, B2> Record2<B1, B2> mapAll(final @NonNull Function<? super A1, ? extends B1> transformation1,
                                                    final @NonNull Function<? super A2, ? extends B2> transformation2) {
        Objects.requireNonNull(transformation1, nullValue("fMapFirst"));
        Objects.requireNonNull(transformation2, nullValue("fMapSecond"));
        return new Record2<>(
            Objects.requireNonNull(transformation1.apply(this.value1), nullResultFrom("fMapFirst")),
            Objects.requireNonNull(transformation2.apply(this.value2), nullResultFrom("fMapSecond"))
        );
    }

    /**
     * <div>
     *     <p>
     *         Lifts two functions from a higher-kinded value and applies them to the respective components.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null}; contained functions/results must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation the higher-kinded value carrying functions for both components
     * @param <B1> the transformed first component type
     * @param <B2> the transformed second component type
     * @return a new {@code Record2} with transformed values
     * @throws NullPointerException if {@code transformation} is {@code null} or any function/result is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public @NonNull <B1, B2> Record2<B1, B2> lift(final @NonNull Higher2<µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        final var narrowedLiftA
            = narrow(transformation);
        return this.mapAll(narrowedLiftA.get1(), narrowedLiftA.get2());
    }

    /**
     * <div>
     *     <p>
     *         Converts this record to a {@link Tuple2} carrying the same values.
     *     </p>
     * </div>
     *
     * @return a {@code Tuple2} containing {@code value1} and {@code value2}
     *
     * @since 1.0.0
     */
    public Tuple2<A1, A2> toTuple() {
        return tuple2(this.value1, this.value2);
    }

    /**
     * <div>
     *     <p>
     *         Transmogrifies this record into another type using the given function.
     *         The transformation is eager and must not yield {@code null}.
     *     </p>
     * </div>
     *
     * @param transmogrifier the transforming function; must not be {@code null} and must not return {@code null}
     * @param <T> the target type
     * @return the transformed value
     * @throws NullPointerException if {@code transmogrifier} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public <T> @NonNull T transmogrify(@NonNull Function<? super Record2<A1, A2>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResultFrom("transmogrifier"));
    }

    /**
     * <div>
     *     <p>
     *         Returns a structural copy of this record (values are identical; identity is not preserved).
     *     </p>
     * </div>
     *
     * @return a copy of this {@code Record2}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Record2<A1, A2> copy() {
        return record2(this.value1, this.value2);
    }

    /**
     * <div>
     *     <p>
     *         Computes the hash code based on both contained values using {@link Objects#hashCode(Object)}.
     *     </p>
     * </div>
     *
     * @return the hash code of this record
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public int hashCode() {
        int result = Objects.hashCode(value1);
        result = 31 * result + Objects.hashCode(value2);
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Structural equality: two {@code Record2} instances are equal if both components are equal.
     *     </p>
     * </div>
     *
     * @param object the object to compare to
     * @return {@code true} if both components are equal; otherwise {@code false}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("DeconstructionCanBeUsed")
    @Override
    @Eager
    public boolean equals(Object object) {
        if (!(object instanceof Record2<?, ?> record2)) return false;
        return Objects.equals(value1, record2.value1)
                && Objects.equals(value2, record2.value2);
    }

    /**
     * <div>
     *     <p>
     *         Returns a stable string representation of this record:
     *         {@code Record2[value1=..., value2=...]}.
     *     </p>
     * </div>
     *
     * @return the string representation of this record
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public String toString() {
        return new StringJoiner(", ", Record2.class.getSimpleName() + "[", "]")
                .add("value1=" + value1)
                .add("value2=" + value2)
                .toString();
    }
}
