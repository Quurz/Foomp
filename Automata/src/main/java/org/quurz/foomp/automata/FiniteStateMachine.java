package org.quurz.foomp.automata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.util.Result;

/**
 * <div>
 *     <p>
 *         Repräsentiert einen generischen endlichen Automaten mit Zuständen, Eingaben und Ausgaben.
 *     </p>
 * </div>
 *
 * @param <S>  der Typ der Zustände
 * @param <IA> der Typ der Eingaben (Input Alphabet)
 * @param <OA> der Typ der Ausgaben (Output Alphabet)
 */
public interface FiniteStateMachine<S, IA, OA> {

    /**
     * <div>
     *     <p>
     *         Führt eine Transition für das gegebene Eingabesymbol aus und liefert die zugehörige Ausgabe.
     *     </p>
     * </div>
     *
     * @param input das Eingabesymbol
     * @return die Ausgabe, die mit der Eingabe verbunden ist
     * @throws NullPointerException     falls {@code input} null ist
     * @throws IllegalArgumentException falls das Eingabesymbol unbekannt ist
     */
    @NonNull
    Result<OA> read(final @NonNull IA input);

    /**
     * <div>
     *     <p>
     *         Gibt den aktuellen Zustand des Automaten zurück.
     *     </p>
     * </div>
     *
     * @return der aktuelle Zustand
     */
    S getCurrentState();

    /**
     * <div>
     *     <p>
     *         Setzt den aktuellen Zustand des Automaten.
     *     </p>
     * </div>
     *
     * @param newCurrentState der neue Zustand
     * @throws NullPointerException     falls {@code newCurrentState} null ist
     * @throws IllegalArgumentException falls der Zustand nicht im Zustandsraum enthalten ist
     */
    void setCurrentState(final @NonNull S newCurrentState);

    /**
     * <div>
     *     <p>
     *         Gibt an, ob sich der Automat aktuell in einem Endzustand befindet.
     *     </p>
     * </div>
     *
     * @return {@code true}, wenn der aktuelle Zustand ein Endzustand ist
     */
    boolean isInFinalState();

}
