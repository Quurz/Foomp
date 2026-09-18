
package org.quurz.foomp.base.misc;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.quurz.foomp.base.localisation.BaseMessages.allShutdownHooksCompleted;
import static org.quurz.foomp.base.localisation.BaseMessages.executingShutdownHook;
import static org.quurz.foomp.base.localisation.BaseMessages.executingShutdownHooks;
import static org.quurz.foomp.base.localisation.BaseMessages.negativeValue;
import static org.quurz.foomp.base.localisation.BaseMessages.nonPositiveValue;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.localisation.BaseMessages.shutdownHookCompleted;
import static org.quurz.foomp.base.localisation.BaseMessages.shutdownHookExecutorNotTerminatedInTime;
import static org.quurz.foomp.base.localisation.BaseMessages.shutdownHookFailed;
import static org.quurz.foomp.base.localisation.BaseMessages.shutdownHookInterrupted;
import static org.quurz.foomp.base.localisation.BaseMessages.shutdownHookRegistryAlreadyInstalled;
import static org.quurz.foomp.base.localisation.BaseMessages.shutdownHookRegistryInstalled;
import static org.quurz.foomp.base.localisation.BaseMessages.shutdownHookTimedOut;
import static org.quurz.foomp.base.localisation.BaseMessages.unexpectedExceptionInShutdownHook;
import static org.quurz.foomp.base.util.Util.requireNonNegativeInt;
import static org.quurz.foomp.base.util.Util.requirePositiveDuration;

