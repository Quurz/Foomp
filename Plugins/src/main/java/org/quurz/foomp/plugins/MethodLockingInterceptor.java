package org.quurz.foomp.plugins;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;

/**
 * <div>
 *     <p>
 *         ByteBuddy-Interceptor zur thread-sicheren Weiterleitung von Methodenaufrufen
 *         an das delegate-Objekt eines {@link DelegatingProxy}.
 *     </p>
 *     <p>
 *         Dieser Interceptor wird dynamisch durch ByteBuddy auf Methoden des Proxy-Objekts
 *         angewendet, sofern {@link LockingMode#LOCK_DELEGATE_METHODS} oder {@link LockingMode#LOCK_BOTH}
 *         aktiv ist. Er sorgt dafür, dass jeder Methodenaufruf auf das delegate-Objekt
 *         innerhalb eines Lese-Locks (ReadLock) erfolgt.
 *     </p>
 *     <p>
 *         Durch die Verwendung eines ReadLocks wird eine gleichzeitige Ausführung mehrerer
 *         lesender Methoden erlaubt, solange keine schreibenden Zugriffe erfolgen.
 *         Speziell wird das Austauschen des Delegate-Objekts während der Ausführung blockiert.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public final class MethodLockingInterceptor {

    private MethodLockingInterceptor() {}

    /**
     * <div>
     *     <p>
     *         Interzeptiert einen Methodenaufruf auf einem ByteBuddy-generierten Proxy-Objekt.
     *     </p>
     *     <p>
     *         Die Methode delegiert den Methodenaufruf an das Zielobjekt ({@code $__delegate}) des
     *         Proxys, wobei der Aufruf innerhalb eines {@code readLock()}-Blocks ausgeführt wird.
     *     </p>
     * </div>
     *
     * @param self      die aufrufende Proxy-Instanz
     * @param method    die ursprünglich aufgerufene Methode
     * @param arguments die Argumente des Methodenaufrufs
     * @return das Rückgabewert-Ergebnis des aufgerufenen delegate-Objekts
     * @throws Exception falls die Methode beim delegate-Objekt eine Ausnahme wirft
     *
     * @since 1.0.0
     */
    @RuntimeType
    public static Object intercept(final @This Object self,
                                   final @Origin Method method,
                                   final @AllArguments Object[] arguments)
            throws Exception {
        final var delegatingProxy
            = (DelegatingProxy<?>) self;
        final var lock
            = delegatingProxy.$__get_delegate_lock();

        lock.readLock().lock();
        try {
            return method.invoke(delegatingProxy.$__get_delegate(), arguments);
        } finally {
            lock.readLock().unlock();
        }
    }

}
