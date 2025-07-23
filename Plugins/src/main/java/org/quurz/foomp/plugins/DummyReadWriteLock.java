package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static org.quurz.foomp.plugins.DummyLock.dummyLock;

/**
 * <div>
 *     <p>
 *         Eine {@link ReadWriteLock}-Implementierung ohne tatsächliche Synchronisation.
 *     </p>
 *     <p>
 *         Diese Klasse verwendet intern eine einzelne Instanz von {@link DummyLock}, die
 *         sowohl für Lese- als auch Schreiboperationen verwendet wird. Sie eignet sich
 *         in Szenarien, in denen bewusst auf Thread-Sicherheit verzichtet werden soll.
 *     </p>
 *     <p>
 *         Wird typischerweise über {@link org.quurz.foomp.plugins.LockingMode#NO_LOCKING}
 *         oder als Fallback bei deaktivierter Sperrung verwendet.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class DummyReadWriteLock
        implements ReadWriteLock {

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue DummyReadWriteLock-Instanz.
     *         Intern wird eine einzige {@link DummyLock}-Instanz verwendet.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static ReadWriteLock dummyReadWriteLock() {
        return new DummyReadWriteLock();
    }

    private final Lock dummyLock;

    private DummyReadWriteLock() {
        this.dummyLock
            = dummyLock();
    }

    /**
     * <div>
     *     <p>
     *         Gibt eine Dummy-Implementierung des Lese-Locks zurück.
     *     </p>
     * </div>
     *
     * @return eine instanzierte {@link DummyLock}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Lock readLock() {
        return this.dummyLock;
    }

    /**
     * <div>
     *     <p>
     *         Gibt eine Dummy-Implementierung des Schreib-Locks zurück.
     *     </p>
     * </div>
     *
     * @return eine instanzierte {@link DummyLock}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Lock writeLock() {
        return this.dummyLock;
    }

}
