package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public class Proxy<A> {

    private A $__plugin;
    final ReadWriteLock $__access_lock
        = new ReentrantReadWriteLock(true);

    public void $__set_plugin(final @NonNull A plugin) {
        Objects.requireNonNull(plugin, nullValue("plugin"));
        $__access_lock.writeLock().lock();
        try {
            $__plugin
                = plugin;
        } finally {
            $__access_lock.writeLock().unlock();
        }
    }

    public A $__get_plugin() {
        $__access_lock.readLock().lock();
        try {
            return this.$__plugin;
        } finally {
            $__access_lock.readLock().unlock();
        }
    }

    public A $__replace_plugin(final @NonNull A plugin) {
        Objects.requireNonNull(plugin, nullValue("plugin"));
        $__access_lock.writeLock().lock();
        try {
            final var oldPlugin
                = this.$__plugin;
            this.$__plugin
                = plugin;
            return oldPlugin;
        } finally {
            $__access_lock.writeLock().unlock();
        }
    }

}
