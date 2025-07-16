package org.quurz.foomp.automata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun2;
import org.quurz.foomp.base.types.Mutable;
import org.quurz.foomp.base.util.Util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.quurz.foomp.automata.localisation.AutomataMessages.emptyInputAlphabet;
import static org.quurz.foomp.automata.localisation.AutomataMessages.emptyOutputAlphabet;
import static org.quurz.foomp.automata.localisation.AutomataMessages.emptyStateSet;
import static org.quurz.foomp.automata.localisation.AutomataMessages.endStatesNotATrueSubsetOfStates;
import static org.quurz.foomp.automata.localisation.AutomataMessages.unknownInputToken;
import static org.quurz.foomp.automata.localisation.AutomataMessages.unknownState;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Abstrakte Basisklasse für deterministische endliche Automaten mit Ein- und Ausgabe.
 *         Diese Klasse implementiert allgemeine Mechanismen wie Zustandsverwaltung und Gültigkeitsprüfungen.
 *     </p>
 *     <p>
 *         Konkrete Automatenmodelle wie Moore- oder Mealy-Automaten müssen die Methode {@link #read(Object)} selbst implementieren.
 *     </p>
 * </div>
 *
 * @param <S>  der Typ der Zustände
 * @param <IA> der Typ der Eingaben (Input Alphabet)
 * @param <OA> der Typ der Ausgaben (Output Alphabet)
 */
@Mutable
public abstract class AbstractMachine<S, IA, OA>
        implements FiniteStateMachine<S, IA, OA> {

    /**
     * Menge aller gültigen Zustände.
     */
    protected final Set<S> states;

    /**
     * Aktueller Zustand des Automaten.
     */
    protected S currentState;

    /**
     * Gültiges Eingabealphabet.
     */
    protected final Set<IA> inputAlphabet;

    /**
     * Gültiges Ausgabealphabet.
     */
    protected final Set<OA> outputAlphabet;

    /**
     * Übergangsfunktion: erhält aktuellen Zustand und Eingabe, liefert den Folgezustand.
     */
    protected final Fun2<S, IA, S> transitionFunction;

    /**
     * Menge der akzeptierenden Endzustände.
     */
    protected final Set<S> endStates;

    protected final Set<MachineListener<S, IA, OA>> listeners;

    /**
     * <div>
     *     <p>
     *         Erzeugt einen neuen endlichen Automaten.
     *     </p>
     * </div>
     *
     * @param states             Menge aller Zustände (darf nicht leer sein)
     * @param startState         Startzustand
     * @param inputAlphabet      Eingabealphabet (darf nicht leer sein)
     * @param outputAlphabet     Ausgabealphabet (darf nicht leer sein)
     * @param transitionFunction Übergangsfunktion
     * @param endStates          Menge der akzeptierenden Endzustände (echte Teilmenge von {@code states})
     * @throws NullPointerException     wenn ein Parameter {@code null} ist
     * @throws IllegalArgumentException wenn eine der Mengen leer oder {@code endStates} keine echte Teilmenge ist
     */
    protected AbstractMachine(final @NonNull Set<S> states,
                              final @NonNull S startState,
                              final @NonNull Set<IA> inputAlphabet,
                              final @NonNull Set<OA> outputAlphabet,
                              final @NonNull Fun2<S, IA, S> transitionFunction,
                              final @NonNull Set<S> endStates) {
        Objects.requireNonNull(states, nullValue("states"));
        this.states
            = Util.requiresNonEmpty(states, emptyStateSet());

        this.currentState
            = Objects.requireNonNull(startState, nullValue("startState"));

        Objects.requireNonNull(inputAlphabet, nullValue("inputAlphabet"));
        this.inputAlphabet
            = Util.requiresNonEmpty(inputAlphabet, emptyInputAlphabet());

        Objects.requireNonNull(outputAlphabet, nullValue("outputAlphabet"));
        this.outputAlphabet
            = Util.requiresNonEmpty(outputAlphabet, emptyOutputAlphabet());

        this.transitionFunction
            = Objects.requireNonNull(transitionFunction, nullValue("transitionFunction"));

        Objects.requireNonNull(endStates, nullValue("endStates"));
        this.endStates
            = Util.mustBeSubSet(this.states, endStates, endStatesNotATrueSubsetOfStates());

        this.listeners
            = new HashSet<>();
    }

    /**
     * <div>
     *     <p>
     *         Verarbeitet die gegebene Eingabe und liefert den neuen Zustand.
     *     </p>
     * </div>
     *
     * @param input Eingabezeichen
     * @return der neue Zustand nach der Transition
     * @throws NullPointerException     falls {@code input} null ist
     * @throws IllegalArgumentException falls das Eingabezeichen nicht zum Alphabet gehört
     * @throws IllegalStateException    falls die Übergangsfunktion einen ungültigen Zustand liefert
     */
    protected S processInput(final @NonNull IA input) {
        Objects.requireNonNull(input, nullValue("input"));
        if (this.inputAlphabet.contains(input)) {
            final var newState
                = Objects.requireNonNull(this.transitionFunction.apply(this.currentState, input), nullResult());
            if (this.states.contains(newState)) {
                return newState;
            } else {
                throw new IllegalStateException(unknownState(newState));
            }
        } else {
            throw new IllegalArgumentException(unknownInputToken(input));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public S getCurrentState() {
        return this.currentState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentState(final @NonNull S newCurrentState) {
        Objects.requireNonNull(newCurrentState, nullValue("newCurrentState"));
        if (this.states.contains(newCurrentState)) {
            this.currentState
                = newCurrentState;
        } else {
            throw new IllegalArgumentException(unknownState(newCurrentState));
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInFinalState() {
        return this.endStates.contains(this.currentState);
    }

    public void addListener(final @NonNull MachineListener<S, IA, OA> listener) {
        Objects.requireNonNull(listener, nullValue("listener"));
        this.listeners.add(listener);
    }

    public void removeListener(final @NonNull MachineListener<S, IA, OA> listener) {
        Objects.requireNonNull(listener, nullValue("listener"));
        this.listeners.remove(listener);
    }

    protected void fireStateTransitionEvent(final MachineEvent.@NonNull MachineStateTransitionEvent<S, IA, OA> event) {
        Objects.requireNonNull(event, nullValue("event"));
        for (final var listener : this.listeners) {
            listener.stateTransitionEvent(event);
        }
    }

    protected void fireErrorEvent(final MachineEvent.@NonNull MachineErrorEvent<S, IA, OA> event) {
        Objects.requireNonNull(event, nullValue("event"));
        for (final var listener : this.listeners) {
            listener.errorEvent(event);
        }
    }

}
