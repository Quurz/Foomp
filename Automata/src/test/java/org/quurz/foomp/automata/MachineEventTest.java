package org.quurz.foomp.automata;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.automata.MachineEvent.machineStateTransitionEvent;
import static org.slf4j.LoggerFactory.getLogger;

class MachineEventTest {

    private static final Logger LOGGER
        = getLogger(MachineEventTest.class);


    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMachineEventMachineStateTransitionEvent() {
        LOGGER.info("Test MachineEvent.machineStateTransitionEvent");

        assertThatThrownBy(
                () -> machineStateTransitionEvent(
                    null,
                    "inputToken",
                    "outputToken",
                    "nwqState"
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("lastState");

        assertThatThrownBy(
                () -> machineStateTransitionEvent(
                    "lastState",
                    null,
                    "outputToken",
                    "nwqState"
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("inputToken");

        assertThatThrownBy(
                () -> machineStateTransitionEvent(
                    "lastState",
                    "inputToken",
                    null,
                    "nwqState"
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("outputToken");

        assertThatThrownBy(
                () -> machineStateTransitionEvent(
                    "lastState",
                    "inputToken",
                    "outputToken",
                    null
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("newState");
    }

    @Test
    void testMachineEventMachineStateTransitionEventEqualsAndHashCode() {
        LOGGER.info("Test MachineEvent.machineStateTransitionEvent equals and hashCode");

        final var event1
            = machineStateTransitionEvent(
                "lastState",
                "inputToken",
                "outputToken",
                "newState"
            );
        final var event2
            = machineStateTransitionEvent(
                "lastState",
                "inputToken",
                "outputToken",
                "newState"
            );
        final var event3
            = machineStateTransitionEvent(
                "lastState",
                "inputToken",
                "outputToken",
                "differentNewState"
            );

        assertThat(event1)
            .isNotEqualTo(null)
            .isNotEqualTo(event3);
        assertThat(event1.hashCode())
            .isNotEqualTo(event3.hashCode());

        assertThat(event1)
            .isEqualTo(event2);
        assertThat(event1.hashCode())
            .isEqualTo(event2.hashCode());
    }

    @Test
    void testMachineEventMachineStateTransitionEventToString() {
        LOGGER.info("Test MachineEvent.machineStateTransitionEvent toString");

        final var event
            = machineStateTransitionEvent(
                "lastState",
                "inputToken",
                "outputToken",
                "newState"
            );

        assertThat(event.toString())
            .isEqualTo(
                "MachineStateTransitionEvent[lastState=lastState, inputToken=inputToken, outputToken=outputToken, newState=newState]"
            );
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMachineEventMachineErrorEvent() {
        LOGGER.info("Test MachineEvent.machineErrorEvent");

        assertThatThrownBy(
                () -> MachineEvent.machineErrorEvent(
                    null,
                    new Exception("error")
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("currentState");

        assertThatThrownBy(
                () -> MachineEvent.machineErrorEvent(
                    "currentState",
                    null
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("error");
    }

    @Test
    void testMachineEventMachineErrorEventEqualsAndHashCode() {
        LOGGER.info("Test MachineEvent.machineErrorEvent equals and hashCode");

        final var error
            = new Exception("error");
        final var event1
            = MachineEvent.machineErrorEvent(
                "currentState",
                error
            );
        final var event2
            = MachineEvent.machineErrorEvent(
                "currentState",
                error
            );
        final var event3
            = MachineEvent.machineErrorEvent(
                "currentState",
                new Exception("different error")
            );

        assertThat(event1)
            .isNotEqualTo(null)
            .isNotEqualTo(event3);
        assertThat(event1.hashCode())
            .isNotEqualTo(event3.hashCode());

        assertThat(event1)
            .isEqualTo(event2);
        assertThat(event1.hashCode())
            .isEqualTo(event2.hashCode());
    }

    @Test
    void testMachineEventMachineErrorEventtoString() {
        LOGGER.info("Test MachineEvent.machineErrorEvent toString");

        final var error
            = new Exception("error");
        final var event
            = MachineEvent.machineErrorEvent(
                "currentState",
                error
            );

        assertThat(event.toString())
            .isEqualTo(
                "MachineErrorEvent[currentState=currentState, error=java.lang.Exception: error]"
            );
    }

}
