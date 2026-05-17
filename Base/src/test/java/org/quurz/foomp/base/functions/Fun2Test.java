package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.functions.Fun2.fun2;
import static org.quurz.foomp.base.functions.Fun2.uncurry;
import static org.slf4j.LoggerFactory.getLogger;

class Fun2Test
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(Fun2Test.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testFun2WithNullArgument() {
        LOGGER.info("Test Fun2.fun2 with null-argument");

        assertThatThrownBy(() -> fun2(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testFun2WithValidArgument() {
        LOGGER.info("Test Fun2.fun2 with valid argument");

        final BiFunction<Integer, Integer, Integer> biFunction
            = Integer::sum;
        assertThatNoException()
            .isThrownBy(() -> {
                final var fun2
                    = fun2(biFunction);
                assertThat(fun2.apply(5, 5))
                    .isEqualTo(biFunction.apply(5, 5));
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAndThenWithNullArgument() {
        LOGGER.info("Test fun2.andThen with null-argument");

        final BiFunction<Integer, Integer, Integer> biFunction
            = Integer::sum;
        assertThatThrownBy(() -> {
                final Fun2<Integer, Integer, Integer> fun
                    = fun2(biFunction);
                fun.andThen(null);
            })
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testAndThenWithValidArgument() {
        LOGGER.info("Test fun2.andThen with valid argument");

        final BiFunction<Integer, Integer, Integer> biFunction
            = Integer::sum;
        final Function<Integer, Integer> fun
            = i -> i * 2;
        assertThatNoException()
            .isThrownBy(() -> {
                final var fun2
                    = fun2(biFunction);
                final var next
                    = fun2.andThen(fun);

                assertThat(next.apply(1, 2))
                    .isEqualTo(6);
            });
    }


    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPartial1WithNullArgument() {
        LOGGER.info("Test fun2.applyPartial1 with null-argument");

        final BiFunction<Integer, Integer, Integer> biFunction
            = Integer::sum;
        assertThatThrownBy(() -> fun2(biFunction).partial1(null))
            .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testApplyPartial1ApplyWithNullArgument() {
        LOGGER.info("Test fun2.applyPartial1.apply with null-argument");

        final BiFunction<Integer, Integer, Integer> biFunction
            = Integer::sum;
        assertThatThrownBy(() -> fun2(biFunction).partial1(() -> 5).apply(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial1WithNullReturningSupplier() {
        LOGGER.info("Test fun2.applyPartial1 with null returning supplier");

        final BiFunction<Integer, Integer, Integer> biFunction
            = Integer::sum;
        assertThatThrownBy(() -> fun2(biFunction).partial1(() -> null).apply(5))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial1WithValidArguments() {
        LOGGER.info("Test fun2.applyPartial1 with valid arguments");

        final BiFunction<Integer, Integer, Integer> biFunction
            = Integer::sum;
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = fun2(biFunction).partial1(() -> 5).apply(5);
                assertThat(result)
                    .isEqualTo(10);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPartial2WithNullArgument() {
        LOGGER.info("Test fun2.applyPartial2 with null-argument");

        final BiFunction<Integer, Integer, Integer> biFunction
            = Integer::sum;
        assertThatThrownBy(() -> fun2(biFunction).partial2(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial2WithNullReturningSupplier() {
        LOGGER.info("Test fun2.applyPartial2 with null returning supplier");

        final BiFunction<Integer, Integer, Integer> biFunction
            = Integer::sum;
        assertThatThrownBy(() -> fun2(biFunction).partial2(() -> null).apply(5))
            .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testApplyPartial2ApplyWithNullArgument() {
        LOGGER.info("Test fun2.applyPartial2.apply with null-argument");

        final BiFunction<Integer, Integer, Integer> biFunction
            = Integer::sum;
        assertThatThrownBy(() -> fun2(biFunction).partial2(() -> 5).apply(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial2WithValidArguments() {
        LOGGER.info("Test fun2.applyPartial2 with valid arguments");

        final BiFunction<Integer, Integer, Integer> biFunction
            = Integer::sum;
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = fun2(biFunction).partial2(() -> 5).apply(5);
                assertThat(result)
                    .isEqualTo(10);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testFlip() {
        LOGGER.info("Test fun2.flip");

        final Fun2<String, String, String> fun2
            = String::concat;
        final var flippedFun2
            = fun2.flip();

        assertThatThrownBy(() -> flippedFun2.apply(null, SOME_STRING_VALUE))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> flippedFun2.apply(SOME_STRING_VALUE, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> flippedFun2.apply(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = flippedFun2.apply(SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE);
                assertThat(result)
                    .isEqualTo(SOME_OTHER_STRING_VALUE.concat(SOME_STRING_VALUE));
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testCurryWithNullArguments() {
        LOGGER.info("Test fun2.curry with null-arguments");

        final BiFunction<Integer, Integer, Integer> biFunction
            = Integer::sum;
        assertThatThrownBy(() -> fun2(biFunction).curry().apply(null).apply(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun2(biFunction).curry().apply(null).apply(5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun2(biFunction).curry().apply(5).apply(null))
            .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testCurryOnNullReturningFun2() {
        LOGGER.info("Test fun2.curry on null returning Fun2");

        final Fun2<Integer, Integer, Integer> fun2
            = (_i1, _i2) -> null;

        assertThatThrownBy(() -> fun2.curry().apply(5).apply(5))
            .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testUncurryWithInvalidArgument() {
        LOGGER.info("Test Fun2.uncurry with invalid argument");

        final Fun<Integer, Fun<Integer, Integer>> fun1
            = _$ -> null;
        final Fun<Integer, Fun<Integer, Integer>> fun2
            = _i1 -> _i2 -> null;

        assertThatThrownBy(() -> uncurry(null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> uncurry(fun1).apply(5, 5))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> uncurry(fun2).apply(5, 5))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testUncurryWithValidArgument() {
        LOGGER.info("Test Fun2.uncurry with valid argument");

        final Fun<Integer, Fun<Integer, Integer>> curried
            = i1 -> i2 -> i1 + i2;

        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = uncurry(curried).apply(5, 5);
                assertThat(result)
                    .isEqualTo(curried.apply(5).apply(5));
            });
    }

}
