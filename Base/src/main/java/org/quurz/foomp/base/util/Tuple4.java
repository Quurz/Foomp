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
 *         Eine unver&auml;nderliche, funktionale Datenstruktur, die vier Werte speichert.
 *         Diese Klasse unterst&uuml;tzt verschiedene funktionale Operationen wie Abbildungen,
 *         Hebungen, Kopierfunktionen und verz&ouml;gerte Auswertungen.
 *     </p>
 * </div>
 *
 * @param <A1> Der Typ des ersten Werts.
 * @param <A2> Der Typ des zweiten Werts.
 * @param <A3> Der Typ des dritten Werts.
 * @param <A4> Der Typ des vierten Werts.
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
     *         Der Witness-Typ f&uuml;r {@link Tuple4}, um diesen in generischen Kontexten zu unterscheiden.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Konvertiert ein {@link Higher4}-Objekt in eine konkrete Instanz von {@code Tuple4}.
     *     </p>
     * </div>
     *
     * @param unfixed Das {@link Higher4}-Objekt, das fixiert werden soll.
     * @param <A1> Der Typ des ersten Elements.
     * @param <A2> Der Typ des zweiten Elements.
     * @param <A3> Der Typ des dritten Elements.
     * @param <A4> Der Typ des vierten Elements.
     * @return Eine fixierte Instanz von {@code Tuple4}.
     * @throws NullPointerException wenn {@code unfixed} {@code null} ist.
     *
     * @since 1.0.0
     */
    public static <A1, A2, A3, A4> Tuple4<A1, A2, A3, A4> fix(@NonNull final Higher4<Tuple4.µ, A1, A2, A3, A4> unfixed) {
        return (Tuple4<A1, A2, A3, A4>) Objects.requireNonNull(unfixed, nullValue("unfixed"));
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue Instanz von {@link Tuple4} mit den angegebenen Werten.
     *     </p>
     * </div>
     *
     * @param value1 Der erste Wert. Darf nicht {@code null} sein.
     * @param value2 Der zweite Wert. Darf nicht {@code null} sein.
     * @param value3 Der dritte Wert. Darf nicht {@code null} sein.
     * @param value4 Der vierte Wert. Darf nicht {@code null} sein.
     * @param <A1> Der Typ des ersten Elements.
     * @param <A2> Der Typ des zweiten Elements.
     * @param <A3> Der Typ des dritten Elements.
     * @param <A4> Der Typ des vierten Elements.
     * @return Eine neue {@link Tuple4}-Instanz mit den angegebenen Werten.
     * @throws NullPointerException wenn einer der Werte {@code null} ist.
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
     *         Gibt zur&uuml;ck, ob der erste Wert vorhanden ist.
     *     </p>
     *     <p>
     *         Da ein {@code Tuple4} immer vier Werte enth&auml;lt, ist dieser Wert stets vorhanden.
     *     </p>
     * </div>
     *
     * @return {@code true}, da der erste Wert immer vorhanden ist.
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
     * @return {@code true}, da der erste Wert immer vorhanden ist
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
     *         Gibt zur&uuml;ck, ob der zweite Wert vorhanden ist.
     *     </p>
     *     <p>
     *         <p>Da ein {@code Tuple4} immer vier Werte enth&auml;lt, ist dieser Wert stets vorhanden.</p>
     *     </p>
     * </div>
     *
     * @return {@code true}, da der zweite Wert immer vorhanden ist.
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
     *         Gibt zur&uuml;ck, ob der dritte Wert vorhanden ist.
     *     </p>
     *     <p>
     *         Da ein {@code Tuple4} immer vier Werte enth&auml;lt, ist dieser Wert stets vorhanden.
     *     </p>
     * </div>
     *
     * @return {@code true}, da der dritte Wert immer vorhanden ist.
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
     *         Gibt zur&uuml;ck, ob der vierte Wert vorhanden ist.
     *     </p>
     *     <p>
     *         Da ein {@code Tuple4} immer vier Werte enth&auml;lt, ist dieser Wert stets vorhanden.
     *     </p>
     * </div>
     *
     * @return {@code true}, da der vierte Wert immer vorhanden ist.
     */
    @Override
    public boolean is4() {
        return true;
    }

    /**
     * <div>
     *     <p>
     *         Gibt den ersten gespeicherten Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return Der erste Wert.
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
     *         Alias f&uuml;r {@link #get1()}.
     *     </p>
     * </div>
     *
     * @return {@code true}, da der erste Wert immer vorhanden ist
     */
    @UnwindingOperation
    @NonNull
    public A1 get() {
        return this.get1();
    }

    /**
     * <div>
     *     <p>
     *         Gibt den zweiten gespeicherten Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return Der zweite Wert.
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
     *         Gibt den dritten gespeicherten Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return Der dritte Wert.
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
     *         Gibt den vierten gespeicherten Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return Der vierte Wert.
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
     *         Erstellt ein neues {@code Tuple4}, wobei das erste Element durch den angegebenen Wert ersetzt wird.
     *     </p>
     *     <p>
     *         Die &uuml;brigen Werte bleiben unver&auml;ndert.
     *     </p>
     * </div>
     *
     * @param value1 Der neue Wert f&uuml;r das erste Element. Darf nicht {@code null} sein.
     * @return Ein neues {@code Tuple4} mit dem aktualisierten ersten Element.
     * @throws NullPointerException wenn {@code value1} {@code null} ist.
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
     *         Erstellt ein neues {@code Tuple4}, wobei das zweite Element durch den angegebenen Wert ersetzt wird.
     *     </p>
     *     <p>
     *         Die &uuml;brigen Werte bleiben unver&auml;ndert.
     *     </p>
     * </div>
     *
     * @param value2 Der neue Wert f&uuml;r das zweite Element. Darf nicht {@code null} sein.
     * @return Ein neues {@code Tuple4} mit dem aktualisierten zweiten Element.
     * @throws NullPointerException wenn {@code value2} {@code null} ist.
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
     *         Erstellt ein neues {@code Tuple4}, wobei das dritte Element durch den angegebenen Wert ersetzt wird.
     *     </p>
     *     <p>
     *         Die &uuml;brigen Werte bleiben unver&auml;ndert.
     *     </p>
     * </div>
     *
     * @param value3 Der neue Wert f&uuml;r das dritte Element. Darf nicht {@code null} sein.
     * @return Ein neues {@code Tuple4} mit dem aktualisierten dritten Element.
     * @throws NullPointerException wenn {@code value3} {@code null} ist.
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
     *         Erstellt ein neues {@code Tuple4}, wobei das vierte Element durch den angegebenen Wert ersetzt wird.
     *     </p>
     *     <p>
     *         Die &uuml;brigen Werte bleiben unver&auml;ndert.
     *     </p>
     * </div>
     *
     * @param value4 Der neue Wert f&uuml;r das vierte Element. Darf nicht {@code null} sein.
     * @return Ein neues {@code Tuple4} mit dem aktualisierten vierten Element.
     * @throws NullPointerException wenn {@code value4} {@code null} ist.
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
     *         Erstellt ein neues {@code Tuple4}, indem die gegebene Abbildungsfunktion auf das erste Element angewendet wird.
     *     </p>
     * </div>
     *
     * @param transformation Die Funktion zur Transformation des ersten Elements. Darf nicht {@code null} sein.
     * @param <B1> Der Zieltyp des transformierten ersten Elements.
     * @return Ein neues {@code Tuple4} mit dem transformierten ersten Element, w&auml;hrend die &uuml;brigen Werte unver&auml;ndert bleiben.
     * @throws NullPointerException wenn {@code fMap} {@code null} ist oder die Anwendung von {@code fMap} {@code null} zur&uuml;ckliefert.
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
     *         Erstellt ein neues {@code Tuple4}, indem die gegebene Abbildungsfunktion auf das zweite Element angewendet wird.
     *     </p>
     * </div>
     *
     * @param transformation Die Funktion zur Transformation des zweiten Elements. Darf nicht {@code null} sein.
     * @param <B2> Der Zieltyp des transformierten zweiten Elements.
     * @return Ein neues {@code Tuple4} mit dem transformierten zweiten Element, w&auml;hrend die &uuml;brigen Werte unver&auml;ndert bleiben.
     * @throws NullPointerException wenn {@code fMap} {@code null} ist oder die Anwendung von {@code fMap} {@code null} zur&uuml;ckliefert.
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
     *         Erstellt ein neues {@code Tuple4}, indem die gegebene Abbildungsfunktion auf das dritte Element angewendet wird.
     *     </p>
     * </div>
     *
     * @param transformation Die Funktion zur Transformation des dritten Elements. Darf nicht {@code null} sein.
     * @param <B3> Der Zieltyp des transformierten dritten Elements.
     * @return Ein neues {@code Tuple4} mit dem transformierten dritten Element, w&auml;hrend die &uuml;brigen Werte unver&auml;ndert bleiben.
     * @throws NullPointerException wenn {@code fMap} {@code null} ist oder die Anwendung von {@code fMap} {@code null} zur&uuml;ckliefert.
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
     *         Erstellt ein neues {@code Tuple4}, indem die gegebene Abbildungsfunktion auf das vierte Element angewendet wird.
     *     </p>
     * </div>
     *
     * @param transformation Die Funktion zur Transformation des vierten Elements. Darf nicht {@code null} sein.
     * @param <B4> Der Zieltyp des transformierten vierten Elements.
     * @return Ein neues {@code Tuple4} mit dem transformierten vierten Element, w&auml;hrend die &uuml;brigen Werte unver&auml;ndert bleiben.
     * @throws NullPointerException wenn {@code fMap} {@code null} ist oder die Anwendung von {@code fMap} {@code null} zur&uuml;ckliefert.
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
     *         Erstellt ein neues {@code Tuple4}, indem die gegebenen Abbildungsfunktionen auf jedes Element angewendet werden.
     *     </p>
     * </div>
     *
     * @param transformation1 Die Funktion zur Transformation des ersten Elements. Darf nicht {@code null} sein.
     * @param transformation2 Die Funktion zur Transformation des zweiten Elements. Darf nicht {@code null} sein.
     * @param transformation3 Die Funktion zur Transformation des dritten Elements. Darf nicht {@code null} sein.
     * @param transformation4 Die Funktion zur Transformation des vierten Elements. Darf nicht {@code null} sein.
     * @param <B1> Der Zieltyp des transformierten ersten Elements.
     * @param <B2> Der Zieltyp des transformierten zweiten Elements.
     * @param <B3> Der Zieltyp des transformierten dritten Elements.
     * @param <B4> Der Zieltyp des transformierten vierten Elements.
     * @return Ein neues {@code Tuple4} mit allen transformierten Elementen.
     * @throws NullPointerException wenn eine der Funktionen {@code null} ist oder die Anwendung einer Funktion {@code null} zur&uuml;ckliefert.
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
     *         Hebt die enthaltenen Funktionen an und wendet sie auf die aktuellen Werte an.
     *     </p>
     * </div>
     *
     * @param transformation Das {@code Higher4}-Objekt, das die Funktionen enth&auml;lt. Darf nicht {@code null} sein.
     * @param <B1> Der Zieltyp des transformierten ersten Elements.
     * @param <B2> Der Zieltyp des transformierten zweiten Elements.
     * @param <B3> Der Zieltyp des transformierten dritten Elements.
     * @param <B4> Der Zieltyp des transformierten vierten Elements.
     * @return Ein neues {@code Tuple4} mit allen transformierten Elementen.
     * @throws NullPointerException wenn {@code liftA} oder eine der darin enthaltenen Funktionen oder deren Anwendung {@code null} ist.
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B1, B2, B3, B4> Tuple4<B1, B2, B3, B4> lift(@NonNull Higher4<µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>, ? extends Function<? super A3, ? extends B3>, ? extends Function<? super A4, ? extends B4>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        final var fixed
            = fix(transformation);
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
     *         Kombiniert die vier Werte dieses {@code Tuple4} mithilfe der angegebenen Funktion.
     *     </p>
     * </div>
     *
     * @param meld Die Funktion, die die vier Werte zu einem einzigen Ergebnis zusammenf&uuml;hrt. Darf nicht {@code null} sein.
     * @param <B> Der R&uuml;ckgabetyp der kombinierten Werte.
     * @return Das Ergebnis der Anwendung von {@code meld} auf die vier Werte.
     * @throws NullPointerException wenn {@code meld} oder die Anwendung der Funktion {@code null} ergibt.
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
     *         Erstellt eine Kopie dieses {@code Tuple4} mit denselben Werten.
     *     </p>
     * </div>
     *
     * @return Eine neue Instanz von {@code Tuple4}, die dieselben Lieferantenreferenzen wie dieses Tupel verwendet.
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
     *         Erstellt ein {@code Record4}-Objekt aus diesem {@code Tuple4}.
     *     </p>
     * </div>
     *
     * @return Ein {@code Record4}, das die aktuellen Werte dieses Tupels enth&auml;lt.
     * @throws NullPointerException wenn einer der gespeicherten Werte {@code null} ist.
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
     *         L&ouml;st das erste Element dieses {@code Tuple4} auf und gibt ein neues {@code Tuple4} zur&uuml;ck,
     *         bei dem das erste Element eager evaluiert wurde.
     *     </p>
     * </div>
     *
     * @return Ein neues {@code Tuple4} mit dem ersten Wert als aufgel&ouml;stem Wert.
     *
     * @since 1.0.0
     */
    public @NonNull Tuple4<A1, A2, A3, A4> unwind1() {
        return new Tuple4<>(this.value1Supplier.get(), this.value2Supplier, this.value3Supplier, this.value4Supplier);
    }

    /**
     * <div>
     *     <p>
     *         L&ouml;st das zweite Element dieses {@code Tuple4} auf und gibt ein neues {@code Tuple4} zur&uuml;ck,
     *         bei dem das zweite Element eager evaluiert wurde.
     *     </p>
     * </div>
     *
     * @return Ein neues {@code Tuple4} mit dem zweiten Wert als aufgel&ouml;stem Wert.
     *
     * @since 1.0.0
     */
    public @NonNull Tuple4<A1, A2, A3, A4> unwind2() {
        return new Tuple4<>(this.value1Supplier, this.value2Supplier.get(), this.value3Supplier, this.value4Supplier);
    }

    /**
     * <div>
     *     <p>
     *         L&ouml;st das dritte Element dieses {@code Tuple4} auf und gibt ein neues {@code Tuple4} zur&uuml;ck,
     *         bei dem das dritte Element eager evaluiert wurde.
     *     </p>
     * </div>
     *
     * @return Ein neues {@code Tuple4} mit dem dritten Wert als aufgel&ouml;stem Wert.
     *
     * @since 1.0.0
     */
    public @NonNull Tuple4<A1, A2, A3, A4> unwind3() {
        return new Tuple4<>(this.value1Supplier, this.value2Supplier, this.value3Supplier.get(), this.value4Supplier);
    }

    /**
     * <div>
     *     <p>
     *         L&ouml;st das vierte Element dieses {@code Tuple4} auf und gibt ein neues {@code Tuple4} zur&uuml;ck,
     *         bei dem das vierte Element eager evaluiert wurde.
     *     </p>
     * </div>
     *
     * @return Ein neues {@code Tuple4} mit dem vierten Wert als aufgel&ouml;stem Wert.
     *
     * @since 1.0.0
     */
    public @NonNull Tuple4<A1, A2, A3, A4> unwind4() {
        return new Tuple4<>(this.value1Supplier, this.value2Supplier, this.value3Supplier, this.value4Supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         L&ouml;st alle Elemente dieses {@code Tuple4} auf und gibt ein neues {@code Tuple4} zur&uuml;ck,
     *         bei dem alle Elemente eager evaluiert wurden.
     *     </p>
     * </div>
     *
     * @return Ein neues {@code Tuple4} mit allen aufgel&ouml;sten Werten.
     * @throws NullPointerException wenn eines der Elemente {@code null} ist.
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
     *         Pr&uuml;ft, ob dieses {@code Tuple4} gleich einem anderen Objekt ist.
     *         Zwei Tupel gelten als gleich, wenn sie vom selben Typ sind und alle vier Werte &uuml;bereinstimmen.
     *         Dabei werden die Werte mittels {@link Objects#equals(Object, Object)} verglichen.
     *     </p>
     * </div>
     *
     * @param other Das Objekt, mit dem dieses Tupel verglichen wird.
     * @return {@code true}, wenn das &uuml;bergebene Objekt ebenfalls ein {@code Tuple4} ist
     *         und alle vier Werte mit denen dieses Tupels &uuml;bereinstimmen, sonst {@code false}.
     * @throws NullPointerException wenn einer der gespeicherten Werte {@code null} ist.
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
     *         Berechnet den Hash-Code dieses {@code Tuple4}.
     *         Der Hash-Code wird basierend auf den vier gespeicherten Werten berechnet.
     *     </p>
     * </div>
     *
     * @return Der berechnete Hash-Code f&uuml;r dieses Tupel.
     * @throws NullPointerException wenn einer der gespeicherten Werte {@code null} ist.
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
     *         Gibt eine String-Darstellung dieses {@code Tuple4} zur&uuml;ck.
     *         Das Format ist {@code "Tuple4{value1, value2, value3, value4}"}.
     *     </p>
     * </div>
     *
     * @return Eine String-Darstellung dieses Tupels mit allen vier Werten.
     * @throws NullPointerException wenn einer der gespeicherten Werte {@code null} ist.
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
