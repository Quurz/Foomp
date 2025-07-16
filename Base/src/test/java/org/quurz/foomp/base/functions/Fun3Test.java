package org.quurz.foomp.base.functions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("DataFlowIssue")
class Fun3Test {

    private static final Logger LOGGER
        = getLogger(Fun3Test.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAndThenWithNullArgument() {
        LOGGER.info("Test fun3.andThen with null-argument");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        assertThatThrownBy(() -> fun3.andThen(null))
            .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("unused")
    @Test
    void testAndThenWithNullReturningNext() {
        LOGGER.info("Test fun3.andThen with null-returning next");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        final Function<Integer, Integer> function
            = i -> null;
        assertThatThrownBy(() -> fun3.andThen(function).apply(1, 2, 3))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testAndThenWithValidNext() {
        LOGGER.info("Test fun3.andThen with valid next");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        final Function<Integer, String> function
            = String::valueOf;
        assertThatNoException()
            .isThrownBy(() -> fun3.andThen(function).apply(1, 2, 3));
        assertThat(fun3.andThen(function).apply(1, 2, 3))
            .isEqualTo("6");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testDefer() {
        LOGGER.info("Test fun3.defer(...)");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (x1, x2, x3) -> x1 + x2 + x3;

        assertThatThrownBy(() -> fun3.defer(null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun3.defer(null, null, () -> 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun3.defer(null, () -> 5, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun3.defer(() -> 5, null, null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> fun3.defer(() -> null, () -> null, () -> null).call())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun3.defer(() -> null, () -> null, () -> 5).call())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun3.defer(() -> null, () -> 5, () -> null).call())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun3.defer(() -> 5, () -> null, () -> null).call())
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = fun3.defer(() -> 5, () -> 5, () -> 5).call();
                Assertions.assertThat(result)
                    .isEqualTo(15);
            });
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testPartial1WithNullSupplier() {
        LOGGER.info("Test fun3.partial1 with null-supplier");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        assertThatThrownBy(() -> fun3.partial1(null))
            .isInstanceOf(NullPointerException.class);

    }

    @Test
    void testPartial1WithNullReturningSupplier() {
        LOGGER.info("Test fun3.partial1 with null-returning supplier");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        assertThatThrownBy(() -> fun3.partial1(() -> null).apply(2, 3))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial1WithValidArgument() {
        LOGGER.info("Test fun3.partial1 with valid argument");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        assertThatNoException()
            .isThrownBy(() -> fun3.partial1(() -> 1).apply(2, 3));
        assertThat(fun3.partial1(() -> 1).apply(2, 3))
            .isEqualTo(6);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testPartial2WithNullSupplier() {
        LOGGER.info("Test fun3.partial2 with null-supplier");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        assertThatThrownBy(() -> fun3.partial2(null))
            .isInstanceOf(NullPointerException.class);

    }

    @Test
    void testPartial2WithNullReturningSupplier() {
        LOGGER.info("Test fun3.partial2 with null-returning supplier");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        assertThatThrownBy(() -> fun3.partial2(() -> null).apply(1, 3))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial2WithValidArgument() {
        LOGGER.info("Test fun3.partial2 with valid argument");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        assertThatNoException()
            .isThrownBy(() -> fun3.partial2(() -> 1).apply(1, 3));
        assertThat(fun3.partial2(() -> 2).apply(1, 3))
            .isEqualTo(6);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testPartial3WithNullSupplier() {
        LOGGER.info("Test fun3.partial3 with null-supplier");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        assertThatThrownBy(() -> fun3.partial3(null))
            .isInstanceOf(NullPointerException.class);

    }

    @Test
    void testPartial3WithNullReturningSupplier() {
        LOGGER.info("Test fun3.partial3 with null-returning supplier");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        assertThatThrownBy(() -> fun3.partial3(() -> null).apply(1, 2))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testPartial3WithValidArgument() {
        LOGGER.info("Test fun3.partial3 with valid argument");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        assertThatNoException()
            .isThrownBy(() -> fun3.partial3(() -> 3).apply(1, 2));
        assertThat(fun3.partial3(() -> 3).apply(1, 2))
            .isEqualTo(6);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testCurryWithNullArguments() {
        LOGGER.info("Test fun3.curry with null-arguments");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        final var curried
            = fun3.curry();
        assertThatThrownBy(() -> curried.apply(null).apply(2).apply(3))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> curried.apply(1).apply(null).apply(3))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> curried.apply(1).apply(2).apply(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testCurryWithValidArguments() {
        LOGGER.info("Test fun3.curry with valid arguments");

        final Fun3<Integer, Integer, Integer, Integer> fun3
            = (i1, i2, i3) -> i1 + i2 + i3;
        final var curried
            = fun3.curry();
        assertThat(curried.apply(1).apply(2).apply(3))
            .isEqualTo(fun3.apply(1, 2, 3));
    }

    @Test
    void testUncurryWithNullArgument() {
        LOGGER.info("Test Fun3.uncurry with null-argument");

        assertThatThrownBy(() -> Fun3.uncurry(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testUncurryWithValidArgument() {
        LOGGER.info("Test fun3.uncurry with valid argument");

        final Function<Integer, Function<Integer, Function<Integer, Integer>>> curried
            = x1 -> x2 -> x3 -> x1 + x2 + x3;
        assertThatNoException().isThrownBy(
            () -> Fun3.uncurry(curried)
        );
        assertThat(Fun3.uncurry(curried).apply(1, 2, 3))
            .isEqualTo(curried.apply(1).apply(2).apply(3));
    }

}
