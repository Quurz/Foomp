package org.quurz.foomp.base.functions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import java.util.function.BinaryOperator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.functions.Operator2.operator2;
import static org.slf4j.LoggerFactory.getLogger;

class Operator2Test
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(Operator2Test.class);

    @SuppressWarnings({"DataFlowIssue", "ResultOfMethodCallIgnored"})
    @Test
    void testOperator2() {
        LOGGER.info("Test Operator2.operator2");

        final BinaryOperator<Integer> binaryOperator
            = Integer::sum;

        assertThatThrownBy(() -> operator2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator2((a1, a2) -> null).apply(5, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator2(binaryOperator).apply(null, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator2(binaryOperator).apply(5, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator2(binaryOperator).apply(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = operator2(binaryOperator).apply(5, 5);
                assertThat(result)
                    .isEqualTo(binaryOperator.apply(5, 5));
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAndThen() {
        LOGGER.info("Test operator2.andThen");

        final Operator2<Integer> operator2
            = Integer::sum;

        assertThatThrownBy(() -> operator2.andThen(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator2.andThen(a -> null).apply(5, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator2.andThen(a -> a + 5).apply(null, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator2.andThen(a -> a + 5).apply(5, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator2.andThen(a -> a + 5).apply(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result = operator2.andThen(a -> a + 5).apply(5, 5);
                assertThat(result)
                    .isEqualTo(15);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPartial1() {
        LOGGER.info("Test operator2.partial1");

        final Operator2<Integer> operator2
            = Integer::sum;

        assertThatThrownBy(() -> operator2.partial1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator2.partial1(() -> null).apply(5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator2.partial1(() -> 5).apply(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = operator2.partial1(() -> 5).apply(5);
                assertThat(result)
                    .isEqualTo(10);
            });
    }

    @Test
    void testPartial2() {
        LOGGER.info("Test operator2.partial2");

        final Operator2<Integer> operator2
            = Integer::sum;

        assertThatThrownBy(() -> operator2.partial2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator2.partial2(() -> null).apply(5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> operator2.partial2(() -> 5).apply(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = operator2.partial2(() -> 5).apply(5);
                assertThat(result)
                    .isEqualTo(10);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testFlip() {
        LOGGER.info("Test operator2.flip");

        final Operator2<String> operator2
                = String::concat;
        final var flippedOperator2
                = operator2.flip();

        Assertions.assertThatThrownBy(() -> flippedOperator2.apply(null, SOME_STRING_VALUE))
            .isInstanceOf(NullPointerException.class);
        Assertions.assertThatThrownBy(() -> flippedOperator2.apply(SOME_STRING_VALUE, null))
            .isInstanceOf(NullPointerException.class);
        Assertions.assertThatThrownBy(() -> flippedOperator2.apply(null, null))
            .isInstanceOf(NullPointerException.class);
        Assertions.assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = flippedOperator2.apply(SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE);
                Assertions.assertThat(result)
                    .isEqualTo(SOME_OTHER_STRING_VALUE.concat(SOME_STRING_VALUE));
            });
    }

}
