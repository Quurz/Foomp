package org.quurz.foomp.automata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun2;
import org.quurz.foomp.base.types.Mutable;
import org.quurz.foomp.base.util.Result;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Repräsentiert eine Mealy-Maschine, einen endlichen Automaten, der auf Eingaben reagiert,
 *         in einen neuen Zustand übergeht und dabei eine Ausgabe erzeugt.
 *         Die Ausgabe wird dabei nicht nur vom aktuellen Zustand,
 *         sondern auch von der aktuellen Eingabe bestimmt.
 *     </p>
 * </div>
 *
 * @param <S> Der Typ der Zustände der Maschine
 * @param <IA> Der Typ der Eingabesymbole
 * @param <OA> Der Typ der Ausgabesymbole
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@Mutable
public class MealyMachine<S, IA, OA>
        extends AbstractMachine<S, IA, OA> {

    /**
     * <div>
     *     <p>
     *         Erzeugt eine neue Mealy-Maschine ohne Endzustände.
     *     </p>
     * </div>
     *
     * @param states Die Menge aller Zustände der Maschine
     * @param startState Der Startzustand
     * @param inputAlphabet Die Menge der Eingabesymbole
     * @param outputAlphabet Die Menge der Ausgabesymbole
     * @param transitionFunction Funktion, die den Folgezustand anhand von Zustand und Eingabe bestimmt
     * @param outputFunction Funktion, die die Ausgabe anhand von Zustand und Eingabe bestimmt
     * @param <S> Der Typ der Zustände
     * @param <IA> Der Typ der Eingaben
     * @param <OA> Der Typ der Ausgaben
     * @return Eine neue Instanz der Mealy-Maschine
     *
     * @throws NullPointerException falls einer der Parameter {@code null} ist
     * @throws IllegalArgumentException falls eine der Mengen leer ist oder andere Vorbedingungen verletzt sind
     *
     * @since 1.0.0
     */
    public static <S, IA, OA> MealyMachine<S, IA, OA> mealyMachine(final @NonNull Set<S> states,
                                                                   final @NonNull S startState,
                                                                   final @NonNull Set<IA> inputAlphabet,
                                                                   final @NonNull Set<OA> outputAlphabet,
                                                                   final @NonNull Fun2<S, IA, S> transitionFunction,
                                                                   final @NonNull Fun2<S, IA, OA> outputFunction) {
        Objects.requireNonNull(outputFunction, nullValue("outputFunction"));

        return new MealyMachine<>(
            states,
            startState,
            inputAlphabet,
            outputAlphabet,
            transitionFunction,
            outputFunction,
            Collections.emptySet()
        );
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt eine neue Mealy-Maschine mit angegebenen Endzuständen.
     *     </p>
     * </div>
     *
     * @param states Die Menge aller Zustände der Maschine
     * @param startState Der Startzustand
     * @param inputAlphabet Die Menge der Eingabesymbole
     * @param outputAlphabet Die Menge der Ausgabesymbole
     * @param transitionFunction Funktion, die den Folgezustand anhand von Zustand und Eingabe bestimmt
     * @param outputFunction Funktion, die die Ausgabe anhand von Zustand und Eingabe bestimmt
     * @param endStates Die Menge der akzeptierenden Endzustände
     * @param <S> Der Typ der Zustände
     * @param <IA> Der Typ der Eingaben
     * @param <OA> Der Typ der Ausgaben
     * @return Eine neue Instanz der Mealy-Maschine
     *
     * @throws NullPointerException falls einer der Parameter {@code null} ist
     * @throws IllegalArgumentException falls eine der Mengen leer ist oder andere Vorbedingungen verletzt sind
     *
     * @since 1.0.0
     */
    public static <S, IA, OA> MealyMachine<S, IA, OA> mealyMachine(final @NonNull Set<S> states,
                                                                   final @NonNull S startState,
                                                                   final @NonNull Set<IA> inputAlphabet,
                                                                   final @NonNull Set<OA> outputAlphabet,
                                                                   final @NonNull Fun2<S, IA, S> transitionFunction,
                                                                   final @NonNull Fun2<S, IA, OA> outputFunction,
                                                                   final @NonNull Set<S> endStates) {
        Objects.requireNonNull(outputFunction, nullValue("outputFunction"));
        Objects.requireNonNull(endStates, nullValue("endStates"));

        return new MealyMachine<>(
            states,
            startState,
            inputAlphabet,
            outputAlphabet,
            transitionFunction,
            outputFunction,
            endStates
        );
    }

    private final Fun2<S, IA, OA> outputFunction;

    private MealyMachine(final Set<S> states,
                         final S startState,
                         final Set<IA> inputAlphabet,
                         final Set<OA> outputAlphabet,
                         final Fun2<S, IA, S> transitionFunction,
                         final Fun2<S, IA, OA> outputFunction,
                         final Set<S> endStates) {
        super(states, startState, inputAlphabet, outputAlphabet, transitionFunction, endStates);
        this.outputFunction
            = outputFunction;
    }

    /**
     * <div>
     *     <p>
     *         Verarbeitet ein Eingabesymbol und liefert das entsprechende Ausgabesymbol.
     *         Die Mealy-Maschine wechselt dabei in den Folgezustand, der von der Übergangsfunktion bestimmt wird.
     *     </p>
     *     <p>
     *         Diese Methode verändert den internen Zustand der Maschine.
     *     </p>
     * </div>
     *
     * @param input Das Eingabesymbol
     * @return Das entsprechende Ausgabesymbol
     * @throws NullPointerException     falls {@code input} {@code null} ist
     * @throws IllegalArgumentException falls das Eingabesymbol nicht im Eingabealphabet enthalten ist
     * @throws IllegalStateException    falls die Ausgabe ungültig oder außerhalb des Ausgabealphabets ist
     * @since 1.0.0
     */
    @Override
    public @NonNull Result<OA> read(final @NonNull IA input) {
        Objects.requireNonNull(input, nullValue("input"));
        final var newState
            = super.processInput(input);
        return null;    // TODO
    }

}
