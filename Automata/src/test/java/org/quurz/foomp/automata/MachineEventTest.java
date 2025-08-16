package org.quurz.foomp.automata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.automata.MachineEvent.machineErrorEvent;
import static org.quurz.foomp.automata.MachineEvent.machineStateTransitionEvent;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("MachineEvent")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MachineEventTest {

    private static final Logger LOGGER
        = getLogger(MachineEventTest.class);

    private static Stream<Exception> exceptionProvider() {
        return Stream.of(new Exception("error"));
    }

    @Nested
    class MachineStateTransitionEvent {

        @Nested
        class Factory {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void should_ThrowNullPointerException_When_LastStateIsNull() {
                LOGGER.info("MachineEvent.machineStateTransitionEvent(...) should throw NullPointerException when lastState is null");

                assertThatThrownBy(
                    () -> machineStateTransitionEvent(
                        null,
                        "inputToken",
                        "outputToken",
                        "newState"
                    )
                )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("lastState");
            }

            @SuppressWarnings("DataFlowIssue")
            @Test
            void should_ThrowNullPointerException_When_InputTokenIsNull() {
                LOGGER.info("MachineEvent.machineStateTransitionEvent(...) should throw NullPointerException when inputToken is null");

                assertThatThrownBy(
                    () -> machineStateTransitionEvent(
                        "lastState",
                        null,
                        "outputToken",
                        "newState"
                    )
                )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("inputToken");
            }

            @SuppressWarnings("DataFlowIssue")
            @Test
            void should_ThrowNullPointerException_When_OutputTokenIsNull() {
                LOGGER.info("MachineEvent.machineStateTransitionEvent(...) should throw NullPointerException when outputToken is null");

                assertThatThrownBy(
                    () -> machineStateTransitionEvent(
                        "lastState",
                        "inputToken",
                        null,
                        "newState"
                    )
                )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("outputToken");
            }

            @SuppressWarnings("DataFlowIssue")
            @Test
            void should_ThrowNullPointerException_When_NewStateIsNull() {
                LOGGER.info("MachineEvent.machineStateTransitionEvent(...) should throw NullPointerException when newState is null");

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

        }

        @Nested
        class Getters {

            @Test
            void should_ReturnLastState() {
                LOGGER.info("MachineStateTransitionEvent.getLastState() should return lastState");

                var event
                    = machineStateTransitionEvent(
                        "state1",
                        "input",
                        "output",
                        "state2"
                    );

                assertThat(event.getLastState())
                    .isEqualTo("state1");
            }

            @Test
            void should_ReturnInputToken() {
                LOGGER.info("MachineStateTransitionEvent.getInputToken() should return inputToken");

                var event
                    = machineStateTransitionEvent(
                        "state1",
                        "input",
                        "output",
                        "state2"
                    );

                assertThat(event.getInputToken())
                    .isEqualTo("input");
            }

            @Test
            void should_ReturnOutputToken() {
                LOGGER.info("MachineStateTransitionEvent.getOutputToken() should return outputToken");

                var event
                    = machineStateTransitionEvent(
                        "state1",
                        "input",
                        "output",
                        "state2"
                    );

                assertThat(event.getOutputToken())
                    .isEqualTo("output");
            }

            @Test
            void should_ReturnNewState() {
                LOGGER.info("MachineStateTransitionEvent.getNewState() should return newState");

                var event
                    = machineStateTransitionEvent(
                        "state1",
                        "input",
                        "output",
                        "state2"
                    );

                assertThat(event.getNewState())
                    .isEqualTo("state2");
            }

        }

        @Nested
        class EqualsAndHashCode {

            @Test
            void should_BeEqual_When_AllFieldsAreEqual() {
                LOGGER.info("MachineStateTransitionEvent equals() and hashCode() should be equal when all fields are equal");

                var event1
                    = machineStateTransitionEvent(
                        "state1",
                        "input",
                        "output",
                        "state2"
                    );
                var event2
                    = machineStateTransitionEvent(
                        "state1",
                        "input",
                        "output",
                        "state2"
                    );

                assertThat(event1)
                    .isEqualTo(event2);
                assertThat(event1
                    .hashCode()).isEqualTo(event2.hashCode());
            }

            @Test
            void should_NotBeEqual_When_LastStateIsDifferent() {
                LOGGER.info("MachineStateTransitionEvent equals() and hashCode() should not be equal when lastState is different");

                var event1
                    = machineStateTransitionEvent(
                        "state1",
                        "input",
                        "output",
                        "state2"
                    );
                var event2
                    = machineStateTransitionEvent(
                        "different",
                        "input",
                        "output",
                        "state2"
                    );

                assertThat(event1)
                    .isNotEqualTo(event2);
                assertThat(event1.hashCode())
                    .isNotEqualTo(event2.hashCode());
            }

            @Test
            void should_NotBeEqual_When_InputTokenIsDifferent() {
                LOGGER.info("MachineStateTransitionEvent equals() and hashCode() should not be equal when inputToken is different");

                var event1
                    = machineStateTransitionEvent(
                        "state1",
                        "input1",
                        "output",
                        "state2"
                    );
                var event2
                    = machineStateTransitionEvent(
                        "state1",
                        "input2",
                        "output",
                        "state2"
                    );

                assertThat(event1)
                    .isNotEqualTo(event2);
                assertThat(event1.hashCode())
                    .isNotEqualTo(event2.hashCode());
            }

            @Test
            void should_NotBeEqual_When_OutputTokenIsDifferent() {
                LOGGER.info("MachineStateTransitionEvent equals() and hashCode() should not be equal when outputToken is different");

                var event1
                    = machineStateTransitionEvent(
                        "state1",
                        "input",
                        "output1",
                        "state2"
                    );
                var event2
                    = machineStateTransitionEvent(
                        "state1",
                        "input",
                        "output2",
                        "state2"
                    );

                assertThat(event1)
                    .isNotEqualTo(event2);
                assertThat(event1.hashCode())
                    .isNotEqualTo(event2.hashCode());
            }

            @Test
            void should_NotBeEqual_When_NewStateIsDifferent() {
                LOGGER.info("MachineStateTransitionEvent equals() and hashCode() should not be equal when newState is different");

                var event1
                    = machineStateTransitionEvent(
                        "state1",
                        "input",
                        "output",
                        "state2"
                    );
                var event2
                    = machineStateTransitionEvent(
                        "state1",
                        "input",
                        "output",
                        "different"
                    );

                assertThat(event1)
                    .isNotEqualTo(event2);
                assertThat(event1.hashCode())
                    .isNotEqualTo(event2.hashCode());
            }

            @SuppressWarnings("ConstantValue")
            @Test
            void should_ReturnFalse_When_ComparedWith_Null() {
                LOGGER.info("MachineStateTransitionEvent equals() should return false when compared with null");

                var event
                    = machineStateTransitionEvent(
                        "s1",
                        "in",
                        "out",
                        "s2"
                    );
                assertThat(event.equals(null))
                    .isFalse();
            }

            @SuppressWarnings("EqualsBetweenInconvertibleTypes")
            @Test
            void should_ReturnFalse_When_ComparedWith_DifferentType() {
                LOGGER.info("MachineStateTransitionEvent equals() should return false when compared with different type");

                var event
                    = machineStateTransitionEvent(
                        "s1",
                        "in",
                        "out",
                        "s2"
                    );
                assertThat(event.equals("not-an-event"))
                    .isFalse();
            }

        }

        @Test
        void should_ReturnFormattedString() {
            LOGGER.info("MachineStateTransitionEvent toString() should return formatted string");

            var event
                = machineStateTransitionEvent(
                    "state1",
                    "input",
                    "output",
                    "state2"
                );

            assertThat(event.toString())
                .isEqualTo("MachineStateTransitionEvent[lastState=state1, inputToken=input, outputToken=output, newState=state2]");
        }

    }

    @Nested
    class MachineErrorEvent {

        @Nested
        class Factory {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void Should_ThrowNullPointerException_When_CurrentStateIsNull() {
                LOGGER.info("MachineState.machineErrorEvent(...) should throw NullPointerException when currentState is null");

                assertThatThrownBy(
                    () -> machineErrorEvent(
                        null,
                        new Exception("error")
                    )
                )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("currentState");
            }

            @SuppressWarnings("DataFlowIssue")
            @Test
            void Should_ThrowNullPointerException_When_ErrorIsNull() {
                LOGGER.info("MachineState.machineErrorEvent(...) should throw NullPointerException when error is null");

                assertThatThrownBy(
                    () -> machineErrorEvent(
                        "currentState",
                        null
                    )
                )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("error");
            }

        }

        @Nested
        class Getters {

            @Test
            void should_ReturnCurrentState() {
                LOGGER.info("MachineErrorEvent.getCurrentState() should return currentState");

                var event
                    = machineErrorEvent(
                        "state1",
                        new Exception("error")
                    );

                assertThat(event.getCurrentState())
                    .isEqualTo("state1");
            }

            @Test
            void should_ReturnError() {
                LOGGER.info("MachineErrorEvent.getError() should return error");

                var exception
                        = new Exception("error");
                var event
                    = machineErrorEvent(
                        "state1",
                        exception
                    );

                assertThat(event.getError())
                    .isEqualTo(exception);
            }

        }

        @Nested
        class EqualsAndHashCode {

            @ParameterizedTest
            @MethodSource("org.quurz.foomp.automata.MachineEventTest#exceptionProvider")
            void should_BeEqual_When_AllFieldsAreEqual(final Exception exception) {
                LOGGER.info("MachineErrorEvent equals() and hashCode() should be equal when all fields are equal");

                var event1
                    = machineErrorEvent(
                        "state1",
                        exception
                    );
                var event2
                    = machineErrorEvent(
                        "state1",
                        exception
                    );

                assertThat(event1)
                    .isEqualTo(event2);
                assertThat(event1.hashCode())
                    .isEqualTo(event2.hashCode());
            }

            @Test
            void should_NotBeEqual_When_CurrentStateIsDifferent() {
                LOGGER.info("MachineErrorEvent equals() and hashCode() should not be equal when currentState is different");

                var event1
                    = machineErrorEvent(
                        "state1",
                        new Exception("error")
                    );
                var event2
                    = machineErrorEvent(
                        "different",
                        new Exception("error")
                    );

                assertThat(event1)
                    .isNotEqualTo(event2);
                assertThat(event1.hashCode())
                    .isNotEqualTo(event2.hashCode());
            }

            @Test
            void should_NotBeEqual_When_ErrorIsDifferent() {
                LOGGER.info("MachineErrorEvent equals() and hashCode() should not be equal when error is different");

                var event1
                    = machineErrorEvent(
                        "state1",
                        new Exception("error1")
                    );
                var event2
                    = machineErrorEvent(
                        "state1",
                        new Exception("error2")
                    );

                assertThat(event1)
                    .isNotEqualTo(event2);
                assertThat(event1.hashCode())
                    .isNotEqualTo(event2.hashCode());
            }

            @SuppressWarnings("ConstantValue")
            @Test
            void should_ReturnFalse_When_ComparedWith_Null() {
                LOGGER.info("MachineErrorEvent equals() should return false when compared with null");

                var event
                    = machineErrorEvent(
                        "state1",
                        new Exception("error")
                    );
                assertThat(event.equals(null))
                    .isFalse();
            }

            @SuppressWarnings("EqualsBetweenInconvertibleTypes")
            @Test
            void should_ReturnFalse_When_ComparedWith_DifferentType() {
                LOGGER.info("MachineErrorEvent equals() should return false when compared with different type");

                var event
                    = machineErrorEvent(
                        "state1",
                        new Exception("error")
                    );
                assertThat(event.equals("not-an-event"))
                    .isFalse();
            }
        }


        @Test
        void should_ReturnFormattedString() {
            LOGGER.info("MachineErrorEvent toString() should return formatted string");

            var event
                = machineErrorEvent(
                    "state1",
                    new Exception("error")
                );

            assertThat(event.toString())
                .isEqualTo("MachineErrorEvent[currentState=state1, error=java.lang.Exception: error]");
        }

    }

}
