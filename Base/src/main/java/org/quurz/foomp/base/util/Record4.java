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
 *         Repr&auml;sentiert ein Record mit vier Werten, das unver&auml;nderlich ist.
 *         Diese Klasse eignet sich, um vier zusammengeh&ouml;rige Werte kompakt zu speichern und zu verarbeiten.
 *     </p>
 * </div>
 *
 * @param <A1> der Typ des ersten Wertes
 * @param <A2> der Typ des zweiten Wertes
 * @param <A3> der Typ des dritten Wertes
 * @param <A4> der Typ des vierten Wertes
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
     *         Der Witness-Typ f&uuml;r {@code Record4}.
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
     *         Wandelt ein {@code Higher4}-Objekt in ein {@code Record4} um.
     *     </p>
     *     <p>
     *         Es wird &uuml;berpr&uuml;ft, ob das &uuml;bergebene Objekt nicht {@code null} ist.
     *     </p>
     * </div>
     *
     * @param wide das zu konvertierende {@code Higher4}-Objekt; darf nicht {@code null} sein
     * @param <A1> der Typ des ersten Wertes
     * @param <A2> der Typ des zweiten Wertes
     * @param <A3> der Typ des dritten Wertes
     * @param <A4> der Typ des vierten Wertes
     * @return das umgewandelte {@code Record4}
     * @throws NullPointerException falls {@code wide} {@code null} ist
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
     *         Erstellt ein Record mit vier Werten.
     *     </p>
     * </div>
     *
     * @param value1 der erste Wert des Records; darf nicht {@code null} sein
     * @param value2 der zweite Wert des Records; darf nicht {@code null} sein
     * @param value3 der dritte Wert des Records; darf nicht {@code null} sein
     * @param value4 der vierte Wert des Records; darf nicht {@code null} sein
     * @param <A1> der Typ des ersten Wertes
     * @param <A2> der Typ des zweiten Wertes
     * @param <A3> der Typ des dritten Wertes
     * @param <A4> der Typ des vierten Wertes
     * @return ein neues {@code Record4}-Objekt, das die angegebenen Werte enth&auml;lt
     * @throws NullPointerException falls einer der Werte {@code null} ist
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
     *         Konstruktor, der dieses {@code Record4} mit den angegebenen Werten initialisiert.
     *     </p>
     * </div>
     *
     * @param value1 der erste Wert; darf nicht {@code null} sein
     * @param value2 der zweite Wert; darf nicht {@code null} sein
     * @param value3 der dritte Wert; darf nicht {@code null} sein
     * @param value4 der vierte Wert; darf nicht {@code null} sein
     * @throws NullPointerException falls einer der Werte {@code null} ist
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
     *         Gibt an, ob der erste Wert pr&auml;sent ist.
     *     </p>
     * </div>
     *
     * @return immer {@code true}
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
     *         Gibt an, ob der erste Wert pr&auml;sent ist.
     *     </p>
     * </div>
     *
     * @return immer {@code true}
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
     *         Gibt an, ob der zweite Wert pr&auml;sent ist.
     *     </p>
     * </div>
     *
     * @return immer {@code true}
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
     *         Gibt an, ob der dritte Wert pr&auml;sent ist.
     *     </p>
     * </div>
     *
     * @return immer {@code true}
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
     *         Gibt an, ob der vierte Wert pr&auml;sent ist.
     *     </p>
     * </div>
     *
     * @return immer {@code true}
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
     *         Gibt den ersten Wert dieses Records zur&uuml;ck.
     *         Entspricht dem Aufruf von {@code get1()}.
     *     </p>
     * </div>
     *
     * @return der erste Wert, niemals {@code null}
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
     *         Gibt den ersten Wert dieses Records zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return der erste Wert, niemals {@code null}
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
     *         Gibt den zweiten Wert dieses Records zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return der zweite Wert, niemals {@code null}
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
     *         Gibt den dritten Wert dieses Records zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return der dritte Wert, niemals {@code null}
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
     *         Gibt den vierten Wert dieses Records zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return der dritte Wert, niemals {@code null}
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
     *         Erstellt eine neue Instanz dieses Records mit einem ge&auml;nderten ersten Wert.
     *     </p>
     * </div>
     *
     * @param value der neue erste Wert; darf nicht {@code null} sein
     * @param <B1> der Typ des neuen ersten Wertes
     * @return ein neues {@code Record4}-Tupel mit dem ge&auml;nderten ersten Wert und den unver&auml;nderten Werten der anderen Elemente
     * @throws NullPointerException falls {@code value} {@code null} ist
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
     *         Erstellt eine neue Instanz dieses Records mit einem ge&auml;nderten zweiten Wert.
     *     </p>
     * </div>
     *
     * @param value der neue zweite Wert; darf nicht {@code null} sein
     * @param <B2> der Typ des neuen zweiten Wertes
     * @return ein neues {@code Record4}-Tupel mit dem ge&auml;nderten zweiten Wert und den unver&auml;nderten Werten der anderen Elemente
     * @throws NullPointerException falls {@code value} {@code null} ist
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
     *         Erstellt eine neue Instanz dieses Records mit einem ge&auml;nderten dritten Wert.
     *     </p>
     * </div>
     *
     * @param value der neue dritte Wert; darf nicht {@code null} sein
     * @param <B3> der Typ des neuen dritten Wertes
     * @return ein neues {@code Record4}-Tupel mit dem ge&auml;nderten dritten Wert und den unver&auml;nderten Werten der anderen Elemente
     * @throws NullPointerException falls {@code value} {@code null} ist
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
     *         Erstellt eine neue Instanz dieses Records mit einem ge&auml;nderten vierten Wert.
     *     </p>
     * </div>
     *
     * @param value der neue vierte Wert; darf nicht {@code null} sein
     * @param <B4> der Typ des neuen vierten Wertes
     * @return ein neues {@code Record4}-Tupel mit dem ge&auml;nderten vierten Wert und den unver&auml;nderten Werten der anderen Elemente
     * @throws NullPointerException falls {@code value} {@code null} ist
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
     *         Wendet die angegebene Abbildungsfunktion auf den ersten Wert dieses Records an und gibt ein neues Tupel
     *         mit dem transformierten ersten Wert zur&uuml;ck. Die anderen Werte bleiben unver&auml;ndert.
     *     </p>
     * </div>
     *
     * @param transformation die Abbildungsfunktion f&uuml;r den ersten Wert
     * @param <B1> der Typ des neuen ersten Wertes
     * @return ein neues {@code Record4}-Tupel mit dem transformierten ersten Wert
     * @throws NullPointerException falls {@code fMap} oder das Ergebnis von {@code fMap} {@code null} ist
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
     *         Wendet die angegebene Abbildungsfunktion auf den zweiten Wert dieses Records an und gibt ein neues Tupel
     *         mit dem transformierten zweiten Wert zur&uuml;ck. Die anderen Werte bleiben unver&auml;ndert.
     *     </p>
     * </div>
     *
     * @param transformation die Abbildungsfunktion f&uuml;r den zweiten Wert
     * @param <B2> der Typ des neuen zweiten Wertes
     * @return ein neues {@code Record4}-Tupel mit dem transformierten zweiten Wert
     * @throws NullPointerException falls {@code fMap} oder das Ergebnis von {@code fMap} {@code null} ist
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
     *         Wendet die angegebene Abbildungsfunktion auf den dritten Wert dieses Records an und gibt ein neues Tupel
     *         mit dem transformierten dritten Wert zur&uuml;ck. Die anderen Werte bleiben unver&auml;ndert.
     *     </p>
     * </div>
     *
     * @param transformation die Abbildungsfunktion f&uuml;r den dritten Wert
     * @param <B3> der Typ des neuen dritten Wertes
     * @return ein neues {@code Record4}-Tupel mit dem transformierten dritten Wert
     * @throws NullPointerException falls {@code fMap} oder das Ergebnis von {@code fMap} {@code null} ist
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
     *         Wendet die angegebene Abbildungsfunktion auf den vierten Wert dieses Records an und gibt ein neues Tupel
     *         mit dem transformierten vierten Wert zur&uuml;ck. Die anderen Werte bleiben unver&auml;ndert.
     *     </p>
     * </div>
     *
     * @param transformation die Abbildungsfunktion f&uuml;r den vierten Wert
     * @param <B4> der Typ des neuen vierten Wertes
     * @return ein neues {@code Record4}-Tupel mit dem transformierten vierten Wert
     * @throws NullPointerException falls {@code fMap} oder das Ergebnis von {@code fMap} {@code null} ist
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
     *         Wendet die angegebenen Abbildungsfunktionen auf alle Werte dieses Records an und gibt ein neues Tupel
     *         mit den transformierten Werten zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation1 die Abbildungsfunktion f&uuml;r den ersten Wert
     * @param transformation2 die Abbildungsfunktion f&uuml;r den zweiten Wert
     * @param transformation3 die Abbildungsfunktion f&uuml;r den dritten Wert
     * @param transformation4 die Abbildungsfunktion f&uuml;r den vierten Wert
     * @param <B1> der Typ des neuen ersten Wertes
     * @param <B2> der Typ des neuen zweiten Wertes
     * @param <B3> der Typ des neuen dritten Wertes
     * @param <B4> der Typ des neuen vierten Wertes
     * @return ein neues {@code Record4}-Tupel mit den transformierten Werten
     * @throws NullPointerException falls eine der Abbildungsfunktionen oder deren Ergebnisse {@code null} sind
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
     *         Hebt eine Funktion in den Kontext dieses Records an und wendet sie auf die jeweiligen Werte an.
     *     </p>
     *     <p>
     *         Es wird eine h&ouml;here Struktur &uuml;bergeben, die Abbildungsfunktionen f&uuml;r jeden der vier Werte enth&auml;lt.
     *         Anschlie&szlig;end werden diese Funktionen extrahiert und auf die entsprechenden Werte angewendet.
     *     </p>
     * </div>
     *
     * @param transformation eine h&ouml;here Struktur, die Abbildungsfunktionen f&uuml;r jeden Wert enth&auml;lt
     * @param <B1> der Typ des transformierten ersten Wertes
     * @param <B2> der Typ des transformierten zweiten Wertes
     * @param <B3> der Typ des transformierten dritten Wertes
     * @param <B4> der Typ des transformierten vierten Wertes
     * @return ein neues {@code Higher4}-Objekt mit den transformierten Werten
     * @throws NullPointerException falls {@code liftA} oder die enthaltenen Funktionen {@code null} sind
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
     *         Gibt dieses Record als {@code Tuple4} zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return das Tupel als {@code Tuple4}
     *
     * @since 1.0.0
     */
    public @NonNull Tuple4<A1, A2, A3, A4> toTuple() {
        return tuple4(this.value1, this.value2, this.value3, this.value4);
    }

    /**
     * <div>
     *     <p>
     *         Wandelt dieses {@code Record4} in einen anderen Typ um, indem die gegebene Transformationsfunktion angewendet wird.
     *     </p>
     *     <p>
     *         Die Transformation erfolgt eager und das Ergebnis darf nicht {@code null} sein.
     *     </p>
     * </div>
     *
     * @param transmogrifier die Funktion, die dieses {@code Record4} in einen anderen Typ umwandelt
     * @param <T>            der Zieltyp der Transformation
     * @return das transformierte Objekt
     * @throws NullPointerException falls {@code transmogrifier} oder das Transformationsergebnis {@code null} ist
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
     *         Erstellt eine Kopie dieses {@code Record4}-Tupels.
     *     </p>
     *     <p>
     *         Die Kopie enth&auml;lt die gleichen Werte wie das Original und ist strukturell identisch.
     *     </p>
     * </div>
     *
     * @return eine Kopie dieses {@code Record4}
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
     *         Berechnet den Hashcode dieses Records anhand der enthaltenen Werte.
     *     </p>
     *     <p>
     *         Der Hashcode wird f&uuml;r jeden der vier Werte unter Verwendung von {@link Objects#hashCode(Object)} ermittelt.
     *     </p>
     * </div>
     *
     * @return der Hashcode dieses Tupels
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
     *         Vergleicht dieses Record mit einem anderen Objekt auf Gleichheit.
     *     </p>
     * </div>
     *
     * @param object das zu vergleichende Objekt
     * @return {@code true}, wenn das Objekt ein {@code Record4} mit denselben Werten ist, sonst {@code false}
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
     *         Gibt eine String-Repr&auml;sentation dieses Tupels zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return eine String-Darstellung im Format {@code Record4[value1=..., value2=..., value3=..., value4=...]}
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
