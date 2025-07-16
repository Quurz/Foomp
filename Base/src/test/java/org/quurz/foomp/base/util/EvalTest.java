package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.types.Value;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Eval.evalAlways;
import static org.quurz.foomp.base.util.Eval.evalLater;
import static org.quurz.foomp.base.util.Eval.evalNow;
import static org.slf4j.LoggerFactory.getLogger;

class EvalTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(EvalTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testNow() {
        LOGGER.info("Test Eval.now");

        assertThatThrownBy(() -> evalNow(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(evalNow(SOME_STRING_VALUE).isPresent())
            .isTrue();
        assertThat(evalNow(SOME_STRING_VALUE).get())
            .isEqualTo(SOME_STRING_VALUE);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testLater() {
        LOGGER.info("Test Eval.later");

        assertThatThrownBy(() -> evalLater(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(evalLater(SOME_STRING_VALUE).isPresent())
            .isTrue();
        assertThat(evalLater(SOME_STRING_VALUE).get())
            .isEqualTo(SOME_STRING_VALUE);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAlways() {
        LOGGER.info("Test Eval.always");

        assertThatThrownBy(() -> evalAlways(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(evalAlways(SOME_STRING_VALUE).isPresent())
            .isTrue();
        assertThat(evalAlways(SOME_STRING_VALUE).get())
            .isEqualTo(SOME_STRING_VALUE);
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testNowMap() {
        LOGGER.info("Test Eval.Now.map");

        final var eval
            = evalNow(SOME_STRING_VALUE);
        final InvocationCountingFun<String, String> fun
            = invocationCountingFun(Fun.identity());

        assertThatThrownBy(() -> eval.map(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> eval.map(_$ -> null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException().isThrownBy(() -> {
            final var mapped
                = eval.map(fun);
            assertThat(fun.getInvocationCount())
                .isEqualTo(1);
            assertThat(mapped.get())
                .isEqualTo(SOME_STRING_VALUE);
            mapped.get();
            assertThat(fun.getInvocationCount())
                .isEqualTo(1);
            assertThat(mapped.get())
                .isEqualTo(SOME_STRING_VALUE);
        });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testLaterMap() {
        LOGGER.info("Test Eval.Later.map");

        final var eval
            = evalLater(SOME_STRING_VALUE);
        final InvocationCountingFun<String, String> fun
            = invocationCountingFun(Fun.identity());

        assertThatThrownBy(() -> eval.map(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> eval.map(_$ -> null).get())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var mapped
                    = eval.map(fun);
                assertThat(fun.getInvocationCount())
                    .isEqualTo(0);
                assertThat(mapped.get())
                    .isEqualTo(SOME_STRING_VALUE);
                assertThat(fun.getInvocationCount())
                    .isEqualTo(1);
                mapped.get();
                assertThat(fun.getInvocationCount())
                    .isEqualTo(1);
                assertThat(mapped.get())
                    .isEqualTo(SOME_STRING_VALUE);
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testAlwaysMap() {
        LOGGER.info("Test Eval.Always.map");

        final var eval
            = evalAlways(SOME_STRING_VALUE);
        final InvocationCountingFun<String, String> fun
            = invocationCountingFun(Fun.identity());

        assertThatThrownBy(() -> eval.map(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> eval.map(_$ -> null).get())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException().
            isThrownBy(() -> {
                final var mapped
                    = eval.map(fun);
                assertThat(fun.getInvocationCount())
                    .isEqualTo(0);
                assertThat(mapped.get())
                    .isEqualTo(SOME_STRING_VALUE);
                assertThat(fun.getInvocationCount())
                    .isEqualTo(1);
                mapped.get();
                assertThat(fun.getInvocationCount())
                    .isEqualTo(2);
                assertThat(mapped.get())
                    .isEqualTo(SOME_STRING_VALUE);
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testNowLift() {
        LOGGER.info("Test Eval.Now.lift");

        final var eval
            = evalNow(SOME_STRING_VALUE);
        final InvocationCountingFun<String, String> fun
            = invocationCountingFun(Fun.identity());

        assertThatThrownBy(() -> eval.lift(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> eval.lift(evalAlways(_$ -> null)).get())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException().isThrownBy(() -> {
            final var lifted
                = eval.lift(evalNow(fun));
            assertThat(fun.getInvocationCount())
                .isEqualTo(1);
            assertThat(lifted.get())
                .isEqualTo(SOME_STRING_VALUE);
            lifted.get();
            assertThat(fun.getInvocationCount())
                .isEqualTo(1);
            assertThat(lifted.get())
                .isEqualTo(SOME_STRING_VALUE);
        });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testLaterLift() {
        LOGGER.info("Test Eval.Later.lift");

        final var eval
            = evalLater(SOME_STRING_VALUE);
        final InvocationCountingFun<String, String> fun
            = invocationCountingFun(Fun.identity());

        assertThatThrownBy(() -> eval.lift(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> eval.lift(evalAlways(_$ -> null)).get())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException().isThrownBy(() -> {
            final var lifted
                = eval.lift(evalLater(fun));
            assertThat(fun.getInvocationCount())
                .isEqualTo(0);
            assertThat(lifted.get())
                .isEqualTo(SOME_STRING_VALUE);
            lifted.get();
            assertThat(fun.getInvocationCount())
                .isEqualTo(1);
            assertThat(lifted.get())
                .isEqualTo(SOME_STRING_VALUE);
        });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testAlwaysLift() {
        LOGGER.info("Test Eval.Always.lift");

        final var eval
            = evalAlways(SOME_STRING_VALUE);
        final InvocationCountingFun<String, String> fun
            = invocationCountingFun(Fun.identity());

        assertThatThrownBy(() -> eval.lift(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> eval.lift(evalAlways(_$ -> null)).get())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException().
            isThrownBy(() -> {
                final var lifted
                    = eval.lift(evalAlways(fun));
                assertThat(fun.getInvocationCount())
                    .isEqualTo(0);
                assertThat(lifted.get())
                    .isEqualTo(SOME_STRING_VALUE);
                assertThat(fun.getInvocationCount())
                    .isEqualTo(1);
                lifted.get();
                assertThat(fun.getInvocationCount())
                    .isEqualTo(2);
                assertThat(lifted.get())
                    .isEqualTo(SOME_STRING_VALUE);
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testNowBind() {
        LOGGER.info("Test Eval.Now.bind");

        final var eval
            = evalNow(SOME_STRING_VALUE);
        final InvocationCountingFun<String, Eval<Integer>> fun
            = invocationCountingFun(s -> evalNow(s.length()));

        assertThatThrownBy(() -> eval.bind(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> eval.bind(_$ -> null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var bound
                    = eval.bind(fun);
                assertThat(fun.getInvocationCount())
                    .isEqualTo(1);
                assertThat(bound.get())
                    .isEqualTo(SOME_STRING_VALUE.length());
                bound.get();
                assertThat(fun.getInvocationCount())
                    .isEqualTo(1);
                assertThat(bound.get())
                    .isEqualTo(SOME_STRING_VALUE.length());
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testLaterBind() {
        LOGGER.info("Test Eval.Later.bind");

        final var eval
            = evalLater(SOME_STRING_VALUE);
        final InvocationCountingFun<String, Eval<Integer>> fun
            = invocationCountingFun(s -> evalLater(s.length()));

        assertThatThrownBy(() -> eval.bind(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> eval.bind(_$ -> null).get())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var bound
                    = eval.bind(fun);
                assertThat(fun.getInvocationCount())
                    .isEqualTo(0);
                assertThat(bound.get())
                    .isEqualTo(SOME_STRING_VALUE.length());
                assertThat(fun.getInvocationCount())
                    .isEqualTo(1);
                bound.get();
                assertThat(fun.getInvocationCount())
                    .isEqualTo(1);
                assertThat(bound.get())
                    .isEqualTo(SOME_STRING_VALUE.length());
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testAlwaysBind() {
        LOGGER.info("Test Eval.Always.bind");

        final var eval
            = evalAlways(SOME_STRING_VALUE);
        final InvocationCountingFun<String, Eval<Integer>> fun
            = invocationCountingFun(s -> evalAlways(s.length()));

        assertThatThrownBy(() -> eval.bind(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> eval.bind(_$ -> null).get())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var bound
                    = eval.bind(fun);
                assertThat(fun.getInvocationCount())
                    .isEqualTo(0);
                assertThat(bound.get())
                    .isEqualTo(SOME_STRING_VALUE.length());
                assertThat(fun.getInvocationCount())
                    .isEqualTo(1);
                bound.get();
                assertThat(fun.getInvocationCount())
                    .isEqualTo(2);
                assertThat(bound.get())
                    .isEqualTo(SOME_STRING_VALUE.length());
            });
    }

    @Test
    void testNowUnwind() {
        LOGGER.info("Test Eval.Now.unwind");

        final var eval
            = evalNow(SOME_STRING_VALUE);
        final InvocationCountingFun<String, String> fun
            = invocationCountingFun(Fun.identity());
        final var mapped
            = eval.map(fun);

        assertThat(fun.getInvocationCount())
            .isEqualTo(1);
        final var unwound
            = mapped.unwind();
        assertThat(unwound.get())
            .isEqualTo(SOME_STRING_VALUE);
        assertThat(fun.getInvocationCount())
            .isEqualTo(1);
        mapped.get();
        assertThat(fun.getInvocationCount())
            .isEqualTo(1);
        unwound.get();
        assertThat(fun.getInvocationCount())
            .isEqualTo(1);
    }

    @Test
    void testLaterUnwind() {
        LOGGER.info("Test Eval.Later.unwind");

        final var eval
            = evalLater(SOME_STRING_VALUE);
        final InvocationCountingFun<String, String> fun
            = invocationCountingFun(Fun.identity());
        final var mapped
            = eval.map(fun);

        assertThat(fun.getInvocationCount())
            .isEqualTo(0);
        final var unwound
            = mapped.unwind();
        assertThat(unwound.get())
            .isEqualTo(SOME_STRING_VALUE);
        assertThat(fun.getInvocationCount())
            .isEqualTo(1);
        unwound.get();
        assertThat(fun.getInvocationCount())
            .isEqualTo(1);
    }

    @Test
    void testAlwaysUnwind() {
        LOGGER.info("Test Eval.Always.unwind");

        final var eval
            = evalAlways(SOME_STRING_VALUE);
        final InvocationCountingFun<String, String> fun
            = invocationCountingFun(Fun.identity());
        final var mapped
            = eval.map(fun);

        assertThat(fun.getInvocationCount())
            .isEqualTo(0);
        final var unwound
            = mapped.unwind();
        assertThat(unwound.get())
            .isEqualTo(SOME_STRING_VALUE);
        assertThat(fun.getInvocationCount())
            .isEqualTo(1);
        mapped.unwind().get();
        assertThat(fun.getInvocationCount())
            .isEqualTo(2);
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testTransmogrify() {
        LOGGER.info("Test Eval.*.transmogrify");

        final var eval
            = evalNow(SOME_STRING_VALUE);

        assertThatThrownBy(() -> eval.transmogrify(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> eval.transmogrify(_$ -> null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> eval.transmogrify(Value::get));
    }
}
