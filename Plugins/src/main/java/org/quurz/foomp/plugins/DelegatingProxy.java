package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.plugins.DummyReadWriteLock.dummyReadWriteLock;

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

    public static <A> DelegatingProxy<A> delegatingProxy(final @NonNull A $__delegate) {
        Objects.requireNonNull($__delegate, nullValue(DELEGATE_FIELD_NAME));
        return delegatingProxy($__delegate, true);
    }

    public static <A> DelegatingProxy<A> delegatingProxy(final @NonNull A $__delegate,
                                                         final boolean lockDelegate) {
        Objects.requireNonNull($__delegate, nullValue(DELEGATE_FIELD_NAME));
        return new DelegatingProxy<>($__delegate, lockDelegate);
    }

    private DelegatingProxy(final A $__delegate,
                            final boolean lockDelegate) {
        super(
            $__delegate,
            lockDelegate
                ? new ReentrantReadWriteLock(true)
                : dummyReadWriteLock()
        );
    }

}
