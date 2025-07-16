package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiFunction;

/**
 * <div>
 *     <p>
 *         Definiert eine rechtsassoziative Faltung &uuml;ber eine Datenstruktur.
 *     </p>
 *     <p>
 *         Die Faltung beginnt am rechten Ende der Struktur und wendet eine Funktion schrittweise von rechts nach links an.
 *     </p>
 * </div>
 *
 * @param <A> Typ der Elemente, die gefaltet werden
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface FoldableRight<A> {

    /**
     * <div>
     *     <p>
     *         Faltet die Elemente von rechts nach links unter Verwendung eines initialen Werts und einer bin&auml;ren Funktion.
     *     </p>
     *     <p>
     *         Die Faltung beginnt mit {@code init} und wendet die Funktion iterativ auf jedes Element an, beginnend am rechten Ende.
     *     </p>
     * </div>
     *
     * @param init     Der initiale Wert der Faltung
     * @param function Die Faltungsfunktion, die das aktuelle Element und das vorherige Zwischenergebnis verarbeitet
     * @return Das akkumulierte Ergebnis der Faltung
     * @param <B>      Typ des akkumulierten Ergebnisses
     *
     * @since 1.0.0
     */
    <B> @NonNull B foldRight(final @NonNull B init,
                             final @NonNull BiFunction<? super A, ? super B, ? extends B> function);

}
