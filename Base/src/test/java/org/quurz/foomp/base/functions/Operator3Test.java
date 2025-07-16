package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.slf4j.LoggerFactory.getLogger;

class Operator3Test {

    private static final Logger LOGGER
        = getLogger(Operator3Test.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAndThen() {
        LOGGER.info("Test operator3.andThen");

        final Operator3<Integer> operator3
            = (a1, a2, a3) -> a1 + a2 + a3;

        assertThatThrownBy(() -> operator3.andThen(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.andThen(a -> null).apply(5, 5, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.andThen(a -> a).apply(5, 5, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.andThen(a -> a).apply(5, null, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.andThen(a -> a).apply(5, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.andThen(a -> a).apply(null, 5, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.andThen(a -> a).apply(null, 5, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.andThen(a -> a).apply(null, null, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.andThen(a -> a).apply(null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = operator3.andThen(a -> a).apply(5, 5, 5);
                assertThat(result)
                    .isEqualTo(15);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPartial1() {
        LOGGER.info("Test operator3.partial1");

        final Operator3<Integer> operator3
            = (a1, a2, a3) -> a1 + a2 + a3;

        assertThatThrownBy(() -> operator3.partial1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.partial1(() -> null).apply(5, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.partial1(() -> 5).apply(5, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.partial1(() -> 5).apply(null, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.partial1(() -> 5).apply(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result = operator3.partial1(() -> 5).apply(5, 5);
                assertThat(result)
                    .isEqualTo(15);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPartial2() {
        LOGGER.info("Test operator3.partial2");

        final Operator3<Integer> operator3
            = (a1, a2, a3) -> a1 + a2 + a3;

        assertThatThrownBy(() -> operator3.partial2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.partial2(() -> null).apply(5, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.partial2(() -> 5).apply(5, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.partial2(() -> 5).apply(null, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.partial2(() -> 5).apply(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result = operator3.partial2(() -> 5).apply(5, 5);
                assertThat(result)
                    .isEqualTo(15);
                });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPartial3() {
        LOGGER.info("Test operator3.partial3");

        final Operator3<Integer> operator3
            = (a1, a2, a3) -> a1 + a2 + a3;

        assertThatThrownBy(() -> operator3.partial3(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.partial3(() -> null).apply(5, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.partial3(() -> 5).apply(5, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.partial3(() -> 5).apply(null, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator3.partial3(() -> 5).apply(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result = operator3.partial3(() -> 5).apply(5, 5);
                assertThat(result)
                    .isEqualTo(15);
                });
    }

}
