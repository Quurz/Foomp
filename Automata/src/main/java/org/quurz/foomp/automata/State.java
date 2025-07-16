package org.quurz.foomp.automata;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Objects;

import static org.quurz.foomp.automata.localisation.AutomataMessages.unknownInputToken;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public class State<S, IA> {

    public static <S, IA> State<S, IA> state(final @NonNull S identifier,
                                             final @NonNull Map<IA, State<S, IA>> transitions,
                                             final boolean endState) {
        Objects.requireNonNull(identifier, nullValue("identifier"));
        Objects.requireNonNull(transitions, nullValue("transitions"));

        return new State<>(identifier, transitions, endState);
    }

    private final S identifier;
    private final Map<IA, State<S, IA>> transitions;
    private final boolean endState;

    private State(final S identifier,
                  final Map<IA, State<S, IA>> transitions,
                  final boolean endState) {
        this.identifier
            = identifier;
        this.transitions
            = transitions;
        this.endState
            = endState;
    }

    public State<S, IA> transition(final @NonNull IA input) {
        Objects.requireNonNull(input, nullValue("input"));

        final State<S, IA> nextState
            = transitions.get(input);
        if (nextState == null) {
            throw new IllegalArgumentException(unknownInputToken(input));
        }
        return nextState;
    }

}
