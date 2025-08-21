package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

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
 *     <p>
 *         Contract:
 *     </p>
 *     <ul>
 *         <li>This method must never throw; failures are returned as {@code Left(Exception)}.</li>
 *         <li>The returned {@link XorValue} must not be {@code null}.</li>
 *         <li>Implementations should document whether evaluation is lazy/eager and whether calls are idempotent.</li>
 *     </ul>
 * </div>
 *
 * @param <A> the expected result type on success
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Triable<A> {

    /**
     * <div>
     *     <p>
     *         Executes the computation and returns either the exception (failure) or the computed result (success).
     *         This method must not throw.
     *     </p>
     * </div>
     *
     * @return a non-null {@link XorValue} containing either {@code Left(Exception)} on failure
     *         or {@code Right(A)} on success
     *
     * @since 1.0.0
     */
    @NonNull
    XorValue<Exception, A> tryIt();

}
