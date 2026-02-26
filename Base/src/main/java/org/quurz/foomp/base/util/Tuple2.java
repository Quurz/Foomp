package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Appliable2;
import org.quurz.foomp.base.types.Mappable2;
import org.quurz.foomp.base.types.Swappable;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.base.types.Value2;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.MutablePair.mutablePair;

/**
 * <div>
 *   <p>
 *     An immutable, lazy 2‑tuple that holds two non‑null values {@code (A1, A2)}.
 *     Both components are stored via {@link java.util.function.Supplier} to allow lazy retrieval,
 *     i.e., values are evaluated only when accessed.
 *   </p>
 *   <p>
 *     Semantics:
 *     <ul>
 *       <li><b>Lazy access</b>: values are retrieved on demand using suppliers (see {@code get1()}/{@code get2()}).</li>
 *       <li><b>Mapping</b>: {@code map}/{@code map1}/{@code map2}/{@code mapAll} transform components eagerly at call site.</li>
 *       <li><b>Applicative</b>: {@code applyTo} applies functions carried by another {@code Tuple2} to the respective values.</li>
 *       <li><b>Structure</b>: {@code swap}, {@code with1}/{@code with2}, {@code copy}, {@code toRecord} provide structural utilities.</li>
 *     </ul>
 *   </p>
 *   <p>
 *     Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     Methods enforce null‑checks to provide predictable behavior with lazy values.
 *   </p>
 *   <p>
 *     Examples:
 *   </p>
 *   <pre>{@code
 *   var t = Tuple2.tuple2(21, "x");
 *   t.get1();                    // 21
 *   t.get2();                    // "x"
 *   t.map1(i -> i * 2);          // (42, "x")
 *   t.map2(s -> s + "!");        // (21, "x!")
 *   t.mapAll(Object::toString, String::length);  // ("21", 1)
 *
 *   var tf = Tuple2.tuple2((Function<Integer, String>) Object::toString,
 *                          (Function<String, Integer>) String::length);
 *   t.applyTo(tf);                  // applies both functions component-wise: ("21", 1)
 *
 *   t.swap();                    // ("x", 21)
 *   t.toRecord();                // Record2[value1=21, value2=x]
 *   }</pre>
 * </div>
 *
 * @param <A1> type of the first value
 * @param <A2> type of the second value
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public final class Tuple2<A1, A2>
        implements Mappable2<Tuple2.µ, A1, A2>,
        Appliable2<Tuple2.µ, A1, A2>,
                   Swappable<Tuple2<A2, A1>, A1, A2>,
                   Copyable<Tuple2<A1, A2>>,
                   Unwindable<Tuple2<A1, A2>>,
                   Value2<A1, A2>,
                   Higher2<Tuple2.µ, A1, A2> {

    /**
     * <div>
     *   <p>
     *     Internal marker class for the WitnessType pattern.
     *   </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *   <p>
     *     Narrows a {@code Higher2<µ, A1, A2>} to a concrete {@code Tuple2<A1, A2>}.
     *   </p>
     * </div>
     *
     * @param unfixed the higher‑kinded value; must not be {@code null}
     * @return the same instance, viewed as {@code Tuple2}
     * @throws NullPointerException if {@code unfixed} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A1, A2> Tuple2<A1, A2> narrow(@NonNull final Higher2<µ, A1, A2> unfixed) {
        return (Tuple2<A1, A2>) Objects.requireNonNull(unfixed, nullValue("unfixed"));
    }

    /**
     * <div>
     *   <p>
     *     Creates a tuple from two values. Values are stored lazily via suppliers.
     *   </p>
     * </div>
     *
     * @param value1 the first value; must not be {@code null}
     * @param value2 the second value; must not be {@code null}
     * @return a new {@code Tuple2}
     * @throws NullPointerException if any value is {@code null}
     *
     * @since 1.0.0
     */
    public static <A1, A2> Tuple2<A1, A2> tuple2(@NonNull final A1 value1,
                                                 @NonNull final A2 value2) {
        Objects.requireNonNull(value1, nullValue("value1"));
        Objects.requireNonNull(value2, nullValue("value2"));
        return new Tuple2<>(() -> value1, () -> value2);
    }

    private final Supplier<A1> value1Supplier;
    private final Supplier<A2> value2Supplier;

    private Tuple2(final Supplier<A1> value1Supplier,
                   final Supplier<A2> value2Supplier) {
        this.value1Supplier
            = value1Supplier;
        this.value2Supplier
            = value2Supplier;
    }

    private Tuple2(final Supplier<A1> value1Supplier,
                   final A2 value2) {
        this(value1Supplier, () -> value2);
    }

    private Tuple2(final A1 value1,
                   final Supplier<A2> value2Supplier) {
        this(() -> value1, value2Supplier);
    }

    /**
     * <div>
     *   <p>
     *     Indicates whether the first component is present.
     *     Always {@code true} because {@code Tuple2} is total for both components.
     *   </p>
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
     *   <p>
     *     Alias for {@link #is1()}.
     *   </p>
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
     *   <p>
     *     Indicates whether the second component is present.
     *     Always {@code true} because {@code Tuple2} is total for both components.
     *   </p>
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
     *   <p>
     *     Returns the first value (evaluates its supplier).
     *   </p>
     * </div>
     *
     * @return the first value; never {@code null}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull A1 get1() {
        return this.value1Supplier.get();
    }

    /**
     * <div>
     *   <p>
     *     Returns the first value.
     *     Alias for {@link #get1()}.
     *   </p>
     * </div>
     *
     * @return the first value; never {@code null}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull A1 get() {
        return this.get1();
    }

    /**
     * <div>
     *   <p>
     *     Returns the second value (evaluates its supplier).
     *   </p>
     * </div>
     *
     * @return the second value; never {@code null}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @Override
    public @NonNull A2 get2() {
        return this.value2Supplier.get();
    }

    /**
     * <div>
     *   <p>
     *     Returns a new tuple with a replaced first value; the second value is kept (lazy).
     *   </p>
     * </div>
     *
     * @param newFirst the new first value; must not be {@code null}
     * @param <B1>     the new first type
     * @return a new {@code Tuple2} with updated first value
     * @throws NullPointerException if {@code newFirst} is {@code null}
     *
     * @since 1.0.0
     */
    public <B1> Tuple2<B1, A2> with1(final @NonNull B1 newFirst) {
        Objects.requireNonNull(newFirst, nullValue("newFirst"));
        return new Tuple2<>(() -> newFirst, this.value2Supplier);
    }

    /**
     * <div>
     *   <p>
     *     Returns a new tuple with a replaced second value; the first value is kept (lazy).
     *   </p>
     * </div>
     *
     * @param newSecond the new second value; must not be {@code null}
     * @param <B2>      the new second type
     * @return a new {@code Tuple2} with updated second value
     * @throws NullPointerException if {@code newSecond} is {@code null}
     *
     * @since 1.0.0
     */
    public <B2> Tuple2<A1, B2> with2(final @NonNull B2 newSecond) {
        Objects.requireNonNull(newSecond, nullValue("newSecond"));
        return new Tuple2<>(this.value1Supplier, () -> newSecond);
    }

    /**
     * <div>
     *   <p>
     *     Melds both values using the given function, returning a single result.
     *     Both values are evaluated.
     *   </p>
     * </div>
     *
     * @param meld combining function; must not be {@code null} and must not return {@code null}
     * @param <B>  the result type
     * @return the meld result; never {@code null}
     * @throws NullPointerException if {@code meld} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    // TODO; Test
    @UnwindingOperation
    public <B> B meld(final @NonNull BiFunction<? super A1, ? super A2, ? extends B> meld) {
        Objects.requireNonNull(meld, nullValue("meld"));
        return Objects.requireNonNull(meld.apply(this.get1(), this.get2()), nullResult());
    }

    /**
     * <div>
     *   <p>
     *     Transforms the first value using both values {@code (A1, A2) -> B1}.
     *     The second value is preserved (lazy).
     *   </p>
     * </div>
     *
     * @param mapper function using both values to produce a new first value; must not be {@code null} and must not return {@code null}
     * @param <B1>   the new first type
     * @return a new {@code Tuple2} with transformed first value
     * @throws NullPointerException if {@code mapper} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    public <B1> Tuple2<B1, A2> mapTo1(final @NonNull BiFunction<? super A1, ? super A2, ? extends B1> mapper) {
        Objects.requireNonNull(mapper, nullValue("mapper"));
        return new Tuple2<>(
            () -> Objects.requireNonNull(mapper.apply(this.get1(), this.get2()), nullResult()),
            this.value2Supplier
        );
    }

    /**
     * <div>
     *   <p>
     *     Transforms the first value using the second value {@code A2 -> B1}.
     *     The second value is preserved (lazy).
     *   </p>
     * </div>
     *
     * @param mapper function using {@code A2} to produce a new first value; must not be {@code null} and must not return {@code null}
     * @param <B1>   the new first type
     * @return a new {@code Tuple2} with transformed first value
     * @throws NullPointerException if {@code mapper} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    public <B1> Tuple2<B1, A2> mapTo1(final @NonNull Function<? super A2, ? extends B1> mapper) {
        Objects.requireNonNull(mapper, nullValue("mapper"));
        return new Tuple2<>(
            () -> Objects.requireNonNull(mapper.apply(this.value2Supplier.get()), nullResult()),
            this.value2Supplier
        );
    }

    /**
     * <div>
     *   <p>
     *     Transforms the second value using both values {@code (A1, A2) -> B2}.
     *     The first value is preserved (lazy).
     *   </p>
     * </div>
     *
     * @param mapper function using both values to produce a new second value; must not be {@code null} and must not return {@code null}
     * @param <B2>   the new second type
     * @return a new {@code Tuple2} with transformed second value
     * @throws NullPointerException if {@code mapper} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    public <B2> Tuple2<A1, B2> mapTo2(final @NonNull BiFunction<? super A1, ? super A2, ? extends B2> mapper) {
        Objects.requireNonNull(mapper, nullValue("mapper"));
        return new Tuple2<>(
            this.value1Supplier,
            () -> Objects.requireNonNull(mapper.apply(this.get1(), this.get2()), nullResult())
        );
    }

    /**
     * <div>
     *   <p>
     *     Transforms the second value using the first value {@code A1 -> B2}.
     *     The first value is preserved (lazy).
     *   </p>
     * </div>
     *
     * @param mapper function using {@code A1} to produce a new second value; must not be {@code null} and must not return {@code null}
     * @param <B2>   the new second type
     * @return a new {@code Tuple2} with transformed second value
     * @throws NullPointerException if {@code mapper} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    public <B2> Tuple2<A1, B2> mapTo2(final @NonNull Function<? super A1, ? extends B2> mapper) {
        Objects.requireNonNull(mapper, nullValue("mapper"));
        return new Tuple2<>(
            this.value1Supplier,
            () -> Objects.requireNonNull(mapper.apply(this.value1Supplier.get()), nullResult())
        );
    }

    /**
     * <div>
     *   <p>
     *     Functor map (alias for {@link #map1(Function)}): transforms the first value.
     *     The second value is preserved.
     *   </p>
     * </div>
     *
     * @param transformation the function for the first value; must not be {@code null} and must not return {@code null}
     * @param <B1>           the new first type
     * @return a new {@code Tuple2} with transformed first value
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public  <B1> @NonNull Tuple2<B1, A2> map(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.map1(transformation);
    }

    /**
     * <div>
     *   <p>
     *     Transforms the first value and preserves the second.
     *   </p>
     * </div>
     *
     * @param transformation the function for the first value; must not be {@code null} and must not return {@code null}
     * @param <B1>           the new first type
     * @return a new {@code Tuple2} with transformed first value
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    public <B1> @NonNull Tuple2<B1, A2> map1(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Tuple2<>(() -> Objects.requireNonNull(transformation.apply(this.get1()), nullResult()), this.value2Supplier);
    }

    /**
     * <div>
     *   <p>
     *     Transforms the second value and preserves the first.
     *   </p>
     * </div>
     *
     * @param transformation the function for the second value; must not be {@code null} and must not return {@code null}
     * @param <B2>           the new second type
     * @return a new {@code Tuple2} with transformed second value
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    public <B2> @NonNull Tuple2<A1, B2> map2(final @NonNull Function<? super A2, ? extends B2> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Tuple2<>(this.value1Supplier, () -> Objects.requireNonNull(transformation.apply(this.get2()), nullResult()));
    }

    /**
     * <div>
     *   <p>
     *     Transforms both values using the provided functions.
     *   </p>
     * </div>
     *
     * @param transformation1 function for the first value; must not be {@code null} and must not return {@code null}
     * @param transformation2 function for the second value; must not be {@code null} and must not return {@code null}
     * @param <B1>            the new first type
     * @param <B2>            the new second type
     * @return a new {@code Tuple2} with transformed values
     * @throws NullPointerException if any function is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B1, B2> Tuple2<B1, B2> mapAll(final @NonNull Function<? super A1, ? extends B1> transformation1,
                                                   final @NonNull Function<? super A2, ? extends B2> transformation2) {
        Objects.requireNonNull(transformation1, nullValue("fMap1"));
        Objects.requireNonNull(transformation2, nullValue("fMap2"));
        return new Tuple2<>(
            () -> Objects.requireNonNull(transformation1.apply(this.get1()), nullResult()),
            () -> Objects.requireNonNull(transformation2.apply(this.get2()), nullResult())
        );
    }

    /**
     * <div>
     *   <p>
     *     Applies functions carried by the given {@link Higher2} to this {@code Tuple2} component-wise.
     *   </p>
     * </div>
     *
     * @param transformation the lifted functions as {@link Higher2}
     * @param <B1> type of the new first value
     * @param <B2> type of the new second value
     * @return a new {@code Tuple2} with transformed values
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B1, B2> Tuple2<B1, B2> applyTo(
        final @NonNull Higher2<µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>> transformation
    ) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        final var fixed
            = narrow(transformation);
        return new Tuple2<>(
            () -> Objects.requireNonNull(fixed.value1Supplier.get().apply(this.value1Supplier.get()), nullResult()),
            () -> Objects.requireNonNull(fixed.value2Supplier.get().apply(this.value2Supplier.get()), nullResult())
        );
    }

    /**
     * <div>
     *   <p>
     *     Swaps the positions of the two values in this {@code Tuple2}.
     *   </p>
     * </div>
     *
     * @return a new {@code Tuple2} with swapped values
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Tuple2<A2, A1> swap() {
        return new Tuple2<>(this.value2Supplier, this.value1Supplier);
    }

    /**
     * <div>
     *   <p>
     *     Creates a copy of this {@code Tuple2}.
     *   </p>
     * </div>
     *
     * @return a new instance of {@code Tuple2} with the same values
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Tuple2<A1, A2> copy() {
        return new Tuple2<>(this.value1Supplier, this.value2Supplier);
    }

    /**
     * <div>
     *   <p>
     *     Creates a {@link Record2} that contains the stored values of this {@code Tuple2}.
     *     Any supplier-based values are evaluated during extraction.
     *   </p>
     * </div>
     *
     * @return a {@link Record2} with the extracted values
     * @see Record2
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public Record2<A1, A2> toRecord() {
        return Record2.record2(this.get1(), this.get2());
    }

    /**
     * <div>
     *   <p>
     *     Converts this tuple to a {@link MutablePair} by evaluating both component suppliers.
     *   </p>
     *   <p>
     *     Semantics:
     *     <ul>
     *       <li>Evaluation: calls {@link #get1()} and {@link #get2()} to obtain the current values.</li>
     *       <li>Mutability: the returned {@code MutablePair} is a separate, mutable container.</li>
     *       <li>Nullability: this tuple holds non-null values by contract; {@code MutablePair} can represent nulls,
     *           but this method will populate it with non-null values.</li>
     *     </ul>
     *   </p>
     * </div>
     *
     * @return a {@link MutablePair} containing the evaluated values of this tuple
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public MutablePair<A1, A2> toPair() {
        return mutablePair(this.get1(), this.get2());
    }

    /**
     * <div>
     *   <p>
     *     Creates a new {@code Tuple2} where only the second value remains stored as a {@code Supplier},
     *     while the first value is already evaluated.
     *   </p>
     * </div>
     *
     * @return a {@link Tuple2} with the first value realized and the second still deferred
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull Tuple2<A1, A2> unwind1() {
        return new Tuple2<>(this.get1(), this.value2Supplier);
    }

    /**
     * <div>
     *   <p>
     *     Creates a new {@code Tuple2} where only the first value remains stored as a {@code Supplier},
     *     while the second value is already evaluated.
     *   </p>
     * </div>
     *
     * @return a {@link Tuple2} with the second value realized and the first still deferred
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull Tuple2<A1, A2> unwind2() {
        return new Tuple2<>(this.value1Supplier, this.get2());
    }

    /**
     * <div>
     *   <p>
     *     Realizes the stored operations by evaluating the {@code Supplier}-backed values of this {@code Tuple2}.
     *   </p>
     * </div>
     *
     * @return a {@code Tuple2} with realized values
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull Tuple2<A1, A2> unwind() {
        return tuple2(this.value1Supplier.get(), this.value2Supplier.get());
    }

    /**
     * <div>
     *   <p>
     *     Compares this {@code Tuple2} for equality with another object.
     *   </p>
     * </div>
     *
     * @param other the object to compare with
     * @return {@code true} if the other object is equal; {@code false} otherwise
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) other;
        return Objects.equals(this.value1Supplier.get(), tuple2.value1Supplier.get())
                && Objects.equals(value2Supplier.get(), tuple2.value2Supplier.get());
    }

    /**
     * <div>
     *   <p>
     *     Computes the hash code for this {@code Tuple2}.
     *   </p>
     * </div>
     *
     * @return the hash code of this {@code Tuple2}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public int hashCode() {
        int result = Objects.hashCode(value1Supplier.get());
        result = 31 * result + Objects.hashCode(value2Supplier.get());
        return result;
    }

    /**
     * <div>
     *   <p>
     *     Returns a string representation of this {@code Tuple2}.
     *   </p>
     * </div>
     *
     * @return the string representation of this {@code Tuple2}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public String toString() {
        return new StringJoiner(", ", Tuple2.class.getSimpleName() + "[", "]")
                .add("value1=" + this.value1Supplier.get())
                .add("value2=" + this.value2Supplier.get())
                .toString();
    }
    
}
