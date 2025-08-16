package org.quurz.foomp.automata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.util.Result;



/**
 * <div>
 *     <p>
 *         Represents a generic finite state machine with states, inputs and outputs.
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
public interface FiniteStateMachine<S, IA, OA> {

    /**
     * <div>
     *     <p>
     *         Performs a transition for the given input symbol and returns the associated output.
     *     </p>
     * </div>
     *
     * @param input the input symbol
     * @return the output associated with the input
     * @throws NullPointerException     if {@code input} is null
     * @throws IllegalArgumentException if the input symbol is unknown
     *
     * @since 1.0.0
     */
    @NonNull
    Result<OA> read(final @NonNull IA input);

    /**
     * <div>
     *     <p>
     *         Returns the current state of the automaton.
     *     </p>
     * </div>
     *
     * @return the current state
     *
     * @since 1.0.0
     */
    S getCurrentState();

    /**
     * <div>
     *     <p>
     *         Sets the current state of the automaton.
     *     </p>
     * </div>
     *
     * @param newCurrentState the new state
     * @throws NullPointerException     if {@code newCurrentState} is null
     * @throws IllegalArgumentException if the state is not contained in the state space
     *
     * @since 1.0.0
     */
    void setCurrentState(final @NonNull S newCurrentState);

    /**
     * <div>
     *     <p>
     *         Indicates whether the automaton is currently in a final state.
     *     </p>
     * </div>
     *
     * @return {@code true} if the current state is a final state
     *
     * @since 1.0.0
     */
    boolean isInFinalState();

}
