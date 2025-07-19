package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.quurz.foomp.base.util.Tuple2;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Tuple2.tuple2;
import static org.quurz.foomp.base.util.Util.requiresNonNullElements;

/**
 * <div>
 *     <p>
 *         Ein typisierter Argument-Wrapper, der sowohl einen Wert als auch dessen expliziten Typ kapselt.
 *     </p>
 *     <p>
 *         Diese Klasse eignet sich besonders für reflektierende Konstruktionen, bei denen {@code null}-Werte
 *         übergeben werden müssen und die Typinformation zur Laufzeit erhalten bleiben soll.
 *     </p>
 *     <p>
 *         Beispiel:
 *         <pre>
 *             {@code Argument<String> arg = Argument.argument(null, String.class);}
 *         </pre>
 *     </p>
 * </div>
 *
 * @param <A> Der zur Compile-Zeit abgeleitete Typ des Argumentwerts
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class Argument<A> {

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue {@link Argument}-Instanz.
     *     </p>
     * </div>
     *
     * @param type  der explizite, nicht-nullbare Typ des Arguments
     * @param value der Argumentwert, darf {@code null} sein
     * @param <A>   der generische Typ des Werts
     * @return eine neue {@link Argument}-Instanz mit Wert und Typ
     * @throws NullPointerException wenn {@code type} {@code null} ist
     *
     * @since 1.0.0
     */
    public static <A> Argument<A> argument(final @NonNull Class<? super A> type,
                                           final @Nullable A value) {
        Objects.requireNonNull(type, nullValue("type"));
        return new Argument<>(value, type);
    }

    /**
     * <div>
     *     <p>
     *         Extrahiert die Typen und Werte aus einem Array von {@link Argument}-Objekten.
     *     </p>
     *     <p>
     *         Das Ergebnis ist ein {@link Tuple2}, bei dem das erste Element ein Array der Typen
     *         und das zweite Element ein Array der entsprechenden Werte ist.
     *     </p>
     *     <p>
     *         Diese Methode eignet sich zur Vorbereitung reflektiver Konstruktoraufrufe.
     *     </p>
     * </div>
     *
     * @param arguments ein nicht-nullbares Array von {@code Argument}-Objekten; darf keine {@code null}-Elemente enthalten
     * @return ein Tupel mit zwei Arrays: einem der Typen und einem der Werte
     * @throws NullPointerException wenn {@code arguments} {@code null} ist
     * @throws IllegalArgumentException wenn ein Element des Arrays {@code null} ist
     *
     * @since 1.0.0
     */
    public static Tuple2<Class<?>[], Object[]> extractTypesAndValues(final @NonNull Argument<?>[] arguments) {
        Objects.requireNonNull(arguments, nullValue("arguments"));
        requiresNonNullElements(arguments,"arguments", IllegalArgumentException::new);

        final var types
            = Arrays.stream(arguments)
                .map(Argument::getType)
                .toArray(Class<?>[]::new);
        final var values
            = Arrays.stream(arguments)
                .map(Argument::getValue)
                .toArray(Object[]::new);

        return tuple2(types, values);
    }

    private final A value;
    private final Class<? super A> type;

    private Argument(final A value,
                     final Class<? super A> type) {
        this.value
            = value;
        this.type
            = type;
    }

    /**
     * <div>
     *     <p>
     *         Gibt den Argumentwert zurück. Dieser kann {@code null} sein.
     *     </p>
     * </div>
     *
     * @return der gespeicherte Wert
     *
     * @since 1.0.0
     */
    @Nullable
    public A getValue() {
        return this.value;
    }

    /**
     * <div>
     *     <p>
     *         Gibt den explizit angegebenen Typ des Arguments zurück.
     *     </p>
     * </div>
     *
     * @return der nicht-nullbare Typ des Arguments
     *
     * @since 1.0.0
     */
    @NonNull
    public Class<? super A> getType() {
        return this.type;
    }

    /**
     * <div>
     *     <p>
     *         Vergleicht dieses {@code Argument} mit einem anderen Objekt auf Gleichheit.
     *     </p>
     *     <p>
     *         Zwei {@code Argument}-Instanzen gelten als gleich, wenn sowohl ihr Typ als auch ihr Wert gleich sind.
     *     </p>
     * </div>
     *
     * @param o das zu vergleichende Objekt
     * @return {@code true}, wenn das angegebene Objekt ein gleiches {@code Argument} ist; sonst {@code false}
     *
     * @since 1.0.0
     */
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Argument<?> argument)) return false;

        return Objects.equals(value, argument.value) && type.equals(argument.type);
    }

    /**
     * <div>
     *     <p>
     *         Gibt einen Hashcode für dieses {@code Argument} zurück.
     *     </p>
     *     <p>
     *         Der Hashcode basiert auf dem Typ und dem Wert des Arguments.
     *     </p>
     * </div>
     *
     * @return der berechnete Hashcode
     *
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        int result = Objects.hashCode(value);
        result = 31 * result + type.hashCode();
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Gibt eine String-Repräsentation dieses {@code Argument} zurück.
     *     </p>
     *     <p>
     *         Das Format ist {@code Argument[type=..., value=...]}.
     *     </p>
     * </div>
     *
     * @return eine lesbare Beschreibung dieses {@code Argument}-Objekts
     *
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Argument.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("value=" + value)
                .toString();
    }


}
