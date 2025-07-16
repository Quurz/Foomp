package org.quurz.foomp.plugins.proxybuilder;

import lombok.Getter;

/**
 * <div>
 *     <p>
 *         Definiert verschiedene Modi zur Steuerung der Synchronisation bei Proxies.
 *     </p>
 *     <p>
 *         Mit diesen Einstellungen kann granular festgelegt werden, ob der Zugriff auf das Delegate-Objekt
 *         selbst, die delegierten Methodenaufrufe oder beides synchronisiert werden sollen.
 *     </p>
 *     <p>
 *         Die Entscheidung für eine bestimmte Sperrstrategie kann je nach Threading-Anforderungen
 *         der Umgebung variieren.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@Getter
public enum LockingMode {

    /**
     * <div>
     *     <p>
     *         Nur der Zugriff auf das Delegate-Objekt wird synchronisiert.
     *         Methodenaufrufe werden nicht gesperrt.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    LOCK_DELEGATE(true, false),

    /**
     * <div>
     *     <p>
     *         Nur delegierte Methodenaufrufe werden synchronisiert.
     *     </p>
     *     <p>
     *         Der Zugriff auf das Delegate-Objekt selbst ist ungeschützt.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    LOCK_DELEGATE_METHODS(false, true),

    /**
     * <div>
     *     <p>
     *         Sowohl der Zugriff auf das Delegate-Objekt
     *         als auch delegierte Methodenaufrufe werden synchronisiert.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    LOCK_BOTH(true, true),

    /**
     * <div>
     *     <p>
     *         Es wird keine Synchronisation durchgeführt.
     *     </p>
     *     <p>
     *         Diese Einstellung ist nur in rein single-threaded Kontexten oder
     *         wenn Thread-Sicherheit explizit anderweitig gewährleistet ist, geeignet.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    NO_LOCKING(false, false);

    private final boolean lockDelegate;
    private final boolean lockMethodCalls;

    /**
     * <div>
     *     <p>
     *         Konstruktor zur internen Initialisierung der Locking-Flags.
     *     </p>
     * </div>
     *
     * @param lockDelegate      {@code true}, wenn das Delegate-Objekt synchronisiert werden soll
     * @param lockMethodCalls   {@code true}, wenn Methodenaufrufe synchronisiert werden sollen
     *
     * @since 1.0.0
     */
    LockingMode(final boolean lockDelegate,
                final boolean lockMethodCalls) {
        this.lockDelegate
            = lockDelegate;
        this.lockMethodCalls
            = lockMethodCalls;
    }

}
