package org.quurz.foomp.base.types;

/**
 * <div>
 *     <p>
 *         Repräsentiert eine Berechnung, die entweder erfolgreich einen Wert vom Typ {@code A} liefert
 *         oder mit einer {@link Exception} fehlschlägt.
 *     </p>
 *     <p>
 *         Das {@code Triable}-Interface dient als funktionale Schnittstelle für lazy oder wiederholbare
 *         Berechnungen, deren Ausgang ungewiss ist. Es lässt sich etwa zur Modellierung fehlertoleranter
 *         oder ausnahmebehafteter Prozesse einsetzen – ähnlich wie ein Supplier, jedoch mit expliziter
 *         Fehlerbehandlung über {@link XorValue}.
 *     </p>
 * </div>
 *
 * @param <A> der Typ des erwarteten Ergebnisses bei erfolgreicher Ausführung
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Triable<A> {

    /**
     * <div>
     *     <p>
     *         Führt die Berechnung aus und liefert entweder das Ergebnis oder einen Fehler.
     *     </p>
     * </div>
     *
     * @return ein {@link XorValue}, das entweder eine {@link Exception} (linke Seite)
     *         oder einen Wert vom Typ {@code A} (rechte Seite) enthält
     *
     * @since 1.0.0
     */
    XorValue<Exception, A> tryIt();

}
