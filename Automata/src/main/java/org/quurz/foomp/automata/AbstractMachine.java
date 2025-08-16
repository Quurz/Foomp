package org.quurz.foomp.automata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun2;
import org.quurz.foomp.base.types.Mutable;
import org.quurz.foomp.base.util.Result;
import org.quurz.foomp.base.util.Util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import static org.quurz.foomp.automata.localisation.AutomataMessages.emptyInputAlphabet;
import static org.quurz.foomp.automata.localisation.AutomataMessages.emptyOutputAlphabet;
import static org.quurz.foomp.automata.localisation.AutomataMessages.emptyStateSet;
import static org.quurz.foomp.automata.localisation.AutomataMessages.endStatesNotATrueSubsetOfStates;
import static org.quurz.foomp.automata.localisation.AutomataMessages.unknownInputToken;
import static org.quurz.foomp.automata.localisation.AutomataMessages.unknownState;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Result.failure;

/**
 * <div>
 *     <p>
 *         Abstract base class for deterministic finite automata with input and output.
 *         This class implements general mechanisms such as state management and validity checks.
 *     </p>
 *     <p>
 *         Concrete automata models like Moore or Mealy machines must implement the {@link #read(Object)} method themselves.
 *     </p>
 * </div>
 *
 * @param <S>  the type of states
 * @param <IA> the type of inputs (Input Alphabet)
 * @param <OA> the type of outputs (Output Alphabet)
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@Mutable
public abstract class AbstractMachine<S, IA, OA>
        implements FiniteStateMachine<S, IA, OA> {

    /**
     * <div>
     *     <p>
     *         Set of all valid states.
     *     </p>
     * </div>
     */
    protected final Set<S> states;

    /**
     * <div>
     *     <p>
     *         Current state of the automaton.
     *     </p>
     * </div>
     */
    protected S currentState;

    /**
     * <div>
     *     <p>
     *         Valid input alphabet.
     *     </p>
     * </div>
     */
    protected final Set<IA> inputAlphabet;

    /**
     * <div>
     *     <p>
     *         Valid output alphabet.
     *     </p>
     * </div>
     */
    protected final Set<OA> outputAlphabet;

    /**
     * <div>
     *     <p>
     *         Transition function: takes current state and input, returns next state.
     *     </p>
     * </div>
     */
    protected final Fun2<S, IA, S> transitionFunction;

    /**
     * <div>
     *     <p>
     *         Set of accepting final states.
     *     </p>
     * </div>
     */
    protected final Set<S> endStates;

    /**
     * <div>
     *     <p>
     *         Set of registered machine listeners.
     *     </p>
     * </div>
     */
    protected final Set<MachineListener<S, IA, OA>> listeners;

    protected final ReentrantLock executionLock;

    /**
     * <div>
     *     <p>
     *         Creates a new finite automaton.
     *     </p>
     * </div>
     *
     * @param states             Set of all states (must not be empty)
     * @param startState         Initial state
     * @param inputAlphabet      Input alphabet (must not be empty)
     * @param outputAlphabet     Output alphabet (must not be empty)
     * @param transitionFunction Transition function
     * @param endStates          Set of accepting final states (proper subset of {@code states})
     * @throws NullPointerException     if any parameter is {@code null}
     * @throws IllegalArgumentException if any set is empty or if {@code endStates} is not a proper subset
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

        if (!this.states.contains(startState)) {
            throw new IllegalArgumentException(unknownState(startState));
        }
        this.currentState
            = Objects.requireNonNull(startState, nullValue("startState"));

        Objects.requireNonNull(inputAlphabet, nullValue("inputAlphabet"));
        this.inputAlphabet
            = new HashSet<>(Util.requiresNonEmpty(inputAlphabet, emptyInputAlphabet()));

        Objects.requireNonNull(outputAlphabet, nullValue("outputAlphabet"));
        this.outputAlphabet
            = new HashSet<>(Util.requiresNonEmpty(outputAlphabet, emptyOutputAlphabet()));

        this.transitionFunction
            = Objects.requireNonNull(transitionFunction, nullValue("transitionFunction"));

        Objects.requireNonNull(endStates, nullValue("endStates"));
        this.endStates
            = new HashSet<>(Util.mustBeSubSet(this.states, endStates, endStatesNotATrueSubsetOfStates()));

        this.listeners
            = new HashSet<>();

        this.executionLock
            = new ReentrantLock(true);
    }

    protected Result<S> processInput(final @NonNull IA input) {
        Objects.requireNonNull(input, nullValue("input"));

        final Result<S> result;
        if (this.inputAlphabet.contains(input)) {
            this.executionLock.lock();
            try {
            } finally {
                this.executionLock.unlock();
            }
        } else {
            return failure(new IllegalArgumentException(unknownInputToken(input)));
        }

        return null;    // TODO
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

    /**
     * <div>
     *     <p>
     *         Adds a listener to this machine that will be notified of state transitions and errors.
     *     </p>
     * </div>
     *
     * @param listener the listener to add
     * @throws NullPointerException if the listener is null
     *
     * @since 1.0.0
     */
    public void addListener(final @NonNull MachineListener<S, IA, OA> listener) {
        Objects.requireNonNull(listener, nullValue("listener"));

        this.executionLock.lock();
        try {
            this.listeners.add(listener);
        } finally {
            this.executionLock.unlock();
        }
    }

    /**
     * <div>
     *     <p>
     *         Removes a listener from this machine.
     *     </p>
     * </div>
     *
     * @param listener the listener to remove
     * @throws NullPointerException if the listener is null
     *
     * @since 1.0.0
     */
    public void removeListener(final @NonNull MachineListener<S, IA, OA> listener) {
        Objects.requireNonNull(listener, nullValue("listener"));

        this.executionLock.lock();
        try {
            this.listeners.remove(listener);
        } finally {
            this.executionLock.unlock();
        }
    }

    /**
     * <div>
     *     <p>
     *         Notifies all registered listeners about a state transition event.
     *     </p>
     * </div>
     *
     * @param event the state transition event to fire
     * @throws NullPointerException if the event is null
     *
     * @since 1.0.0
     */
    protected void fireStateTransitionEvent(final MachineEvent.@NonNull MachineStateTransitionEvent<S, IA, OA> event) {
        Objects.requireNonNull(event, nullValue("event"));

        this.executionLock.lock();
        try {
            for (final var listener : this.listeners) {
                listener.stateTransitionEvent(event);
            }
        } finally {
            this.executionLock.unlock();
        }
    }

    /**
     * <div>
     *     <p>
     *         Notifies all registered listeners about an error event.
     *     </p>
     * </div>
     *
     * @param event the error event to fire
     * @throws NullPointerException if the event is null
     *
     * @since 1.0.0
     */
    protected void fireErrorEvent(final MachineEvent.@NonNull MachineErrorEvent<S, IA, OA> event) {
        Objects.requireNonNull(event, nullValue("event"));

        this.executionLock.lock();
        try {
            for (final var listener : this.listeners) {
                listener.errorEvent(event);
            }
        } finally {
            this.executionLock.unlock();
        }
    }

}
