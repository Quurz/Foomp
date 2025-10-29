package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public class Plugin<A> {

    private A $__implementation;
    final ReadWriteLock $__access_lock
        = new ReentrantReadWriteLock(true);

    public void $__set_implementation(final @NonNull A implementation) {
        Objects.requireNonNull(implementation, nullValue("implementation"));
        $__access_lock.writeLock().lock();
        try {
            $__implementation
                = implementation;
        } finally {
            $__access_lock.writeLock().unlock();
        }
    }

    public A $__get_implementation() {
        $__access_lock.readLock().lock();
        try {
            return this.$__implementation;
        } finally {
            $__access_lock.readLock().unlock();
        }
    }

    public A $__replace_implementation(final @NonNull A plugin) {
        Objects.requireNonNull(plugin, nullValue("plugin"));
        $__access_lock.writeLock().lock();
        try {
            final var oldPlugin
                = this.$__implementation;
            this.$__implementation
                = plugin;
            return oldPlugin;
        } finally {
            $__access_lock.writeLock().unlock();
        }
    }

}
