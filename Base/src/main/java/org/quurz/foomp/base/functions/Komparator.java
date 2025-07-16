package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Comparator;
import java.util.Objects;

import static java.lang.Integer.signum;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Ein funktionales Interface zur Definition von Vergleichslogik zwischen zwei Objekten eines bestimmten Typs.
 *         Es bietet eine klar definierte {@link Komparison}-Enum, die Ergebnisse von Vergleichen auf die Zust&auml;nde
 *         "LESS", "EQUAL" und "GREATER" beschr&auml;nkt.
 *     </p>
 * </div>
 *
 * @param <A> der Typ der Objekte, die verglichen werden
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Komparator<A>
        extends Fun2<A, A, Komparator.Komparison> {

    /**
     * <div>
     *     <p>
     *         Ein Enum, das die m&ouml;glichen Vergleichsergebnisse beschreibt.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    enum Komparison {
        /**
         * <div>
         *     <p>
         *         Das erste Objekt ist kleiner als das zweite Objekt.
         *     </p>
         * </div>
         */
        LESS,

        /**
         * <div>
         *     <p>
         *         Das erste Objekt ist gleich dem zweiten Objekt.
         *     </p>
         * </div>
         */
        EQUAL,

        /**
         * <div>
         *     <p>
         *         Das erste Objekt ist gr&ouml;&szlig;er als das zweite Objekt.
         *     </p>
         * </div>
         */
        GREATER
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt einen {@code Komparator} basierend auf einem Standard-{@link Comparator}.
     *         Diese Methode konvertiert die Ergebnisse von {@code compare}-Aufrufen in die Zust&auml;nde
     *         der {@link Komparison}-Enum, um die Verwendung in erweiterten Switch-Anweisungen zu erm&ouml;glichen.
     *     </p>
     * </div>
     *
     * @param <A>        der Typ der Objekte, die verglichen werden
     * @param comparator ein Standard-{@link Comparator}, der nicht null sein darf
     *
     * @return einen {@code Komparator}, der die Logik des gegebenen {@code comparator} verwendet
     *
     * @throws NullPointerException wenn der {@code comparator} null ist
     *
     * @since 1.0.0
     */
    static <A> Komparator<A> komparator(final @NonNull Comparator<A> comparator) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        return (first, second) -> {
            Objects.requireNonNull(first, nullValue("first"));
            Objects.requireNonNull(second, nullValue("second"));
            return switch (signum(comparator.compare(first, second))) {
                case -1 -> Komparison.LESS;
                case 1 -> Komparison.GREATER;
                default -> Komparison.EQUAL;
            };
        };
    }

    /**
     * <div>
     *     <p>
     *         Vergleicht zwei Objekte und liefert das Ergebnis als einen {@link Komparison}-Wert.
     *     </p>
     * </div>
     *
     * @param first  das erste zu vergleichende Objekt, darf nicht null sein
     * @param second das zweite zu vergleichende Objekt, darf nicht null sein
     * @return das Ergebnis des Vergleichs als {@link Komparison}
     *
     * @throws NullPointerException wenn eines der Objekte null ist
     *
     * @since 1.0.0
     */
    @NonNull
    Komparison kompare(final @NonNull A first,
                       final @NonNull A second);

    /**
     * <div>
     *     <p>
     *         Diese Methode vergleicht zwei Objekte und liefert das Ergebnis als einen {@link Komparison}-Wert.
     *         Sie stellt sicher, dass keine der Eingaben null ist.
     *     </p>
     * </div>
     *
     * @see #kompare(Object, Object)
     * @see Fun2#apply(Object, Object)
     *
     * @param first  das erste zu vergleichende Objekt, darf nicht null sein
     * @param second das zweite zu vergleichende Objekt, darf nicht null sein
     *
     * @return das Ergebnis des Vergleichs als {@link Komparison}
     *
     * @throws NullPointerException wenn eines der Objekte null ist
     *
     * @since 1.0.0
     */
    @Override
    default Komparator.@NonNull Komparison apply(final @NonNull A first,
                                                 final @NonNull A second) {
        Objects.requireNonNull(first, nullValue("first"));
        Objects.requireNonNull(second, nullValue("second"));
        return kompare(first, second);
    }

}
