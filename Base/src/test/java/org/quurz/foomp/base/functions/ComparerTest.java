package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.functions.Comparer.comparer;
import static org.slf4j.LoggerFactory.getLogger;

class ComparerTest {

    private static final Logger LOGGER
        = getLogger(ComparerTest.class);

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testComparer() {
        LOGGER.info("Test Comparer.comparer");

        assertThatThrownBy(() -> comparer(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> comparer((_1, _2) -> 0));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testCompareAndApply() {
        LOGGER.info("Test comparer.compare and comparer.apply");

        final Comparer<Integer> comparer
            = comparer(Integer::compare);

        assertThatThrownBy(() -> comparer.compare(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> comparer.compare(null, 0))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> comparer.compare(0, null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> comparer.apply(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> comparer.apply(null, 0))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> comparer.apply(0, null))
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> {
                assertThat(comparer.compare(0, 0))
                    .isEqualTo(Comparer.Relation.EQUAL);
                assertThat(comparer.compare(0, 1))
                    .isEqualTo(Comparer.Relation.LESS);
                assertThat(comparer.compare(1, 0))
                    .isEqualTo(Comparer.Relation.GREATER);

                assertThat(comparer.apply(0, 0))
                    .isEqualTo(Comparer.Relation.EQUAL);
                assertThat(comparer.apply(0, 1))
                    .isEqualTo(Comparer.Relation.LESS);
                assertThat(comparer.apply(1, 0))
                    .isEqualTo(Comparer.Relation.GREATER);
            });
    }

}
