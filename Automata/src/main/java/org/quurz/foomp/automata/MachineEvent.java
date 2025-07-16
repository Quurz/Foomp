package org.quurz.foomp.automata;

import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.StringJoiner;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public sealed interface MachineEvent<S, IA, OA> {

    static <S, IA, OA> MachineStateTransitionEvent<S, IA, OA> machineStateTransitionEvent(final @NonNull S lastState,
                                                                                          final @NonNull IA inputToken,
                                                                                          final @NonNull OA outputToken,
                                                                                          final @NonNull S newState) {
        Objects.requireNonNull(lastState, "lastState");
        Objects.requireNonNull(inputToken, nullValue("inputToken"));
        Objects.requireNonNull(outputToken, nullValue("outputToken"));
        Objects.requireNonNull(newState, nullValue("newState"));
        return new MachineStateTransitionEvent<S, IA, OA>(lastState, inputToken, outputToken, newState);
    }

    static <S, IA, OA> MachineErrorEvent<S, IA, OA> machineErrorEvent(final @NonNull S currentState,
                                                                      final @NonNull Exception error) {
        Objects.requireNonNull(currentState, nullValue("currentState"));
        Objects.requireNonNull(error, nullValue("error"));
        return new MachineErrorEvent<S, IA, OA>(currentState, error);
    }

    @Getter
    final class MachineStateTransitionEvent<S, IA, OA> implements MachineEvent<S, IA, OA> {

        private final S lastState;
        private final IA inputToken;
        private final OA outputToken;
        private final S newState;

        private MachineStateTransitionEvent(final S lastState,
                                            final IA inputToken,
                                            final OA outputToken,
                                            final S newState) {
            this.lastState
                = lastState;
            this.inputToken
                = inputToken;
            this.outputToken
                = outputToken;
            this.newState
                = newState;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MachineStateTransitionEvent<?, ?, ?> that)) return false;

            return lastState.equals(that.lastState) && inputToken.equals(that.inputToken) && outputToken.equals(that.outputToken) && newState.equals(that.newState);
        }

        @Override
        public int hashCode() {
            int result = lastState.hashCode();
            result = 31 * result + inputToken.hashCode();
            result = 31 * result + outputToken.hashCode();
            result = 31 * result + newState.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", MachineStateTransitionEvent.class.getSimpleName() + "[", "]")
                    .add("lastState=" + lastState)
                    .add("inputToken=" + inputToken)
                    .add("outputToken=" + outputToken)
                    .add("newState=" + newState)
                    .toString();
        }

    }

    @Getter
    final class MachineErrorEvent<S, IA, OA> implements MachineEvent<S, IA, OA> {

        private final S currentState;
        private final Exception error;

        private MachineErrorEvent(final S currentState,
                                  final Exception error) {
            this.currentState
                = currentState;
            this.error
                = error;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MachineErrorEvent<?, ?, ?> that)) return false;

            return currentState.equals(that.currentState) && error.equals(that.error);
        }

        @Override
        public int hashCode() {
            int result = currentState.hashCode();
            result = 31 * result + error.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", MachineErrorEvent.class.getSimpleName() + "[", "]")
                    .add("currentState=" + currentState)
                    .add("error=" + error)
                    .toString();
        }

    }

}
