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
 *         Ein record-basiertes Tupel mit zwei Werten.
 *     </p>
 * </div>
 *
 * @param <A1> der Typ des ersten Wertes
 * @param <A2> der Typ des zweiten Wertes
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
     *         Der Witness-Typ f&uuml;r {@code Record2}.
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
     *         Wandelt ein {@code Higher2} in ein {@code Record2} um.
     *     </p>
     *     <p>
     *         &Uuml;berpr&uuml;ft, ob das &uuml;bergebene {@code Higher2} eine Instanz von {@code Record2} ist
     *         und gibt es dann zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param wide das zu konvertierende {@code Higher2}; darf nicht {@code null} sein
     * @param <A1> der Typ des ersten Wertes
     * @param <A2> der Typ des zweiten Wertes
     * @return das umgewandelte {@code Record2}
     * @throws NullPointerException wenn {@code wide} {@code null} ist
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
     *         Erstellt eine neue Instanz von {@code Record2} mit den gegebenen Werten.
     *     </p>
     *     <p>
     *         Diese Methode ist eine bequeme Abk&uuml;rzung f&uuml;r den Konstruktor.
     *     </p>
     * </div>
     *
     * @param value1 der erste Wert; darf nicht {@code null} sein
     * @param value2 der zweite Wert; darf nicht {@code null} sein
     * @param <A1> der Typ des ersten Wertes
     * @param <A2> der Typ des zweiten Wertes
     * @return ein neues {@code Record2}-Objekt mit den angegebenen Werten
     * @throws NullPointerException wenn einer der Werte {@code null} ist
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
     *         Erstellt eine neue Instanz von {@code Record2} mit den gegebenen Werten.
     *     </p>
     * </div>
     *
     * @param value1 der erste Wert; darf nicht {@code null} sein
     * @param value2 der zweite Wert; darf nicht {@code null} sein
     * @throws NullPointerException wenn einer der Werte {@code null} ist
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
     *         Gibt an, ob der erste Wert pr&auml;sent ist.
     *     </p>
     * </div>
     *
     * @return immer {@code true}, da das Tupel stets Werte enth&auml;lt
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
     *         Gibt den ersten Wert des Tupels zur&uuml;ck.
     *     </p>
     *     <p>
     *         Entspricht dem Aufruf von {@code get1()}.
     *     </p>
     * </div>
     *
     * @return Der erste Wert.
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
     *         Gibt den ersten Wert des Tupels zur&uuml;ck.
     *     </p>
     *     <p>
     *         Entspricht dem Aufruf von {@code get1()}.
     *     </p>
     * </div>
     *
     * @return Der erste Wert.
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
     *         Gibt den zweiten Wert dieses Datensatzes zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return der zweite Wert des Datensatzes, niemals {@code null}
     */
    @Override
    public @NonNull A2 get2() {
        return this.value2();
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue Instanz mit einem ge&auml;nderten ersten Wert.
     *     </p>
     * </div>
     *
     * @param value der neue erste Wert; darf nicht {@code null} sein
     * @param <B1> der Typ des neuen ersten Wertes
     * @return ein neues {@code Record2} mit ge&auml;ndertem ersten Wert
     * @throws NullPointerException wenn {@code value} {@code null} ist
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
     *         Erstellt eine neue Instanz mit einem ge&auml;nderten zweiten Wert.
     *     </p>
     * </div>
     *
     * @param value der neue zweite Wert; darf nicht {@code null} sein
     * @param <B2> der Typ des neuen zweiten Wertes
     * @return ein neues {@code Record2} mit ge&auml;ndertem zweiten Wert
     * @throws NullPointerException wenn {@code value} {@code null} ist
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
     *         Wendet die gegebene Abbildungsfunktion auf den ersten Wert dieses Datensatzes an.
     *     </p>
     * </div>
     *
     * @param transformation die Abbildungsfunktion f&uuml;r den ersten Wert
     * @param <B1> der Typ des transformierten ersten Werts
     * @return ein neuer Datensatz mit dem transformierten ersten Wert und unver&auml;ndertem zweiten Wert
     * @throws NullPointerException falls {@code fMap} oder das Ergebnis von {@code fMap} {@code null} ist
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
     *         Alias f&uuml;r {@link #map(Function)}.
     *     </p>
     * </div>
     *
     * @param transformation die Abbildungsfunktion f&uuml;r den ersten Wert
     * @param <B1> der Typ des transformierten ersten Werts
     * @return ein neuer Datensatz mit dem transformierten ersten Wert und unver&auml;ndertem zweiten Wert
     * @throws NullPointerException falls {@code fMap} oder das Ergebnis von {@code fMap} {@code null} ist
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
     *         Wendet die gegebene Abbildungsfunktion auf den zweiten Wert dieses Datensatzes an.
     *     </p>
     * </div>
     *
     * @param transformation die Abbildungsfunktion f&uuml;r den zweiten Wert
     * @param <B2> der Typ des transformierten zweiten Werts
     * @return ein neuer Datensatz mit unver&auml;ndertem ersten Wert und transformiertem zweiten Wert
     * @throws NullPointerException falls {@code fMap} oder das Ergebnis von {@code fMap} {@code null} ist
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
     *         Wendet die gegebenen Abbildungsfunktionen auf beide Werte dieses Datensatzes an.
     *     </p>
     * </div>
     *
     * @param transformation1  die Abbildungsfunktion f&uuml;r den ersten Wert
     * @param transformation2 die Abbildungsfunktion f&uuml;r den zweiten Wert
     * @param <B1> der Typ des transformierten ersten Werts
     * @param <B2> der Typ des transformierten zweiten Werts
     * @return ein neuer Datensatz mit transformierten Werten
     * @throws NullPointerException falls {@code fMapFirst}, {@code fMapSecond} oder deren Ergebnisse {@code null} sind
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
     *         Wendet zwei Funktionen aus einer h&ouml;heren Struktur auf die Werte dieses Datensatzes an.
     *     </p>
     *     <p>
     *         Die Funktionen werden aus der gegebenen {@code Higher2}-Struktur extrahiert und auf die jeweiligen Werte angewendet.
     *     </p>
     * </div>
     *
     * @param transformation eine h&ouml;here Struktur, die je eine Funktion f&uuml;r den ersten und den zweiten Wert enth&auml;lt
     * @param <B1>  der Typ des transformierten ersten Werts
     * @param <B2>  der Typ des transformierten zweiten Werts
     * @return ein neuer Datensatz mit den transformierten Werten
     * @throws NullPointerException falls {@code liftA}, die extrahierten Funktionen oder deren Ergebnisse {@code null} sind
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
     *         Gibt dieses Record als {@code Tuple2} zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return das Tupel als {@code Tuple2}
     *
     * @since 1.0.0
     */
    public Tuple2<A1, A2> toTuple() {
        return tuple2(this.value1, this.value2);
    }

    /**
     * <div>
     *     <p>
     *         Wandelt dieses {@code Record2} in einen anderen Typ um, indem die gegebene Transformationsfunktion angewendet wird.
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
    public <T> @NonNull T transmogrify(@NonNull Function<? super Record2<A1, A2>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResultFrom("transmogrifier"));
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine Kopie dieses {@code Record2}-Datensatzes.
     *     </p>
     *     <p>
     *         Die Kopie enthält die gleichen Werte wie das Original und ist strukturell identisch.
     *     </p>
     * </div>
     *
     * @return eine Kopie dieses {@code Record2}
     * @since 1.0.0
     */
    @Override
    public @NonNull Record2<A1, A2> copy() {
        return record2(this.value1, this.value2);
    }

    /**
     * <div>
     *     <p>
     *         Berechnet den Hashcode f&uuml;r diesen Datensatz anhand der enthaltenen Werte.
     *     </p>
     *     <p>
     *         Der Hashcode wird basierend auf {@link Objects#hashCode(Object)} f&uuml;r beide Werte bestimmt.
     *     </p>
     * </div>
     *
     * @return der Hashcode dieses Datensatzes
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
     *         &Uuml;berpr&uuml;ft, ob dieses Objekt gleich einem anderen ist.
     *     </p>
     *     <p>
     *         Zwei {@code Record2}-Instanzen gelten als gleich, wenn ihre Werte paarweise
     *         mit {@link Objects#equals(Object, Object)} &uuml;bereinstimmen.
     *     </p>
     * </div>
     *
     * @param object das zu vergleichende Objekt
     * @return {@code true}, wenn das Objekt gleich diesem ist, sonst {@code false}
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
     *         Gibt eine String-Darstellung dieses Datensatzes zur&uuml;ck.
     *     </p>
     *     <p>
     *         Das Format entspricht {@code "Record2[value1=..., value2=...]"}.
     *     </p>
     * </div>
     *
     * @return eine String-Repr&auml;sentation dieses Datensatzes
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
