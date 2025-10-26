package org.quurz.foomp.automata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun2;
import org.quurz.foomp.base.types.Mutable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.requireNonNull;
import static org.quurz.foomp.automata.localisation.AutomataMessages.*;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Util.*;

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
public abstract class AbstractFiniteStateMachine<S, IA, OA>
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

    protected final ReentrantLock executionLock;

    /**
     * <div>
     *     <p>
     *         Creates a new finite automaton.
     *     </p>
     * </div>
     *
     * @param states             Set of all states (must not be empty)
     * @param inputAlphabet      Input alphabet (must not be empty)
     * @param outputAlphabet     Output alphabet (must not be empty)
     * @param transitionFunction Transition function
     * @param endStates          Set of accepting final states (proper subset of {@code states})
     * @throws NullPointerException     if any parameter is {@code null}
     * @throws IllegalArgumentException if any set is empty or if {@code endStates} is not a proper subset
     */
    protected AbstractFiniteStateMachine(final @NonNull Set<S> states,
                                         final @NonNull Set<IA> inputAlphabet,
                                         final @NonNull Set<OA> outputAlphabet,
                                         final @NonNull Fun2<S, IA, S> transitionFunction,
                                         final @NonNull Set<S> endStates) {
        this.states
            = new HashSet<>(
                requireNonNullElements(
                    requireNonEmpty(
                        requireNonNull(states, nullValue("states")),
                        emptyStateSet()
                    ),
                    "states",
                    IllegalArgumentException::new
                )
            );

        this.inputAlphabet
            = new HashSet<>(
                requireNonNullElements(
                    requireNonEmpty(
                        requireNonNull(inputAlphabet, nullValue("inputAlphabet")),
                        emptyInputAlphabet()
                    ),
                    "inputAlphabet",
                    IllegalArgumentException::new
                )
            );

        this.outputAlphabet
            = new HashSet<>(
                requireNonNullElements(
                    requireNonEmpty(
                        requireNonNull(outputAlphabet, nullValue("outputAlphabet")),
                        emptyInputAlphabet()
                    ),
                    "outputAlphabet",
                    IllegalArgumentException::new
                )
            );

        this.transitionFunction
            = requireNonNull(transitionFunction, nullValue("transitionFunction"));

        requireNonNull(endStates, nullValue("endStates"));
        this.endStates
            = new HashSet<>(mustBeSubSet(this.states, endStates, endStatesNotATrueSubsetOfStates()));

        this.executionLock
            = new ReentrantLock(true);
    }

    protected S processInput(final @NonNull S currentState,
                             final @NonNull IA input) {
        requireNonNull(currentState, nullValue("currentState"));
        requireNonNull(input, nullValue("input"));

        if (this.inputAlphabet.contains(input)) {
            this.executionLock.lock();
            try {
                final var newState
                    = this.transitionFunction.apply(currentState, input);
                requireNonNull(newState, nullResultFrom("transitionFunction"));
                if (this.states.contains(newState)) {
                    return newState;
                } else {
                    throw new IllegalArgumentException(unknownState(newState));
                }
            } finally {
                this.executionLock.unlock();
            }
        } else {
           throw new IllegalArgumentException(unknownInputToken(input));
        }
    }

}
