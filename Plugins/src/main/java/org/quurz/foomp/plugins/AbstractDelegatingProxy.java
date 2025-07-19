package org.quurz.foomp.plugins;

import java.util.concurrent.locks.ReadWriteLock;

public abstract class AbstractDelegatingProxy {

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

    protected AbstractDelegatingProxy() {
        this.$__delegate_lock
            = new DummyReadWriteLock();
    }

}
