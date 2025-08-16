package org.quurz.foomp.automata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun2;
import org.quurz.foomp.base.types.Mutable;
import org.quurz.foomp.base.util.Result;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Eine Moore-Maschine ist ein endlicher Automat, bei dem die Ausgaben ausschließlich vom aktuellen Zustand abhängen –
 *         im Gegensatz zur Mealy-Maschine, bei der die Ausgabe zusätzlich von der Eingabe beeinflusst wird.
 *     </p>
 *     <p>
 *         Dieser Automat verarbeitet Eingaben gemäß einer Übergangsfunktion und erzeugt Ausgaben über eine
 *         Ausgabefunktion, die jedem Zustand ein Ausgabesymbol zuordnet.
 *     </p>
 * </div>
 *
 * @param <S>  Typ der Zustände
 * @param <IA> Typ der Eingabealphabet-Symbole
 * @param <OA> Typ der Ausgabealphabet-Symbole
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@Mutable
public class MooreMachine<S, IA, OA>
        extends AbstractMachine<S, IA, OA> {

    /**
     * <div>
     *     <p>
     *         Erzeugt eine neue Moore-Maschine ohne Endzustände.
     *     </p>
     * </div>
     *
     * @param states             Menge aller Zustände
     * @param startState         Startzustand
     * @param inputAlphabet      Menge der gültigen Eingabesymbole
     * @param outputAlphabet     Menge der gültigen Ausgabesymbole
     * @param transitionFunction Übergangsfunktion: bestimmt den nächsten Zustand anhand des aktuellen Zustands und der Eingabe
     * @param outputFunction     Ausgabefunktion: liefert zu jedem Zustand ein Ausgabesymbol
     * @param <S>                Typ der Zustände
     * @param <IA>               Typ der Eingabealphabet-Symbole
     * @param <OA>               Typ der Ausgabealphabet-Symbole
     * @return eine neue Moore-Maschine
     * @throws NullPointerException wenn eine der übergebenen Funktionen oder Mengen {@code null} ist
     *
     * @since 1.0.0
     */
    public static <S, IA, OA> MooreMachine<S, IA, OA> mooreMachine(final @NonNull Set<S> states,
                                                                   final @NonNull S startState,
                                                                   final @NonNull Set<IA> inputAlphabet,
                                                                   final @NonNull Set<OA> outputAlphabet,
                                                                   final @NonNull Fun2<S, IA, S> transitionFunction,
                                                                   final @NonNull Fun<S, OA> outputFunction) {
        Objects.requireNonNull(outputFunction, nullValue("outputFunction"));

        return new MooreMachine<>(
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
     *         Erzeugt eine neue Moore-Maschine mit definierten Endzuständen.
     *     </p>
     * </div>
     *
     * @param states             Menge aller Zustände
     * @param startState         Startzustand
     * @param inputAlphabet      Menge der gültigen Eingabesymbole
     * @param outputAlphabet     Menge der gültigen Ausgabesymbole
     * @param transitionFunction Übergangsfunktion: bestimmt den nächsten Zustand anhand des aktuellen Zustands und der Eingabe
     * @param outputFunction     Ausgabefunktion: liefert zu jedem Zustand ein Ausgabesymbol
     * @param endStates          Menge der Endzustände
     * @param <S>                Typ der Zustände
     * @param <IA>               Typ der Eingabealphabet-Symbole
     * @param <OA>               Typ der Ausgabealphabet-Symbole
     * @return eine neue Moore-Maschine
     * @throws NullPointerException wenn eine der übergebenen Funktionen oder Mengen {@code null} ist
     *
     * @since 1.0.0
     */
    public static <S, IA, OA> MooreMachine<S, IA, OA> mooreMachine(final @NonNull Set<S> states,
                                                                   final @NonNull S startState,
                                                                   final @NonNull Set<IA> inputAlphabet,
                                                                   final @NonNull Set<OA> outputAlphabet,
                                                                   final @NonNull Fun2<S, IA, S> transitionFunction,
                                                                   final @NonNull Fun<S, OA> outputFunction,
                                                                   final @NonNull Set<S> endStates) {
        Objects.requireNonNull(outputFunction, nullValue("outputFunction"));
        Objects.requireNonNull(endStates, nullValue("endStates"));

        return new MooreMachine<>(
                states,
                startState,
                inputAlphabet,
                outputAlphabet,
                transitionFunction,
                outputFunction,
                endStates
        );
    }

    private final Fun<S, OA> outputFunction;

    private MooreMachine(final Set<S> states,
                         final S startState,
                         final Set<IA> inputAlphabet,
                         final Set<OA> outputAlphabet,
                         final Fun2<S, IA, S> transitionFunction,
                         final Fun<S, OA> outputFunction,
                         final Set<S> endStates) {
        super(states, startState, inputAlphabet, outputAlphabet, transitionFunction, endStates);
        this.outputFunction
            = outputFunction;
    }

    /**
     * <div>
     *     <p>
     *         Verarbeitet eine Eingabe und liefert das Ausgabesymbol des resultierenden Zustands.
     *     </p>
     *     <p>
     *         Anders als bei einer Mealy-Maschine hängt die Ausgabe bei einer Moore-Maschine nicht von der Eingabe ab,
     *         sondern ausschließlich vom Zustand, in den überführt wurde.
     *     </p>
     * </div>
     *
     * @param input Eingabesymbol
     * @return das Ausgabesymbol des neuen Zustands
     * @throws NullPointerException     falls {@code input} {@code null} ist
     * @throws IllegalArgumentException falls das Eingabesymbol nicht im Eingabealphabet enthalten ist
     * @throws IllegalStateException    falls der neue Zustand ungültig ist oder kein gültiges Ausgabesymbol liefert
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
