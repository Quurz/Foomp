package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Eager;
import org.quurz.foomp.base.types.Liftable3;
import org.quurz.foomp.base.types.Mappable3;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.Value3;
import org.quurz.foomp.higher.Higher3;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Tuple3.tuple3;

/**
 * <div>
 *     <p>
 *         Repr&auml;sentiert ein Record mit drei Werten, das unver&auml;nderlich ist.
 *         Diese Klasse kann verwendet werden, um drei miteinander verbundene Werte
 *         zusammen zu speichern und zu verarbeiten.
 *     </p>
 * </div>
 *
 * @param <A1> der Typ des ersten Wertes
 * @param <A2> der Typ des zweiten Wertes
 * @param <A3> der Typ des dritten Wertes
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public record Record3<A1, A2, A3>(A1 value1,
                                  A2 value2,
                                  A3 value3)
        implements Liftable3<Record3.µ, A1, A2, A3>,
                   Mappable3<Record3.µ, A1, A2, A3>,
                   Transmogrifyable<Record3<A1, A2, A3>>,
                   Copyable<Record3<A1, A2, A3>>,
                   Value3<A1, A2, A3>,
                   Higher3<Record3.µ, A1, A2, A3> {

    /**
     * <div>
     *     <p>
     *         Der Witness-Typ f&uuml;r {@code Record3}.
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
     *         Wandelt ein {@code Higher3}-Objekt in ein {@code Record3} um.
     *     </p>
     *     <p>
     *         &Uuml;berpr&uuml;ft, ob das &uuml;bergebene {@code Higher3} nicht {@code null} ist und gibt es als
     *         {@code Record3} zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param wide das zu konvertierende {@code Higher3}; darf nicht {@code null} sein
     * @param <A1> der Typ des ersten Wertes
     * @param <A2> der Typ des zweiten Wertes
     * @param <A3> der Typ des dritten Wertes
     * @return das umgewandelte {@code Record3}
     * @throws NullPointerException falls {@code wide} {@code null} ist
     *
     * @since 1.0.0
     */
    public static <A1, A2, A3> Record3<A1, A2, A3> narrow(final @NonNull Higher3<Record3.µ, A1, A2, A3> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        return (Record3<A1, A2, A3>) wide;
    }

    /**
     * <div>
     *     <p>
     *         Erstellt ein neues {@code Record3}-Tupel mit den angegebenen Werten.
     *     </p>
     *     <p>
     *         Diese Methode ist eine bequeme Abk&uuml;rzung f&uuml;r den Konstruktor.
     *     </p>
     * </div>
     *
     * @param value1 der erste Wert; darf nicht {@code null} sein
     * @param value2 der zweite Wert; darf nicht {@code null} sein
     * @param value3 der dritte Wert; darf nicht {@code null} sein
     * @param <A1> der Typ des ersten Wertes
     * @param <A2> der Typ des zweiten Wertes
     * @param <A3> der Typ des dritten Wertes
     * @return ein neues {@code Record3}-Objekt mit den angegebenen Werten
     * @throws NullPointerException falls einer der Werte {@code null} ist
     *
     * @since 1.0.0
     */
    public static <A1, A2, A3> Record3<A1, A2, A3> record3(final @NonNull A1 value1,
                                                           final @NonNull A2 value2,
                                                           final @NonNull A3 value3) {
        Objects.requireNonNull(value1, nullValue("value1"));
        Objects.requireNonNull(value2, nullValue("value2"));
        Objects.requireNonNull(value3, nullValue("value3"));
        return new Record3<>(value1, value2, value3);
    }

    /**
     * <div>
     *     <p>
     *         Konstruktor, der dieses {@code Record3} mit den angegebenen Werten initialisiert.
     *     </p>
     * </div>
     *
     * @param value1 der erste Wert; darf nicht {@code null} sein
     * @param value2 der zweite Wert; darf nicht {@code null} sein
     * @param value3 der dritte Wert; darf nicht {@code null} sein
     * @throws NullPointerException falls einer der Werte {@code null} ist
     *
     * @since 1.0.0
     */
    public Record3(final @NonNull A1 value1,
                   final @NonNull A2 value2,
                   final @NonNull A3 value3) {
        this.value1 = Objects.requireNonNull(value1, nullValue("value1"));
        this.value2 = Objects.requireNonNull(value2, nullValue("value2"));
        this.value3 = Objects.requireNonNull(value3, nullValue("value3"));
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
     *         Gibt den ersten Wert dieses Tupels zur&uuml;ck.
     *     </p>
     *     <p>
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
        return this.get1();
    }

    /**
     * <div>
     *     <p>
     *         Gibt den ersten Wert dieses Tupels zur&uuml;ck.
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
     *         Gibt den zweiten Wert dieses Tupels zur&uuml;ck.
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
     *         Gibt den dritten Wert dieses Tupels zur&uuml;ck.
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
     *         Erstellt eine neue Instanz dieses Tupels mit einem ge&auml;nderten ersten Wert.
     *     </p>
     * </div>
     *
     * @param value der neue erste Wert; darf nicht {@code null} sein
     * @param <B1> der Typ des neuen ersten Wertes
     * @return ein neues {@code Record3}-Tupel mit dem ge&auml;nderten ersten Wert und den unver&auml;nderten zweiten und dritten Werten
     * @throws NullPointerException falls {@code value} {@code null} ist
     *
     * @since 1.0.0
     */
    public <B1> @NonNull Record3<B1, A2, A3> with1(final @NonNull B1 value) {
        Objects.requireNonNull(value, nullValue("value"));
        return record3(value, this.value2, this.value3);
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue Instanz dieses Tupels mit einem ge&auml;nderten zweiten Wert.
     *     </p>
     * </div>
     *
     * @param value der neue zweite Wert; darf nicht {@code null} sein
     * @param <B2> der Typ des neuen zweiten Wertes
     * @return ein neues {@code Record3}-Tupel mit dem ge&auml;nderten zweiten Wert und den unver&auml;nderten ersten und dritten Werten
     * @throws NullPointerException falls {@code value} {@code null} ist
     *
     * @since 1.0.0
     */

    public <B2> @NonNull Record3<A1, B2, A3> with2(final @NonNull B2 value) {
        Objects.requireNonNull(value, nullValue("value"));
        return record3(this.value1, value, this.value3);
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue Instanz dieses Tupels mit einem ge&auml;nderten dritten Wert.
     *     </p>
     * </div>
     *
     * @param value der neue dritte Wert; darf nicht {@code null} sein
     * @param <B3> der Typ des neuen dritten Wertes
     * @return ein neues {@code Record3}-Tupel mit dem ge&auml;nderten dritten Wert und den unver&auml;nderten ersten und zweiten Werten
     * @throws NullPointerException falls {@code value} {@code null} ist
     *
     * @since 1.0.0
     */
    public <B3> @NonNull Record3<A1, A2, B3> with3(final @NonNull B3 value) {
        Objects.requireNonNull(value, nullValue("value"));
        return record3(this.value1, this.value2, value);
    }

    /**
     * <div>
     *     <p>
     *         Wendet die angegebene Abbildungsfunktion auf den ersten Wert dieses Tupels an und gibt ein neues Tupel
     *         mit dem transformierten ersten Wert sowie den unver&auml;nderten zweiten und dritten Werten zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation die Abbildungsfunktion f&uuml;r den ersten Wert
     * @param <B1> der Typ des neuen ersten Wertes
     * @return ein neues {@code Record3}-Tupel mit dem transformierten ersten Wert
     * @throws NullPointerException falls {@code fMap} oder das Ergebnis der Abbildungsfunktion {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public @NonNull <B1> Record3<B1, A2, A3> map1(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.mapAll(transformation, Function.identity(), Function.identity());
    }

    /**
     * <div>
     *     <p>
     *         Wendet die angegebene Abbildungsfunktion auf den zweiten Wert dieses Tupels an und gibt ein neues Tupel
     *         mit dem transformierten zweiten Wert sowie den unver&auml;nderten ersten und dritten Werten zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation die Abbildungsfunktion f&uuml;r den zweiten Wert
     * @param <B2> der Typ des neuen zweiten Wertes
     * @return ein neues {@code Record3}-Tupel mit dem transformierten zweiten Wert
     * @throws NullPointerException falls {@code fMap} oder das Ergebnis der Abbildungsfunktion {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public @NonNull <B2> Record3<A1, B2, A3> map2(final @NonNull Function<? super A2, ? extends B2> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.mapAll(Function.identity(), transformation, Function.identity());
    }

    /**
     * <div>
     *     <p>
     *         Wendet die angegebene Abbildungsfunktion auf den dritten Wert dieses Tupels an und gibt ein neues Tupel
     *         mit dem transformierten dritten Wert sowie den unver&auml;nderten ersten und zweiten Werten zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation die Abbildungsfunktion f&uuml;r den dritten Wert
     * @param <B3> der Typ des neuen dritten Wertes
     * @return ein neues {@code Record3}-Tupel mit dem transformierten dritten Wert
     * @throws NullPointerException falls {@code fMap} oder das Ergebnis der Abbildungsfunktion {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public @NonNull <B3> Record3<A1, A2, B3> map3(final @NonNull Function<? super A3, ? extends B3> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.mapAll(Function.identity(), Function.identity(), transformation);
    }

    /**
     * <div>
     *     <p>
     *         Wendet die angegebenen Abbildungsfunktionen auf alle Werte dieses Tupels an und gibt ein neues Tupel
     *         mit den transformierten Werten zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation1 die Abbildungsfunktion f&uuml;r den ersten Wert
     * @param transformation2 die Abbildungsfunktion f&uuml;r den zweiten Wert
     * @param transformation3 die Abbildungsfunktion f&uuml;r den dritten Wert
     * @param <B1> der Typ des neuen ersten Wertes
     * @param <B2> der Typ des neuen zweiten Wertes
     * @param <B3> der Typ des neuen dritten Wertes
     * @return ein neues {@code Record3}-Tupel mit den transformierten Werten
     * @throws NullPointerException falls eine der Abbildungsfunktionen oder deren Ergebnisse {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public <B1, B2, B3> Record3<B1, B2, B3> mapAll(final @NonNull Function<? super A1, ? extends B1> transformation1,
                                                   final @NonNull Function<? super A2, ? extends B2> transformation2,
                                                   final @NonNull Function<? super A3, ? extends B3> transformation3) {
        Objects.requireNonNull(transformation1, nullValue("fMap1"));
        Objects.requireNonNull(transformation2, nullValue("fMap2"));
        Objects.requireNonNull(transformation3, nullValue("fMap3"));
        return record3(
            Objects.requireNonNull(transformation1.apply(this.value1), nullResultFrom("fMap1")),
            Objects.requireNonNull(transformation2.apply(this.value2), nullResultFrom("fMap2")),
            Objects.requireNonNull(transformation3.apply(this.value3), nullResultFrom("fMap3"))
        );
    }

    /**
     * <div>
     *     <p>
     *         Hebt eine Funktion in den Kontext dieses Tupels an und wendet sie auf die jeweiligen Werte an.
     *     </p>
     *     <p>
     *         Es wird eine h&ouml;here Struktur &uuml;bergeben, die Funktionen f&uuml;r jeden der drei Werte enth&auml;lt.
     *         Anschlie&szlig;end werden diese Funktionen extrahiert und auf die Werte angewendet.
     *     </p>
     * </div>
     *
     * @param transformation eine h&ouml;here Struktur, die Abbildungsfunktionen f&uuml;r jeden Wert enth&auml;lt
     * @param <B1> der Typ des transformierten ersten Wertes
     * @param <B2> der Typ des transformierten zweiten Wertes
     * @param <B3> der Typ des transformierten dritten Wertes
     * @return ein neues {@code Higher3}-Objekt mit den transformierten Werten
     * @throws NullPointerException falls {@code liftA} oder die enthaltenen Funktionen {@code null} sind
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public @NonNull <B1, B2, B3> Higher3<µ, B1, B2, B3> lift(@NonNull Higher3<µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>, ? extends Function<? super A3, ? extends B3>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        final var narrowedLiftA
            = narrow(transformation);
        return this.mapAll(narrowedLiftA.get1(), narrowedLiftA.get2(), narrowedLiftA.get3());
    }

    /**
     * <div>
     *     <p>
     *         Gibt dieses {@code Record3} als {@code Tuple3} zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return das Tupel als {@code Tuple3}
     *
     * @since 1.0.0
     */
    public Tuple3<A1, A2, A3> toTuple() {
        return tuple3(this.value1, this.value2, this.value3);
    }

    /**
     * <div>
     *     <p>
     *         Wandelt dieses {@code Record3} in einen anderen Typ um, indem die gegebene Transformationsfunktion angewendet wird.
     *     </p>
     *     <p>
     *         Die Transformation erfolgt eager und das Ergebnis darf nicht {@code null} sein.
     *     </p>
     * </div>
     *
     * @param transmogrifier die Funktion, die dieses {@code Record2} in einen anderen Typ umwandelt
     * @param <T>            der Zieltyp der Transformation
     * @return das transformierte Objekt
     * @throws NullPointerException falls {@code transmogrifier} oder das Transformationsergebnis {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    public <T> @NonNull T transmogrify(final @NonNull Function<? super Record3<A1, A2, A3>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResultFrom("transmogrifier"));
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine Kopie dieses {@code Record3}-Datensatzes.
     *     </p>
     *     <p>
     *         Die Kopie enth&auml;lt die gleichen Werte wie das Original und ist strukturell identisch.
     *     </p>
     * </div>
     *
     * @return eine Kopie dieses {@code Record2}
     * @since 1.0.0
     */
    @Override
    public @NonNull Record3<A1, A2, A3> copy() {
        return record3(this.value1, this.value2, this.value3);
    }

    /**
     * <div>
     *     <p>
     *         Berechnet den Hashcode f&uuml;r dieses Tupel anhand der enthaltenen Werte.
     *     </p>
     *     <p>
     *         Der Hashcode wird basierend auf {@link Objects#hashCode(Object)} f&uuml;r jeden Wert ermittelt.
     *     </p>
     * </div>
     *
     * @return der Hashcode dieses Tupels
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public int hashCode() {
        int result = Objects.hashCode(value1);
        result = 31 * result + Objects.hashCode(value2);
        result = 31 * result + Objects.hashCode(value3);
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Vergleicht dieses Tupel mit einem anderen Objekt auf Gleichheit.
     *     </p>
     * </div>
     *
     * @param object das zu vergleichende Objekt
     * @return {@code true}, wenn das Objekt ein {@code Record3} mit denselben Werten ist, sonst {@code false}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("DeconstructionCanBeUsed")
    @Override
    @Eager
    public boolean equals(final Object object) {
        if (!(object instanceof Record3<?, ?, ?> record3)) return false;
        return Objects.equals(value1, record3.value1)
                && Objects.equals(value2, record3.value2)
                && Objects.equals(value3, record3.value3);
    }

    /**
     * <div>
     *     <p>
     *         Gibt eine String-Repr&auml;sentation dieses Tupels zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return eine String-Darstellung im Format {@code Record3[value1=..., value2=..., value3=...]}
     *
     * @since 1.0.0
     */
    @Override
    @Eager
    public String toString() {
        return new StringJoiner(", ", Record3.class.getSimpleName() + "[", "]")
                .add("value1=" + value1)
                .add("value2=" + value2)
                .add("value3=" + value3)
                .toString();
    }

}
