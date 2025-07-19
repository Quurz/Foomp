package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Abstrakte Basisklasse für Proxies mit Delegation und optionalem Thread-Safety.
 *     </p>
 *     <p>
 *         Stellt die grundlegende Infrastruktur für Delegation und Synchronisation bereit.
 *     </p>
 * </div>
 *
 * @param <A> Der Typ des Objekts, an das delegiert wird
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public abstract class AbstractDelegatingProxy<A>
        implements Delegator<A>{

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
     * <div>
     *     <p>
     *         Erstellt eine neue Proxy-Instanz mit dem angegebenen Lock.
     *     </p>
     * </div>
     *
     * @param $__delegate_lock Das zu verwendende Lock für Thread-Safety
     * @throws NullPointerException wenn {@code $__delegate_lock} {@code null} ist
     *
     * @since 1.0.0
     */
    protected AbstractDelegatingProxy(final @NonNull ReadWriteLock $__delegate_lock) {
        Objects.requireNonNull($__delegate_lock, nullValue(DELEGATE_LOCK_FIELD_NAME));
        this.$__delegate_lock
            = $__delegate_lock;
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue Proxy-Instanz mit dem angegebenen Delegate und Lock.
     *     </p>
     * </div>
     *
     * @param $__delegate Das initiale Delegate-Objekt
     * @param lockDele Das zu verwendende Lock für Thread-Safety
     * @throws NullPointerException wenn {@code $__delegate} oder {@code lockDele} {@code null} ist
     *
     * @since 1.0.0
     */
    protected AbstractDelegatingProxy(final @NonNull A $__delegate,
                                      final @NonNull ReadWriteLock lockDele) {
        Objects.requireNonNull($__delegate, nullValue(DELEGATE_FIELD_NAME));
        Objects.requireNonNull(lockDele, nullValue(DELEGATE_LOCK_FIELD_NAME));
        this.$__delegate
            = $__delegate;
        this.$__delegate_lock
            = lockDele;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract @NonNull A $__get_delegate();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void $__set_delegate(final @NonNull A $__delegate);

    /**
     * <div>
     *     <p>
     *         Gibt das für diesen Proxy verwendete {@link ReadWriteLock} zurück.
     *     </p>
     * </div>
     *
     * @return Das Sperrobjekt für Thread-Safety
     *
     * @since 1.0.0
     */
    public @NonNull ReadWriteLock $__get_delegate_lock() {
        return this.$__delegate_lock;
    }

}
