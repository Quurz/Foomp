package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * <div>
 *     <p>
 *         Eine Lock-Implementierung, die keinerlei tatsächliche Synchronisation durchführt.
 *     </p>
 *     <p>
 *         Diese Klasse kann verwendet werden, wenn explizit auf Sperren verzichtet werden soll
 *         (z. B. über {@link LockingMode#NO_LOCKING}).
 *         Methoden dieser Klasse haben keinen Einfluss auf Thread-Sicherheit und führen
 *         keine echten Warte- oder Sperrmechanismen aus.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class DummyLock
        implements Lock {

    /**
     * <div>
     *
     *     <p>
     *         Erstellt eine neue Instanz eines DummyLocks mit einer passiven {@link Condition}-Implementierung.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static Lock dummyLock() {
        return new DummyLock();
    }

    private final Condition condition;

    private DummyLock() {
        this.condition
            = new Condition() {
                @Override
                public void await() {}

                @Override
                public void awaitUninterruptibly() {}

                @Override
                public long awaitNanos(long nanosTimeout) throws InterruptedException {
                    return 0;
                }

                @Override
                public boolean await(long time,
                                     final TimeUnit unit) throws InterruptedException {
                    return false;
                }

                @Override
                public boolean awaitUntil(final @NonNull Date deadline) throws InterruptedException {
                    return false;
                }

                @Override
                public void signal() {}

                @Override
                public void signalAll() {}
            };
    }

    /**
     * <div>
     *     <p>
     *         Führt keine Aktion durch.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    @Override
    public void lock() {}

    /**
     * <div>
     *     <p>
     *         Führt keine Aktion durch.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    @Override
    public void lockInterruptibly() {}

    /**
     * <div>
     *     <p>
     *         Gibt immer {@code true} zurück.
     *     </p>
     * </div>
     *
     * @return immer {@code true}
     *
     * @since 1.0.0
     */
    @Override
    public boolean tryLock() {
        return true;
    }

    /**
     * <div>
     *     <p>
     *         Gibt immer {@code true} zurück, unabhängig von Zeitwerten.
     *     </p>
     * </div>
     *
     * @param time Wartezeit
     * @param unit Zeiteinheit
     * @return immer {@code true}
     *
     * @since 1.0.0
     */
    @Override
    public boolean tryLock(long time,
                           final @NonNull TimeUnit unit) {
        return true;
    }

    /**
     * <div>
     *     <p>
     *         Führt keine Aktion durch.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    @Override
    public void unlock() {}

    /**
     * <div>
     *     <p>
     *         Gibt eine passive {@link Condition} zurück, deren Methoden keine Wirkung haben.
     *     </p>
     * </div>
     *
     * @return Dummy-Condition
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Condition newCondition() {
        return this.condition;
    }

}
