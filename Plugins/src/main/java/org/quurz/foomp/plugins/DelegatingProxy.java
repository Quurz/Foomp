package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * Eine generische Basisklasse für Proxies, die Aufrufe an ein internes Delegate-Objekt weiterleiten.
 * <p>
 * Diese Klasse stellt eine delegierende Instanz {@code $__delegate} bereit und schützt den Zugriff
 * darauf optional mit einem {@link ReadWriteLock}. Dadurch kann bei Bedarf Thread-Sicherheit sichergestellt
 * werden.
 * </p>
 *
 * <p>
 * Die Klasse ist vor allem für dynamisch generierte Proxies gedacht (z.&nbsp;B. mittels ByteBuddy),
 * die zur Laufzeit eine Weiterleitung an ein echtes Zielobjekt durchführen müssen.
 * </p>
 *
 * @param <A> Der Typ des Objekts, an das delegiert wird
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class DelegatingProxy<A>
        extends AbstractDelegatingProxy<A> {

    /**
     * Erstellt eine neue Instanz von {@link DelegatingProxy} mit optionalem Locking.
     *
     * @param $__delegate   das Delegate-Objekt, darf nicht {@code null} sein
     * @param lockDelegate  gibt an, ob Zugriffe auf das Delegate synchronisiert werden sollen
     * @throws NullPointerException wenn {@code $__delegate} {@code null} ist
     */
    public DelegatingProxy(final @NonNull A $__delegate,
                           final boolean lockDelegate) {
        super(
            $__delegate,
            lockDelegate
                ? new ReentrantReadWriteLock(true)
                : new DummyReadWriteLock()
        );
    }

    /**
     * Gibt das Delegate-Objekt zurück. Der Zugriff wird ggf. lesend gesperrt.
     *
     * @return das aktuelle Delegate-Objekt
     */
    public @NonNull A $__get_delegate() {
        super.$__delegate_lock.readLock().lock();
        try {
            return super.$__delegate;
        } finally {
            super.$__delegate_lock.readLock().unlock();
        }
    }

    /**
     * Setzt ein neues Delegate-Objekt. Der Zugriff wird schreibend gesperrt.
     *
     * @param $__delegate das neue Delegate-Objekt, darf nicht {@code null} sein
     * @throws NullPointerException wenn {@code $__delegate} {@code null} ist
     */
    public void $__set_delegate(final @NonNull A $__delegate) {
        Objects.requireNonNull($__delegate, nullValue(DELEGATE_FIELD_NAME));
        super.$__delegate_lock.writeLock().lock();
        try {
            super.$__delegate
                = $__delegate;
        } finally {
            super.$__delegate_lock.writeLock().unlock();
        }
    }

}
