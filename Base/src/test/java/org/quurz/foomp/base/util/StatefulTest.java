package org.quurz.foomp.base.util;

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

class StatefulTest {

    private static final Logger LOGGER
        = getLogger(StatefulTest.class);

    private static final Function<Integer, Tuple2<Boolean, Integer>> RUN_STATE
        = state -> tuple2((state % 2) == 0, state + 1);

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testState() {
        LOGGER.info("Test Stateful.stateful");

        assertThatThrownBy(() -> stateful(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> stateful(_$ -> null).runState("<STATE>"))
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> stateful(obj -> tuple2(obj, 0)));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testStateOf() {
        LOGGER.info("Test Stateful.stateOf");

        assertThatThrownBy(() -> stateOf(null))
            .isInstanceOf(NullPointerException.class);

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
    void testGetState() {
        LOGGER.info("Test Stateful.getState");

        assertThatNoException()
            .isThrownBy(Stateful::getState);
        assertThat(getState().runState("<STATE>").get1())
            .isEqualTo("<STATE>");
        assertThat(getState().runState("<STATE>").get2())
            .isEqualTo("<STATE>");
        assertThat(getState().execValue("<STATE>"))
            .isEqualTo("<STATE>");
        assertThat(getState().execState("<STATE>"))
            .isEqualTo("<STATE>");
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testModifyState() {
        LOGGER.info("Test Stateful.modifyState");

        assertThatThrownBy(() -> modifyState(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> modifyState(_$ -> null)
            .runState("<STATE>")).isInstanceOf(NullPointerException.class);

        assertThat(modifyState((Function<Integer, Integer>) i -> i + 1).execState(4))
            .isEqualTo(5);
        assertThat(modifyState((Function<Integer, Integer>) i -> i + 1).execValue(4))
            .isEqualTo(nothing);
        assertThat(modifyState((Function<Integer, Integer>) i -> i + 1).runState(10).get1())
            .isEqualTo(nothing);
        assertThat(modifyState((Function<Integer, Integer>) i -> i + 1).runState(10).get2())
            .isEqualTo(11);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPutState() {
        LOGGER.info("Test Stateful.putState");

        assertThatThrownBy(() -> putState(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(putState("<STATE>").execValue("?"))
            .isEqualTo(nothing);
        assertThat(putState("<STATE>").execState("?"))
            .isEqualTo("<STATE>");
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMap() {
        LOGGER.info("Test Stateful.map");

        assertThatThrownBy(() -> stateful(RUN_STATE).map(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> stateful(RUN_STATE).map(_$ -> null).runState(1))
            .isInstanceOf(NullPointerException.class);

        assertThat(stateful(RUN_STATE).map(String::valueOf).execValue(1))
            .isEqualTo("false");
        assertThat(stateful(RUN_STATE).map(String::valueOf).execState(1))
            .isEqualTo(1);
        assertThat(stateful(RUN_STATE).map(String::valueOf).runState(1).get1())
            .isEqualTo("false");
        assertThat(stateful(RUN_STATE).map(String::valueOf).runState(1).get2())
            .isEqualTo(1);
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testLift() {
        LOGGER.info("Test stateful.lift");

        final Stateful<String, Integer> lifted
            = stateful(RUN_STATE).lift(stateful(state -> tuple2(String::valueOf, state)));

        assertThatThrownBy(() -> stateful(RUN_STATE).lift(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> stateful(RUN_STATE).lift(stateful(_$ -> null)).runState(1))
            .isInstanceOf(NullPointerException.class);

        assertThat(lifted.execValue(1))
            .isEqualTo("false");
        assertThat(lifted.execState(1))
            .isEqualTo(1);
        assertThat(lifted.runState(1))
            .isEqualTo(tuple2("false", 1));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testBind() {
        LOGGER.info("Test stateful.bind");

        assertThatThrownBy(() -> stateful(RUN_STATE).bind(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> stateful(RUN_STATE).bind(_$ -> null).runState(1))
            .isInstanceOf(NullPointerException.class);

        assertThat(stateful(RUN_STATE).bind(result -> stateful(state -> tuple2(String.valueOf(result), state))).execValue(1))
            .isEqualTo("false");
        assertThat(stateful(RUN_STATE).bind(result -> stateful(state -> tuple2(String.valueOf(result), state))).execState(1))
            .isEqualTo(1);
        assertThat(stateful(RUN_STATE).bind(result -> stateful(state -> tuple2(String.valueOf(result), state))).runState(1))
            .isEqualTo(tuple2("false", 1));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRunStateTuple() {
        LOGGER.info("Test stateful.runStateTuple");

        final var stateful
            = stateful(RUN_STATE);

        assertThatThrownBy(() -> stateful.runStateTuple(null))
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> {
                final var tuple
                    = stateful.runState(1);
                assertThat(tuple)
                    .isEqualTo(tuple2(false, 2));
                assertThat(stateful.runStateTuple(tuple))
                    .isEqualTo(tuple2(true, 3));
            });
    }

}
