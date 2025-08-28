package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Liftable4;
import org.quurz.foomp.base.types.Mappable4;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.Value4;
import org.quurz.foomp.higher.Higher4;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Tuple4.tuple4;

/**
 * <div>
 *     <p>
 *         A record-based, immutable 4-tuple carrying four non-null values. This type provides
 *         value accessors, structural updates (with1/with2/with3/with4), mapping utilities
 *         (map1/map2/map3/map4/mapAll), applicative lifting (lift), conversions (toTuple),
 *         transmogrification and copying.
 *     </p>
 *     <p>
 *         Semantics:
 *         <ul>
 *             <li>Non-null contract: inputs must not be {@code null}, results must not be {@code null}.</li>
 *             <li>Eager evaluation: mapping and lifting operations evaluate immediately.</li>
 *             <li>Structural equality: equals/hashCode compare values component-wise.</li>
 *             <li>Stable string format: {@code Record4[value1=..., value2=..., value3=..., value4=...]}</li>
 *         </ul>
 *     </p>
 * </div>
 *
 * @param <A1> the type of the first value
 * @param <A2> the type of the second value
 * @param <A3> the type of the third value
 * @param <A4> the type of the fourth value
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public record Record4<A1, A2, A3, A4>(A1 value1,
                                      A2 value2,
                                      A3 value3,
                                      A4 value4)
        implements Liftable4<Record4.µ, A1, A2, A3, A4>,
                   Mappable4<Record4.µ, A1, A2, A3, A4>,
                   Transmogrifyable<Record4<A1, A2, A3, A4>>,
                   Copyable<Record4<A1, A2, A3, A4>>,
                   Value4<A1, A2, A3, A4>,
                   Higher4<Record4.µ, A1, A2, A3, A4> {

    /**
     * <div>
     *     <p>
     *         The witness type for {@code Record4}.
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
     *         Narrows a {@code Higher4} value to a concrete {@code Record4}.
     *     </p>
     *     <p>
     *         Contract: {@code wide} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param wide the higher-kinded value to narrow; must not be {@code null}
     * @param <A1> the first component type
     * @param <A2> the second component type
     * @param <A3> the third component type
     * @param <A4> the fourth component type
     * @return the narrowed {@code Record4}
     * @throws NullPointerException if {@code wide} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A1, A2, A3, A4> Record4<A1, A2, A3, A4> narrow(final @NonNull Higher4<Record4.µ, A1, A2, A3, A4> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        return (Record4<A1, A2, A3, A4>) wide;
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code Record4} with the given values (convenience factory).
     *     </p>
     *     <p>
     *         Contract: all values must be non-null.
     *     </p>
     * </div>
     *
     * @param value1 the first value; must not be {@code null}
     * @param value2 the second value; must not be {@code null}
     * @param value3 the third value; must not be {@code null}
     * @param value4 the fourth value; must not be {@code null}
     * @param <A1> the first component type
     * @param <A2> the second component type
     * @param <A3> the third component type
     * @param <A4> the fourth component type
     * @return a new {@code Record4} containing the values
     * @throws NullPointerException if any value is {@code null}
     *
     * @since 1.0.0
     */
    public static <A1, A2, A3, A4> Record4<A1, A2, A3, A4> record4(final @NonNull A1 value1,
                                                                   final @NonNull A2 value2,
                                                                   final @NonNull A3 value3,
                                                                   final @NonNull A4 value4) {
        Objects.requireNonNull(value1, nullValue("value1"));
        Objects.requireNonNull(value2, nullValue("value2"));
        Objects.requireNonNull(value3, nullValue("value3"));
        Objects.requireNonNull(value4, nullValue("value4"));
        return new Record4<>(value1, value2, value3, value4);
    }

    /**
     * <div>
     *     <p>
     *         Canonical constructor validating non-null components.
     *     </p>
     * </div>
     *
     * @param value1 the first value; must not be {@code null}
     * @param value2 the second value; must not be {@code null}
     * @param value3 the third value; must not be {@code null}
     * @param value4 the fourth value; must not be {@code null}
     * @throws NullPointerException if any value is {@code null}
     *
     * @since 1.0.0
     */
    public Record4(final @NonNull A1 value1,
                   final @NonNull A2 value2,
                   final @NonNull A3 value3,
                   final @NonNull A4 value4) {
        this.value1 = Objects.requireNonNull(value1, nullValue("value1"));
        this.value2 = Objects.requireNonNull(value2, nullValue("value2"));
        this.value3 = Objects.requireNonNull(value3, nullValue("value3"));
        this.value4 = Objects.requireNonNull(value4, nullValue("value4"));
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
     *         Indicates whether the third value is present (always {@code true}).
     *     </p>
     * </div>
     *
     * @return always {@code true}
     *
     * @since 1.0.0
     */
    @Override
    public boolean is3() {
        return true;
    }

    /**
     * <div>
     *     <p>
     *         Indicates whether the fourth value is present (always {@code true}).
     *     </p>
     * </div>
     *
     * @return always {@code true}
     *
     * @since 1.0.0
     */
    @Override
    public boolean is4() {
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
        return this.value1();
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
     *         Returns the third value.
     *     </p>
     * </div>
     *
     * @return the third value; never {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull A3 get3() {
        return this.value3();
    }

    /**
     * <div>
     *     <p>
     *         Returns the fourth value.
     *     </p>
     * </div>
     *
     * @return the fourth value; never {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull A4 get4() {
        return this.value4();
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Record4} with a replaced first value.
     *     </p>
     *     <p>
     *         Contract: {@code value} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param value the new first value; must not be {@code null}
     * @param <B1> the new first component type
     * @return a new {@code Record4} with the updated first value
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public @NonNull <B1> Record4<B1, A2, A3, A4> with1(final @NonNull B1 value) {
        Objects.requireNonNull(value, nullValue("value"));
        return record4(value, this.value2, this.value3, this.value4);
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Record4} with a replaced second value.
     *     </p>
     *     <p>
     *         Contract: {@code value} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param value the new second value; must not be {@code null}
     * @param <B2> the new second component type
     * @return a new {@code Record4} with the updated second value
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public @NonNull <B2> Record4<A1, B2, A3, A4> with2(final @NonNull B2 value) {
        Objects.requireNonNull(value, nullValue("value"));
        return record4(this.value1, value, this.value3, this.value4);
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Record4} with a replaced third value.
     *     </p>
     *     <p>
     *         Contract: {@code value} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param value the new third value; must not be {@code null}
     * @param <B3> the new third component type
     * @return a new {@code Record4} with the updated third value
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public @NonNull <B3> Record4<A1, A2, B3, A4> with3(final @NonNull B3 value) {
        Objects.requireNonNull(value, nullValue("value"));
        return record4(this.value1, this.value2, value, this.value4);
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Record4} with a replaced fourth value.
     *     </p>
     *     <p>
     *         Contract: {@code value} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param value the new fourth value; must not be {@code null}
     * @param <B4> the new fourth component type
     * @return a new {@code Record4} with the updated fourth value
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public @NonNull <B4> Record4<A1, A2, A3, B4> with4(final @NonNull B4 value) {
        Objects.requireNonNull(value, nullValue("value"));
        return record4(this.value1, this.value2, this.value3, value);
    }

    /**
     * <div>
     *     <p>
     *         Maps the first value using the provided transformation. The other values remain unchanged.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation function to transform the first value
     * @param <B1> the transformed first component type
     * @return a new {@code Record4} with the transformed first value
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B1> Record4<B1, A2, A3, A4> map1(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.mapAll(transformation, Function.identity(), Function.identity(), Function.identity());
    }

    /**
     * <div>
     *     <p>
     *         Maps the second value using the provided transformation. The other values remain unchanged.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation function to transform the second value
     * @param <B2> the transformed second component type
     * @return a new {@code Record4} with the transformed second value
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B2> Record4<A1, B2, A3, A4> map2(final @NonNull Function<? super A2, ? extends B2> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.mapAll(Function.identity(), transformation, Function.identity(), Function.identity());
    }

    /**
     * <div>
     *     <p>
     *         Maps the third value using the provided transformation. The other values remain unchanged.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation function to transform the third value
     * @param <B3> the transformed third component type
     * @return a new {@code Record4} with the transformed third value
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B3> Record4<A1, A2, B3, A4> map3(final @NonNull Function<? super A3, ? extends B3> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.mapAll(Function.identity(), Function.identity(), transformation, Function.identity());
    }

    /**
     * <div>
     *     <p>
     *         Maps the fourth value using the provided transformation. The other values remain unchanged.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation function to transform the fourth value
     * @param <B4> the transformed fourth component type
     * @return a new {@code Record4} with the transformed fourth value
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B4> Record4<A1, A2, A3, B4> map4(final @NonNull Function<? super A4, ? extends B4> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.mapAll(Function.identity(), Function.identity(), Function.identity(), transformation);
    }

    /**
     * <div>
     *     <p>
     *         Maps all four values using the provided transformations.
     *     </p>
     *     <p>
     *         Contract: no transformation may be {@code null}, and no result may be {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation1 function to transform the first value; must not be {@code null} and must not return {@code null}
     * @param transformation2 function to transform the second value; must not be {@code null} and must not return {@code null}
     * @param transformation3 function to transform the third value; must not be {@code null} and must not return {@code null}
     * @param transformation4 function to transform the fourth value; must not be {@code null} and must not return {@code null}
     * @param <B1> the transformed first component type
     * @param <B2> the transformed second component type
     * @param <B3> the transformed third component type
     * @param <B4> the transformed fourth component type
     * @return a new {@code Record4} with transformed values
     * @throws NullPointerException if any transformation is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B1, B2, B3, B4> Record4<B1, B2, B3, B4> mapAll(final @NonNull Function<? super A1, ? extends B1> transformation1,
                                                                    final @NonNull Function<? super A2, ? extends B2> transformation2,
                                                                    final @NonNull Function<? super A3, ? extends B3> transformation3,
                                                                    final @NonNull Function<? super A4, ? extends B4> transformation4) {
        Objects.requireNonNull(transformation1, nullValue("fMap1"));
        Objects.requireNonNull(transformation2, nullValue("fMap2"));
        Objects.requireNonNull(transformation3, nullValue("fMap3"));
        Objects.requireNonNull(transformation4, nullValue("fMap4"));
        return record4(
            Objects.requireNonNull(transformation1.apply(this.value1), nullResultFrom("fMap1")),
            Objects.requireNonNull(transformation2.apply(this.value2), nullResultFrom("fMap2")),
            Objects.requireNonNull(transformation3.apply(this.value3), nullResultFrom("fMap3")),
            Objects.requireNonNull(transformation4.apply(this.value4), nullResultFrom("fMap4"))
        );
    }

    /**
     * <div>
     *     <p>
     *         Lifts four functions from a higher-kinded value and applies them to the respective components.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null}; contained functions/results must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation the higher-kinded value carrying functions for all components
     * @param <B1> the transformed first component type
     * @param <B2> the transformed second component type
     * @param <B3> the transformed third component type
     * @param <B4> the transformed fourth component type
     * @return a new {@code Record4} with transformed values
     * @throws NullPointerException if {@code transformation} is {@code null} or any function/result is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B1, B2, B3, B4> Record4<B1, B2, B3, B4> lift(final @NonNull Higher4<µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>, ? extends Function<? super A3, ? extends B3>, ? extends Function<? super A4, ? extends B4>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        final var narrowedLiftA
            = narrow(transformation);
        return this.mapAll(narrowedLiftA.get1(), narrowedLiftA.get2(), narrowedLiftA.get3(), narrowedLiftA.get4());
    }

    /**
     * <div>
     *     <p>
     *         Converts this record to a {@link Tuple4} carrying the same values.
     *     </p>
     * </div>
     *
     * @return a {@code Tuple4} containing {@code value1}, {@code value2}, {@code value3} and {@code value4}
     *
     * @since 1.0.0
     */
    public @NonNull Tuple4<A1, A2, A3, A4> toTuple() {
        return tuple4(this.value1, this.value2, this.value3, this.value4);
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
    public <T> @NonNull T transmogrify(final @NonNull Function<? super Record4<A1, A2, A3, A4>, ? extends T> transmogrifier) {
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
     * @return a copy of this {@code Record4}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Record4<A1, A2, A3, A4> copy() {
        return record4(this.value1, this.value2, this.value3, this.value4);
    }

    /**
     * <div>
     *     <p>
     *         Computes the hash code based on all contained values using {@link Objects#hashCode(Object)}.
     *     </p>
     * </div>
     *
     * @return the hash code of this record
     *
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        int result = Objects.hashCode(value1);
        result = 31 * result + Objects.hashCode(value2);
        result = 31 * result + Objects.hashCode(value3);
        result = 31 * result + Objects.hashCode(value4);
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Structural equality: two {@code Record4} instances are equal if all components are equal.
     *     </p>
     * </div>
     *
     * @param object the object to compare to
     * @return {@code true} if all components are equal; otherwise {@code false}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("DeconstructionCanBeUsed")
    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Record4<?, ?, ?, ?> record4)) return false;
        return Objects.equals(value1, record4.value1)
                && Objects.equals(value2, record4.value2)
                && Objects.equals(value3, record4.value3)
                && Objects.equals(value4, record4.value4);
    }

    /**
     * <div>
     *     <p>
     *         Returns a stable string representation in the format
     *         {@code Record4[value1=..., value2=..., value3=..., value4=...]}.
     *     </p>
     * </div>
     *
     * @return the string representation of this record
     *
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Record4.class.getSimpleName() + "[", "]")
                .add("value1=" + value1)
                .add("value2=" + value2)
                .add("value3=" + value3)
                .add("value4=" + value4)
                .toString();
    }

}
