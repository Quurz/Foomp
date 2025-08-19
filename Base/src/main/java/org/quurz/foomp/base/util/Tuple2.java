package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Liftable2;
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
 *     <p>
 *         Ein unver&auml;nderliches Tupel aus zwei Werten, das verschiedene Funktionalit&auml;ten bietet,
 *         einschlie&szlig;lich der Abbildung und Manipulation der enthaltenen Werte.
 *         Diese Klasse unterst&uuml;tzt Lazy Evaluation, d.h., die Werte werden nur bei Bedarf abgerufen.
 *     </p>
 * </div>
 *
 * @param <A1> Der Typ des ersten Wertes
 * @param <A2> Der Typ des zweiten Wertes
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public final class Tuple2<A1, A2>
        implements Mappable2<Tuple2.µ, A1, A2>,
                   Liftable2<Tuple2.µ, A1, A2>,
                   Swappable<Tuple2<A2, A1>, A1, A2>,
                   Copyable<Tuple2<A1, A2>>,
                   Unwindable<Tuple2<A1, A2>>,
                   Value2<A1, A2>,
                   Higher2<Tuple2.µ, A1, A2> {

    /**
     * <div>
     *     <p>
     *         Innere Kennzeichenklasse f&uuml;r das WitnessType-Pattern.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Wandelt eine Instanz von {@code Higher2<µ, A1, A2>} in {@code Tuple2<A1, A2>} um.
     *     </p>
     * </div>
     *
     * @param unfixed Das umgewandelte Objekt
     * @return Eine Instanz von {@code Tuple2}
     * @throws NullPointerException Wenn das &uuml;bergebene Objekt {@code null} ist
     *
     * @since 1.0.0
     */
    public static <A1, A2> Tuple2<A1, A2> fix(@NonNull final Higher2<µ, A1, A2> unfixed) {
        return (Tuple2<A1, A2>) Objects.requireNonNull(unfixed, nullValue("unfixed"));
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein neues Tupel aus zwei Werten.
     *     </p>
     * </div>
     *
     * @param value1 Der erste Wert
     * @param value2 Der zweite Wert
     * @return Ein neues Tuple2 mit den angegebenen Werten
     * @throws NullPointerException Wenn einer der Werte {@code null} ist
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
     *     <p>
     *         Gibt an, ob das erste Element vorhanden ist.
     *     </p>
     * </div>
     *
     * @return {@code true}, da das erste Element immer vorhanden ist
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
     *         Alias f&uuml;r {@link #is1()}.
     *     </p>
     * </div>
     *
     * @return {@code true}, da das erste Element immer vorhanden ist
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
     *         Gibt an, ob das zweite Element vorhanden ist.
     *     </p>
     * </div>
     *
     * @return {@code true}, da das zweite Element immer vorhanden ist
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
     *         Gibt das erste Element zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return Der erste Wert im Tupel
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull A1 get1() {
        return this.value1Supplier.get();
    }

    /**
     * <div>
     *     <p>
     *         Gibt das erste Element zur&uuml;ck.
     *         Alias f&uuml;r {@link #get1()}.
     *     </p>
     * </div>
     *
     * @return Der erste Wert im Tupel
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull A1 get() {
        return this.get1();
    }

    /**
     * <div>
     *     <p>
     *         Gibt das zweite Element zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return Der zweite Wert im Tupel
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
     *     <p>
     *         Erzeugt ein neues Tupel mit einem neuen ersten Wert und dem aktuellen zweiten Wert.
     *     </p>
     * </div>
     *
     * @param newFirst Der neue erste Wert
     * @return Ein neues Tuple2 mit dem neuen ersten Wert
     * @throws NullPointerException Wenn der neue erste Wert {@code null} ist
     *
     * @since 1.0.0
     */
    public <B1> Tuple2<B1, A2> with1(final @NonNull B1 newFirst) {
        Objects.requireNonNull(newFirst, nullValue("newFirst"));
        return new Tuple2<>(() -> newFirst, this.value2Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein neues Tupel mit einem neuen zweiten Wert und dem aktuellen ersten Wert.
     *     </p>
     * </div>
     *
     * @param newSecond Der neue zweite Wert
     * @return Ein neues Tuple2 mit dem neuen zweiten Wert
     * @throws NullPointerException Wenn der neue zweite Wert {@code null} ist
     *
     * @since 1.0.0
     */
    public <B2> Tuple2<A1, B2> with2(final @NonNull B2 newSecond) {
        Objects.requireNonNull(newSecond, nullValue("newSecond"));
        return new Tuple2<>(this.value1Supplier, () -> newSecond);
    }

    /**
     * <div>
     *     <p>
     *         Verarbeitet beide Werte im Tupel mit der angegebenen Funktion und gibt das Ergebnis zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param meld Die Funktion zur Verarbeitung beider Werte
     * @param <B>  Der Typ des Ergebnisses der Funktion
     * @return Das Ergebnis der Funktion
     * @throws NullPointerException Wenn {@code meld} {@code null} ist oder das Ergebnis {@code null} ist
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public <B> B meld(final @NonNull BiFunction<? super A1, ? super A2, ? extends B> meld) {
        Objects.requireNonNull(meld, nullValue("meld"));
        return Objects.requireNonNull(meld.apply(this.get1(), this.get2()), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Wendet eine Funktion auf beide Elemente an, um den ersten Wert zu transformieren.
     *     </p>
     * </div>
     *
     * @param mapper Die Funktion, die den ersten Wert unter Ber&uuml;cksichtigung des zweiten Werts transformiert
     * @param <B1>   Der Typ des neuen ersten Werts
     * @return Ein neues {@code Tuple2} mit dem transformierten ersten Wert und dem unver&auml;nderten zweiten Wert
     * @throws NullPointerException Wenn {@code mapper} {@code null} ist oder das Ergebnis {@code null} ist
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
     *     <p>
     *         Wendet eine Funktion auf den zweiten Wert an, um den ersten Wert zu transformieren.
     *     </p>
     * </div>
     *
     * @param mapper Die Funktion, die den zweiten Wert verwendet, um den ersten Wert zu transformieren
     * @param <B1>   Der Typ des neuen ersten Werts
     * @return Ein neues {@code Tuple2} mit dem transformierten ersten Wert und dem unver&auml;nderten zweiten Wert
     * @throws NullPointerException Wenn {@code mapper} {@code null} ist oder das Ergebnis {@code null} ist
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
     *     <p>
     *         Wendet eine Funktion auf beide Elemente an, um den zweiten Wert zu transformieren.
     *     </p>
     * </div>
     *
     * @param mapper Die Funktion, die den zweiten Wert unter Ber&uuml;cksichtigung des ersten Werts transformiert
     * @param <B2>   Der Typ des neuen zweiten Werts
     * @return Ein neues {@code Tuple2} mit dem unver&auml;nderten ersten Wert und dem transformierten zweiten Wert
     * @throws NullPointerException Wenn {@code mapper} {@code null} ist oder das Ergebnis {@code null} ist
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
     *     <p>
     *         Wendet eine Funktion auf den ersten Wert an, um den zweiten Wert zu transformieren.
     *     </p>
     * </div>
     *
     * @param mapper Die Funktion, die den ersten Wert verwendet, um den zweiten Wert zu transformieren
     * @param <B2>   Der Typ des neuen zweiten Werts
     * @return Ein neues {@code Tuple2} mit dem unver&auml;nderten ersten Wert und dem transformierten zweiten Wert
     * @throws NullPointerException Wenn {@code mapper} {@code null} ist oder das Ergebnis {@code null} ist
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
     *     <p>
     *         Alias f&uuml;r {@link #map1(Function)}.
     *     </p>
     * </div>
     *
     * @param transformation Die Funktion zur Transformation des ersten Werts
     * @param <B1> Der Typ des neuen ersten Werts
     * @return Ein neues {@code Tuple2} mit dem unver&auml;nderten zweiten Wert und dem transformierten ersten Wert
     * @throws NullPointerException Wenn {@code fMap} {@code null} ist oder das Ergebnis {@code null} ist
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
     *     <p>
     *         Wendet eine Funktion auf den ersten Wert an und gibt ein neues {@code Tuple2} mit dem transformierten ersten Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation Die Funktion zur Transformation des ersten Werts
     * @param <B1> Der Typ des neuen ersten Werts
     * @return Ein neues {@code Tuple2} mit dem unver&auml;nderten zweiten Wert und dem transformierten ersten Wert
     * @throws NullPointerException Wenn {@code fMap} {@code null} ist oder das Ergebnis {@code null} ist
     *
     * @since 1.0.0
     */
    public <B1> @NonNull Tuple2<B1, A2> map1(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Tuple2<>(() -> Objects.requireNonNull(transformation.apply(this.get1()), nullResult()), this.value2Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Wendet eine Funktion auf den zweiten Wert an und gibt ein neues {@code Tuple2} mit dem transformierten zweiten Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation Die Funktion zur Transformation des zweiten Werts
     * @param <B2> Der Typ des neuen zweiten Werts
     * @return Ein neues {@code Tuple2} mit dem unver&auml;nderten ersten Wert und dem transformierten zweiten Wert
     * @throws NullPointerException Wenn {@code fMap} {@code null} ist oder das Ergebnis {@code null} ist
     *
     * @since 1.0.0
     */
    public <B2> @NonNull Tuple2<A1, B2> map2(final @NonNull Function<? super A2, ? extends B2> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Tuple2<>(this.value1Supplier, () -> Objects.requireNonNull(transformation.apply(this.get2()), nullResult()));
    }

    /**
     * <div>
     *     <p>
     *         Wendet separate Funktionen auf beide Werte an und gibt ein neues {@code Tuple2} mit den transformierten Werten zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation1 Die Funktion zur Transformation des ersten Werts
     * @param transformation2 Die Funktion zur Transformation des zweiten Werts
     * @param <B1>  Der Typ des neuen ersten Werts
     * @param <B2>  Der Typ des neuen zweiten Werts
     * @return Ein neues {@code Tuple2} mit den transformierten Werten
     * @throws NullPointerException Wenn {@code fMap1} oder {@code fMap2} {@code null} ist oder ein Ergebnis {@code null} ist
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
     *     <p>
     *         Wendet die Funktionen von {@link Higher2} auf dieses {@code Tuple2} an.
     *     </p>
     * </div>
     *
     * @param transformation Die zu hebenden Funktionen als {@link Higher2}
     * @param <B1>  Der Typ des neuen ersten Werts
     * @param <B2>  Der Typ des neuen zweiten Werts
     * @return Ein neues {@code Tuple2} mit den transformierten Werten
     * @throws NullPointerException Wenn {@code liftA} {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B1, B2> Tuple2<B1, B2> lift(
        final @NonNull Higher2<µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>> transformation
    ) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        final var fixed
            = fix(transformation);
        return new Tuple2<>(
            () -> Objects.requireNonNull(fixed.value1Supplier.get().apply(this.value1Supplier.get()), nullResult()),
            () -> Objects.requireNonNull(fixed.value2Supplier.get().apply(this.value2Supplier.get()), nullResult())
        );
    }

    /**
     * <div>
     *     <p>
     *         Vertauscht die Positionen der beiden Werte in diesem {@code Tuple2}.
     *     </p>
     * </div>
     *
     * @return Ein neues {@code Tuple2} mit vertauschten Werten
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Tuple2<A2, A1> swap() {
        return new Tuple2<>(this.value2Supplier, this.value1Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine Kopie dieses {@code Tuple2}.
     *     </p>
     * </div>
     *
     * @return Eine neue Instanz von {@code Tuple2} mit denselben Werten
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Tuple2<A1, A2> copy() {
        return new Tuple2<>(this.value1Supplier, this.value2Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Erstellt ein {@link Record2}, das die gespeicherten Werte dieses {@code Tuple2} enth&auml;lt.
     *         Dabei werden eventuell vorhandene {@code Supplier}-basierte Werte direkt abgerufen.
     *     </p>
     * </div>
     *
     * @return Ein {@link Record2} mit den extrahierten Werten
     * @see Record2
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public Record2<A1, A2> toRecord() {
        return Record2.record2(this.get1(), this.get2());
    }

    // TODO: Test & JavaDoc
    @UnwindingOperation
    public MutablePair<A1, A2> toPair() {
        return mutablePair(this.get1(), this.get2());
    }

    /**
     * <div>
     *     <p>
     *         Erstellt ein neues {@code Tuple2}, bei dem nur der zweite Wert weiterhin als {@code Supplier} gespeichert wird,
     *         w&auml;hrend der erste Wert bereits abgerufen wird.
     *     </p>
     * </div>
     *
     * @return Ein {@link Tuple2}, bei dem der erste Wert realisiert und der zweite verz&ouml;gert bleibt
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull Tuple2<A1, A2> unwind1() {
        return new Tuple2<>(this.get1(), this.value2Supplier);
    }

    /**
     * <div>
     *     <p>
     *         Erstellt ein neues {@code Tuple2}, bei dem nur der erste Wert weiterhin als {@code Supplier} gespeichert wird,
     *         w&auml;hrend der zweite Wert bereits abgerufen wird.
     *     </p>
     * </div>
     *
     * @return Ein {@link Tuple2}, bei dem der zweite Wert realisiert und der erste verz&ouml;gert bleibt
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public @NonNull Tuple2<A1, A2> unwind2() {
        return new Tuple2<>(this.value1Supplier, this.get2());
    }

    /**
     * <div>
     *     <p>
     *         Wendet die gespeicherten Operationen in diesem {@code Tuple2} an, indem die {@code Supplier}-Werte abgerufen werden.
     *     </p>
     * </div>
     *
     * @return Ein {@code Tuple2} mit realisierten Werten
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
     *     <p>
     *         Vergleicht dieses {@code Tuple2} mit einem anderen Objekt auf Gleichheit.
     *     </p>
     * </div>
     *
     * @param other Das zu vergleichende Objekt
     * @return {@code true}, wenn das andere Objekt gleich ist; andernfalls {@code false}
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
     *     <p>
     *         Berechnet den Hash-Code f&uuml;r dieses {@code Tuple2}.
     *     </p>
     * </div>
     *
     * @return Der Hash-Code des {@code Tuple2}
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
     *     <p>
     *         Gibt eine String-Darstellung dieses {@code Tuple2} zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return Die String-Darstellung des {@code Tuple2}
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
