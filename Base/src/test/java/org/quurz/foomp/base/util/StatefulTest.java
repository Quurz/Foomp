package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.quurz.foomp.base.util.Nothing.nothing;
import static org.quurz.foomp.base.util.Stateful.getState;
import static org.quurz.foomp.base.util.Stateful.modifyState;
import static org.quurz.foomp.base.util.Stateful.putState;
import static org.quurz.foomp.base.util.Stateful.stateOf;
import static org.quurz.foomp.base.util.Stateful.stateful;
import static org.quurz.foomp.base.util.Tuple2.tuple2;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Stateful")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StatefulTest {

    private static final Logger LOGGER
        = getLogger(StatefulTest.class);

    private static final Function<Integer, Tuple2<Boolean, Integer>> RUN_STATE
        = state -> tuple2((state % 2) == 0, state + 1);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void stateful_null_runner_throws_npe_and_non_null_is_ok() {
            LOGGER.info("Stateful.stateful: null runState should throw, non-null should pass");

            assertThatThrownBy(() -> stateful(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("runState");

            assertThatThrownBy(() -> stateful(_$ -> null).runState("<STATE>"))
                .isInstanceOf(NullPointerException.class);

            assertThatNoException()
                .isThrownBy(() -> stateful(obj -> tuple2(obj, 0)));
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void stateOf_null_value_throws_npe_and_non_null_is_ok() {
            LOGGER.info("Stateful.stateOf: null value should throw, non-null should pass");

            assertThatThrownBy(() -> stateOf(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("value");

            assertThat(stateOf(5).runState("<STATE>").get2())
                .isEqualTo("<STATE>");
            assertThatNoException()
                .isThrownBy(() -> stateOf(5));
            assertThat(stateOf(5).execValue("<STATE>"))
                .isEqualTo(5);
            assertThat(stateOf(5).execState("<STATE>"))
                .isEqualTo("<STATE>");
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Test
        void getState_putState_and_modifyState_behave_and_enforce_contracts() {
            LOGGER.info("Stateful.getState / putState / modifyState: behaviour and contracts");

            // getState
            assertThatNoException().isThrownBy(Stateful::getState);
            assertThat(getState().runState("<STATE>").get1()).isEqualTo("<STATE>");
            assertThat(getState().runState("<STATE>").get2()).isEqualTo("<STATE>");
            assertThat(getState().execValue("<STATE>")).isEqualTo("<STATE>");
            assertThat(getState().execState("<STATE>")).isEqualTo("<STATE>");

            // putState
            //noinspection DataFlowIssue
            assertThatThrownBy(() -> putState(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("state");
            assertThat(putState("<STATE>").execValue("?")).isEqualTo(nothing);
            assertThat(putState("<STATE>").execState("?")).isEqualTo("<STATE>");

            // modifyState
            //noinspection DataFlowIssue
            assertThatThrownBy(() -> modifyState(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("modifier");
            assertThatThrownBy(() -> modifyState(_$ -> null).runState("<STATE>"))
                .isInstanceOf(NullPointerException.class);
            assertThat(modifyState((Function<Integer, Integer>) i -> i + 1).execState(4)).isEqualTo(5);
            assertThat(modifyState((Function<Integer, Integer>) i -> i + 1).execValue(4)).isEqualTo(nothing);
            assertThat(modifyState((Function<Integer, Integer>) i -> i + 1).runState(10).get1()).isEqualTo(nothing);
            assertThat(modifyState((Function<Integer, Integer>) i -> i + 1).runState(10).get2()).isEqualTo(11);
        }
    }

    @Nested
    @DisplayName("Behaviour")
    class Behaviour {

        @Nested
        @DisplayName("map")
        class Map_ {

            @SuppressWarnings({"DataFlowIssue", "unused"})
            @Test
            void map_transforms_value_and_enforces_null_contracts() {
                LOGGER.info("Stateful.map should transform value and fail on nulls");

                assertThatThrownBy(() -> stateful(RUN_STATE).map(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("transformation");
                assertThatThrownBy(() -> stateful(RUN_STATE).map(_$ -> null).runState(1))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("transformation");

                final var mapped = stateful(RUN_STATE).map(String::valueOf);

                assertThat(mapped.execValue(1)).isEqualTo("false");
                // State is threaded through: 1 -> 2
                assertThat(mapped.execState(1)).isEqualTo(2);
                assertThat(mapped.runState(1).get1()).isEqualTo("false");
                assertThat(mapped.runState(1).get2()).isEqualTo(2);
            }
        }

        @Nested
        @DisplayName("lift (applicative)")
        class Lift_ {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void lift_applies_function_stateful_and_enforces_null_contracts() {
                LOGGER.info("Stateful.lift should apply function-in-stateful to current value");

                final Stateful<String, Integer> lifted =
                    stateful(RUN_STATE).lift(stateful(state -> tuple2(String::valueOf, state)));

                assertThatThrownBy(() -> stateful(RUN_STATE).lift(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("transformation");
                assertThatThrownBy(() -> stateful(RUN_STATE).lift(stateful(_$ -> null)).runState(1))
                    .isInstanceOf(NullPointerException.class);

                // Applicative threading: tf uses s0, then value uses s1 -> increments to 2
                assertThat(lifted.execValue(1)).isEqualTo("false");
                assertThat(lifted.execState(1)).isEqualTo(2);
                assertThat(lifted.runState(1)).isEqualTo(tuple2("false", 2));
            }
        }

        @Nested
        @DisplayName("bind (flatMap)")
        class Bind_ {

            @SuppressWarnings({"DataFlowIssue", "unused"})
            @Test
            void bind_sequences_and_enforces_null_contracts() {
                LOGGER.info("Stateful.bind should sequence computations (flatMap) and enforce null contracts");

                assertThatThrownBy(() -> stateful(RUN_STATE).bind(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("transformation");
                assertThatThrownBy(() -> stateful(RUN_STATE).bind(_$ -> null).runState(1))
                    .isInstanceOf(NullPointerException.class);

                // First step increments to 2, second step preserves the state it receives
                final var bound = stateful(RUN_STATE)
                    .bind(result -> stateful(state -> tuple2(String.valueOf(result), state)));

                assertThat(bound.execValue(1)).isEqualTo("false");
                assertThat(bound.execState(1)).isEqualTo(2);
                assertThat(bound.runState(1)).isEqualTo(tuple2("false", 2));
            }
        }

        @Nested
        @DisplayName("run and helpers")
        class Run_ {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void runState_execValue_execState_and_runStateTuple() {
                LOGGER.info("Stateful.runState/execValue/execState/runStateTuple should behave");

                final var st = stateful(RUN_STATE);

                assertThat(st.execValue(1)).isFalse();
                assertThat(st.execState(1)).isEqualTo(2);
                assertThat(st.runState(1)).isEqualTo(tuple2(false, 2));

                // runStateTuple uses the state from the tuple
                assertThat(st.runStateTuple(tuple2(false, 2)))
                    .isEqualTo(tuple2(true, 3));

                //noinspection DataFlowIssue
                assertThatThrownBy(() -> st.runStateTuple(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("tuple");
            }
        }

        @Nested
        @DisplayName("unwrap (join)")
        class Unwrap_ {

            @Test
            void unwrap_flattens_nested_state_and_threads_state_correctly() {
                LOGGER.info("Stateful.unwrap should flatten nested stateful and thread state");

                // Outer increments once, returns inner that increments once more
                final var outer = stateful((Integer s0) -> tuple2(
                    stateful((Integer s1) -> tuple2("ok", s1 + 1)),
                    s0 + 1
                ));

                final var unwrapped = Stateful.unwrap(outer);

                assertThat(unwrapped.runState(10))
                    .isEqualTo(tuple2("ok", 12)); // 10 -> 11 (outer), then 11 -> 12 (inner)
            }
        }
    }
}
