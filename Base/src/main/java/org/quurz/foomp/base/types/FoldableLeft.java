package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiFunction;

/**
 * <div>
 *     <p>
 *         Definiert eine linksassoziative Faltung &uuml;ber eine Datenstruktur.
 *     </p>
 *     <p>
 *         Die Faltung beginnt mit einem initialen Wert und wendet eine Funktion schrittweise von links nach rechts an.
 *     </p>
 * </div>
 *
 * @param <A> Typ der Elemente, die gefaltet werden
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface FoldableLeft<A> {

    /**
     * <div>
     *     <p>
     *         Faltet die Elemente von links nach rechts unter Verwendung eines initialen Werts und einer bin&auml;ren Funktion.
     *     </p>
     *     <p>
     *         Die Faltung beginnt mit {@code init} und wendet die Funktion iterativ auf jedes Element an.
     *     </p>
     * </div>
     *
     * @param init     Der initiale Wert der Faltung
     * @param function Die Faltungsfunktion, die das vorherige Zwischenergebnis und das aktuelle Element verarbeitet
     * @return Das akkumulierte Ergebnis der Faltung
     * @param <B>      Typ des akkumulierten Ergebnisses
     *
     * @since 1.0.0
     */
    <B> @NonNull B foldLeft(final @NonNull B init,
                            final @NonNull BiFunction<? super B, ? super A, ? extends B> function);

}
