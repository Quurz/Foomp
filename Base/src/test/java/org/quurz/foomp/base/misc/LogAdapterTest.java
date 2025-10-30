package org.quurz.foomp.base.misc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.misc.LogAdapter.delegatingLogAdapter;
import static org.quurz.foomp.base.misc.LogAdapter.noOpLogAdapter;
import static org.slf4j.LoggerFactory.getLogger;

class LogAdapterTest {

    private static final Logger LOGGER
        = getLogger(LogAdapterTest.class);

    @Nested
    @DisplayName("NoOpLogAdapter tests")
    class NoOpLogAdapterTests {

        private LogAdapter adapter;

        @BeforeEach
        void setUp() {
            adapter = noOpLogAdapter();
        }

        @Test
        @DisplayName("noOpLogAdapter() returns non-null instance")
        void noOpLogAdapterReturnsNonNullInstance() {
            LOGGER.info("Test noOpLogAdapter() returns non-null instance");

            assertThat(adapter).isNotNull();
        }

        @Test
        @DisplayName("noOpLogAdapter() returns same instance (singleton)")
        void noOpLogAdapterReturnsSingletonInstance() {
            LOGGER.info("Test noOpLogAdapter() returns singleton");

            final var adapter1 = noOpLogAdapter();
            final var adapter2 = noOpLogAdapter();

            assertThat(adapter1).isSameAs(adapter2);
        }

        @Test
        @DisplayName("noOpLogger() returns non-null instance")
        void noOpLoggerReturnsNonNullInstance() {
            LOGGER.info("Test noOpLogger() returns non-null instance");

            final var logger = LogAdapter.noOpLogger();

            assertThat(logger).isNotNull();
        }

        @Test
        @DisplayName("noOpLogger() returns same instance as noOpLogAdapter()")
        void noOpLoggerReturnsSameInstanceAsNoOpLogAdapter() {
            LOGGER.info("Test noOpLogger() returns same instance as noOpLogAdapter()");

            final var logger = LogAdapter.noOpLogger();
            final var adapter = noOpLogAdapter();

            assertThat(logger).isSameAs(adapter);
        }

        @Test
        @DisplayName("noOpLogger() returns singleton")
        void noOpLoggerReturnsSingleton() {
            LOGGER.info("Test noOpLogger() returns singleton");

            final var logger1 = LogAdapter.noOpLogger();
            final var logger2 = LogAdapter.noOpLogger();

            assertThat(logger1).isSameAs(logger2);
        }

        @Test
        @DisplayName("debug(message, args) accepts valid input")
        void debugWithArgsAcceptsValidInput() {
            LOGGER.info("Test debug(message, args) accepts valid input");

            // Should not throw
            adapter.debug("Test message", "arg1", "arg2");
        }

