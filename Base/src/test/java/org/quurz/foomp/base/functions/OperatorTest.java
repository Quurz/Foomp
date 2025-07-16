package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.functions.Operator.operator;
import static org.slf4j.LoggerFactory.getLogger;

class OperatorTest {

    private static final Logger LOGGER
        = getLogger(OperatorTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testOperator() {
        LOGGER.info("Test Operator.operator");

        assertThatThrownBy(() -> operator(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator((UnaryOperator<Integer>) i -> i + 5).apply(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator((UnaryOperator<Integer>) i -> null).apply(5))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = operator((UnaryOperator<Integer>) i -> i + 5).apply(5);
                assertThat(result)
                    .isEqualTo(10);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testCompose() {
        LOGGER.info("Test operator.compose");

        final Operator<Integer> operator
            = operator(a -> a + 5);

        assertThatThrownBy(() -> operator.compose(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator.compose(a -> null).apply(5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator.compose(a -> a + 5).apply(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = operator.compose(a -> a + 5).apply(0);
                assertThat(result)
                    .isEqualTo(10);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAndThen() {
        LOGGER.info("Test operator.andThen");

        final Operator<Integer> operator
            = operator(a -> a + 5);

        assertThatThrownBy(() -> operator.andThen(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator.andThen(a -> null).apply(5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator.andThen(a -> a + 5).apply(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = operator.andThen(a -> a + 5).apply(0);
                assertThat(result)
                    .isEqualTo(10);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testIdentity() {
        LOGGER.info("Test Operator.identity");

        assertThatThrownBy(() -> Operator.identity().apply(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = Operator.identity().apply(5);
                assertThat(result)
                    .isEqualTo(5);
            });
    }

}
