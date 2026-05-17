package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.functions.Applicable.applicable;
import static org.slf4j.LoggerFactory.getLogger;

class ApplicableTest
        extends TestHelper {

    private final Logger LOGGER
        = getLogger(ApplicableTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testApplicable() {
        LOGGER.info("Test Applicable.applicable with null function");

        assertThatThrownBy(() -> applicable(null))
            .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("unused")
    @Test
    void testApplicableWithNullReturningFunction() {
        LOGGER.info("Test Applicable.applicable with null returning function");

        assertThatThrownBy(() -> applicable(_$ -> null).apply(""))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testApplicableWithValidFunction() {
        LOGGER.info("Test Applicable.applicable with valid function");

        assertThatNoException()
            .isThrownBy(() -> {
                final Applicable<Integer, Integer> applicable
                    = applicable(i -> i + 5);

                assertThat(applicable.apply(5))
                    .isEqualTo(10);
            });
    }

    @Test
    void testSafe() {
        LOGGER.info("Test applicable.safen");

        final Applicable<Integer, Integer> applicable
            = i -> {
                if (i != 0) {
                    return i;
                } else {
                    throw new IllegalArgumentException();
                }
            };

        assertThatNoException()
            .isThrownBy(() -> applicable.safe().apply(1));
        assertThatNoException()
            .isThrownBy(() -> applicable.safe().apply(0));
        checkIsRightWithValue(applicable.safe().apply(1), 1);
        checkIsLeftWithPredicate(
            applicable.safe().apply(0),
            expected -> expected instanceof IllegalArgumentException
        );

    }


    @Test
    void testIdentity() {
        LOGGER.info("Test Applicable.identity");

        assertThatNoException()
            .isThrownBy((() -> {
                final var result
                    = Applicable.identity().apply(1);
                assertThat(result)
                    .isEqualTo(1);
            }));
    }

}
