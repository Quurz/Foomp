package org.quurz.foomp.automata;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.functions.Fun2;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.automata.MooreMachine.mooreMachine;
import static org.slf4j.LoggerFactory.getLogger;

class MooreMachineTest {

    private static final Logger LOGGER
        = getLogger(MooreMachineTest.class);

    private final Set<Integer> states;
    private final Set<Character> inputAlphabet;
    private final Set<String> outputAlphabet;
    private final Fun2<Integer, Character, Integer> transitionFunction;
    private final Set<Integer> endStates;

    MooreMachineTest() {} {
        this.states
            = new HashSet<>();
        this.states.add(1);
        this.states.add(2);
        this.states.add(3);

        this.inputAlphabet
            = new HashSet<>();
        this.inputAlphabet.add('A');
        this.inputAlphabet.add('B');
        this.inputAlphabet.add('C');

        this.outputAlphabet
            = new HashSet<>();
        this.outputAlphabet.add("Alpha");
        this.outputAlphabet.add("Beta");
        this.outputAlphabet.add("Gamma");

        this.transitionFunction
            = (state, input) ->
                switch (state) {
                    case 1 -> input == 'A' ? 2 : 3;
                    case 2 -> input == 'B' ? 3 : 1;
                    case 3 -> input == 'C' ? 1 : 2;
                    default -> throw new IllegalArgumentException("Should never happen: " + state);
                };

        this.endStates
            = new HashSet<>();
        this.endStates.add(3);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMooreMAchineMoreMachine() {
        LOGGER.info("Test MooreMachine.moreMachine");

        assertThatThrownBy(() ->
            mooreMachine(
                this.states,
                1,
                this.inputAlphabet,
                this.outputAlphabet,
                this.transitionFunction,
                null
            )
        ).isInstanceOf(NullPointerException.class)
            .hasMessageContaining("null");
    }

}
