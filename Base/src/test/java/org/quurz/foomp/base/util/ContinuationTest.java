package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Continuation.callCurrentCont;
import static org.quurz.foomp.base.util.Continuation.continuation;
import static org.quurz.foomp.base.util.Continuation.pureContinuation;
import static org.slf4j.LoggerFactory.getLogger;

class ContinuationTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(ContinuationTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testContinuation() {
        LOGGER.info("Test Continuation.continuation");

        assertThatThrownBy(() -> continuation(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("runCont");

        assertThatNoException()
            .isThrownBy(() -> continuation(a -> a));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPureContinuation() {
        LOGGER.info("Test Continuation.pureContinuation");

        assertThatThrownBy(() -> pureContinuation(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("value");

        assertThatNoException()
            .isThrownBy(() -> pureContinuation(42));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMap() {
        LOGGER.info("Test continuation.map");

        final Function<Function<Integer, Integer>, Integer> runCont1
            = f -> f.apply(21);
        final Continuation<Integer, Integer> continuation1
            = continuation(runCont1);
        final Function<Integer, Integer> fMap1
            = i -> i * 2;

        final Function<Function<Integer, String>, String> runCont2
            = f -> f.apply(21);
        final Continuation<Integer, String> continuation2
            = continuation(runCont2);
        final Function<Integer, String> fMap2
            = i -> Integer.toString(i * 2);

        assertThatThrownBy(() -> continuation1.map(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("transformation");
        assertThatThrownBy(() -> continuation1.map(_$ -> null).apply(_$ -> 17))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("transformation");

        assertThat(continuation1.map(fMap1).apply(Fun.identity()))
            .isEqualTo(42);

        assertThat(continuation2.map(fMap2).map(answer -> "The answer to life, the universe, and everything: " + answer).apply(Fun.identity()))
            .isEqualTo("The answer to life, the universe, and everything: 42");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testLift() {
        LOGGER.info("Test continuation.lift");

        final Function<Function<Integer, Integer>, Integer> runCont
            = f -> f.apply(21);
        final Continuation<Integer, Integer> continuation
            = continuation(runCont);
        final Continuation<Function<Integer, String>, Integer> transformation
            = continuation(cont -> cont.apply(i -> Integer.toString(i * 2)));

        assertThatThrownBy(() -> continuation.lift(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("transformation");

        assertThat(continuation.lift(transformation).apply(Integer::valueOf))
            .isEqualTo(42);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testBind() {
        LOGGER.info("Test continuation.bind");

        final Function<Function<Integer, Integer>, Integer> runCont
            = f -> f.apply(21);
        final Continuation<Integer, Integer> continuation
            = continuation(runCont);

        assertThatThrownBy(() -> continuation.bind(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("transformation");

        assertThat(continuation.bind(i -> pureContinuation(i * 2)).apply(Fun.identity()))
            .isEqualTo(42);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testApply() {
        LOGGER.info("Test continuation.apply");

        final Function<Function<Integer, Integer>, Integer> runCont
            = f -> f.apply(21);
        final Continuation<Integer, Integer> continuation
            = continuation(runCont);

        assertThatThrownBy(() -> continuation.apply(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("computation");

        assertThat(continuation.apply(Fun.identity()))
            .isEqualTo(21);
        assertThat(continuation.apply(i -> i * 2))
            .isEqualTo(42);
    }

    @Test
    void testThen() {
        LOGGER.info("Test continuation.then");

        final Function<Function<Integer, Integer>, Integer> runCont
            = f -> f.apply(21);
        final Continuation<Integer, Integer> continuation
            = continuation(runCont);
        final InvocationCountingFun<Integer, Integer> transformation
            = this.invocationCountingFun(i -> i * 2);

        //noinspection DataFlowIssue
        assertThatThrownBy(() -> continuation.then(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("continuation");

        final var mapped
         = continuation.map(transformation);

        assertThat(transformation.getInvocationCount())
            .isZero();
        assertThat(mapped.then(pureContinuation(23)).apply(Fun.identity()))
            .isEqualTo(23);
        assertThat(transformation.getInvocationCount())
            .isOne();
    }

    @Test
    void testCallCurrentCont() {
        LOGGER.info("Test Continuation.callCurrentCont");

        final var list
            = new ArrayList<Integer>();

        final Continuation<Integer, Integer> continuation
            = pureContinuation(5);
        continuation.bind(
            i -> {
                list.add(i);
                return callCurrentCont(
                    (Function<Integer, Continuation<Integer, Integer>> k) -> k.apply(42)
                        .then(pureContinuation(23))
                );
            }
        ).apply(f -> {
            list.add(f);
            return 0;
        });

        assertThat(list.toArray(Integer[]::new))
            .containsExactly(5, 42);
    }

}
