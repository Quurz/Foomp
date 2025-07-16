package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.slf4j.LoggerFactory.getLogger;

class EchoTest {

    private static final Logger LOGGER
        = getLogger(EchoTest.class);

    private final Echo echo
        = new Echo() {
            @Override
            public @NonNull String echo() {
                return Echo.class.getSimpleName();
            }
        };

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testEcho() {
        LOGGER.info("Test echo.echo(transformer)");

        assertThatThrownBy(() -> echo.echo(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> echo.echo(_$ -> null))
            .isInstanceOf(NullPointerException.class);

        assertThat(echo.echo())
            .isEqualTo(Echo.class.getSimpleName());
        assertThat(echo.echo(_$ -> "GUMPF"))
            .isEqualTo("GUMPF");
    }

}
