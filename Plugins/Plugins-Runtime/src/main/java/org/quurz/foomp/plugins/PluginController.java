package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.misc.LogAdapter;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.misc.LogAdapter.noOpLogAdapter;
import static org.quurz.foomp.base.misc.ShutdownHookRegistry.shutdownHookRegistry;
import static org.quurz.foomp.plugins.CommandQueue.commandQueue;

public class PluginController {

    private static PluginController INSTANCE;
    private static final Object INSTANCE_LOCK
        = new Object();

    public static PluginController pluginController(final @NonNull PluginRepository repository) {
        Objects.requireNonNull(repository, nullValue("repository"));
        return pluginController(repository, noOpLogAdapter());
    }

    public static PluginController pluginController(final @NonNull PluginRepository repository,
                                                    final @NonNull LogAdapter logAdapter) {
        Objects.requireNonNull(repository, nullValue("repository"));
        Objects.requireNonNull(logAdapter, nullValue("logAdapter"));

        synchronized (INSTANCE_LOCK) {
            if (INSTANCE == null) {
                INSTANCE
                    = new PluginController(repository, logAdapter);
            }
            return INSTANCE;
        }
    }

    private final Lock accessLock;

    private final PluginRepository repository;
    private AtomicBoolean shutDown;
    private final ExecutorService executorService;

    private final LogAdapter logAdapter;

    private PluginController(final @NonNull PluginRepository repository,
                             final @NonNull LogAdapter logAdapter) {
        this.accessLock
            = new ReentrantLock(true);

        this.repository
            = repository;

        this.shutDown
            = new AtomicBoolean(false);
        this.executorService
            = Executors.newSingleThreadExecutor();
        shutdownHookRegistry()
            .register("CommandQueue-ShutdownHook", 100, this::shutdown, Duration.ofSeconds(10))
            .withLogAdapter(logAdapter)
            .build()
            .install();

        executorService.submit(
            () -> {
                final var commandQueue
                    = commandQueue();
                while (!this.shutDown.get()) {
                    commandQueue
                        .poll()
                        .filter(Command::isPending)
                        .ifSome(this::processCommand);
                }
            }
        );

        this.logAdapter
            = logAdapter;
    }

    private void processCommand(final Command command) {
        this.accessLock.lock();
        try {
            // TODO
        } finally {
            this.accessLock.unlock();
        }
    }

    private void shutdown() {
        this.accessLock.lock();
        try {
            this.shutDown.set(true);
            this.executorService.shutdown();
        } finally {
            this.accessLock.unlock();
        }
    }

}
