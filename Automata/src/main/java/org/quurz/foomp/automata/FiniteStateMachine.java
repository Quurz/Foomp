package org.quurz.foomp.automata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun2;
import org.quurz.foomp.base.types.Mutable;
import org.quurz.foomp.base.util.Result;

import java.util.HashSet;
import java.util.Objects;
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
 *
 *     </p>
 * </div>
 *
 * @param <S>
 * @param <IA>
 * @param <OA>
 */
@Mutable
public class FiniteStateMachine<S, IA, OA>
        implements StateMachine<S, IA, OA> {

    public static <S, IA, OA> FiniteStateMachine<S, IA, OA> mooreMachine(final @NonNull Set<S> states,
                                                                         final @NonNull Set<IA> inputAlphabet,
                                                                         final @NonNull Set<OA> outputAlphabet,
                                                                         final @NonNull Fun2<S, IA, S> transitionFunction,
                                                                         final @NonNull Fun<S, OA> outputFunction,
                                                                         final @NonNull Set<S> endStates,
                                                                         final @NonNull S initialState) {
        return new FiniteStateMachine<>(
            states,
            inputAlphabet,
            outputAlphabet,
            transitionFunction,
            s -> $_ -> outputFunction.apply(s),
            endStates,
            initialState
        );
    }

    public static <S, IA, OA> FiniteStateMachine<S, IA, OA> mealyMachine(final @NonNull Set<S> states,
                                                                         final @NonNull Set<IA> inputAlphabet,
                                                                         final @NonNull Set<OA> outputAlphabet,
                                                                         final @NonNull Fun2<S, IA, S> transitionFunction,
                                                                         final @NonNull Fun2<S, IA, OA> outputFunction,
                                                                         final @NonNull Set<S> endStates,
                                                                         final @NonNull S initialState) {
        return new FiniteStateMachine<>(
            states,
            inputAlphabet,
            outputAlphabet,
            transitionFunction,
            s -> ia -> outputFunction.apply(s, ia),
            endStates,
            initialState
        );
    }

    private final Set<S> states;
    private final Set<IA> inputAlphabet;
    private final Set<OA> outputAlphabet;
    private final Fun2<S, IA, S> transitionFunction;
    private final Fun<S, Fun<IA, OA>> outputFunction;
    private final Set<S> endStates;

    private final S initialState;
    private S currentState;

    protected final ReentrantLock executionLock;

    private FiniteStateMachine(final @NonNull Set<S> states,
                               final @NonNull Set<IA> inputAlphabet,
                               final @NonNull Set<OA> outputAlphabet,
                               final @NonNull Fun2<S, IA, S> transitionFunction,
                               final @NonNull Fun<S, Fun<IA, OA>> outputFunction,
                               final @NonNull Set<S> endStates,
                               final @NonNull S initialState) {
        // TODO: Ein Validator wäre nicht schlecht für all diese Prüfungen. Könnte man dann bestimmt auch an anderen stellen gebrauchen

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
                        emptyOutputAlphabet()    // ❌ Sollte emptyOutputAlphabet() sein!
                    ),
                    "outputAlphabet",
                    IllegalArgumentException::new
                )
            );

        this.transitionFunction
            = requireNonNull(transitionFunction, nullValue("transitionFunction"));

        this.outputFunction
            = requireNonNull(outputFunction, nullValue("outputFunction"));

        requireNonNull(endStates, nullValue("endStates"));
        this.endStates
            = new HashSet<>(mustBeSubSet(this.states, endStates, endStatesNotATrueSubsetOfStates()));

        this.executionLock
            = new ReentrantLock(true);

        this.initialState
            = initialState;
        this.currentState
            = requireNonNull(initialState, nullValue("initialState"));
        if (!this.states.contains(this.currentState)) {
            throw new IllegalArgumentException(unknownState(this.currentState));
        }
    }

    @Override
    public @NonNull OA read(final @NonNull IA input) {
        Objects.requireNonNull(input, nullValue("input"));
        if (this.inputAlphabet.contains(input)) {
            this.executionLock.lock();
            try {
                final var newState
                    = requireNonNull(this.transitionFunction.apply(currentState, input), nullResultFrom("transitionFunction"));
                if (this.states.contains(newState)) {
                    final var output
                        = Objects.requireNonNull(
                            this.outputFunction.apply(this.currentState).apply(input),
                            nullResultFrom("outputFunction")
                    );
                    this.currentState
                        = newState;
                    if (this.outputAlphabet.contains(output)) {
                        return output;
                    } else {
                        throw new IllegalArgumentException(unknownOutputToken(output));
                    }
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

    public FiniteStateMachine<S, IA, OA> reset() {
        this.executionLock.lock();
        try {
            this.currentState
                = this.initialState;
            return this;
        } finally {
            this.executionLock.unlock();
        }
    }

}
