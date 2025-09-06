package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun4;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Liftable4;
import org.quurz.foomp.base.types.Mappable4;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.base.types.Value4;
import org.quurz.foomp.higher.Higher4;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         An immutable, functional data structure that stores four values.
 *         This class supports functional operations such as mapping, lifting, copying, and lazy evaluation.
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
public final class Tuple4<A1, A2, A3, A4>
        implements Liftable4<Tuple4.µ, A1, A2, A3, A4>,
                   Mappable4<Tuple4.µ, A1, A2, A3, A4>,
                   Copyable<Tuple4<A1, A2, A3, A4>>,
                   Unwindable<Tuple4<A1, A2, A3, A4>>,
                   Value4<A1, A2, A3, A4>,
                   Higher4<Tuple4.µ, A1, A2, A3, A4> {

    /**
     * <div>
     *     <p>
     *         The witness type for {@link Tuple4}, used in higher‑kinded encodings.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Narrows a {@link Higher4} value into a concrete {@code Tuple4} instance.
     *     </p>
     * </div>
     *
     * @param unfixed the {@link Higher4} value to narrow
     * @param <A1> type of the first element
     * @param <A2> type of the second element
     * @param <A3> type of the third element
     * @param <A4> type of the fourth element
     * @return the narrowed {@code Tuple4} instance
     * @throws NullPointerException if {@code unfixed} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A1, A2, A3, A4> Tuple4<A1, A2, A3, A4> narrow(@NonNull final Higher4<Tuple4.µ, A1, A2, A3, A4> unfixed) {
        return (Tuple4<A1, A2, A3, A4>) Objects.requireNonNull(unfixed, nullValue("unfixed"));
    }

    /**
     * <div>
     *     <p>
     *         Creates a new instance of {@link Tuple4} with the given values.
     *     </p>
     * </div>
     *
     * @param value1 the first value; must not be {@code null}
     * @param value2 the second value; must not be {@code null}
     * @param value3 the third value; must not be {@code null}
     * @param value4 the fourth value; must not be {@code null}
     * @param <A1> type of the first element
     * @param <A2> type of the second element
     * @param <A3> type of the third element
     * @param <A4> type of the fourth element
     * @return a new {@link Tuple4} instance with the given values
     * @throws NullPointerException if any value is {@code null}
     *
     * @since 1.0.0
     */
    public static <A1, A2, A3, A4> Tuple4<A1, A2, A3, A4> tuple4(final @NonNull A1 value1,
                                                                 final @NonNull A2 value2,
                                                                 final @NonNull A3 value3,
                                                                 final @NonNull A4 value4) {
        Objects.requireNonNull(value1, nullValue("value1"));
        Objects.requireNonNull(value2, nullValue("value2"));
        Objects.requireNonNull(value3, nullValue("value3"));
        Objects.requireNonNull(value4, nullValue("value4"));
        return new Tuple4<>(() -> value1, () -> value2, () -> value3, () -> value4);
    }

    private final Supplier<A1> value1Supplier;
    private final Supplier<A2> value2Supplier;
    private final Supplier<A3> value3Supplier;
    private final Supplier<A4> value4Supplier;

    private Tuple4(final Supplier<A1> value1Supplier,
                   final Supplier<A2> value2Supplier,
                   final Supplier<A3> value3Supplier,
                   final Supplier<A4> value4Supplier) {
        this.value1Supplier
            = value1Supplier;
        this.value2Supplier
            = value2Supplier;
        this.value3Supplier
            = value3Supplier;
        this.value4Supplier
            = value4Supplier;
    }

    private Tuple4(final A1 value1,
                   final Supplier<A2> value2Supplier,
                   final Supplier<A3> value3Supplier,
                   final Supplier<A4> value4Supplier) {
        this.value1Supplier
            = () -> value1;
        this.value2Supplier
            = value2Supplier;
        this.value3Supplier
            = value3Supplier;
        this.value4Supplier
            = value4Supplier;
    }

    private Tuple4(final Supplier<A1> value1Supplier,
                   final A2 value2,
                   final Supplier<A3> value3Supplier,
                   final Supplier<A4> value4Supplier) {
        this.value1Supplier
            = value1Supplier;
        this.value2Supplier
            = () -> value2;
        this.value3Supplier
            = value3Supplier;
        this.value4Supplier
            = value4Supplier;
    }

    private Tuple4(final Supplier<A1> value1Supplier,
                   final Supplier<A2> value2Supplier,
                   final A3 value3,
                   final Supplier<A4> value4Supplier) {
        this.value1Supplier
            = value1Supplier;
        this.value2Supplier
            = value2Supplier;
        this.value3Supplier
            = () -> value3;
        this.value4Supplier
            = value4Supplier;
    }

    private Tuple4(final Supplier<A1> value1Supplier,
                   final Supplier<A2> value2Supplier,
                   final Supplier<A3> value3Supplier,
                   final A4 value4) {
        this.value1Supplier
            = value1Supplier;
        this.value2Supplier
            = value2Supplier;
        this.value3Supplier
            = value3Supplier;
        this.value4Supplier
            = () -> value4;
    }

    /**
     * <div>
     *     <p>
     *         Indicates whether the first value is present (always {@code true}).
     *     </p>
     * </div>
     *
     * @return {@code true}
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
     *         Alias for {@link #is1()}.
     *     </p>
     * </div>
     *
     * @return {@code true}
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
     *         Indicates whether the second value is present (always {@code true}).
     *     </p>
     * </div>
     *
     * @return {@code true}
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
     * @return {@code true}
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
     * @return {@code true}
     */
    @Override
    public boolean is4() {
        return true;
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
    @UnwindingOperation
    @Override
    @NonNull
    public A1 get1() {
        return this.value1Supplier.get();
    }

    /**
     * <div>
     *     <p>
     *         Alias for {@link #get1()}.
     *     </p>
     * </div>
     *
     * @return the first value
     */
    @UnwindingOperation
    @NonNull
    public A1 get() {
        return this.get1();
    }

    /**
     * <div>
     *     <p>
     *         Returns the second value.
     *     </p>
     * </div>
     *
     * @return the second value
     */
    @UnwindingOperation
    @Override
    @NonNull
    public A2 get2() {
        return this.value2Supplier.get();
    }

    /**
     * <div>
     *     <p>
     *         Returns the third value.
     *     </p>
     * </div>
     *
     * @return the third value
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @Override
    @NonNull
    public A3 get3() {
        return this.value3Supplier.get();
    }

    /**
     * <div>
     *     <p>
     *         Returns the fourth value.
     *     </p>
     * </div>
     *
     * @return the fourth value
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @Override
    @NonNull
    public A4 get4() {
        return this.value4Supplier.get();
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Tuple4} with the first element replaced by the given value.
     *         Remaining values are preserved (lazy).
     *     </p>
     * </div>
     *
     * @param value1 the new first value; must not be {@code null}
     * @return a new {@code Tuple4} with the updated first element
     * @throws NullPointerException if {@code value1} is {@code null}
     *
     * @since 1.0.0
     */
    public Tuple4<A1, A2, A3, A4> with1(final @NonNull A1 value1) {
        Objects.requireNonNull(value1, nullValue("value1"));
        return new Tuple4<>(value1, this.value2Supplier, this.value3Supplier, this.value4Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Tuple4} with the second element replaced by the given value.
     *         Remaining values are preserved (lazy).
     *     </p>
     * </div>
     *
     * @param value2 the new second value; must not be {@code null}
     * @return a new {@code Tuple4} with the updated second element
     * @throws NullPointerException if {@code value2} is {@code null}
     *
     * @since 1.0.0
     */
    public Tuple4<A1, A2, A3, A4> with2(final @NonNull A2 value2) {
        Objects.requireNonNull(value2, nullValue("value2"));
        return new Tuple4<>(this.value1Supplier, value2, this.value3Supplier, this.value4Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Tuple4} with the third element replaced by the given value.
     *         Remaining values are preserved (lazy).
     *     </p>
     * </div>
     *
     * @param value3 the new third value; must not be {@code null}
     * @return a new {@code Tuple4} with the updated third element
     * @throws NullPointerException if {@code value3} is {@code null}
     *
     * @since 1.0.0
     */
    public Tuple4<A1, A2, A3, A4> with3(final @NonNull A3 value3) {
        Objects.requireNonNull(value3, nullValue("value3"));
        return new Tuple4<>(this.value1Supplier, this.value2Supplier, value3, this.value4Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Tuple4} with the fourth element replaced by the given value.
     *         Remaining values are preserved (lazy).
     *     </p>
     * </div>
     *
     * @param value4 the new fourth value; must not be {@code null}
     * @return a new {@code Tuple4} with the updated fourth element
     * @throws NullPointerException if {@code value4} is {@code null}
     *
     * @since 1.0.0
     */
    public Tuple4<A1, A2, A3, A4> with4(final @NonNull A4 value4) {
        Objects.requireNonNull(value4, nullValue("value4"));
        return new Tuple4<>(this.value1Supplier, this.value2Supplier, this.value3Supplier, value4);
    }

    /**
     * <div>
     *     <p>
     *         Transforms the first element using the given function and returns a new {@code Tuple4}.
     *     </p>
     * </div>
     *
     * @param transformation the transformation function for the first element; must not be {@code null}
     * @param <B1> the new first element type
     * @return a new {@code Tuple4} with the transformed first element; other elements remain unchanged
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B1> Tuple4<B1, A2, A3, A4> map1(@NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Tuple4<>(
            () -> Objects.requireNonNull(transformation.apply(this.value1Supplier.get()), nullResult()),
            this.value2Supplier,
            this.value3Supplier,
            this.value4Supplier
        );
    }

    /**
     * <div>
     *     <p>
     *         Transforms the second element using the given function and returns a new {@code Tuple4}.
     *     </p>
     * </div>
     *
     * @param transformation the transformation function for the second element; must not be {@code null}
     * @param <B2> the new second element type
     * @return a new {@code Tuple4} with the transformed second element; other elements remain unchanged
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B2> Tuple4<A1, B2, A3, A4> map2(@NonNull Function<? super A2, ? extends B2> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Tuple4<>(
            this.value1Supplier,
            () -> Objects.requireNonNull(transformation.apply(this.value2Supplier.get()), nullResult()),
            this.value3Supplier,
            this.value4Supplier
        );
    }

    /**
     * <div>
     *     <p>
     *         Transforms the third element using the given function and returns a new {@code Tuple4}.
     *     </p>
     * </div>
     *
     * @param transformation the transformation function for the third element; must not be {@code null}
     * @param <B3> the new third element type
     * @return a new {@code Tuple4} with the transformed third element; other elements remain unchanged
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     */
    @Override
    public @NonNull <B3> Tuple4<A1, A2, B3, A4> map3(@NonNull Function<? super A3, ? extends B3> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Tuple4<>(
            this.value1Supplier,
            this.value2Supplier,
            () -> Objects.requireNonNull(transformation.apply(this.value3Supplier.get()), nullResult()),
            this.value4Supplier
        );
    }

    /**
     * <div>
     *     <p>
     *         Transforms the fourth element using the given function and returns a new {@code Tuple4}.
     *     </p>
     * </div>
     *
     * @param transformation the transformation function for the fourth element; must not be {@code null}
     * @param <B4> the new fourth element type
     * @return a new {@code Tuple4} with the transformed fourth element; other elements remain unchanged
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B4> Tuple4<A1, A2, A3, B4> map4(@NonNull Function<? super A4, ? extends B4> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Tuple4<>(
            this.value1Supplier,
            this.value2Supplier,
            this.value3Supplier,
            () -> Objects.requireNonNull(transformation.apply(this.value4Supplier.get()), nullResult())
        );
    }

    /**
     * <div>
     *     <p>
     *         Transforms all four elements using the provided functions and returns a new {@code Tuple4}.
     *     </p>
     * </div>
     *
     * @param transformation1 transformation for the first element; must not be {@code null} and must not return {@code null}
     * @param transformation2 transformation for the second element; must not be {@code null} and must not return {@code null}
     * @param transformation3 transformation for the third element; must not be {@code null} and must not return {@code null}
     * @param transformation4 transformation for the fourth element; must not be {@code null} and must not return {@code null}
     * @param <B1> the new first element type
     * @param <B2> the new second element type
     * @param <B3> the new third element type
     * @param <B4> the new fourth element type
     * @return a new {@code Tuple4} with all elements transformed
     * @throws NullPointerException if any function is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public <B1, B2, B3, B4> @NonNull Tuple4<B1, B2, B3, B4> mapAll(final @NonNull Function<? super A1, ? extends B1> transformation1,
                                                                   final @NonNull Function<? super A2, ? extends B2> transformation2,
                                                                   final @NonNull Function<? super A3, ? extends B3> transformation3,
                                                                   final @NonNull Function<? super A4, ? extends B4> transformation4) {
        Objects.requireNonNull(transformation1, nullValue("fMap1"));
        Objects.requireNonNull(transformation2, nullValue("fMap2"));
        Objects.requireNonNull(transformation3, nullValue("fMap3"));
        Objects.requireNonNull(transformation4, nullValue("fMap4"));
        return new Tuple4<>(
            () -> Objects.requireNonNull(transformation1.apply(this.value1Supplier.get()), nullResult()),
            () -> Objects.requireNonNull(transformation2.apply(this.value2Supplier.get()), nullResult()),
            () -> Objects.requireNonNull(transformation3.apply(this.value3Supplier.get()), nullResult()),
            () -> Objects.requireNonNull(transformation4.apply(this.value4Supplier.get()), nullResult())
        );
    }

    /**
     * <div>
     *     <p>
     *         Applies lifted functions carried in {@link Higher4} to the current values (component‑wise).
     *     </p>
     * </div>
     *
     * @param transformation the {@link Higher4} carrying the functions; must not be {@code null}
     * @param <B1> the new first element type
     * @param <B2> the new second element type
     * @param <B3> the new third element type
     * @param <B4> the new fourth element type
     * @return a new {@code Tuple4} with transformed elements
     * @throws NullPointerException if {@code transformation} is {@code null}, any function is {@code null}, or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B1, B2, B3, B4> Tuple4<B1, B2, B3, B4> lift(@NonNull Higher4<µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>, ? extends Function<? super A3, ? extends B3>, ? extends Function<? super A4, ? extends B4>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        final var fixed
            = narrow(transformation);
        return new Tuple4<>(
            () -> Objects.requireNonNull(fixed.value1Supplier.get().apply(this.value1Supplier.get()), nullResult()),
            () -> Objects.requireNonNull(fixed.value2Supplier.get().apply(this.value2Supplier.get()), nullResult()),
            () -> Objects.requireNonNull(fixed.value3Supplier.get().apply(this.value3Supplier.get()), nullResult()),
            () -> Objects.requireNonNull(fixed.value4Supplier.get().apply(this.value4Supplier.get()), nullResult())
        );
    }

    /**
     * <div>
     *     <p>
     *         Combines the four values of this {@code Tuple4} using the given function and returns a single result.
     *     </p>
     * </div>
     *
     * @param meld function combining the four values; must not be {@code null} and must not return {@code null}
     * @param <B>  the result type
     * @return the result of combining the four values
     * @throws NullPointerException if {@code meld} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull <B> B meld(final @NonNull Fun4<A1, A2, A3, A4, B> meld) {
        Objects.requireNonNull(meld, nullValue("meld"));
        return Objects.requireNonNull(meld.apply(this.value1Supplier.get(), value2Supplier.get(), value3Supplier.get(), value4Supplier.get()), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Creates a copy of this {@code Tuple4} with the same values.
     *     </p>
     * </div>
     *
     * @return a new instance of {@code Tuple4} backed by the same suppliers
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Tuple4<A1, A2, A3, A4> copy() {
        return new Tuple4<>(this.value1Supplier, this.value2Supplier, this.value3Supplier, this.value4Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Creates a {@code Record4} from this {@code Tuple4}, evaluating any deferred values.
     *     </p>
     * </div>
     *
     * @return a {@code Record4} containing the current values of this tuple
     * @throws NullPointerException if any stored value is {@code null}
     * @see Record4
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public Record4<A1, A2, A3, A4> toRecord() {
        return Record4.record4(value1Supplier.get(), value2Supplier.get(), value3Supplier.get(), value4Supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Tuple4} where only the first value is evaluated; others remain deferred.
     *     </p>
     * </div>
     *
     * @return a {@code Tuple4} with the first value realized and the rest deferred
     *
     * @since 1.0.0
     */
    public @NonNull Tuple4<A1, A2, A3, A4> unwind1() {
        return new Tuple4<>(this.value1Supplier.get(), this.value2Supplier, this.value3Supplier, this.value4Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Tuple4} where only the second value is evaluated; others remain deferred.
     *     </p>
     * </div>
     *
     * @return a {@code Tuple4} with the second value realized and the rest deferred
     *
     * @since 1.0.0
     */
    public @NonNull Tuple4<A1, A2, A3, A4> unwind2() {
        return new Tuple4<>(this.value1Supplier, this.value2Supplier.get(), this.value3Supplier, this.value4Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Tuple4} where only the third value is evaluated; others remain deferred.
     *     </p>
     * </div>
     *
     * @return a {@code Tuple4} with the third value realized and the rest deferred
     *
     * @since 1.0.0
     */
    public @NonNull Tuple4<A1, A2, A3, A4> unwind3() {
        return new Tuple4<>(this.value1Supplier, this.value2Supplier, this.value3Supplier.get(), this.value4Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Returns a new {@code Tuple4} where only the fourth value is evaluated; others remain deferred.
     *     </p>
     * </div>
     *
     * @return a {@code Tuple4} with the fourth value realized and the rest deferred
     *
     * @since 1.0.0
     */
    public @NonNull Tuple4<A1, A2, A3, A4> unwind4() {
        return new Tuple4<>(this.value1Supplier, this.value2Supplier, this.value3Supplier, this.value4Supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         Realizes all deferred values and returns a new {@code Tuple4} with evaluated values.
     *     </p>
     * </div>
     *
     * @return a new {@code Tuple4} with realized values
     * @throws NullPointerException if any evaluated value is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull Tuple4<A1, A2, A3, A4> unwind() {
        return tuple4(this.value1Supplier.get(), this.value2Supplier.get(), this.value3Supplier.get(), this.value4Supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         Compares this {@code Tuple4} to another object for equality (component‑wise structural equality).
     *     </p>
     * </div>
     *
     * @param other the object to compare against
     * @return {@code true} if the other object is a {@code Tuple4} with equal components; otherwise {@code false}
     * @throws NullPointerException if any stored value is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Tuple4<?, ?, ?, ?> tuple4 = (Tuple4<?, ?, ?, ?>) other;
        return Objects.equals(this.value1Supplier.get(), tuple4.value1Supplier.get())
            && Objects.equals(value2Supplier.get(), tuple4.value2Supplier.get())
            && Objects.equals(value3Supplier.get(), tuple4.value3Supplier.get())
            && Objects.equals(value4Supplier.get(), tuple4.value4Supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         Computes the hash code of this {@code Tuple4} based on all four stored values.
     *     </p>
     * </div>
     *
     * @return the computed hash code
     * @throws NullPointerException if any stored value is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public int hashCode() {
        int result = Objects.hashCode(value1Supplier.get());
        result = 31 * result + Objects.hashCode(value2Supplier.get());
        result = 31 * result + Objects.hashCode(value3Supplier.get());
        result = 31 * result + Objects.hashCode(value4Supplier.get());
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Returns a string representation of this {@code Tuple4} in the form
     *         {@code Tuple4[value1=..., value2=..., value3=..., value4=...]}.
     *     </p>
     * </div>
     *
     * @return the string representation of this tuple including all four values
     * @throws NullPointerException if any stored value is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public String toString() {
        return new StringJoiner(", ", Tuple4.class.getSimpleName() + "[", "]")
                .add("value1=" + value1Supplier.get())
                .add("value2=" + value2Supplier.get())
                .add("value3=" + value3Supplier.get())
                .add("value4=" + value4Supplier.get())
                .toString();
    }
}