/**
 * <div>
 *     <p>
 *         A registry for application shutdown hooks with priority-based execution and timeout support.
 *     </p>
 *     <p>
 *         This class allows registration of shutdown hooks that will be executed when the JVM
 *         terminates normally. Hooks are executed in order of priority (higher priority first),
 *         and each hook can have an individual timeout. If a hook exceeds its timeout, it will
 *         be interrupted and the next hook will be executed.
 *     </p>
 *     <p>
 *         All hooks are executed sequentially even if some fail. Exceptions are logged but do
 *         not prevent other hooks from running. This ensures maximum reliability during shutdown.
 *     </p>
 *     <p>
 *         Instances are created using the builder pattern via {@link #shutdownHookRegistry()}.
 *         The registry must be explicitly installed using {@link #install()} to activate it.
 *     </p>
 *     <p>
 *         Example usage:
 *     </p>
 *     <pre>{@code
 *     var registry = ShutdownHookRegistry.shutdownHookRegistry()
 *         .withLogAdapter(myLogger)
 *         .register("Database", 100, database::close, Duration.ofSeconds(5))
 *         .register("Cache", 50, cache::flush, Duration.ofSeconds(3))
 *         .build();
 *     registry.install();
 *     }</pre>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class ShutdownHookRegistry {

    /**
     * <div>
     *     <p>
     *         Creates a new shutdown hook with the specified parameters.
     *     </p>
     * </div>
     *
     * @param name the name of the hook for identification in logs; must not be {@code null}
     * @param priority the execution priority; higher values execute first; must be non-negative
     * @param action the action to execute on shutdown; must not be {@code null}
     * @param timeout the maximum execution time; must be positive
     * @return a new shutdown hook
     * @throws NullPointerException if {@code name}, {@code action}, or {@code timeout} is {@code null}
     * @throws IllegalArgumentException if {@code priority} is negative or {@code timeout} is not positive
     *
     * @since 1.0.0
     */
    public static ShutdownHook shutdownHook(final @NonNull String name,
                                            final int priority,
                                            final @NonNull Runnable action,
                                            final @NonNull Duration timeout) {
        return new ShutdownHook(
            Objects.requireNonNull(name, nullValue("name")),
            requireNonNegativeInt(
                priority,
                () -> new IllegalArgumentException(negativeValue("priority"))
            ),
            Objects.requireNonNull(action, nullValue("action")),
            requirePositiveDuration(
                Objects.requireNonNull(timeout, nullValue("timeout")),
                () -> new IllegalArgumentException(nonPositiveValue("timeout"))
            )
        );
    }

    /**
     * <div>
     *     <p>
     *         Represents a single shutdown hook with name, priority, action, and timeout.
     *     </p>
     *     <p>
     *         Shutdown hooks are comparable based on priority (higher first) and then name
     *         for stable ordering. Equality is based on name and priority only.
     *     </p>
     *     <p>
     *         Instances should be created using {@link #shutdownHook(String, int, Runnable, Duration)}.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class ShutdownHook
            implements Comparable<ShutdownHook> {

        private final String name;
        private final int priority;
        private final Runnable action;
        private final Duration timeout;

        private ShutdownHook(final @NonNull String name,
                             final int priority,
                             final @NonNull Runnable action,
                             final @NonNull Duration timeout) {
            this.name
                = name;
            this.priority
                = priority;
            this.action
                = action;
            this.timeout
                = timeout;
        }

        /**
         * <div>
         *     <p>
         *         Returns the name of this shutdown hook.
         *     </p>
         * </div>
         *
         * @return the hook name; never {@code null}
         *
         * @since 1.0.0
         */
        public String getName() {
            return this.name;
        }

        /**
         * <div>
         *     <p>
         *         Returns the execution priority of this shutdown hook.
         *     </p>
         * </div>
         *
         * @return the priority value; higher values execute first
         *
         * @since 1.0.0
         */
        public int getPriority() {
            return this.priority;
        }

        /**
         * <div>
         *     <p>
         *         Returns the action to execute on shutdown.
         *     </p>
         * </div>
         *
         * @return the action; never {@code null}
         *
         * @since 1.0.0
         */
        public Runnable getAction() {
            return this.action;
        }

        /**
         * <div>
         *     <p>
         *         Returns the maximum execution time for this hook.
         *     </p>
         * </div>
         *
         * @return the timeout duration; never {@code null}
         *
         * @since 1.0.0
         */
        public Duration getTimeout() {
            return this.timeout;
        }

        @Override
        public int compareTo(final ShutdownHook other) {
            // Higher priority first
            final int priorityCompare = Integer.compare(other.priority, this.priority);
            if (priorityCompare != 0) {
                return priorityCompare;
            }
            // Then by name for stable ordering
            return this.name.compareTo(other.name);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof ShutdownHook other)) return false;
            return priority == other.priority && name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, priority);
        }

        @Override
        public String toString() {
            return "ShutdownHook[name=%s, priority=%d, timeout=%s]"
                    .formatted(name, priority, timeout);
        }
    }

    /**
     * <div>
     *     <p>
     *         Builder for creating and configuring a shutdown hook registry.
     *     </p>
     *     <p>
     *         The builder allows fluent registration of multiple shutdown hooks and configuration
     *         of the log adapter. Hooks can be registered either as {@link ShutdownHook} instances
     *         or by providing individual parameters.
     *     </p>
     *     <p>
     *         Instances should be created using {@link #shutdownHookRegistry()}.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class ShutdownHookRegistryBuilder {

        private final List<ShutdownHook> hooks;
        private LogAdapter logAdapter;

        private ShutdownHookRegistryBuilder() {
            this.hooks
                = new ArrayList<>();
            this.logAdapter
                = LogAdapter.noOpLogAdapter();
        }

        /**
         * <div>
         *     <p>
         *         Registers a shutdown hook in this builder.
         *     </p>
         * </div>
         *
         * @param hook the hook to register; must not be {@code null}
         * @return this builder for method chaining
         * @throws NullPointerException if {@code hook} is {@code null}
         *
         * @since 1.0.0
         */
        public ShutdownHookRegistryBuilder register(final @NonNull ShutdownHook hook) {
            Objects.requireNonNull(hook, nullValue("hook"));
            this.hooks.add(hook);
            return this;
        }

        /**
         * <div>
         *     <p>
         *         Registers a shutdown hook by specifying its parameters directly.
         *     </p>
         * </div>
         *
         * @param name the name of the hook for identification; must not be {@code null}
         * @param priority the execution priority; higher values execute first; must be non-negative
         * @param action the action to execute; must not be {@code null}
         * @param timeout the maximum execution time; must be positive
         * @return this builder for method chaining
         * @throws NullPointerException if {@code name}, {@code action}, or {@code timeout} is {@code null}
         * @throws IllegalArgumentException if {@code priority} is negative or {@code timeout} is not positive
         *
         * @since 1.0.0
         */
        public ShutdownHookRegistryBuilder register(final @NonNull String name,
                                                    final int priority,
                                                    final @NonNull Runnable action,
                                                    final @NonNull Duration timeout) {
            Objects.requireNonNull(name, nullValue("name"));
            Objects.requireNonNull(action, nullValue("action"));
            Objects.requireNonNull(timeout, nullValue("timeout"));
            return this.register(shutdownHook(name, priority, action, timeout));
        }

        /**
         * <div>
         *     <p>
         *         Sets the log adapter for shutdown hook execution logging.
         *     </p>
         *     <p>
         *         If not set, a no-op log adapter is used by default.
         *     </p>
         * </div>
         *
         * @param logAdapter the log adapter to use; must not be {@code null}
         * @return this builder for method chaining
         * @throws NullPointerException if {@code logAdapter} is {@code null}
         *
         * @since 1.0.0
         */
        public ShutdownHookRegistryBuilder withLogAdapter(final @NonNull LogAdapter logAdapter) {
            this.logAdapter
                = Objects.requireNonNull(logAdapter, nullValue("logAdapter"));
            return this;
        }

        /**
         * <div>
         *     <p>
         *         Builds and returns a new shutdown hook registry.
         *     </p>
         *     <p>
         *         The registry must be explicitly installed using {@link #install()} to activate it.
         *     </p>
         * </div>
         *
         * @return a new shutdown hook registry; never {@code null}
         *
         * @since 1.0.0
         */
        public ShutdownHookRegistry build() {
            return new ShutdownHookRegistry(this.hooks, this.logAdapter);
        }
    }

    /**
     * <div>
     *     <p>
     *         Creates a new builder for configuring a shutdown hook registry.
     *     </p>
     * </div>
     *
     * @return a new builder instance; never {@code null}
     *
     * @since 1.0.0
     */
    public static ShutdownHookRegistryBuilder shutdownHookRegistry() {
        return new ShutdownHookRegistryBuilder();
    }

    private final SortedSet<ShutdownHook> hooks;
    private final LogAdapter logAdapter;
    private volatile boolean registered;

    private ShutdownHookRegistry(final List<ShutdownHook> hooks,
                                 final LogAdapter logAdapter) {
        this.hooks = new TreeSet<>(hooks);
        this.logAdapter = logAdapter;
        this.registered = false;
    }

    /**
     * <div>
     *     <p>
     *         Registers this shutdown hook registry with the JVM.
     *     </p>
     *     <p>
     *         Can only be called once. Subsequent calls will throw an exception.
     *     </p>
     * </div>
     *
     * @throws IllegalStateException if already registered
     *
     * @since 1.0.0
     */
    public synchronized void install() {
        if (this.registered) {
            throw new IllegalStateException(shutdownHookRegistryAlreadyInstalled());
        }

        Runtime.getRuntime().addShutdownHook(new Thread(
                this::executeAllHooks,
                "ShutdownHookRegistry-Executor"
        ));

        this.registered = true;
        this.logAdapter.info(shutdownHookRegistryInstalled(this.hooks.size()));
    }

    private void executeAllHooks() {
        this.logAdapter.info(executingShutdownHooks(this.hooks.size()));

        final var executor = Executors.newSingleThreadExecutor(r -> {
            final var thread = new Thread(r, "ShutdownHook-Worker");
            thread.setDaemon(false);
            return thread;
        });

        for (final var hook : hooks) {
            executeHook(executor, hook);
        }

        shutdownExecutor(executor);

        this.logAdapter.info(allShutdownHooksCompleted());
    }

    private void executeHook(final ExecutorService executor, final ShutdownHook hook) {
        try {
            this.logAdapter.debug(
                executingShutdownHook(hook.getName(),
                    hook.getPriority(),
                    hook.getTimeout()
                )
            );

            final var future = executor.submit(hook.getAction());

            try {
                future.get(hook.getTimeout().toMillis(), TimeUnit.MILLISECONDS);
                this.logAdapter.debug(shutdownHookCompleted(hook.getName()));
            } catch (final TimeoutException timeoutException) {
                this.logAdapter.warn(shutdownHookTimedOut(hook.getName()));
                future.cancel(true);
            } catch (final ExecutionException executionException) {
                this.logAdapter.error(shutdownHookFailed(hook.getName(), executionException.getMessage()));
            } catch (final InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                this.logAdapter.warn(shutdownHookInterrupted(hook.getName()));
            }
        } catch (final Exception exception) {
            this.logAdapter.error(unexpectedExceptionInShutdownHook(hook.getName(), exception.getMessage()));
        }
    }

    private void shutdownExecutor(final ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                this.logAdapter.warn(shutdownHookExecutorNotTerminatedInTime());
                executor.shutdownNow();
            }
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }
    }

    /**
     * <div>
     *     <p>
     *         Returns the number of registered hooks.
     *     </p>
     * </div>
     *
     * @return the hook count
     *
     * @since 1.0.0
     */
    public int size() {
        return this.hooks.size();
    }

    /**
     * <div>
     *     <p>
     *         Checks if this registry has been installed.
     *     </p>
     * </div>
     *
     * @return {@code true} if installed, {@code false} otherwise
     *
     * @since 1.0.0
     */
    public boolean isInstalled() {
        return this.registered;
    }

}