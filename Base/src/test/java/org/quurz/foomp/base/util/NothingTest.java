package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Nothing.nothing;
import static org.slf4j.LoggerFactory.getLogger;

class NothingTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(NothingTest.class);

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testTransmogrify() {
        LOGGER.info("test noting.transmogrify");

        assertThatThrownBy(() -> nothing.transmogrify(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> nothing.transmogrify(_$ -> null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("transmogrifier");
        assertThat((String) nothing.transmogrify(_$ -> SOME_STRING_VALUE))
            .isEqualTo(SOME_STRING_VALUE);
    }

    @SuppressWarnings("ConstantValue")
    @Test
    void testEqualsAndHashCode() {
        LOGGER.info("test noting.equals and noting.hashCode");

        final var nothing1
            = nothing;
        final var nothing2
            = nothing;
        final var nothing3
            = nothing;

        assertThat(nothing1)
            .isEqualTo(nothing2);
        assertThat(nothing2)
            .isEqualTo(nothing1);
        assertThat(nothing1.equals(nothing2) && nothing2.equals(nothing3) && nothing1.equals(nothing3))
            .isTrue();
        assertThat(nothing1.equals(nothing2) && (nothing1.hashCode() == nothing2.hashCode()))
            .isTrue();

    }

    @Test
    void testToString() {
        LOGGER.info("test noting.toString");

        assertThat(nothing.toString())
            .isEqualTo("Nothing");
    }

}
