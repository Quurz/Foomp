package org.quurz.foomp.base.functions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.slf4j.LoggerFactory.getLogger;

class Fun4Test {

    private static final Logger LOGGER
        = getLogger(Fun4Test.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAndThenWithNullArgument() {
        LOGGER.info("Test fun.andThen with null-argument");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        assertThatThrownBy(() -> fun4.andThen(null))
            .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("unused")
    @Test
    void testAndThenWithNullReturningNext() {
        LOGGER.info("Test fun.andThen with null returning next");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        final Function<Integer, Integer> function
            = _$ -> null;
        assertThatThrownBy(() -> fun4.andThen(function).apply(1, 2, 3, 4))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testAndThenWithValidNext() {
        LOGGER.info("Test fun.andThen with valid next");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        final Function<Integer, Integer> function
            = x -> x * 2;
        assertThatNoException()
            .isThrownBy(() -> fun4.andThen(function).apply(1, 2, 3, 4));
        assertThat(fun4.andThen(function).apply(1, 2, 3, 4))
            .isEqualTo(20);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testDefer() {
        LOGGER.info("Test fun4.defer(...)");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;

        assertThatThrownBy(() -> fun4.defer(null, null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun4.defer(null, null, null, () -> 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun4.defer(null, null, () -> 5, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun4.defer(null, () -> 5, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun4.defer(() -> 5, null, null, null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> fun4.defer(() -> null, () -> null, () -> null, () -> null).call())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun4.defer(() -> null, () -> null, () -> null, () -> 5).call())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun4.defer(() -> null, () -> null, () -> 5, () -> null).call())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun4.defer(() -> null, () -> 5, () -> null, () -> null).call())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun4.defer(() -> 5, () -> null, () -> null, () -> null).call())
            .isInstanceOf(NullPointerException.class);

        Assertions.assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = fun4.defer(() -> 5, () -> 5, () -> 5, () -> 5).call();
                Assertions.assertThat(result)
                    .isEqualTo(20);
            });
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testPartial1WithNullSupplier() {
        LOGGER.info("Test fun4.partial1 with null-supplier");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        assertThatThrownBy(() -> fun4.partial1(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial1WithNullReturningSupplier() {
        LOGGER.info("Test fun4.partial1 with null-returning supplier");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        assertThatThrownBy(() -> fun4.partial1(() -> null).apply(2, 3, 4))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial1WithValidArgument() {
        LOGGER.info("Test fun4.partial1 with valid argument");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        Assertions.assertThatNoException()
            .isThrownBy(() -> fun4.partial1(() -> 1).apply(2, 3, 4));
        assertThat(fun4.partial1(() -> 1).apply(2, 3, 4))
            .isEqualTo(10);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testPartial2WithNullSupplier() {
        LOGGER.info("Test fun4.partial2 with null-supplier");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        assertThatThrownBy(() -> fun4.partial2(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial2WithNullReturningSupplier() {
        LOGGER.info("Test fun4.partial2 with null-returning supplier");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
    assertThatThrownBy(() -> fun4.partial2(() -> null).apply(1, 3, 4))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial2WithValidArgument() {
        LOGGER.info("Test fun4.partial2 with valid argument");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        Assertions.assertThatNoException()
            .isThrownBy(() -> fun4.partial2(() -> 2).apply(1, 3, 4));
        assertThat(fun4.partial1(() -> 2).apply(1, 3, 4))
            .isEqualTo(10);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testPartial3WithNullSupplier() {
        LOGGER.info("Test fun4.partial3 with null-supplier");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        assertThatThrownBy(() -> fun4.partial3(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial3WithNullReturningSupplier() {
        LOGGER.info("Test fun4.partial3 with null-returning supplier");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        assertThatThrownBy(() -> fun4.partial3(() -> null).apply(1, 2, 4))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial3WithValidArgument() {
        LOGGER.info("Test fun4.partial3 with valid argument");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        Assertions.assertThatNoException()
            .isThrownBy(() -> fun4.partial3(() -> 3).apply(1, 2, 4));
        assertThat(fun4.partial1(() -> 3).apply(1, 2, 4))
            .isEqualTo(10);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testPartial4WithNullSupplier() {
        LOGGER.info("Test fun4.partial4 with null-supplier");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        assertThatThrownBy(() -> fun4.partial4(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial4WithNullReturningSupplier() {
        LOGGER.info("Test fun4.partial4 with null-returning supplier");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        assertThatThrownBy(() -> fun4.partial4(() -> null).apply(1, 2, 4))
            .isInstanceOf(NullPointerException.class);
}

    @Test
    void testPartial4WithValidArgument() {
        LOGGER.info("Test fun4.partial4 with valid argument");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        Assertions.assertThatNoException()
            .isThrownBy(() -> fun4.partial4(() -> 4).apply(1, 2, 3));
        assertThat(fun4.partial1(() -> 4).apply(1, 2, 3))
            .isEqualTo(10);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testCurryWithNullArguments() {
        LOGGER.info("Test fun4.curry with null-arguments");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        final var curried
            = fun4.curry();
        assertThatThrownBy(() -> curried.apply(null).apply(2).apply(3).apply(4))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> curried.apply(1).apply(null).apply(3).apply(4))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> curried.apply(1).apply(2).apply(null).apply(4))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> curried.apply(1).apply(2).apply(3).apply(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testCurryWithValidArguments() {
        LOGGER.info("Test fun4.curry with valid arguments");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        final var curried
            = fun4.curry();
        assertThat(curried.apply(1).apply(2).apply(3).apply(4))
            .isEqualTo(fun4.apply(1, 2, 3, 4));
    }

    @Test
    void testUncurryWithNullArgument() {
        LOGGER.info("Test Fun3.uncurry with null-argument");

        assertThatThrownBy(() -> Fun4.uncurry(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testUncurryWithValidArgument() {
        LOGGER.info("Test fun4.uncurry with valid argument");

        final Fun4<Integer, Integer, Integer, Integer, Integer> fun4
            = (x1, x2, x3, x4) -> x1 + x2 + x3 + x4;
        final Function<Integer, Function<Integer, Function<Integer, Function<Integer, Integer>>>> curried
                = x1 -> x2 -> x3 -> x4 -> x1 + x2 + x3 + x4;
        Assertions.assertThatNoException().isThrownBy(
            () -> Fun4.uncurry(curried)
        );
        assertThat(Fun4.uncurry(curried).apply(1, 2, 3, 4))
            .isEqualTo(curried.apply(1).apply(2).apply(3).apply(4));
    }

}
