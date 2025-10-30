package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Mutable;
import org.quurz.foomp.base.util.Maybe;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Maybe.maybeOfNullable;

/**
 * <div>
 *     <p>
 *         A thread-safe queue for plugin lifecycle commands with automatic cancellation of superseded commands.
 *     </p>
 *     <p>
 *         This queue manages commands for plugin loading and unloading operations. When a new command
 *         is offered for a plugin that already has a pending command in the queue, the old command
 *         is automatically cancelled. This ensures that only the most recent command for each plugin
 *         will be executed.
 *     </p>
 *     <p>
 *         All operations are protected by a fair reentrant lock, making this class safe for concurrent use.
 *         Commands are processed in FIFO order via {@link #poll()}, which returns cancelled commands
 *         but removes them from internal tracking.
 *     </p>
 *     <p>
 *         Instances should be created using the static factory method {@link #commandQueue()}.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@Mutable
public class CommandQueue {

    /**
     * <div>
     *     <p>
     *         Creates a new empty command queue.
     *     </p>
     * </div>
     *
     * @return a new {@code CommandQueue} instance
     *
     * @since 1.0.0
     */
    public static CommandQueue commandQueue() {
        return new CommandQueue();
    }

    private final Queue<Command> queue;
    private final Map<String, Command> pluginNamesToCommands;
    private final Lock accessLock;

    private CommandQueue() {
        this.queue
            = new LinkedList<>();
        this.pluginNamesToCommands
            = new HashMap<>();
        this.accessLock
            = new ReentrantLock(true);
    }

    /**
     * <div>
     *     <p>
     *         Adds a command to the queue.
     *     </p>
     *     <p>
     *         If a command for the same plugin (identified by plugin name) already exists in the queue,
     *         that command will be cancelled before the new command is added. This automatic cancellation
     *         ensures that only the most recent command for each plugin will be executed.
     *     </p>
     *     <p>
     *         This method is thread-safe and blocks until the lock is acquired.
     *     </p>
     * </div>
     *
     * @param command the command to add; must not be {@code null}
     * @throws NullPointerException if {@code command} is {@code null}
     *
     * @since 1.0.0
     */
    public void offer(final @NonNull Command command) {
        Objects.requireNonNull(command, nullValue("command"));
        this.accessLock.lock();
        try {
            final var nullableUnprocessedCommand
                = this.pluginNamesToCommands.get(command.getPluginCoordinate().getName());
            if (nullableUnprocessedCommand != null) {
                nullableUnprocessedCommand.cancel();
            }
            this.pluginNamesToCommands.put(command.getPluginCoordinate().getName(), command);
            this.queue.offer(command);
        } finally {
            this.accessLock.unlock();
        }
    }

    /**
     * <div>
     *     <p>
     *         Retrieves and removes the head of this queue.
     *     </p>
     *     <p>
     *         If the polled command is not cancelled, it is removed from internal tracking.
     *         Cancelled commands are also removed from the queue but remain in the result,
     *         allowing the caller to decide how to handle them.
     *     </p>
     *     <p>
     *         This method is thread-safe and blocks until the lock is acquired.
     *     </p>
     * </div>
     *
     * @return a {@link Maybe} containing the head command if the queue is not empty,
     *         or an empty {@code Maybe} if the queue is empty
     *
     * @since 1.0.0
     */
    public Maybe<Command> poll() {
        this.accessLock.lock();
        try {
            final var nullableUnprocessedCommand
                = this.queue.poll();
            if (   nullableUnprocessedCommand != null
                && !nullableUnprocessedCommand.isCancelled()) {
                this.pluginNamesToCommands.remove(nullableUnprocessedCommand.getPluginCoordinate().getName());
            }
            return maybeOfNullable(nullableUnprocessedCommand);
        } finally {
            this.accessLock.unlock();
        }
    }

}
