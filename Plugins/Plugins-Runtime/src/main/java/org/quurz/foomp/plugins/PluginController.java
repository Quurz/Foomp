package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.misc.LogAdapter;

import java.util.Objects;
import java.util.function.Consumer;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.misc.LogAdapter.noOpLogAdapter;
import static org.quurz.foomp.plugins.CommandQueue.commandQueue;

public class PluginController {

    private static final class CommandQueueThread
            extends Thread {

        private final CommandQueue commandQueue;
        private final Consumer<Command> commandConsumer;

        private CommandQueueThread(final CommandQueue commandQueue,
                                   final Consumer<Command> commandConsumer) {
            super("CommandQueueThread");

            this.commandQueue
                = commandQueue;
            this.commandConsumer
                = commandConsumer;
        }

        public void run() {
            this.commandQueue
                .poll()
                .filter(Command::isPending)
                .ifSomeOrElse(this.commandConsumer,
                              Thread::yield);
        }

    }

    private static volatile PluginController INSTANCE;
    private static final Object INSTANCE_LOCK
        = new Object();

    public static PluginController pluginController(final @NonNull PluginRepository pluginRepository) {
        Objects.requireNonNull(pluginRepository, nullValue("pluginRepository"));
        return pluginController(pluginRepository, noOpLogAdapter());
    }

    public static PluginController pluginController(final @NonNull PluginRepository pluginRepository,
                                                    final @NonNull LogAdapter logAdapter) {
        Objects.requireNonNull(pluginRepository, nullValue("pluginRepository"));
        Objects.requireNonNull(logAdapter, nullValue("logAdapter"));

        synchronized (INSTANCE_LOCK) {
            if (INSTANCE == null) {
                final PluginController pluginController
                    = new PluginController(pluginRepository, logAdapter);
                INSTANCE = pluginController;
                return pluginController;
            } else {
                throw new IllegalStateException("PluginController already initialized");    // TODO: Lokalisierung
            }
        }
    }

    private final PluginRepository pluginRepository;
    private final CommandQueue commandQueue;
    private final CommandQueueThread commandQueueThread;

    private final LogAdapter logAdapter;

    private PluginController(final PluginRepository pluginRepository,
                             final LogAdapter logAdapter) {
        this.pluginRepository
            = pluginRepository;
        this.commandQueue
            = commandQueue();
        this.commandQueueThread
            = new CommandQueueThread(commandQueue, this::processCommand);

        this.logAdapter
            = logAdapter;
    }

    private void processCommand(final Command command) {
        this.logAdapter.debug("Processing command: %s".formatted(command));
    }

    public void start() {
        this.commandQueueThread.start();
        this.logAdapter.debug("Started command queue thread");
    }

}
