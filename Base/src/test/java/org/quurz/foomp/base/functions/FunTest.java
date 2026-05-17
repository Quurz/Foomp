package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;
import static org.quurz.foomp.base.functions.Fun.fun;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("DataFlowIssue")
class FunTest
        extends TestHelper {

    private static final Logger LOGGER
            = getLogger(FunTest.class);

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testFunWithNullFunction() {
        LOGGER.info("Test Fun.fun with null function");

        final Function<String, String> nullReturningFunction
                = _$ -> null;
        final Function<String, String> function
                = Function.identity();

        assertThatThrownBy(() -> fun(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun(nullReturningFunction).apply(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun(nullReturningFunction).apply(SOME_STRING_VALUE))
                .isInstanceOf(NullPointerException.class);

        assertThat(fun(function).apply(SOME_STRING_VALUE))
                .isEqualTo(SOME_STRING_VALUE);
    }

    @SuppressWarnings("unused")
    @Test
    void testFunWithNullReturningFunction() {
        LOGGER.info("Test Fun.fun with null returning function");

        assertThatThrownBy(() -> fun(_$ -> null).apply(SOME_STRING_VALUE))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testFunWithValidFunction() {
        LOGGER.info("Test Fun.fun with valid function");

        final Function<Integer, Integer> function
                = i -> i + 5;
        assertThatNoException()
                .isThrownBy(() -> {
                    final var fun
                            = fun(function);

                    assertThat(fun.apply(5))
                            .isEqualTo(10);
                });
    }

    @SuppressWarnings({"unused", "DataFlowIssue"})
    @Test
    void testComposeWithNullFunction() {
        LOGGER.info("Test fun.compose with null-function");

        final Fun<Integer, Integer> fun
                = i -> i + 5;
        assertThatException()
                .isThrownBy(() -> {
                    final var _$
                            = fun.compose(null);
                }).isInstanceOf(NullPointerException.class);

    }

    @SuppressWarnings({"unused", "DataFlowIssue"})
    @Test
    void testComposeWithNullReturningAfter() {
        LOGGER.info("Test fun.compose with null-returning after");

        final Fun<Integer, Integer> fun
                = i -> null;
        final Fun<Integer, Integer> before
                = i -> i + 5;
        assertThatException()
                .isThrownBy(() -> {
                    final var _$
                            = fun.compose(before).apply(0);
                })
                .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("unused")
    @Test
    void testComposeWithNullReturningBefore() {
        LOGGER.info("Test fun.compose with null-returning before");

        final Fun<Integer, Integer> fun
                = i -> i + 5;
        final Fun<String, Integer> before
                = i -> null;
        assertThatException()
                .isThrownBy(() -> {
                    final var _$
                            = fun.compose(before).apply(SOME_STRING_VALUE);
                })
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testComposeWithValidBefore() {
        LOGGER.info("Test fun.compose with valid funs");

        Fun<Integer, Integer> fun
                = i -> i + 5;
        assertThatNoException()
                .isThrownBy(() -> {
                    final var result
                            = fun.compose(fun).apply(0);
                    assertThat(result)
                            .isEqualTo(10);
                });
    }

    @SuppressWarnings({"unused", "DataFlowIssue"})
    @Test
    void testAndThenWithNullAfter() {
        LOGGER.info("Test fun.andThen with null-fun");

        final Fun<Integer, Integer> fun
                = i -> i + 5;
        assertThatThrownBy(() -> {
            final var _$
                    = fun.andThen(null);
        })
                .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("unused")
    @Test
    void testAndThenWithNullReturningAfter() {
        LOGGER.info("Test fun.andThen with null-returning after");

        final Fun<Integer, Integer> fun
                = i -> i + 5;
        final Fun<Integer, Integer> after
                = i -> null;
        assertThatThrownBy(() -> {
            final var _$
                    = fun.andThen(after).apply(0);
        })
                .isInstanceOf(NullPointerException.class);
    }




    // ============================================
    // Tests für identity, memoise, nullSafe, etc.
    // ============================================

    @SuppressWarnings({"unused", "DataFlowIssue"})
    @Test
    void testIdentityWithNullArgument() {
        LOGGER.info("Test Fun.identity with null-argument");

        assertThatThrownBy(() -> {
            final var _$
                    = Fun.identity().apply(null);
        })
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testIdentityWithValidArgument() {
        LOGGER.info("Test Fun.identity with valid argument");

        assertThatNoException()
                .isThrownBy(() -> {
                    final var result
                            = Fun.identity().apply(SOME_STRING_VALUE);
                    assertThat(result)
                            .isEqualTo(SOME_STRING_VALUE);
                });
    }

    @Test
    void testMemoise() {
        LOGGER.info("Test fun.memoising");

        final Fun<String, String> fun
                = s -> s;
        assertThatNoException()
                .isThrownBy(fun::memoise);
        assertThat(fun.memoise())
                .isNotNull();
        assertThat(fun.memoise().apply(SOME_STRING_VALUE))
                .isEqualTo(SOME_STRING_VALUE);
    }

    @SuppressWarnings({"LambdaBodyCanBeCodeBlock", "unused"})
    @Test
    void testNullSafe() {
        LOGGER.info("Test fun.nullChecked");

        final Fun<String, String> nullReturningFun
                = _$ -> null;
        final Fun<String, String> fun
                = Fun.identity();

        assertThatThrownBy(() -> nullReturningFun.nullSafe().apply(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> nullReturningFun.nullSafe().apply(SOME_STRING_VALUE))
                .isInstanceOf(NullPointerException.class);
        assertThat(fun.nullSafe().apply(SOME_STRING_VALUE))
                .isEqualTo(SOME_STRING_VALUE);
    }

    @Test
    void testApplicable() {
        LOGGER.info("Test fun.applicable");

        final Fun<String, String> fun
                = s -> s;
        assertThatNoException()
                .isThrownBy(fun::applicable);
        assertThat(fun.applicable())
                .isNotNull();
        assertThatNoException()
                .isThrownBy(() -> {
                    final var result
                            = fun.applicable().apply(SOME_STRING_VALUE);
                    assertThat(result)
                            .isEqualTo(SOME_STRING_VALUE);
                });
    }

}