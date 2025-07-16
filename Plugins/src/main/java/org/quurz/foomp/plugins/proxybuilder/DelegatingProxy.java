package org.quurz.foomp.plugins.proxybuilder;

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
        implements Delegator<A> {

    /**
     * Der Feldname für das Delegate-Objekt (zur Verwendung in Bytecode-Generierung).
     */
    public static final String DELEGATE_FIELD_NAME
            = "$__delegate";

    /**
     * Der Feldname für das zugehörige {@link ReadWriteLock}.
     */
    public static final String DELEGATE_LOCK_FIELD_NAME
            = "$__delegate_lock";


    protected A $__delegate;
    protected final ReadWriteLock $__delegate_lock;

    /**
     * Erstellt eine neue Instanz von {@link DelegatingProxy} mit optionalem Locking.
     *
     * @param $__delegate   das Delegate-Objekt, darf nicht {@code null} sein
     * @param lockDelegate  gibt an, ob Zugriffe auf das Delegate synchronisiert werden sollen
     * @throws NullPointerException wenn {@code $__delegate} {@code null} ist
     */
    public DelegatingProxy(final @NonNull A $__delegate,
                           final boolean lockDelegate) {
        Objects.requireNonNull($__delegate, nullValue(DELEGATE_LOCK_FIELD_NAME));
        this.$__delegate
                = $__delegate;
        this.$__delegate_lock
            = lockDelegate
                ? new ReentrantReadWriteLock(true)
                : new DummyReadWriteLock();
    }

    /**
     * Gibt das Delegate-Objekt zurück. Der Zugriff wird ggf. lesend gesperrt.
     *
     * @return das aktuelle Delegate-Objekt
     */
    public A $__get_delegate() {
        this.$__delegate_lock.readLock().lock();
        try {
            return this.$__delegate;
        } finally {
            this.$__delegate_lock.readLock().unlock();
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
        this.$__delegate_lock.writeLock().lock();
        try {
            this.$__delegate
                = $__delegate;
        } finally {
            this.$__delegate_lock.writeLock().unlock();
        }
    }

    /**
     * Gibt das aktuell verwendete {@link ReadWriteLock} zurück.
     *
     * @return das zugehörige Sperrobjekt
     */
    public ReadWriteLock $__get_delegate_lock() {
        return this.$__delegate_lock;
    }

}
