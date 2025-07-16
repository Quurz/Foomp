package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.functions.Komparator.komparator;
import static org.slf4j.LoggerFactory.getLogger;

class KomparatorTest {

    private static final Logger LOGGER
        = getLogger(KomparatorTest.class);

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testKomparator() {
        LOGGER.info("Test Komparator.komparator");

        assertThatThrownBy(() -> komparator(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> komparator((_1, _2) -> 0));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testKompareAndApply() {
        LOGGER.info("Test komparator.kompare and komparator.apply");

        final Komparator<Integer> komparator
            = komparator(Integer::compare);

        assertThatThrownBy(() -> komparator.kompare(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> komparator.kompare(null, 0))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> komparator.kompare(0, null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> komparator.apply(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> komparator.apply(null, 0))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> komparator.apply(0, null))
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> {
                assertThat(komparator.kompare(0, 0))
                    .isEqualTo(Komparator.Komparison.EQUAL);
                assertThat(komparator.kompare(0, 1))
                    .isEqualTo(Komparator.Komparison.LESS);
                assertThat(komparator.kompare(1, 0))
                    .isEqualTo(Komparator.Komparison.GREATER);

                assertThat(komparator.apply(0, 0))
                    .isEqualTo(Komparator.Komparison.EQUAL);
                assertThat(komparator.apply(0, 1))
                    .isEqualTo(Komparator.Komparison.LESS);
                assertThat(komparator.apply(1, 0))
                    .isEqualTo(Komparator.Komparison.GREATER);
            });
    }

}
