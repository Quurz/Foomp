package org.quurz.foomp.base.misc;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.BiConsumer;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Abstraction layer for logging that allows integration with any logging framework.
 *     </p>
 *     <p>
 *         This interface decouples the application from specific logging implementations,
 *         enabling users to integrate their preferred logging backend (SLF4J, Log4j2,
 *         java.util.logging, etc.) through adapter implementations.
 *     </p>
 *     <p>
 *         All logging methods accept a message and optional arguments. The interpretation
 *         of arguments (e.g., parameterized message formatting, exception handling) is
 *         delegated to the underlying logging framework implementation.
 *     </p>
 *     <p>
 *         Implementations should handle the last argument specially if it is a {@link Throwable},
 *         treating it as an exception to be logged with its stack trace.
 *     </p>
 *     <p>
 *         Example usage (syntax depends on the logging framework used):
 *     </p>
 *     <pre>{@code
 *     logger.info("Loading plugin", pluginName, version);
 *     logger.error("Failed to initialize plugin", pluginName, exception);
 *     }</pre>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface LogAdapter {

    /**
     * <div>
     *     <p>
     *         Creates a no-op logger that silently discards all log messages.
     *     </p>
     *     <p>
     *         This implementation is useful for testing scenarios or when logging is explicitly
     *         not desired. All logging methods perform null checks but are otherwise no-ops.
     *     </p>
     * </div>
     *
     * @return a no-op logger instance; never {@code null}
     *
     * @since 1.0.0
     */
    static LogAdapter noOpLogAdapter() {
        return NoOpLogAdapter.INSTANCE;
    }

    /**
     * <div>
     *     <p>
     *         Creates a delegating logger that forwards log calls to the provided consumers.
     *     </p>
     *     <p>
     *         This factory method enables easy integration with any logging framework through
     *         lambda expressions or method references. Each log level is handled by a separate
     *         {@link BiConsumer} that receives the message and arguments array.
     *     </p>
     *     <p>
     *         Example usage with SLF4J:
     *     </p>
     *     <pre>{@code
     *     Logger slf4jLogger = LoggerFactory.getLogger(MyClass.class);
     *     LogAdapter adapter = LogAdapter.delegatingLogAdapter(
     *         slf4jLogger::debug,
     *         slf4jLogger::info,
     *         slf4jLogger::warn,
     *         slf4jLogger::error
     *     );
     *     }</pre>
     * </div>
     *
     * @param debugDelegator the consumer handling debug-level messages; must not be {@code null}
     * @param infoDelegator  the consumer handling info-level messages; must not be {@code null}
     * @param warnDelegator  the consumer handling warning-level messages; must not be {@code null}
     * @param errorDelegator the consumer handling error-level messages; must not be {@code null}
     * @return a delegating log adapter instance; never {@code null}
     * @throws NullPointerException if any delegator is {@code null}
     *
     * @since 1.0.0
     */
    static @NonNull LogAdapter delegatingLogAdapter(final @NonNull BiConsumer<String, Object[]> debugDelegator,
                                                    final @NonNull BiConsumer<String, Object[]> infoDelegator,
                                                    final @NonNull BiConsumer<String, Object[]> warnDelegator,
                                                    final @NonNull BiConsumer<String, Object[]> errorDelegator) {
        Objects.requireNonNull(debugDelegator, nullValue("debugDelegator"));
        Objects.requireNonNull(infoDelegator, nullValue("infoDelegator"));
        Objects.requireNonNull(warnDelegator, nullValue("warnDelegator"));
        Objects.requireNonNull(errorDelegator, nullValue("errorDelegator"));

        return new DelegatingLogAdapter(debugDelegator, infoDelegator, warnDelegator, errorDelegator);
    }

    /**
     * <div>
     *     <p>
     *         Delegating logger implementation that forwards log calls to configurable consumers.
     *     </p>
     *     <p>
     *         This implementation enables flexible integration with any logging framework by
     *         accepting {@link BiConsumer}s for each log level. Each consumer receives the
     *         message and arguments array, allowing framework-specific formatting and handling.
     *     </p>
     *     <p>
     *         Instances should be created via {@link #delegatingLogAdapter(BiConsumer, BiConsumer, BiConsumer, BiConsumer)}.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    void debug(final @NonNull String message,
               final Object... args);

    /**
     * <div>
     *     <p>
     *         Logs a debug-level message.
     *     </p>
     *     <p>
     *         Debug messages provide detailed diagnostic information useful during development
     *         and troubleshooting. They typically include fine-grained state information and
     *         should not be enabled in production environments due to their verbosity.
     *     </p>
     * </div>
     *
     * @param message the log message; must not be {@code null}
     *
     * @since 1.0.0
     */
    void debug(final @NonNull String message);

    /**
     * <div>
     *     <p>
     *         Logs an info-level message with optional arguments.
     *     </p>
     *     <p>
     *         Info messages highlight the progress of the application at a coarse-grained level.
     *         They provide general informational messages about normal system operation and
     *         significant events or state changes.
     *     </p>
     * </div>
     *
     * @param message the log message; must not be {@code null}
     * @param args    optional arguments; interpretation depends on the logging framework
     *
     * @since 1.0.0
     */
    void info(final @NonNull String message,
              final Object... args);

    /**
     * <div>
     *     <p>
     *         Logs an info-level message.
     *     </p>
     *     <p>
     *         Info messages highlight the progress of the application at a coarse-grained level.
     *         They provide general informational messages about normal system operation and
     *         significant events or state changes.
     *     </p>
     * </div>
     *
     * @param message the log message; must not be {@code null}
     *
     * @since 1.0.0
     */
    void info(final @NonNull String message);

    /**
     * <div>
     *     <p>
     *         Logs a warning-level message with optional arguments.
     *     </p>
     *     <p>
     *         Warning messages indicate potentially harmful situations or unexpected behavior
     *         that does not prevent the system from functioning. They signal conditions that
     *         should be reviewed but do not require immediate action. Examples include deprecated
     *         API usage, suboptimal configuration, or recoverable errors.
     *     </p>
     * </div>
     *
     * @param message the log message; must not be {@code null}
     * @param args    optional arguments; interpretation depends on the logging framework
     *
     * @since 1.0.0
     */
    void warn(final @NonNull String message,
              final Object... args);

    /**
     * <div>
     *     <p>
     *         Logs a warning-level message.
     *     </p>
     *     <p>
     *         Warning messages indicate potentially harmful situations or unexpected behavior
     *         that does not prevent the system from functioning. They signal conditions that
     *         should be reviewed but do not require immediate action. Examples include deprecated
     *         API usage, suboptimal configuration, or recoverable errors.
     *     </p>
     * </div>
     *
     * @param message the log message; must not be {@code null}
     *
     * @since 1.0.0
     */
    void warn(final @NonNull String message);

    /**
     * <div>
     *     <p>
     *         Logs an error-level message with optional arguments.
     *     </p>
     *     <p>
     *         Error messages indicate serious problems that prevented an operation from completing
     *         successfully. They represent failures that require attention but typically allow the
     *         system to continue running. If the last argument is a {@link Throwable}, it should
     *         be logged with its stack trace.
     *     </p>
     * </div>
     *
     * @param message the log message; must not be {@code null}
     * @param args    optional arguments; interpretation depends on the logging framework;
     *                the last argument may be a {@link Throwable} for exception logging
     *
     * @since 1.0.0
     */
    void error(final @NonNull String message,
               final Object... args);

    /**
     * <div>
     *     <p>
     *         Logs an error-level message.
     *     </p>
     *     <p>
     *         Error messages indicate serious problems that prevented an operation from completing
     *         successfully. They represent failures that require attention but typically allow the
     *         system to continue running.
     *     </p>
     * </div>
     *
     * @param message the log message; must not be {@code null}
     *
     * @since 1.0.0
     */
    void error(final @NonNull String message);

    /**
     * <div>
     *     <p>
     *         Creates a no-op logger that silently discards all log messages.
     *     </p>
     *     <p>
     *         This implementation is useful for testing scenarios or when logging is explicitly
     *         not desired. All logging methods are no-ops.
     *     </p>
     * </div>
     *
     * @return a no-op logger instance; never {@code null}
     *
     * @since 1.0.0
     */
    static @NonNull LogAdapter noOpLogger() {
        return NoOpLogAdapter.INSTANCE;
    }

    /**
     * <div>
     *     <p>
     *         Singleton no-op logger implementation.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    final class NoOpLogAdapter implements LogAdapter {

        private static final NoOpLogAdapter INSTANCE = new NoOpLogAdapter();

        private NoOpLogAdapter() {}

        @Override
        public void debug(final @NonNull String message,
                          final Object... args) {
            Objects.requireNonNull(message, nullValue("message"));
            Objects.requireNonNull(args, nullValue("args"));
            // no-op
        }


        @Override
        public void debug(final @NonNull String message) {
            Objects.requireNonNull(message, nullValue("message"));
            // no-op
        }

        @Override
        public void info(final @NonNull String message,
                         final @NonNull Object... args) {
            Objects.requireNonNull(message, nullValue("message"));
            Objects.requireNonNull(args, nullValue("args"));
            // no-op
        }

        @Override
        public void info(final @NonNull String message) {
            Objects.requireNonNull(message, nullValue("message"));
            // no-op
        }

        @Override
        public void warn(final @NonNull String message,
                         final @NonNull Object... args) {
            Objects.requireNonNull(message, nullValue("message"));
            Objects.requireNonNull(args, nullValue("args"));
            // no-op
        }

        @Override
        public void warn(final @NonNull String message) {
            Objects.requireNonNull(message, nullValue("message"));
            // no-op
        }

        @Override
        public void error(final @NonNull String message,
                          final @NonNull Object... args) {
            Objects.requireNonNull(message, nullValue("message"));
            Objects.requireNonNull(args, nullValue("args"));
            // no-op
        }

        @Override
        public void error(final @NonNull String message) {
            Objects.requireNonNull(message, nullValue("message"));
            // no-op
        }

    }

    final class DelegatingLogAdapter
            implements LogAdapter {

        private static final Object[] EMPTY_ARGS
            = new Object[0];

        private final BiConsumer<String, Object[]> debugDelegator;
        private final BiConsumer<String, Object[]> infoDelegator;
        private final BiConsumer<String, Object[]> warnDelegator;
        private final BiConsumer<String, Object[]> errorDelegator;

        private DelegatingLogAdapter(final BiConsumer<String, Object[]> debugDelegator,
                                     final BiConsumer<String, Object[]> infoDelegator,
                                     final BiConsumer<String, Object[]> warnDelegator,
                                     final BiConsumer<String, Object[]> errorDelegator) {
            this.debugDelegator
                = debugDelegator;
            this.infoDelegator
                = infoDelegator;
            this.warnDelegator
                = warnDelegator;
            this.errorDelegator
                = errorDelegator;
        }

        @Override
        public void debug(final @NonNull String message,
                          final @NonNull Object... args) {
            Objects.requireNonNull(message, nullValue("message"));
            Objects.requireNonNull(args, nullValue("args"));

            this.debugDelegator.accept(message, args);
        }

        @Override
        public void debug(@NonNull String message) {
            Objects.requireNonNull(message, nullValue("message"));

            this.debugDelegator.accept(message, EMPTY_ARGS);
        }

        @Override
        public void info(final @NonNull String message,
                         final @NonNull  Object... args) {
            Objects.requireNonNull(message, nullValue("message"));
            Objects.requireNonNull(args, nullValue("args"));

            this.infoDelegator.accept(message, args);
        }

        @Override
        public void info(final @NonNull String message) {
            Objects.requireNonNull(message, nullValue("message"));

            this.infoDelegator.accept(message, EMPTY_ARGS);
        }

        @Override
        public void warn(final @NonNull String message,
                         final @NonNull Object... args) {
            Objects.requireNonNull(message, nullValue("message"));
            Objects.requireNonNull(args, nullValue("args"));

            this.warnDelegator.accept(message, args);
        }

        @Override
        public void warn(final @NonNull String message) {
            Objects.requireNonNull(message, nullValue("message"));

            this.warnDelegator.accept(message, EMPTY_ARGS);
        }

        @Override
        public void error(final @NonNull String message,
                          final @NonNull Object... args) {
            Objects.requireNonNull(message, nullValue("message"));
            Objects.requireNonNull(args, nullValue("args"));

            this.errorDelegator.accept(message, args);
        }

        @Override
        public void error(final @NonNull String message) {
            Objects.requireNonNull(message, nullValue("message"));

            this.errorDelegator.accept(message, EMPTY_ARGS);
        }

    }

}