        @Test
        @DisplayName("debug(message) accepts valid input")
        void debugWithoutArgsAcceptsValidInput() {
            LOGGER.info("Test debug(message) accepts valid input");

            // Should not throw
            adapter.debug("Test message");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("debug(message, args) throws on null message")
        void debugWithArgsThrowsOnNullMessage() {
            LOGGER.info("Test debug(message, args) throws on null message");

            assertThatThrownBy(() -> adapter.debug(null, "arg1"))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("debug(message, args) throws on null args")
        void debugWithArgsThrowsOnNullArgs() {
            LOGGER.info("Test debug(message, args) throws on null args");

            assertThatThrownBy(() -> adapter.debug("message", (Object[]) null))
                .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("debug(message) throws on null message")
        void debugWithoutArgsThrowsOnNullMessage() {
            LOGGER.info("Test debug(message) throws on null message");

            assertThatThrownBy(() -> adapter.debug(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("info(message, args) accepts valid input")
        void infoWithArgsAcceptsValidInput() {
            LOGGER.info("Test info(message, args) accepts valid input");

            adapter.info("Test message", "arg1", "arg2");
        }

        @Test
        @DisplayName("info(message) accepts valid input")
        void infoWithoutArgsAcceptsValidInput() {
            LOGGER.info("Test info(message) accepts valid input");

            adapter.info("Test message");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("info(message, args) throws on null message")
        void infoWithArgsThrowsOnNullMessage() {
            LOGGER.info("Test info(message, args) throws on null message");

            assertThatThrownBy(() -> adapter.info(null, "arg1"))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("info(message, args) throws on null args")
        void infoWithArgsThrowsOnNullArgs() {
            LOGGER.info("Test info(message, args) throws on null args");

            assertThatThrownBy(() -> adapter.info("message", (Object[]) null))
                .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("info(message) throws on null message")
        void infoWithoutArgsThrowsOnNullMessage() {
            LOGGER.info("Test info(message) throws on null message");

            assertThatThrownBy(() -> adapter.info(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("warn(message, args) accepts valid input")
        void warnWithArgsAcceptsValidInput() {
            LOGGER.info("Test warn(message, args) accepts valid input");

            adapter.warn("Test message", "arg1", "arg2");
        }

        @Test
        @DisplayName("warn(message) accepts valid input")
        void warnWithoutArgsAcceptsValidInput() {
            LOGGER.info("Test warn(message) accepts valid input");

            adapter.warn("Test message");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("warn(message, args) throws on null message")
        void warnWithArgsThrowsOnNullMessage() {
            LOGGER.info("Test warn(message, args) throws on null message");

            assertThatThrownBy(() -> adapter.warn(null, "arg1"))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("warn(message, args) throws on null args")
        void warnWithArgsThrowsOnNullArgs() {
            LOGGER.info("Test warn(message, args) throws on null args");

            assertThatThrownBy(() -> adapter.warn("message", (Object[]) null))
                .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("warn(message) throws on null message")
        void warnWithoutArgsThrowsOnNullMessage() {
            LOGGER.info("Test warn(message) throws on null message");

            assertThatThrownBy(() -> adapter.warn(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("error(message, args) accepts valid input")
        void errorWithArgsAcceptsValidInput() {
            LOGGER.info("Test error(message, args) accepts valid input");

            adapter.error("Test message", "arg1", "arg2");
        }

        @Test
        @DisplayName("error(message) accepts valid input")
        void errorWithoutArgsAcceptsValidInput() {
            LOGGER.info("Test error(message) accepts valid input");

            adapter.error("Test message");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("error(message, args) throws on null message")
        void errorWithArgsThrowsOnNullMessage() {
            LOGGER.info("Test error(message, args) throws on null message");

            assertThatThrownBy(() -> adapter.error(null, "arg1"))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("error(message, args) throws on null args")
        void errorWithArgsThrowsOnNullArgs() {
            LOGGER.info("Test error(message, args) throws on null args");

            assertThatThrownBy(() -> adapter.error("message", (Object[]) null))
                .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("error(message) throws on null message")
        void errorWithoutArgsThrowsOnNullMessage() {
            LOGGER.info("Test error(message) throws on null message");

            assertThatThrownBy(() -> adapter.error(null))
                .isInstanceOf(NullPointerException.class);
        }

    }

    @Nested
    @DisplayName("DelegatingLogAdapter tests")
    class DelegatingLogAdapterTests {

        private List<String> debugMessages;
        private List<String> infoMessages;
        private List<String> warnMessages;
        private List<String> errorMessages;

        private BiConsumer<String, Object[]> debugDelegator;
        private BiConsumer<String, Object[]> infoDelegator;
        private BiConsumer<String, Object[]> warnDelegator;
        private BiConsumer<String, Object[]> errorDelegator;

        private LogAdapter adapter;

        @BeforeEach
        void setUp() {
            debugMessages = new ArrayList<>();
            infoMessages = new ArrayList<>();
            warnMessages = new ArrayList<>();
            errorMessages = new ArrayList<>();

            debugDelegator = (msg, args) -> debugMessages.add(msg + ":" + args.length);
            infoDelegator = (msg, args) -> infoMessages.add(msg + ":" + args.length);
            warnDelegator = (msg, args) -> warnMessages.add(msg + ":" + args.length);
            errorDelegator = (msg, args) -> errorMessages.add(msg + ":" + args.length);

            adapter = delegatingLogAdapter(debugDelegator, infoDelegator, warnDelegator, errorDelegator);
        }

        @Test
        @DisplayName("delegatingLogAdapter() returns non-null instance")
        void delegatingLogAdapterReturnsNonNullInstance() {
            LOGGER.info("Test delegatingLogAdapter() returns non-null instance");

            assertThat(adapter).isNotNull();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("delegatingLogAdapter() throws on null debugDelegator")
        void delegatingLogAdapterThrowsOnNullDebugDelegator() {
            LOGGER.info("Test delegatingLogAdapter() throws on null debugDelegator");

            assertThatThrownBy(() -> delegatingLogAdapter(null, infoDelegator, warnDelegator, errorDelegator))
                .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("delegatingLogAdapter() throws on null infoDelegator")
        void delegatingLogAdapterThrowsOnNullInfoDelegator() {
            LOGGER.info("Test delegatingLogAdapter() throws on null infoDelegator");

            assertThatThrownBy(() -> delegatingLogAdapter(debugDelegator, null, warnDelegator, errorDelegator))
                .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("delegatingLogAdapter() throws on null warnDelegator")
        void delegatingLogAdapterThrowsOnNullWarnDelegator() {
            LOGGER.info("Test delegatingLogAdapter() throws on null warnDelegator");

            assertThatThrownBy(() -> delegatingLogAdapter(debugDelegator, infoDelegator, null, errorDelegator))
                .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("delegatingLogAdapter() throws on null errorDelegator")
        void delegatingLogAdapterThrowsOnNullErrorDelegator() {
            LOGGER.info("Test delegatingLogAdapter() throws on null errorDelegator");

            assertThatThrownBy(() -> delegatingLogAdapter(debugDelegator, infoDelegator, warnDelegator, null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("debug(message, args) delegates correctly")
        void debugWithArgsDelegatesCorrectly() {
            LOGGER.info("Test debug(message, args) delegates correctly");

            adapter.debug("Debug message", "arg1", "arg2");

            assertThat(debugMessages).hasSize(1);
            assertThat(debugMessages.getFirst()).isEqualTo("Debug message:2");
        }

        @Test
        @DisplayName("debug(message) delegates with empty args")
        void debugWithoutArgsDelegatesWithEmptyArgs() {
            LOGGER.info("Test debug(message) delegates with empty args");

            adapter.debug("Debug message");

            assertThat(debugMessages).hasSize(1);
            assertThat(debugMessages.getFirst()).isEqualTo("Debug message:0");
        }

        @Test
        @DisplayName("info(message, args) delegates correctly")
        void infoWithArgsDelegatesCorrectly() {
            LOGGER.info("Test info(message, args) delegates correctly");

            adapter.info("Info message", "arg1", "arg2", "arg3");

            assertThat(infoMessages).hasSize(1);
            assertThat(infoMessages.getFirst()).isEqualTo("Info message:3");
        }

        @Test
        @DisplayName("info(message) delegates with empty args")
        void infoWithoutArgsDelegatesWithEmptyArgs() {
            LOGGER.info("Test info(message) delegates with empty args");

            adapter.info("Info message");

            assertThat(infoMessages).hasSize(1);
            assertThat(infoMessages.getFirst()).isEqualTo("Info message:0");
        }

        @Test
        @DisplayName("warn(message, args) delegates correctly")
        void warnWithArgsDelegatesCorrectly() {
            LOGGER.info("Test warn(message, args) delegates correctly");

            adapter.warn("Warn message", "arg1");

            assertThat(warnMessages).hasSize(1);
            assertThat(warnMessages.getFirst()).isEqualTo("Warn message:1");
        }

        @Test
        @DisplayName("warn(message) delegates with empty args")
        void warnWithoutArgsDelegatesWithEmptyArgs() {
            LOGGER.info("Test warn(message) delegates with empty args");

            adapter.warn("Warn message");

            assertThat(warnMessages).hasSize(1);
            assertThat(warnMessages.getFirst()).isEqualTo("Warn message:0");
        }

        @Test
        @DisplayName("error(message, args) delegates correctly")
        void errorWithArgsDelegatesCorrectly() {
            LOGGER.info("Test error(message, args) delegates correctly");

            adapter.error("Error message", "arg1", "arg2");

            assertThat(errorMessages).hasSize(1);
            assertThat(errorMessages.getFirst()).isEqualTo("Error message:2");
        }

        @Test
        @DisplayName("error(message) delegates with empty args")
        void errorWithoutArgsDelegatesWithEmptyArgs() {
            LOGGER.info("Test error(message) delegates with empty args");

            adapter.error("Error message");

            assertThat(errorMessages).hasSize(1);
            assertThat(errorMessages.getFirst()).isEqualTo("Error message:0");
        }

        @Test
        @DisplayName("multiple calls delegate independently")
        void multipleCallsDelegateIndependently() {
            LOGGER.info("Test multiple calls delegate independently");

            adapter.debug("Debug 1");
            adapter.info("Info 1", "arg1");
            adapter.warn("Warn 1");
            adapter.error("Error 1", "arg1", "arg2");

            assertThat(debugMessages).hasSize(1);
            assertThat(infoMessages).hasSize(1);
            assertThat(warnMessages).hasSize(1);
            assertThat(errorMessages).hasSize(1);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("debug throws on null message")
        void debugThrowsOnNullMessage() {
            LOGGER.info("Test debug throws on null message");

            assertThatThrownBy(() -> adapter.debug(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("info throws on null args")
        void infoThrowsOnNullArgs() {
            LOGGER.info("Test info throws on null args");

            assertThatThrownBy(() -> adapter.info("message", (Object[]) null))
                .isInstanceOf(NullPointerException.class);
        }

    }

}
