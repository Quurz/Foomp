package org.quurz.foomp.automata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.functions.Fun2;
import org.quurz.foomp.base.util.Result;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

class AbstractMachineTest {

    private static final Logger LOGGER
        = getLogger(AbstractMachineTest.class);

    private static final class DummyMachine<S, IA, OA>
        extends AbstractMachine<S, IA, OA> {

        public DummyMachine(final @NonNull Set<S> states,
                            final @NonNull S startState,
                            final @NonNull Set<IA> inputAlphabet,
                            final @NonNull Set<OA> outputAlphabet,
                            final @NonNull Fun2<S, IA, S> transitionFunction,
                            final @NonNull Set<S> endStates) {
            super(
                states,
                startState,
                inputAlphabet,
                outputAlphabet,
                transitionFunction,
                endStates
            );
        }

        @Override
        public @NonNull Result<OA> read(@NonNull IA input) {
            throw new UnsupportedOperationException();
        }

    }

    private final Set<String> states;
    private final Set<Integer> inputAlphabet;
    private final Set<Integer> outputAlphabet;
    private final Set<String> endStates;

    AbstractMachineTest() {
        this.states
            = new HashSet<>();
        this.states.add("<START_STATE>");
        this.states.add("<NEXT_STATE>");
        this.states.add("<FINAL_STATE>");

        this.inputAlphabet
            = new HashSet<>();
        this.inputAlphabet.add(1);
        this.inputAlphabet.add(2);
        this.inputAlphabet.add(3);

        this.outputAlphabet
            = new HashSet<>();
        this.outputAlphabet.add(10);
        this.outputAlphabet.add(20);
        this.outputAlphabet.add(30);

        this.endStates
            = new HashSet<>();
        this.endStates.add("<FINAL_STATE>");
    }

    @SuppressWarnings({"DataFlowIssue", "MismatchedQueryAndUpdateOfCollection"})
    @Test
    void testConstructor() {
        LOGGER.info("Test AbstractMachine constructor");

        final var illegalEndStates
            = new HashSet<String>();
        illegalEndStates.add("<ILLEGAL_FINAL_STATE>");

        assertThatThrownBy(
            () -> new DummyMachine<>(
                    null,
                    "<START_STATE>",
                    this.inputAlphabet,
                    this.outputAlphabet,
                    (string, integer) -> "<NEXT_STATE>",
                    endStates
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("states");
        assertThatThrownBy(
            () -> new DummyMachine<>(
                    new HashSet<>(),
                    "<START_STATE>",
                    this.inputAlphabet,
                    this.outputAlphabet,
                    (string, integer) -> "<NEXT_STATE>",
                    this.endStates
                )
            )
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(
            () -> new DummyMachine<>(
                    states,
                    null,
                    inputAlphabet,
                    outputAlphabet,
                    (string, integer) -> "<NEXT_STATE>",
                    endStates
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("startState");

        assertThatThrownBy(
            () -> new DummyMachine<>(
                    this.states,
                    "<START_STATE>",
                    (Set<Integer>) null,
                    this.outputAlphabet,
                    (string, integer) -> "<NEXT_STATE>",
                    this.endStates
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("inputAlphabet");
        assertThatThrownBy(
            () -> new DummyMachine<>(
                    this.states,
                    "<START_STATE>",
                    new HashSet<>(),
                    this.outputAlphabet,
                    (string, integer) -> "<NEXT_STATE>",
                    this.endStates
                )
            )
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(
            () -> new DummyMachine<>(
                    this.states,
                    "<START_STATE>",
                    this.inputAlphabet,
                    null,
                    (string, integer) -> "<NEXT_STATE>",
                    this.endStates
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("outputAlphabet");
        assertThatThrownBy(
                () -> new DummyMachine<>(
                    this.states,
                    "<START_STATE>",
                    this.inputAlphabet,
                    new HashSet<>(),
                    (string, integer) -> "<NEXT_STATE>",
                    this.endStates
                )
            )
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(
            () -> new DummyMachine<>(
                    this.states,
                    "<START_STATE>",
                    this.inputAlphabet,
                    this.outputAlphabet,
                    null,
                    this.endStates
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("transitionFunction");

        assertThatThrownBy(
                () -> new DummyMachine<>(
                        this.states,
                        "<START_STATE>",
                        this.inputAlphabet,
                        this.outputAlphabet,
                        (string, integer) -> "<NEXT_STATE>",
                        null
                    )
            )
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("endStates");
        assertThatThrownBy(
            () -> new DummyMachine<>(
                    this.states,
                    "<START_STATE>",
                    this.inputAlphabet,
                    this.outputAlphabet,
                    (string, integer) -> "<NEXT_STATE>",
                    illegalEndStates
                )
            )
            .isInstanceOf(IllegalArgumentException.class);

        assertThatNoException()
            .isThrownBy(
                () -> new DummyMachine<>(
                        this.states,
                        "<START_STATE>",
                        this.inputAlphabet,
                        this.outputAlphabet,
                        (string, integer) -> "<NEXT_STATE>",
                        this.endStates
                    )
            );
    }

    @Test
    void testGetCurrentState() {
        LOGGER.info("Test abstractMachine.getCurrentState");

        final var machine
            = new DummyMachine<>(
                this.states,
                "<START_STATE>",
                this.inputAlphabet,
                this.outputAlphabet,
                (string, integer) -> "<NEXT_STATE>",
                this.endStates
            );

        assertThat(machine.getCurrentState())
            .isEqualTo("<START_STATE>");
    }

    @Test
    void testSetCurrentState() {
        LOGGER.info("Test abstractMachine.setCurrentState");

        final var machine
            = new DummyMachine<>(
                this.states,
                "<START_STATE>",
                this.inputAlphabet,
                this.outputAlphabet,
                (string, integer) -> "<NEXT_STATE>",
                this.endStates
            );

        assertThatNoException()
            .isThrownBy(() -> machine.setCurrentState("<NEXT_STATE>"));
        assertThat(machine.getCurrentState())
            .isEqualTo("<NEXT_STATE>");

        assertThatThrownBy(() -> machine.setCurrentState("<ILLEGAL_STATE>"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("<ILLEGAL_STATE>");
    }

}
