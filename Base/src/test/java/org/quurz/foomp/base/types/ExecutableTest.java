package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestException;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.slf4j.LoggerFactory.getLogger;

class ExecutableTest {

    private static final Logger LOGGER
        = getLogger(ExecutableTest.class);

    private static final class TestExecutable
            implements Executable<String> {

        private final boolean shouldThrow;

        private TestExecutable(final boolean shouldThrow) {
            this.shouldThrow
                = shouldThrow;
        }

        @Override
        public @NonNull String execute() throws Exception {
            if (!this.shouldThrow) {
                return "EXECUTED";
            } else {
                throw new TestException("TEST");
            }
        }

    }

    private final Executable<String> executable1
        = new TestExecutable(false);
    private final Executable<String> executable2
        = new TestExecutable(true);

    @Test
    void testCall() {
        LOGGER.info("Test executable.call");

        assertThatThrownBy(executable2::call)
            .isInstanceOf(TestException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = executable1.call();
                assertThat(result)
                    .isEqualTo("EXECUTED");
            });
    }

    @Test
    void testSafe() {
        LOGGER.info("Test executable.safe");

        assertThat(executable2.safe().executeSafe().isLeft())
            .isTrue();
        assertThat(executable1.safe().executeSafe().isRight())
            .isTrue();
        assertThat(executable1.safe().executeSafe().get())
            .isEqualTo("EXECUTED");
    }

}
