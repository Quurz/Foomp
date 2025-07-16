package org.quurz.foomp.plugins.proxybuilder;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

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
 *         Wird typischerweise über {@link com.oneandone.bs.billing.util.foomp.proxies.LockingMode#NO_LOCKING}
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

    private final Lock dummyLock;

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
    public DummyReadWriteLock() {
        this.dummyLock
            = new DummyLock();
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
