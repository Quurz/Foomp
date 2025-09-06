package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun3;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Liftable3;
import org.quurz.foomp.base.types.Mappable3;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.base.types.Value3;
import org.quurz.foomp.higher.Higher3;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Record3.record3;

/**
 * <div>
 *     <p>
 *         An immutable tuple with three values that supports functional operations such as mapping, copying, and lazy evaluation.
 *         {@code Tuple3} uses {@link Supplier} instances to enable deferred evaluation of its values.
 *     </p>
 * </div>
 *
 * @param <A1> type of the first value
 * @param <A2> type of the second value
 * @param <A3> type of the third value
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public final class Tuple3<A1, A2, A3>
        implements Liftable3<Tuple3.µ, A1, A2, A3>,
                   Mappable3<Tuple3.µ, A1, A2, A3>,
                   Copyable<Tuple3<A1, A2, A3>>,
                   Unwindable<Tuple3<A1, A2, A3>>,
                   Value3<A1, A2, A3>,
                   Higher3<Tuple3.µ, A1, A2, A3> {

    /**
     * <div>
     *     <p>
     *         Marker class for the WitnessType pattern.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Narrows a {@link Higher3} value into a concrete {@code Tuple3} instance.
     *     </p>
     * </div>
     *
     * @param unfixed the higher‑kinded value to narrow
     * @return the narrowed {@code Tuple3} instance
     * @throws NullPointerException if {@code unfixed} is {@code null}
     */
    public static <A1, A2, A3> Tuple3<A1, A2, A3> narrow(@NonNull final Higher3<Tuple3.µ, A1, A2, A3> unfixed) {
        return (Tuple3<A1, A2, A3>) Objects.requireNonNull(unfixed, nullValue("unfixed"));
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code Tuple3} with the given values.
     *     </p>
     * </div>
     *
     * @param value1 the first value
     * @param value2 the second value
     * @param value3 the third value
     * @return a new {@code Tuple3}
     * @throws NullPointerException if any value is {@code null}
     */
    public static <A1, A2, A3> Tuple3<A1, A2, A3> tuple3(final @NonNull A1 value1,
                                                         final @NonNull A2 value2,
                                                         final @NonNull A3 value3) {
        Objects.requireNonNull(value1, nullValue("value1"));
        Objects.requireNonNull(value2, nullValue("value2"));
        Objects.requireNonNull(value3, nullValue("value3"));
        return new Tuple3<>(() -> value1, () -> value2, () -> value3);
    }

    private final Supplier<A1> value1Supplier;
    private final Supplier<A2> value2Supplier;
    private final Supplier<A3> value3Supplier;

    private Tuple3(final Supplier<A1> value1Supplier,
                   final Supplier<A2> value2Supplier,
                   final Supplier<A3> value3Supplier) {
        this.value1Supplier
            = value1Supplier;
        this.value2Supplier
            = value2Supplier;
        this.value3Supplier
            = value3Supplier;
    }

    private Tuple3(final A1 value1,
                   final Supplier<A2> value2Supplier,
                   final Supplier<A3> value3Supplier) {
        this.value1Supplier
            = () -> value1;
        this.value2Supplier
            = value2Supplier;
        this.value3Supplier
            = value3Supplier;
    }

    private Tuple3(final Supplier<A1> value1Supplier,
                   final A2 value2,
                   final Supplier<A3> value3Supplier) {
        this.value1Supplier
            = value1Supplier;
        this.value2Supplier
            = () -> value2;
        this.value3Supplier
            = value3Supplier;
    }

    private Tuple3(final Supplier<A1> value1Supplier,
                   final Supplier<A2> value2Supplier,
                   final A3 value3) {
        this.value1Supplier
            = value1Supplier;
        this.value2Supplier
            = value2Supplier;
        this.value3Supplier
            = () -> value3;
    }

    /**
     * <div>
     *     <p>
     *         Indicates whether the first value is present.
     *     </p>
     * </div>
     *
     * @return {@code true}, since the first value is always present
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
     * @return {@code true}, since the first value is always present
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
     *         Indicates whether the second value is present.
     *     </p>
     * </div>
     *
     * @return {@code true}, since the second value is always present
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
     *         Indicates whether the third value is present.
     *     </p>
     * </div>
     *
     * @return {@code true}, since the third value is always present
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
     *         Returns the first value of the tuple.
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
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @Override
    @NonNull
    public A1 get() {
        return this.get1();
    }

    /**
     * <div>
     *     <p>
     *         Returns the second value of the tuple.
     *     </p>
     * </div>
     *
     * @return the second value
     *
     * @since 1.0.0
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
     *         Returns the third value of the tuple.
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
     *         Creates a new {@code Tuple3} with the first element replaced by the given value.
     *         The remaining elements are preserved and keep their original {@link Supplier}s.
     *     </p>
     * </div>
     *
     * @param value1 the new first value of the tuple
     * @return a new {@code Tuple3} with the updated first element
     * @throws NullPointerException if {@code value1} is {@code null}
     *
     * @since 1.0.0
     */
    public Tuple3<A1, A2, A3> with1(final @NonNull A1 value1) {
        Objects.requireNonNull(value1, nullValue("value1"));
        return new Tuple3<>(value1, this.value2Supplier, this.value3Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code Tuple3} with the second element replaced by the given value.
     *         The remaining elements are preserved and keep their original {@link Supplier}s.
     *     </p>
     * </div>
     *
     * @param value2 the new second value of the tuple
     * @return a new {@code Tuple3} with the updated second element
     * @throws NullPointerException if {@code value2} is {@code null}
     *
     * @since 1.0.0
     */
    public Tuple3<A1, A2, A3> with2(final @NonNull A2 value2) {
        Objects.requireNonNull(value2, nullValue("value2"));
        return new Tuple3<>(this.value1Supplier, value2, this.value3Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code Tuple3} with the third element replaced by the given value.
     *         The remaining elements are preserved and keep their original {@link Supplier}s.
     *     </p>
     * </div>
     *
     * @param value3 the new third value of the tuple
     * @return a new {@code Tuple3} with the updated third element
     * @throws NullPointerException if {@code value3} is {@code null}
     *
     * @since 1.0.0
     */
    public Tuple3<A1, A2, A3> with3(final @NonNull A3 value3) {
        Objects.requireNonNull(value3, nullValue("value3"));
        return new Tuple3<>(this.value1Supplier, this.value2Supplier, value3);
    }

    /**
     * <div>
     *     <p>
     *         Transforms the first value using the given function and returns a new {@code Tuple3}.
     *     </p>
     * </div>
     *
     * @param transformation the transformation function
     * @return a new {@code Tuple3} with the transformed first value
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B1> Tuple3<B1, A2, A3> map1(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Tuple3<>(
            () -> Objects.requireNonNull(transformation.apply(this.value1Supplier.get()), nullResult()),
            this.value2Supplier,
            this.value3Supplier
        );
    }

    /**
     * <div>
     *     <p>
     *         Transforms the second value using the given function and returns a new {@code Tuple3}.
     *     </p>
     * </div>
     *
     * @param transformation the transformation function
     * @return a new {@code Tuple3} with the transformed second value
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B2> Tuple3<A1, B2, A3> map2(final @NonNull Function<? super A2, ? extends B2> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Tuple3<>(
            this.value1Supplier,
            () -> Objects.requireNonNull(transformation.apply(this.value2Supplier.get()), nullResult()),
            this.value3Supplier
        );
    }

    /**
     * <div>
     *     <p>
     *         Transforms the third value using the given function and returns a new {@code Tuple3}.
     *     </p>
     * </div>
     *
     * @param transformation the transformation function
     * @return a new {@code Tuple3} with the transformed third value
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B3> Tuple3<A1, A2, B3> map3(final @NonNull Function<? super A3, ? extends B3> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Tuple3<>(
            this.value1Supplier,
            this.value2Supplier,
            () -> Objects.requireNonNull(transformation.apply(this.value3Supplier.get()), nullResult())
        );
    }

    /**
     * <div>
     *     <p>
     *         Erm&ouml;glicht das Mapping aller drei Werte des Tupels gleichzeitig und gibt ein neues {@code Tuple3} zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation1 Die Funktion zur Transformation des ersten Wertes
     * @param transformation2 Die Funktion zur Transformation des zweiten Wertes
     * @param transformation3 Die Funktion zur Transformation des dritten Wertes
     * @return Ein neues {@code Tuple3} mit allen transformierten
     *
     * @since 1.0.0
     */
    @Override
    public <B1, B2, B3> Tuple3<B1, B2, B3> mapAll(final @NonNull Function<? super A1, ? extends B1> transformation1,
                                                  final @NonNull Function<? super A2, ? extends B2> transformation2,
                                                  final @NonNull Function<? super A3, ? extends B3> transformation3) {
        Objects.requireNonNull(transformation1, nullValue("fMap1"));
        Objects.requireNonNull(transformation2, nullValue("fMap2"));
        Objects.requireNonNull(transformation3, nullValue("fMap3"));
        return new Tuple3<>(
            () -> Objects.requireNonNull(transformation1.apply(this.value1Supplier.get()), nullResult()),
            () -> Objects.requireNonNull(transformation2.apply(this.value2Supplier.get()), nullResult()),
            () -> Objects.requireNonNull(transformation3.apply(this.value3Supplier.get()), nullResult())
        );
    }

    /**
     * <div>
     *     <p>
     *         Hebt eine Funktion, die auf die Werte dieses Tupels angewendet wird, auf und gibt ein neues Tupel zur&uuml;ck.
     *         Die Funktion wird auf die einzelnen Werte dieses Tupels angewendet und zur&uuml;ckgegeben, wobei der Zustand der Werte
     *         nicht ver&auml;ndert wird.
     *     </p>
     * </div>
     *
     * @param transformation Eine {@link Higher3}-Instanz, die eine Transformation auf die Werte dieses Tupels anwendet
     * @param <B1> Der Typ des transformierten ersten Wertes
     * @param <B2> Der Typ des transformierten zweiten Wertes
     * @param <B3> Der Typ des transformierten dritten Wertes
     * @return Ein neues {@code Tuple3} mit den transformierten Werten
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B1, B2, B3> Tuple3<B1, B2, B3> lift(final @NonNull Higher3<µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>, ? extends Function<? super A3, ? extends B3>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        final var fixed
            = narrow(transformation);
        return new Tuple3<>(
            () -> Objects.requireNonNull(fixed.value1Supplier.get().apply(this.value1Supplier.get()), nullResult()),
            () -> Objects.requireNonNull(fixed.value2Supplier.get().apply(this.value2Supplier.get()), nullResult()),
            () -> Objects.requireNonNull(fixed.value3Supplier.get().apply(this.value3Supplier.get()), nullResult())
        );
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine benutzerdefinierte Operation auf den drei Werten des Tupels aus und gibt das Ergebnis zur&uuml;ck.
     *         Diese Methode ist eine Form der Reduktion, bei der die Werte des Tupels zu einem neuen Wert gemeldet werden.
     *     </p>
     * </div>
     *
     * @param meld Eine Funktion, die drei Werte des Tupels zu einem neuen Wert kombiniert
     * @param <B> Der R&uuml;ckgabetyp der Funktion
     * @return Das Ergebnis der Meldung der Werte
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull <B> B meld(final @NonNull Fun3<A1, A2, A3, B> meld) {
        Objects.requireNonNull(meld, nullValue("meld"));
        return Objects.requireNonNull(meld.apply(this.value1Supplier.get(), value2Supplier.get(), value3Supplier.get()), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt eine Kopie des aktuellen Tupels.
     *         Da {@code Tuple3} immutabel ist, wird ein neues Tupel mit denselben Werten erstellt.
     *     </p>
     * </div>
     *
     * @return Eine Kopie des aktuellen {@code Tuple3}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Tuple3<A1, A2, A3> copy() {
        return new Tuple3<>(this.value1Supplier, this.value2Supplier, this.value3Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Wandelt dieses Tupel in ein {@link Record3} um, indem alle verz&ouml;gerten Werte evaluiert werden.
     *     </p>
     * </div>
     *
     * @return Ein {@code Record3} mit den berechneten Werten dieses Tupels.
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public Record3<A1, A2, A3> toRecord() {
        return record3(value1Supplier.get(), value2Supplier.get(), value3Supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         L&ouml;st den ersten Wert des Tupels auf und gibt ein neues {@code Tuple3} zur&uuml;ck,
     *         bei dem nur der erste Wert evaluiert wurde, w&auml;hrend die anderen verz&ouml;gert bleiben.
     *     </p>
     * </div>
     *
     * @return Ein neues {@code Tuple3} mit evaluiertem ersten Wert.
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull Tuple3<A1, A2, A3> unwind1() {
        return new Tuple3<>(this.value1Supplier.get(), this.value2Supplier, this.value3Supplier);
    }

    /**
     * <div>
     *     <p>
     *         L&ouml;st den zweiten Wert des Tupels auf und gibt ein neues {@code Tuple3} zur&uuml;ck,
     *         bei dem nur der zweite Wert evaluiert wurde, w&auml;hrend die anderen verz&ouml;gert bleiben.
     *     </p>
     * </div>
     *
     * @return Ein neues {@code Tuple3} mit evaluiertem zweiten Wert.
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull Tuple3<A1, A2, A3> unwind2() {
        return new Tuple3<>(this.value1Supplier, this.value2Supplier.get(), this.value3Supplier);
    }

    /**
     * <div>
     *     <p>
     *         L&ouml;st den dritten Wert des Tupels auf und gibt ein neues {@code Tuple3} zur&uuml;ck,
     *         bei dem nur der dritte Wert evaluiert wurde, w&auml;hrend die anderen verz&ouml;gert bleiben.
     *     </p>
     * </div>
     *
     * @return Ein neues {@code Tuple3} mit evaluiertem dritten Wert.
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull Tuple3<A1, A2, A3> unwind3() {
        return new Tuple3<>(this.value1Supplier, this.value2Supplier, this.value3Supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         L&ouml;st alle verz&ouml;gerten Werte des Tupels auf und gibt ein neues {@code Tuple3} mit den berechneten Werten zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return Ein neues {@code Tuple3} mit evaluierten Werten.
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull Tuple3<A1, A2, A3> unwind() {
        return tuple3(this.value1Supplier.get(), this.value2Supplier.get(), this.value3Supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         &Uuml;berpr&uuml;ft die Gleichheit von zwei {@code Tuple3}-Instanzen.
     *         Zwei Tupel sind gleich, wenn alle drei Werte gleich sind.
     *     </p>
     * </div>
     *
     * @param other Das andere Objekt, mit dem das aktuelle Tupel verglichen wird
     * @return {@code true}, wenn das andere Objekt ein gleiches Tupel ist, ansonsten {@code false}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) other;
        return Objects.equals(this.value1Supplier.get(), tuple3.value1Supplier.get())
                && Objects.equals(value2Supplier.get(), tuple3.value2Supplier.get())
                && Objects.equals(value3Supplier.get(), tuple3.value3Supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         Berechnet den Hashcode f&uuml;r dieses Tupel basierend auf den drei Werten.
     *     </p>
     * </div>
     *
     * @return Der Hashcode des Tupels
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public int hashCode() {
        int result = Objects.hashCode(value1Supplier.get());
        result = 31 * result + Objects.hashCode(value2Supplier.get());
        result = 31 * result + Objects.hashCode(value3Supplier.get());
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Gibt eine String-Darstellung dieses Tupels zur&uuml;ck, wobei die Werte durch ein Komma getrennt werden.
     *     </p>
     * </div>
     *
     * @return Eine String-Darstellung des Tupels
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public String toString() {
        return new StringJoiner(", ", Tuple3.class.getSimpleName() + "[", "]")
                .add("value1=" + value1Supplier.get())
                .add("value2=" + value2Supplier.get())
                .add("value3=" + value3Supplier.get())
                .toString();
    }
}
